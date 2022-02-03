package org.firstinspires.ftc.teamcode.robot.subsystems;

import androidx.annotation.NonNull;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Kicker implements Subsystem {
    private HardwareMap hardwareMap;
    private Servo kicker;

    public enum POSITION {
        OPEN,
        CLOSE;

        public double getPosition() {
            switch (this) {
                case OPEN:
                    return 0.0;
                case CLOSE:
                    return 1.0;
                default:
                    return CLOSE.getPosition();
            }
        }

        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case OPEN:
                    return "Open";
                case CLOSE:
                    return "Close";
                default:
                    return "Defaulted";
            }
        }
    }

    private Kicker.POSITION position;

    public Kicker(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        kicker = hardwareMap.get(Servo.class, "kicker");
        kicker.setDirection(Servo.Direction.FORWARD);

        position = POSITION.CLOSE;
    }

    @Override
    public void periodic() {
        kicker.setPosition(position.getPosition());
    }

    public void setPosition(POSITION position) {
        this.position = position;
    }

    public POSITION getPosition() {
        return position;
    }

    public void toggle() {
        if(position == POSITION.OPEN) {
            position = POSITION.CLOSE;
        } else {
            position = POSITION.OPEN;
        }
    }

    public void open() {
        position = POSITION.OPEN;
    }
    public void close() {
        position = POSITION.CLOSE;
    }
}

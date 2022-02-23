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
        HOLD,
        CLOSE;

        public double getPosition() {
            switch (this) {
                case OPEN:
                    return 0.5;
                case HOLD:
                    return 0.2;
                case CLOSE:
                    return 0.0;
                default:
                    return HOLD.getPosition();
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

        position = POSITION.HOLD;
    }

    @Override
    public void periodic() {
        kicker.setPosition(position.getPosition());
    }

    public void setPosition(POSITION position) {
        this.position = position;
    }

    public void setDirection(Servo.Direction direction) {
        kicker.setDirection(direction);
    }

    public POSITION getPosition() {
        return position;
    }

    public Servo.Direction getDirection() {
        return kicker.getDirection();
    }

    public void toggle() {
        if(position == POSITION.OPEN) {
            position = POSITION.HOLD;
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
    public void hold() {
        position = POSITION.HOLD;
    }
}

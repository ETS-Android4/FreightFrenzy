package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Cap implements Subsystem {
    private HardwareMap hardwareMap;
    private Servo cap;

    private POSITIONS position;
    private double pos;

    public static double testPos = -1;

    public enum POSITIONS {
        IN,
        HOLD,
        PLACE,
        OUT;

        public double getPosition() {
            switch (this) {
                case IN:
                    return 1.0;
                case HOLD:
                    return 0.5;
                case PLACE:
                    return 0.4;
                case OUT:
                    return 0.1;
                default:
                    return IN.getPosition();
            }
        }
    }

    public Cap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        cap = hardwareMap.get(Servo.class, "cap");
        cap.setDirection(Servo.Direction.FORWARD);

        position = POSITIONS.IN;
        pos = position.getPosition();
    }

    @Override
    public void periodic() {


        // Easy position testing setup using FtcDashboard
        if(testPos != -1.0) {
            pos = testPos;
        }

        cap.setPosition(pos);
    }

    public void setPosition(POSITIONS position) {
        this.position = position;
        pos = position.getPosition();
    }
    public void increment(double val) {
        pos += val;
    }

    public void setDirection(Servo.Direction direction) {
        cap.setDirection(direction);
    }

    public POSITIONS getPosition() {
        return position;
    }

    public Servo.Direction getDirection() {
        return cap.getDirection();
    }
}

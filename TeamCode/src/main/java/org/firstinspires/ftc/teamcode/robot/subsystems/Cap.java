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

    public static double testPos = -1;

    public enum POSITIONS {
        IN,
        OUT;

        public double getPosition() {
            switch (this) {
                case IN:
                    return 0.0;
                case OUT:
                    return 0.5;
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

    }

    @Override
    public void periodic() {
        double pos = position.getPosition();

        // Easy position testing setup using FtcDashboard
        if(testPos != -1.0) {
            pos = testPos;
        }

        cap.setPosition(pos);
    }

    public void setPosition(POSITIONS position) {
        this.position = position;
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

package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CapArm implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo cap;

    public enum POSITION {
        BOTTOM,
        TOP;

        public double getPosition() {   // TODO: Check values
            switch (this) {
                case BOTTOM:
                    return 0;
                case TOP:
                    return 1;
                default:
                    return 0;
            }
        }
    }

    private POSITION targetPos = POSITION.BOTTOM;

    public CapArm(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        cap = hardwareMap.get(Servo.class, "cap_arm");
        cap.setPosition(Arm.POSITION.INTAKE.getPosition());
    }

    @Override
    public void periodic() {
        cap.setPosition(targetPos.getPosition());
    }

    public void top() {
        targetPos = POSITION.TOP;
    }

    public void bottom() {
        targetPos = POSITION.BOTTOM;
    }


    public void setTargetPos(POSITION targetPos) {
        this.targetPos = targetPos;
    }

    public POSITION getTargetPos() {
        return targetPos;
    }
}

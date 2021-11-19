package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CapArm implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo arm;

    public enum POSITION {
        BOTTOM,
        MIDDLE,
        TOP;

        public double getPosition() {   // TODO: Check values
            switch (this) {
                case BOTTOM:
                    return 0.07;
                case MIDDLE:
                    return 0.35;
                case TOP:
                    return 1.0;
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
        arm = hardwareMap.get(Servo.class, "cap_arm");
        arm.setPosition(targetPos.getPosition());
    }

    @Override
    public void periodic() {
        arm.setPosition(targetPos.getPosition());
    }

    public void top() {
        targetPos = POSITION.TOP;
    }

    public void middle() {
        targetPos = POSITION.MIDDLE;
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

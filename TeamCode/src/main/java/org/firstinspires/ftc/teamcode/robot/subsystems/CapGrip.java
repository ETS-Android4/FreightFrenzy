package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Deprecated
public class CapGrip implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo gripper;

    public enum POSITION {
        GRIP,
        PLACE;

        public double getPosition() {   // TODO: Check values
            switch (this) {
                case GRIP:
                    return 0.0;
                case PLACE:
                    return 1.0;
                default:
                    return 0;
            }
        }
    }

    private POSITION targetPos = POSITION.GRIP;

    public CapGrip(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        gripper = hardwareMap.get(Servo.class, "cap_grip");
        gripper.setPosition(targetPos.getPosition());
    }

    @Override
    public void periodic() {
        gripper.setPosition(targetPos.getPosition());
    }

    public void grip() {
        targetPos = POSITION.GRIP;
    }

    public void place() {
        targetPos = POSITION.PLACE;
    }

    public void toggle() {
        targetPos = targetPos == POSITION.GRIP ? POSITION.PLACE : POSITION.GRIP;
    }

    public void setTargetPos(POSITION targetPos) {
        this.targetPos = targetPos;
    }

    public POSITION getTargetPos() {
        return targetPos;
    }
}

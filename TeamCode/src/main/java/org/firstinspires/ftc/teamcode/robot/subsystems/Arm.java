package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo arm;

    public enum POSITION {
        INTAKE,
        MIDDLE,
        DEPOSIT;

        public double getPosition() {   // TODO: Check values
            switch (this) {
                case INTAKE:
                    return 0;
                case MIDDLE:
                    return 0.5;
                case DEPOSIT:
                    return 1;
                default:
                    return 0;
            }
        }
    }

    private double targetPos;

    public Arm(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        arm = hardwareMap.get(Servo.class, "arm");
        arm.setPosition(POSITION.INTAKE.getPosition());
    }

    @Override
    public void periodic() {
        arm.setPosition(targetPos);
    }

    public void intake() {
        targetPos = POSITION.INTAKE.getPosition();
    }

    public void middle() {
        targetPos = POSITION.MIDDLE.getPosition();
    }

    public void deposit() {
        targetPos = POSITION.DEPOSIT.getPosition();
    }

    public void setTargetPos(int targetPos) {
        this.targetPos = targetPos;
    }

    public double getTargetPos() {
        return targetPos;
    }
}

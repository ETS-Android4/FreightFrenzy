package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.annotation.Target;

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
                    return 0.25;
                case DEPOSIT:
                    return 0.7;
                default:
                    return 0;
            }
        }
    }

    private POSITION targetPos = POSITION.INTAKE;

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
        arm.setPosition(targetPos.getPosition());
    }

    public void intake() {
        targetPos = POSITION.INTAKE;
    }

    public void middle() {
        targetPos = POSITION.MIDDLE;
    }

    public void deposit() {
        targetPos = POSITION.DEPOSIT;
    }

    public void setTargetPos(POSITION targetPos) {
        this.targetPos = targetPos;
    }

    public POSITION getTargetPos() {
        return targetPos;
    }
}

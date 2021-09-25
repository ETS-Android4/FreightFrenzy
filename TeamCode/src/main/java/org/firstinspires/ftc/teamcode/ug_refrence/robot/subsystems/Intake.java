package org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake implements Subsystem {
    private HardwareMap hardwareMap;
    private DcMotor intake;

    private double power = 0;

    public Intake(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        intake = hardwareMap.get(DcMotor.class,"intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        intake.setPower(power);
    }

    public double getPower() {
        return power;
    }

    public DcMotorSimple.Direction getDirection() {
        return intake.getDirection();
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        intake.setDirection(direction);
    }

    public void reverse() {
        if(intake.getDirection() == DcMotorSimple.Direction.FORWARD) intake.setDirection(DcMotorSimple.Direction.REVERSE);
        else intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

}

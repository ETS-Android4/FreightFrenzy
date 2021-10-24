package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Carousel implements Subsystem {
    private HardwareMap hardwareMap;
    private DcMotorEx carousel;

    private double power;

    public Carousel(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        carousel = hardwareMap.get(DcMotorEx.class, "carousel");
        carousel.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        carousel.setPower(power);
    }

    public double getPower() {
        return power;
    }

    public DcMotorSimple.Direction getDirection() {
        return carousel.getDirection();
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        carousel.setDirection(direction);
    }

    public void reverse() {
        if(carousel.getDirection() == DcMotorSimple.Direction.FORWARD) carousel.setDirection(DcMotorSimple.Direction.REVERSE);
        else carousel.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}

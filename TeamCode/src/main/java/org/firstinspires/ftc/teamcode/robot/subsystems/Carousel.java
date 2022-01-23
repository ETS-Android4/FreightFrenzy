package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Carousel implements Subsystem {
    private HardwareMap hardwareMap;
    private CRServo leftCarousel;
    private CRServo rightCarousel;

    private double power;

    public Carousel(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        leftCarousel = hardwareMap.get(CRServo.class, "left_carousel");
        rightCarousel = hardwareMap.get(CRServo.class, "right_carousel");

        leftCarousel.setDirection(CRServo.Direction.FORWARD);
        rightCarousel.setDirection(CRServo.Direction.FORWARD);

        power = 0.5;
    }

    @Override
    public void periodic() {
        leftCarousel.setPower(power);
        rightCarousel.setPower(power);
    }

    public double getPower() {
        return power;
    }

    public CRServo.Direction getDirection() {
        return leftCarousel.getDirection();
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setDirection(CRServo.Direction direction) {
        leftCarousel.setDirection(direction);
        rightCarousel.setDirection(direction);
    }

    public void reverse() {
        if(leftCarousel.getDirection() == CRServo.Direction.FORWARD) leftCarousel.setDirection(CRServo.Direction.REVERSE);
        else leftCarousel.setDirection(CRServo.Direction.FORWARD);

        if(rightCarousel.getDirection() == CRServo.Direction.FORWARD) rightCarousel.setDirection(CRServo.Direction.REVERSE);
        else rightCarousel.setDirection(CRServo.Direction.FORWARD);
    }
}

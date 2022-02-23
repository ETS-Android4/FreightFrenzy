package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner_util.RegressionUtil;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class Carousel implements Subsystem {
    private HardwareMap hardwareMap;
    private CRServo leftCarousel;
    private CRServo rightCarousel;

    private Telemetry telemetry;

    private double power;


    public Carousel(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.telemetry = null;
    }

    @Override
    public void initHardware() {
        leftCarousel = hardwareMap.get(CRServo.class, "left_carousel");
        rightCarousel = hardwareMap.get(CRServo.class, "right_carousel");

        leftCarousel.setDirection(CRServo.Direction.FORWARD);
        rightCarousel.setDirection(CRServo.Direction.FORWARD);

        power = 0;

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

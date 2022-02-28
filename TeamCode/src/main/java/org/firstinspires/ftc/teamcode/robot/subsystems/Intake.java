package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake implements Subsystem {
    private HardwareMap hardwareMap;
    private DcMotorEx intake;
    private Servo lift;

    private double power;
    private LIFT_POSITIONS position;

    public enum LIFT_POSITIONS {
        OUT,
        IN;

        public double getPosition() {
            switch (this) {
                case IN:
                    return 0.4;
                case OUT:
                    return 1.0;
                default:
                    return 0.0;
            }
        }
    }

    public Intake(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        intake = hardwareMap.get(DcMotorEx.class,"intake");
        lift = hardwareMap.get(Servo.class, "intake_lift");

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        power = 0.0;

        lift.setPosition(LIFT_POSITIONS.IN.getPosition());

        position = LIFT_POSITIONS.IN;
    }

    @Override
    public void periodic() {
        intake.setPower(power);
        lift.setPosition(position.getPosition());
    }

    public double getPower() {
        return power;
    }

    public DcMotorSimple.Direction getDirection() {
        return intake.getDirection();
    }

    public LIFT_POSITIONS getPosition() {
        return position;
    }

    public double getCurrent() {
        return intake.getCurrent(CurrentUnit.MILLIAMPS);
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setPosition(LIFT_POSITIONS position) {
        this.position = position;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        intake.setDirection(direction);
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        intake.setMode(runMode);
    }

    public void reverse() {
        if(intake.getDirection() == DcMotorSimple.Direction.FORWARD) intake.setDirection(DcMotorSimple.Direction.REVERSE);
        else intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void toggle() {
        if(position == LIFT_POSITIONS.IN) out();
        else in();
    }

    public void in() {
        setPosition(LIFT_POSITIONS.IN);
    }

    public void out() {
        setPosition(LIFT_POSITIONS.OUT);
    }
}

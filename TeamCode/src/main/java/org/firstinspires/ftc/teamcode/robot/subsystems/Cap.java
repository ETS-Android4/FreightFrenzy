package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Cap implements Subsystem {
    private HardwareMap hardwareMap;
    private CRServo cap;

    private double power;
    private double prevPower = 0.0;

    public Cap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        cap = hardwareMap.get(CRServo.class, "cap");

        cap.setDirection(DcMotorSimple.Direction.FORWARD);

        power = 0.0;

        cap.setPower(power);
    }

    @Override
    public void periodic() {
        cap.setPower(power);
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        cap.setDirection(direction);
    }

    public double getPower() {
        return power;
    }

    public DcMotorSimple.Direction getDirection() {
        return cap.getDirection();
    }

    public void reverse() {
        if(getDirection() == DcMotorSimple.Direction.FORWARD) setDirection(DcMotorSimple.Direction.REVERSE);
        else setDirection(DcMotorSimple.Direction.FORWARD);
    }
}

package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

public class Arm implements Subsystem {
    private HardwareMap hardwareMap;

    private CRServo arm1;
    private CRServo arm2;
    private DcMotorEx encoder;

    private SwampbotsUtil util;

    private final double POWER_SCALAR = 0.7;

    @Deprecated
    public enum OLD_POSITION {
        INTAKE,
        MIDDLE,
        DEPOSIT,
        LOW_SHARED,
        HIGH_SHARED;

        public double getPosition() {   // TODO: Check values
            switch (this) {
                case INTAKE:
                    return 0;
                case MIDDLE:
                    return 0.25;
                case DEPOSIT:
                    return 0.7;
                case LOW_SHARED:
                    return 0.3;
                case HIGH_SHARED:
                    return 0.5;
                default:
                    return 0;
            }
        }
    }

    public enum POSITION {
        INTAKE,
        MIDDLE,
        DEPOSIT,
        LOW_SHARED,
        HIGH_SHARED;

        public int getPosition() {   // TODO: Check values
            switch (this) {
                case INTAKE:
                    return 0;
                case MIDDLE:
                    return 25;
                case DEPOSIT:
                    return 70;
                case LOW_SHARED:
                    return 30;
                case HIGH_SHARED:
                    return 50;
                default:
                    return 0;
            }
        }
    }

    private POSITION targetPos = POSITION.INTAKE;
    private int encoder0;

    public Arm(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        util = new SwampbotsUtil();
    }

    @Override
    public void initHardware() {
        arm1 = hardwareMap.get(CRServo.class, "arm1");
        arm2 = hardwareMap.get(CRServo.class, "arm2");
        encoder = hardwareMap.get(DcMotorEx.class, "intake");

        encoder0 = encoder.getCurrentPosition();

        arm1.setDirection(CRServo.Direction.FORWARD);
        arm2.setDirection(CRServo.Direction.REVERSE);

        targetPos = POSITION.INTAKE;

        arm1.setPower(scalePower(0.0));
        arm2.setPower(scalePower(0.0));

//        arm1.setPosition(POSITION.INTAKE.getPosition());
//        arm2.setPosition(POSITION.INTAKE.getPosition());
    }

    @Override
    public void periodic() {
        double power = calculatePower(encoder.getCurrentPosition(), targetPos.getPosition());

        arm1.setPower(scalePower(power));
        arm2.setPower(scalePower(power));

//        arm1.setPosition(targetPos.getPosition());
//        arm2.setPosition(targetPos.getPosition());
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

    public void lowShared() {
        targetPos = POSITION.LOW_SHARED;
    }

    public void highShared() {
        targetPos = POSITION.HIGH_SHARED;
    }

    public void setTargetPos(POSITION targetPos) {
        this.targetPos = targetPos;
    }

    public POSITION getTargetPos() {
        return targetPos;
    }

    public int getCurrentPos() {
        return encoder.getCurrentPosition();
    }

    public int getInitialPos() {
        return encoder0;
    }

    public double calculatePower(double current, int target) {
        current = current - encoder0;

        if(util.isCloseEnough((int) current, target, 5))
            return 0.0;
        if(current > target) {
            return -1.0;
        }
        if(current < target) {
            return 1.0;
        }

        return 0.0;
    }

    private double scalePower(double power) {
        // [-1, 1] -> [0, 1]
        return (power * POWER_SCALAR + 1.0) / 2.0;
    }
}

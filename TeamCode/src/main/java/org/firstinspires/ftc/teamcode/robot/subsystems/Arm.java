package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.framework.qual.Unused;
import org.firstinspires.ftc.teamcode.roadrunner_util.Encoder;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;
import org.firstinspires.ftc.teamcode.swampbots_util.SynchronousPID;
import org.jetbrains.annotations.TestOnly;

@Config
public class Arm implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo arm1;
    private Servo arm2;
//    private CRServo arm1;
//    private CRServo arm2;
//    private Encoder encoder;

    private SwampbotsUtil util;

    public static double POWER_SCALAR = 0.7;

//    public double power = 0.0;
//
//    public static double target = 100.0;
//    public static double tolerance = 10;
//
//    public static double kP = 1.0e-3;
//    public static double kI = 8.0e-7;
//    public static double kD = -4.0e-5;
//    public static double kSq = 0.001;
//    public static double kBase = 10;
//    public static double kL = 0;
//    public static double kInv = 3;
//
//    public double kSqrt = 0;
//    public double kLog = 0;
//    public double kInverse = 0;
//
//    public int testTarget = 0;
//    public double error = 0;

    public static double testPos = -1.0;

//    public SynchronousPID pid;
//    private ArmFeedforward feedforward = new ArmFeedforward();

//    @Deprecated
    public enum POSITION {
        INTAKE,
        MIDDLE,
        DEPOSIT,
        LOW_HUB,
        MIDDLE_HUB,
        LOW_SHARED,
        HIGH_SHARED;

        public double getPosition() {
            switch (this) {
                case INTAKE:
                    return 0.405;
                case MIDDLE:
                    return 0.425;
                case DEPOSIT:
                    return 0.56;
                case LOW_HUB:
                    return 0.61;
                case MIDDLE_HUB:
                    return 0.60;
                case LOW_SHARED:
                    return 0.58;
                case HIGH_SHARED:
                    return 0.58;
                default:
                    return 0;
            }
        }
    }

    @Deprecated
    public enum NEW_POSITION {
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
                    return 570;
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
//        pid = new SynchronousPID(kP, kI, kD);
    }

    @Override
    public void initHardware() {
        arm1 = hardwareMap.get(Servo.class, "arm1");
        arm2 = hardwareMap.get(Servo.class, "arm2");
//        arm1 = hardwareMap.get(CRServo.class, "arm1");
//        arm2 = hardwareMap.get(CRServo.class, "arm2");
//        encoder = new Encoder(hardwareMap.get(DcMotorEx.class, "intake"));
//
//
//
//        encoder0 = encoder.getCurrentPosition();
//
//        arm1.setDirection(CRServo.Direction.FORWARD);
//        arm2.setDirection(CRServo.Direction.FORWARD);

        targetPos = POSITION.INTAKE;

//        arm1.setPower(0.0);
//        arm2.setPower(0.0);

//        arm1.setPosition(POSITION.INTAKE.getPosition());
//        arm2.setPosition(POSITION.INTAKE.getPosition());
    }

    @Override
    public void periodic() {
        double position = targetPos.getPosition();

//        if(testPos >= 0.0)
//            position = testPos;

//        if(POWER_SCALAR < 0.6)
        arm1.setPosition(position);
//        if(POWER_SCALAR > 0.4)
        arm2.setPosition(position);
//        arm1.setPower(position);
//        arm2.setPower(position);

//        double power = calculatePower(encoder.getCurrentPosition(), targetPos.getPosition());
//        power = calculatePower(encoder.getCurrentPosition(), testTarget);
//
//        arm1.setPower(power * POWER_SCALAR);
//        arm2.setPower(power * POWER_SCALAR);

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

    @TestOnly
    public void setTargetPos(int targetPos) {
//        testTarget = targetPos;
    }

    public POSITION getTargetPos() {
        return targetPos;
    }

//    public int getCurrentPos() {
//        return encoder.getCurrentPosition();
//    }

    public int getInitialPos() {
        return encoder0;
    }

    @TestOnly
    public double calculatePower(double current, int target) {
//        current = current - encoder0;
//        error = current - target;
////        pid.setSetpoint(target);
////        return pid.calculate(current);
//        kSqrt = Math.signum(error) * Math.sqrt(Math.abs(error)) * kSq;
//        kLog = Math.abs(error) > 1 ? Math.log10(Math.abs(error)) / Math.log10(kBase) * kL : 0;
//        kInverse = error == 0 ? 0 : 1 / error * kInv;
//        return kSqrt + kLog + kInverse;

        return -1;

//        if(util.isCloseEnough((int) current, target, ((int) tolerance)))
//            return 0.0;
//        if(current > target) {
//            return -1.0;
//        }
//        if(current < target) {
//            return 1.0;
//        }

        
    }
}

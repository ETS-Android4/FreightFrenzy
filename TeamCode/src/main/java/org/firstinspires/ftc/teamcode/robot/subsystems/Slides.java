package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

@Config
public class Slides implements Subsystem {
    private HardwareMap hardwareMap;

    private DcMotorEx slideRight;
    private DcMotorEx slideLeft;

    public static double testVal1 = 0;
    public static double testVal2 = 0;
    public static double motorCombo = 0;

    public enum TARGETS {
        IN,
        MIDDLE,
        OUT,
        AUTO_MID,
        BOTTOM_HUB,
        LOW_SHARED,
        HIGH_SHARED;

        @NonNull
        public int getTargets() {     // TODO: Check values
            switch (this) {
                case IN:
                    return 0;
                case MIDDLE:
                    return -15;
                case OUT:
                    return -450;
                case AUTO_MID:
                    return -85;
                case BOTTOM_HUB:
                    return -250;
                case LOW_SHARED:
                    return -120;
                case HIGH_SHARED:
                    return -120;
                default:
                    return 0;
            }
        }
    }

    private double power;

    private int targetPos;

    public Slides(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        initHardware();
    }

    public void initHardware() {
        slideLeft   = hardwareMap.get(DcMotorEx.class, "slide_left");
        slideRight  = hardwareMap.get(DcMotorEx.class, "slide_right");

//        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        targetPos = SwampbotsUtil.mean(slideLeft.getCurrentPosition(), slideRight.getCurrentPosition());

        slideLeft.setTargetPosition(targetPos);
        slideRight.setTargetPosition(targetPos);

        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slideLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION,
                new PIDFCoefficients(5.0, 0, 0, 0));
        slideRight.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION,
                new PIDFCoefficients(5.0, 0, 0, 0));

        slideLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        slideRight.setDirection(DcMotorSimple.Direction.REVERSE);

        power = 0.0;
    }

    @Override
    public void periodic() {
        if (slideLeft.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            slideLeft.setTargetPosition(targetPos);
        }
        if (slideRight.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            slideRight.setTargetPosition(targetPos);
        }

        if(motorCombo % 2 == 0)
            slideLeft.setPower(power);
        if(motorCombo % 3 == 0)
            slideRight.setPower(power);
    }


    public DcMotor.RunMode getRunMode() {
        return slideLeft.getMode();
    }

    public double getPower() {
        return power;
    }

    public boolean isBusy() {
        return slideLeft.isBusy();
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        slideLeft.setMode(runMode);
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setTargetPos(int targetPos) {
        this.targetPos = targetPos;
    }

    public int getCurrentPos() {
        return slideLeft.getCurrentPosition();
    }

    public int getTargetPos() {
        return slideLeft.getTargetPosition();
    }

    public void resetEncoder() {
        // Reset Left
        DcMotor.RunMode currentRunMode = slideLeft.getMode();
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setMode(currentRunMode);

        // Reset Right
        currentRunMode = slideRight.getMode();
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setMode(currentRunMode);
    }
}

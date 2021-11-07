package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import androidx.annotation.NonNull;

public class Slides implements Subsystem {
    private HardwareMap hardwareMap;

    private DcMotorEx slide;

    public enum TARGETS {
        IN,
        MIDDLE,
        OUT;

        @NonNull
        public int getTargets() {     // TODO: Check values
            switch (this) {
                case IN:
                    return 0;
                case MIDDLE:
                    return -500;
                case OUT:
                    return -2650;
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
        slide = hardwareMap.get(DcMotorEx.class, "slides");
        targetPos = slide.getCurrentPosition();

        slide.setTargetPosition(targetPos);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slide.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION,
                new PIDFCoefficients(5.0, 0, 0, 0));

        power = 0.0;
    }

    @Override
    public void periodic() {
        if (slide.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            slide.setTargetPosition(targetPos);
        }

        slide.setPower(power);
    }


    public DcMotor.RunMode getRunMode() {
        return slide.getMode();
    }

    public double getPower() {
        return power;
    }

    public boolean isBusy() {
        return slide.isBusy();
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        slide.setMode(runMode);
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setTargetPos(int targetPos) {
        this.targetPos = targetPos;
    }

    public int getCurrentPos() {
        return slide.getCurrentPosition();
    }

    public int getTargetPos() {
        return slide.getTargetPosition();
    }

    public void resetEncoder() {
        DcMotor.RunMode currentRunMode = slide.getMode();
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(currentRunMode);
    }
}

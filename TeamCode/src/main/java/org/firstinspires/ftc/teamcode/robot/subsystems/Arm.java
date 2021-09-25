package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Arm implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private DcMotorEx arm;

    public enum TARGETS {
        UP,
        DOWN;

        private int diff      = -450;   // difference between top and bottom setPosition

        protected void setDiff(int diff) {
            this.diff = diff;
        }

        //TODO: Confirm targets
        public int getTarget() {
            switch (this){
                case UP:        return 0;
                case DOWN:      return diff; //-300: top of wobble, -530: bottom of wobble
                default:        return 0;
            }
        }
    }

    private double power = 0;

    private int targetPos;

    public Arm(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        initHardware();
    }

    public void initHardware() {
        arm = hardwareMap.get(DcMotorEx.class, "arm");

        targetPos = arm.getCurrentPosition();

        arm.setTargetPosition(targetPos);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setPositionPIDFCoefficients(5.0);
    }

    @Override
    public void periodic() {
        if(arm.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            arm.setTargetPosition(targetPos);
        }

        arm.setPower(power);
    }

    public DcMotor.RunMode getRunMode(){
        return arm.getMode();
    }

    public double getPower(){
        return power;
    }

    public boolean isBusy() {return arm.isBusy();}

    public void setRunMode(DcMotor.RunMode runMode){
        arm.setMode(runMode);
    }

    public void setPower(double power){
        this.power = power;
    }

    public void setTargetPos(int targetPos){
        this.targetPos = targetPos;
    }

    public int getCurrentPos(){
        return arm.getCurrentPosition();
    }

    public int getTargetPos(){
        return arm.getTargetPosition();
    }

    public void resetEncoder() {
        DcMotor.RunMode currentRunMode = arm.getMode();
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(currentRunMode);
    }

    public void setDownPos(int pos) {
        TARGETS.DOWN.setDiff(pos);
    }

    public PIDFCoefficients getPIDFCoefficients(){
        return arm.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
    }

}


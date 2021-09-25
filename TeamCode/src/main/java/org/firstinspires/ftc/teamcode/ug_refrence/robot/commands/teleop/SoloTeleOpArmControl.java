package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Arm;

import java.util.Date;

public class SoloTeleOpArmControl implements Command {
    private Gamepad gamepad;
    private Arm wobble;
    private Telemetry telemetry;

    private final double POWER_SCALAR = 0.5;
    private final int TOLERANCE = 50;

    public static boolean tellGripToToggle = false;
    private boolean queueGripToOpen;
    private long openTimeCount;

    private boolean queueGripToClose;
    private long closeTimeCount;
    private final long CLOSE_TIME_THRESHOLD = 1200L;     // 1200 milliseconds

    public SoloTeleOpArmControl(Arm wobble, Gamepad gamepad, Telemetry telemetry){
        this.gamepad = gamepad;
        this.wobble = wobble;
        this.telemetry = telemetry;
    }

    public SoloTeleOpArmControl(Arm wobble, Gamepad gamepad){
        this(wobble, gamepad, null);
    }

    @Override
    public void start() {
        wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wobble.setPower(0);

        queueGripToOpen  = false;
        queueGripToClose = false;
    }

    @Override
    public void periodic() {
        // In (encoder): RT     Out (encoder): LT   Up (power): dUp     Down (power): dDown     Reset Encoder: dRight
        if(gamepad.left_bumper || gamepad.right_bumper) {
            wobble.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            wobble.setPower(1.0 + 0 * POWER_SCALAR);

            if(gamepad.left_bumper) {
                wobble.setTargetPos(Arm.TARGETS.DOWN.getTarget());
                queueGripToOpen = true;
                openTimeCount = new Date().getTime();
            }
            else if(gamepad.right_bumper && !queueGripToClose) {
                if(SoloTeleOpGripControl.gripOpen) {
                    queueGripToClose = true;
                    closeTimeCount = new Date().getTime();
                } else {
                    wobble.setTargetPos(Arm.TARGETS.UP.getTarget());
                }
            }
        }

        if(gamepad.dpad_up || gamepad.dpad_down || wobble.getRunMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)) {
            wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if(gamepad.dpad_up)         wobble.setPower(1.0 * POWER_SCALAR);
            else if(gamepad.dpad_down)  wobble.setPower(-1.0 * POWER_SCALAR);
            else wobble.setPower(0);
        }

        if( queueGripToOpen &&
            Math.abs(wobble.getCurrentPos() - wobble.getTargetPos()) < TOLERANCE &&
            wobble.getRunMode().equals(DcMotor.RunMode.RUN_TO_POSITION) &&
            !tellGripToToggle &&
            (new Date().getTime() - openTimeCount > 100)
        ) {
            tellGripToToggle = true;
            queueGripToOpen = false;
        }
        if(SoloTeleOpGripControl.tellArmGipToggled) {
            tellGripToToggle = false;
        }

        if(queueGripToClose && (new Date().getTime() - closeTimeCount) > CLOSE_TIME_THRESHOLD) {
            wobble.setTargetPos(Arm.TARGETS.UP.getTarget());
            queueGripToClose = false;
            closeTimeCount = Long.MAX_VALUE;
        }

        if(gamepad.right_stick_button) { // Reset down pos
            wobble.setDownPos(wobble.getCurrentPos());
        }

        if(gamepad.left_stick_button) { // Reset up pos
            wobble.resetEncoder();
        }

        if(telemetry != null) {
            telemetry.addData("Target", wobble.getTargetPos());
            telemetry.addData("Current", wobble.getCurrentPos());
            telemetry.addData("Power",wobble.getPower());
            telemetry.addData("Run Mode", wobble.getRunMode());
            telemetry.addData("PIDF", wobble.getPIDFCoefficients());
            telemetry.addData("isBusy()",wobble.isBusy());
            telemetry.addData("tellGripToToggle",tellGripToToggle);
            telemetry.addData("tellArmGripToggled",TeleOpGripControl.tellArmGipToggled);
            telemetry.addData("|curr-target|", Math.abs(wobble.getCurrentPos() - wobble.getTargetPos()));
            telemetry.addData("Tolerance",TOLERANCE);
            telemetry.addData("Queue to close", queueGripToClose);
            telemetry.addData("now - closeTimeCount", new Date().getTime() - closeTimeCount);
            telemetry.addData("Close Threshold",CLOSE_TIME_THRESHOLD);
            telemetry.addData("Grip Closed", TeleOpGripControl.gripOpen);
            telemetry.update();
        }
    }

    @Override
    public void stop() {
        wobble.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

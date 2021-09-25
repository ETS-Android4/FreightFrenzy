package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

import java.util.Date;

public class TeleOpArmControl implements Command {
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

    public TeleOpArmControl(Arm wobble, Gamepad gamepad, Telemetry telemetry){
        this.gamepad = gamepad;
        this.wobble = wobble;
        this.telemetry = telemetry;
    }

    public TeleOpArmControl(Arm wobble, Gamepad gamepad){
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
        // In (encoder): X     Out (encoder): Y   Up (power): dUp     Down (power): dDown     Reset Encoder: RStickB, LStickB
        boolean encoderIn           = gamepad.y;
        boolean encoderOut          = gamepad.x;
        boolean powerUp             = gamepad.dpad_up;
        boolean powerDown           = gamepad.dpad_down;
        boolean resetEncoder        = gamepad.left_stick_button;
        boolean resetEncoderDiff    = gamepad.right_stick_button;

        // Enter encoder based mode
        if(encoderOut || encoderIn) {
            wobble.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            wobble.setPower(1.0 + 0 * POWER_SCALAR);

            if(encoderOut) {
                wobble.setTargetPos(Arm.TARGETS.DOWN.getTarget());
                queueGripToOpen = true;
                openTimeCount = new Date().getTime();
            }
            else if(encoderIn && !queueGripToClose) {
                if(TeleOpGripControl.gripOpen) {
                    queueGripToClose = true;
                    closeTimeCount = new Date().getTime();
                } else {
                    wobble.setTargetPos(Arm.TARGETS.UP.getTarget());
                }
            }
        }

        // Enter manual power mode
        if(powerUp || powerDown || wobble.getRunMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)) {
            wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if(powerUp)         wobble.setPower(1.0 * POWER_SCALAR);
            else if(powerDown)  wobble.setPower(-1.0 * POWER_SCALAR);
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
        if(TeleOpGripControl.tellArmGipToggled) {
            tellGripToToggle = false;
        }

        if(queueGripToClose && (new Date().getTime() - closeTimeCount) > CLOSE_TIME_THRESHOLD) {
            wobble.setTargetPos(Arm.TARGETS.UP.getTarget());
            queueGripToClose = false;
            closeTimeCount = Long.MAX_VALUE;
        }

        if(resetEncoderDiff) { // Reset down pos
            wobble.setDownPos(wobble.getCurrentPos());
        }

        if(resetEncoder) { // Reset up pos
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

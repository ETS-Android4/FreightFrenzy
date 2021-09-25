package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.teamcode.CommandDrive.TRIGGER_THRESHOLD;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

public class TestArmPositionsCommand implements Command {
    private Gamepad gamepad;
    private Arm wobble;
    private Telemetry telemetry;

    private final double POWER_SCALAR = 0.5;
    private final int TOLERANCE = 50;

    public TestArmPositionsCommand(Arm wobble, Gamepad gamepad, Telemetry telemetry){
        this.gamepad = gamepad;
        this.wobble = wobble;
        this.telemetry = telemetry;
    }

    public TestArmPositionsCommand(Arm wobble, Gamepad gamepad){
        this(wobble, gamepad, null);
    }

    @Override
    public void start() {
        wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wobble.setPower(0);

    }

    @Override
    public void periodic() {
        // In (encoder): RT     Out (encoder): LT   Up (power): dUp     Down (power): dDown     Reset Encoder: dRight
        if(gamepad.left_trigger > TRIGGER_THRESHOLD) {
            wobble.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            wobble.setPower(0.8 + 0 * POWER_SCALAR);
            wobble.setTargetPos(Arm.TARGETS.DOWN.getTarget());
        } else if(gamepad.right_trigger > TRIGGER_THRESHOLD) {
            wobble.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            wobble.setPower(0.8 + 0 * POWER_SCALAR);
            wobble.setTargetPos(Arm.TARGETS.UP.getTarget());
        } else if(gamepad.a) {
            wobble.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            wobble.setPower(0.8 + 0 * POWER_SCALAR);
            wobble.setTargetPos(-Arm.TARGETS.DOWN.getTarget());
        }


        if(gamepad.dpad_up || gamepad.dpad_down || wobble.getRunMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)) {
            wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if(gamepad.dpad_up)         wobble.setPower(1.0 * POWER_SCALAR);
            else if(gamepad.dpad_down)  wobble.setPower(-1.0 * POWER_SCALAR);
            else wobble.setPower(0);
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
            telemetry.addData("|curr-target|", Math.abs(wobble.getCurrentPos() - wobble.getTargetPos()));
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

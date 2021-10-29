package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CommandDrive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

public class TeleOpSlideControl implements Command {
    private Gamepad gamepad;
    private Slides slides;
    private Telemetry telemetry;

    private final double POWER_SCALAR = 0.7;

    public  TeleOpSlideControl(Slides slides, Gamepad gamepad, Telemetry telemetry) {
        this.slides = slides;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public TeleOpSlideControl(Slides slides, Gamepad gamepad) {
        this(slides, gamepad, null);
    }

    @Override
    public void start() {
        slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slides.setPower(0.0);
    }

    @Override
    public void periodic() {
        boolean powerIn      = gamepad.dpad_up;     //gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean powerOut     = gamepad.dpad_down ;    //gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean encoderIn   = gamepad.left_bumper;
        boolean encoderOut  = gamepad.right_bumper;

        if(powerIn || powerOut) {
            slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slides.setPower(((powerIn ? 1 : 0) - (powerOut ? 1 : 0)) * POWER_SCALAR);
        } else if(encoderIn ^ encoderOut) {
            slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            slides.setPower(POWER_SCALAR);
            if(encoderIn) {
                slides.setTargetPos(Slides.TARGETS.IN.getTargets());
            }
            if(encoderOut) {
                slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
            }
        } else /*if(slides.getRunMode() != DcMotor.RunMode.RUN_TO_POSITION)*/{ //TODO: Uncomment once we finish testing
            slides.setPower(0);
        }

        if(telemetry != null) {
            telemetry.addLine("Slide Telemetry:");
            telemetry.addData("PowerIn:", powerIn);
            telemetry.addData("PowerOut:",powerOut);
            telemetry.addData("EncoderIn:", encoderIn);
            telemetry.addData("EncoderOut:",encoderOut);
            telemetry.addLine();
            telemetry.addData("Power:", slides.getPower());
            telemetry.addData("Current Pos:", slides.getCurrentPos());
            telemetry.addData("Target Pos:", slides.getTargetPos());
            telemetry.addData("RunMode:", slides.getRunMode());
            telemetry.update();
        }


    }

    @Override
    public void stop() {
        slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slides.setTargetPos(slides.getCurrentPos());
        slides.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

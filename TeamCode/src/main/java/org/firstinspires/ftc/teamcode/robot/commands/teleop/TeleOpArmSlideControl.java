package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

public class TeleOpArmSlideControl implements Command {
    private Arm arm;
    private Slides slides;
    private Gamepad gamepad;
    private Telemetry telemetry;

    private boolean autoMode = false;
    private final double POWER_SCALAR = 0.7;

    public TeleOpArmSlideControl(Arm arm, Slides slides, Gamepad gamepad, Telemetry telemetry) {
        this.arm = arm;
        this.slides = slides;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public TeleOpArmSlideControl(Arm arm, Slides slides, Gamepad gamepad) {
        this(arm, slides, gamepad, null);
    }

    @Override
    public void start() {
        arm.intake();

        slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slides.setPower(0.0);
    }

    @Override
    public void periodic() {
        boolean intake      = gamepad.a;    //intake
        boolean middle      = gamepad.x;    //middle
        boolean deposit     = gamepad.b;    //deposit
        boolean powerIn     = gamepad.dpad_up;      //gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean powerOut    = gamepad.dpad_down;    //gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean encoderIn   = gamepad.left_bumper;
        boolean encoderOut  = gamepad.right_bumper;

        if(autoMode) {
            // Arm pos = f(slide pos) = {intake if slide pos > -500, middle if slide pos slideMax < slide pos < -500, out only by input}
            if(slides.getCurrentPos() > -500) {
                arm.intake();
            } else if(slides.getCurrentPos() < Slides.TARGETS.OUT.getTargets()) {
                arm.middle();
            }
        } else {
            if(middle) {
                arm.middle();
            } else if(intake) {
                arm.intake();
            } else if(deposit) {
                arm.deposit();
            }
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
        }




        if(telemetry != null) {
            telemetry.addLine("Arm telemetry:");
            telemetry.addData("intake?", intake);
            telemetry.addData("middle?", middle);
            telemetry.addData("deposit?", deposit);
            telemetry.addLine();
            telemetry.addData("target pos:", arm.getTargetPos());
            telemetry.addLine();

            telemetry.addLine("Slide Telemetry:");
            telemetry.addData("power in?", powerIn);
            telemetry.addData("power out?",powerOut);
            telemetry.addData("encoder in?", encoderIn);
            telemetry.addData("encoder out?",encoderOut);
            telemetry.addLine();
            telemetry.addData("power:", slides.getPower());
            telemetry.addData("current pos:", slides.getCurrentPos());
            telemetry.addData("target pos:", slides.getTargetPos());
            telemetry.addData("runMode:", slides.getRunMode());

            telemetry.update();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

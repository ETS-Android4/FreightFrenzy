package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CommandDrive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;


public class TeleOpIntakeControl implements Command {
    private Gamepad gamepad;
    private Intake intake;
    private Telemetry telemetry;

    private final double POWER_SCALAR = 0.69;

    private boolean liftToggle = true;

    public TeleOpIntakeControl(Intake intake, Gamepad gamepad, Telemetry telemetry) {
        this.intake = intake;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public TeleOpIntakeControl(Intake intake, Gamepad gamepad) {
        this(intake, gamepad, null);
    }

    @Override
    public void start() {
        intake.setPower(0);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

//        intake.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset encoder for Arm // Not needed anymore b/c of switch to normal servos
//        intake.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        intake.out();
    }

    @Override
    public void periodic() {
        // Two Person Controls:
        // In: RB   Out: RT
        intake.setPower(
                (       gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD   ? 1.0 :
                        gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD  ? -1.0 : 0.0
                ) * POWER_SCALAR);

        boolean toggleLift = gamepad.guide;
        if(toggleLift) {
            if(liftToggle) {
                intake.toggle();
                liftToggle = false;
            }
        } else {
            liftToggle = true;
        }

        if(telemetry != null) {
            telemetry.addLine("Intake Telemetry:");
            telemetry.addData("Power:", intake.getPower());
            telemetry.addData("Direction:", intake.getDirection());
            telemetry.update();
        }
    }

    @Override
    public void stop() {
        intake.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

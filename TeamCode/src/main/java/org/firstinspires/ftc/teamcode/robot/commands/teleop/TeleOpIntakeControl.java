package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import static org.firstinspires.ftc.teamcode.CommandDrive.TRIGGER_THRESHOLD;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;

public class TeleOpIntakeControl implements Command {
    private Gamepad gamepad;

    private Intake intake;

    private final double POWER_SCALAR = 1.0;

    public TeleOpIntakeControl(Intake intake, Gamepad gamepad) {
        this.intake = intake;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        intake.setPower(0);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        // Two Person Controls:
        // In: RB   Out: RT
        intake.setPower(
                (       gamepad.right_trigger > TRIGGER_THRESHOLD                        ? 1.0 :
                        gamepad.right_bumper   ? -1.0 : 0.0
                ) * POWER_SCALAR);
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

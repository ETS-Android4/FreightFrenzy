package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop;

import static org.firstinspires.ftc.teamcode.ug_refrence.CommandDrive.TRIGGER_THRESHOLD;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Transfer;

public class SoloTeleOpTransferControl implements Command {
    private Gamepad gamepad;
    private Transfer transfer;

    private final double POWER_SCALAR = 1.0;

    public SoloTeleOpTransferControl(Transfer transfer, Gamepad gamepad){
        this.transfer = transfer;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        transfer.setPower(0);
        transfer.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        // One Person Controls:
        // In: LT  Out: RT
        transfer.setPower(
                (   gamepad.right_trigger > TRIGGER_THRESHOLD ? 1.0 : gamepad.left_trigger > TRIGGER_THRESHOLD ? -1.0 : 0.0
                ) * POWER_SCALAR
        );
    }

    @Override
    public void stop() {
        transfer.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Flap;

@Deprecated
public class TeleOpFlapControl implements Command {
    private Flap flap;
    private Gamepad gamepad;
    private Telemetry telemetry;

    public TeleOpFlapControl(Flap flap, Gamepad gamepad, Telemetry telemetry) {
        this.flap = flap;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public TeleOpFlapControl(Flap flap, Gamepad gamepad) {
        this(flap, gamepad, null);
    }

    @Override
    public void start() {
        flap.setPosition(Flap.POSITION.CLOSE);
    }

    @Override
    public void periodic() {
        boolean y = gamepad.y;

        flap.setPosition(
                y ? Flap.POSITION.OPEN : Flap.POSITION.CLOSE
        );

        if(telemetry != null) {
            telemetry.addLine("Flap Data:");
            telemetry.addData("Position:", flap.getPosition().toString());
            telemetry.update();
        }
    }

    @Override
    public void stop() {
        flap.setPosition(Flap.POSITION.CLOSE);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

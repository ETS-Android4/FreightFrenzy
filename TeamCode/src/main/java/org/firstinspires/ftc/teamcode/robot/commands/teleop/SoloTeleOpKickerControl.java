package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;

public class SoloTeleOpKickerControl implements Command {
    private TeleOpKickerControl kickerControl;

    public SoloTeleOpKickerControl(Kicker kicker, Gamepad gamepad) {
        kickerControl = new TeleOpKickerControl(kicker, gamepad);
    }


    @Override
    public void start() {
        kickerControl.start();
    }

    @Override
    public void periodic() {
        kickerControl.periodic();
    }

    @Override
    public void stop() {
        kickerControl.stop();
    }

    @Override
    public boolean isCompleted() {
        return kickerControl.isCompleted();
    }
}

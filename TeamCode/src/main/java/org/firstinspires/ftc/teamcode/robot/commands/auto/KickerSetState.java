package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;

public class KickerSetState implements Command {
    private Kicker kicker;
    private Kicker.POSITION target;

    public KickerSetState(Kicker kicker, Kicker.POSITION target) {
        this.kicker = kicker;
        this.target = target;
    }

    @Override
    public void start() {
        kicker.setPosition(target);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}

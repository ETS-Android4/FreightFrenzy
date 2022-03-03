package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;

public class IntakeSetState implements Command {
    private Intake intake;
    private Intake.LIFT_POSITIONS target;

    public IntakeSetState(Intake intake, Intake.LIFT_POSITIONS target) {
        this.intake = intake;
        this.target = target;
    }

    @Override
    public void start() {
        intake.setPosition(target);
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

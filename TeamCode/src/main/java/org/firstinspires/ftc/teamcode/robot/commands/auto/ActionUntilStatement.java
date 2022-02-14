package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import java.util.function.BooleanSupplier;

public class ActionUntilStatement implements Command {
    private Command action;
    private BooleanSupplier statement;

    /**
     * Run a Command until a statement is true
     *
     * @param action Command to run
     * @param statement what needs to be true to stop
     */
    public ActionUntilStatement(Command action, BooleanSupplier statement) {
        this.action = action;
        this.statement = statement;
    }

    @Override
    public void start() {
        action.start();
    }

    @Override
    public void periodic() {
        action.periodic();
    }

    @Override
    public void stop() {
        action.stop();
    }

    @Override
    public boolean isCompleted() {
        return action.isCompleted() || statement.getAsBoolean();
    }
}

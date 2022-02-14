package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import java.util.function.BooleanSupplier;

public class ActionAfterStatement implements Command {
    private Command action;
    private BooleanSupplier statement;

    private boolean started = false;

    public ActionAfterStatement(Command action, BooleanSupplier statement) {
        this.action = action;
        this.statement = statement;
    }

    @Override
    public void start() {

    }

    @Override
    public void periodic() {
        if(started || statement.getAsBoolean()) {
            if(!started) {
                action.start();
                started = true;
            } else {
                action.periodic();
            }
        }
    }

    @Override
    public void stop() {
        action.stop();
    }

    @Override
    public boolean isCompleted() {
        return started && action.isCompleted();
    }
}

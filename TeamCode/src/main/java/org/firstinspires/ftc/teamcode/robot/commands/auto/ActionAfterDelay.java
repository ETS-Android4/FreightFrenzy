package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ActionAfterDelay implements Command {
    private Command action;

    private ElapsedTime timer;
    private double delay;

    private boolean started = false;


    public ActionAfterDelay(Command action, double delay) {
        this.action = action;
        this.delay = delay;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void periodic() {
        if(timer.seconds() > delay) {
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
        return timer.seconds() > delay && action.isCompleted();
    }
}

package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil.TIME_UNITS;

public class WaitByTimer implements Command {
    private ElapsedTime timer;
    private TIME_UNITS units;

    private double time;

    /**
     * Wait some amount of time
     *
     * @param time time to wait. Defaults to seconds
     * @param units the units to use for the time
     */
    public WaitByTimer(double time, TIME_UNITS units) {
        this.time = time;
        this.units = units;

        timer = new ElapsedTime();
    }

    public WaitByTimer(double time) {
        this(time, TIME_UNITS.SECONDS);
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void periodic() {
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        double currentTime;

        switch (units) {
            case MINUTES:
                currentTime = timer.seconds() / 60;
                break;
            case SECONDS:
                currentTime = timer.seconds();
                break;
            case MILLISECONDS:
                currentTime = timer.milliseconds();
                break;
            case NANOSECONDS:
                currentTime = timer.nanoseconds();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + units);
        }

        return currentTime > time;
    }
}

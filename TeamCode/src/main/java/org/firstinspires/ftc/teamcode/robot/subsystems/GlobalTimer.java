package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Command;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.util.ElapsedTime;

public class GlobalTimer implements Subsystem {
    private ElapsedTime timer;

    public GlobalTimer() {

    }

    @Override
    public void initHardware() {
        timer = new ElapsedTime();
        timer.reset();
    }

    @Override
    public void periodic() {

    }

    public double getSeconds() {
        return timer.seconds();
    }

    public double getMilliseconds() {
        return timer.milliseconds();
    }

    public long getNanoseconds() {
        return timer.nanoseconds();
    }

    public void reset() {
        timer.reset();
    }
}

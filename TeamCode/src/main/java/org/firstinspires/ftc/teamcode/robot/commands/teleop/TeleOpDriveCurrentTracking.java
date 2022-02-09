package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import android.annotation.SuppressLint;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.DataSnooper;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;


public class TeleOpDriveCurrentTracking implements Command {
    private Drive drive;

    private DataSnooper ds;
    private int autoSaveClock = 0;

    public TeleOpDriveCurrentTracking(Drive drive) {
        this.drive = drive;

        ds = new DataSnooper("drive_current");
    }

    @Override
    public void start() {
        ds.start();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void periodic() {
        String snoopTxt = "";
        double[] current = drive.getCurrent();
        snoopTxt += String.format("[%f,%f,%f,%f]", current[0], current[1], current[2], current[3]);
        ds.addData(snoopTxt);

        ds.periodic();

        autoSaveClock++;
        if(autoSaveClock >= 100) {
            ds.save();
            autoSaveClock = 0;
        }
    }

    @Override
    public void stop() {
        ds.stop();
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

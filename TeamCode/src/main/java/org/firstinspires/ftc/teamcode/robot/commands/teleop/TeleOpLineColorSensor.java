package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import android.annotation.SuppressLint;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.DataSnooper;
import org.firstinspires.ftc.teamcode.robot.subsystems.LineColorSensor;

public class TeleOpLineColorSensor implements Command {
    private LineColorSensor colorSensor;

    private DataSnooper ds;
    private boolean snoop;

    private int autoSaveClock = 0;

    public TeleOpLineColorSensor(LineColorSensor colorSensor, boolean snoop) {
        this.colorSensor = colorSensor;
        this.snoop = snoop;

        if(this.snoop) {
            ds = new DataSnooper("line_color_sensor");
        }
    }

    public TeleOpLineColorSensor(LineColorSensor colorSensor) {
        this(colorSensor, false);
    }

    @Override
    public void start() {
        if(snoop) {
            ds.start();
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void periodic() {
        if(snoop) {
            String snoopTxt = "";
            double[] sensorLeft = colorSensor.getColorLeft().val;
            double[] sensorRight = colorSensor.getColorRight().val;
            snoopTxt += String.format("l:[%d,%d,%d]", (int)sensorLeft[0], (int)sensorLeft[1], (int)sensorLeft[2]);
            snoopTxt += ",";
            snoopTxt += String.format("r:[%d,%d,%d]", (int)sensorRight[0], (int)sensorRight[1], (int)sensorRight[2]);
            ds.addData(snoopTxt);

            ds.periodic();


            // Autosave every 100 cycles
            autoSaveClock++;
            if(autoSaveClock >= 100) {
                ds.save();
                autoSaveClock = 0;
            }
        }
    }

    @Override
    public void stop() {
        if(snoop) {
            ds.stop();
        }
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

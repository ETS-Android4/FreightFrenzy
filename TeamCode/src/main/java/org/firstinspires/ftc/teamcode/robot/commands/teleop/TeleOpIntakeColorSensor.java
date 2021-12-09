package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import android.annotation.SuppressLint;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.DataSnooper;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.LineColorSensor;

public class TeleOpIntakeColorSensor implements Command {
    private IntakeColorSensor colorSensor;

    private DataSnooper ds;
    private boolean snoop;

    private int autoSaveClock = 0;

    public TeleOpIntakeColorSensor(IntakeColorSensor colorSensor, boolean snoop) {
        this.colorSensor = colorSensor;
        this.snoop = snoop;

        if(this.snoop) {
            ds = new DataSnooper("intake_color_sensor");
        }
    }

    public TeleOpIntakeColorSensor(IntakeColorSensor colorSensor) {
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
            double[] val = colorSensor.getColor().val;
            snoopTxt += String.format("l:[%d,%d,%d]", (int)val[0], (int)val[1], (int)val[2]);
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

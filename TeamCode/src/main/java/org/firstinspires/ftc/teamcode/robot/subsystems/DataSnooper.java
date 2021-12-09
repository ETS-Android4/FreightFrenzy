package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Command;
import com.disnodeteam.dogecommander.Subsystem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

public class DataSnooper implements Subsystem {
    private JsonObject json;
    private String name;

    private String data;

    private ElapsedTime timer;

    private final String INIT_TEXT = "Ready!";

    public DataSnooper(String name) {
        this.name = name + new Date().getTime() + ".json";
        //file = new File(this.name);

        try {
            this.json = (JsonObject) new JsonParser()
                    .parse(ReadWriteFile
                    .readFile(AppUtil.getInstance()
                    .getSettingsFile(this.name))
            );
        } catch (Exception e){
            e.printStackTrace();
            this.json = new JsonObject();
        }



        timer = new ElapsedTime();
    }


    public void start() {
        data = INIT_TEXT;

        timer.reset();
    }

    @Override
    public void initHardware() {

    }

    @Override
    public void periodic() {
        if(data == "") return;
        String timestamp = Integer.toString((int) timer.milliseconds());
        if(this.json.has(timestamp)){
            this.json.remove(timestamp);
        }
        this.json.addProperty(timestamp, data);

        if(data == INIT_TEXT) resetData();

    }

    public void stop() {
        ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile(name), json.toString());
    }


    public void resetData() {
        data = "";
    }

    public void addData(String data) {
        this.data = data;
    }

    public void save() {
        ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile(name), json.toString());
    }
}

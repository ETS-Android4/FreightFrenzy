package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.opencv.core.Scalar;

public class IntakeColorSensor implements Subsystem {
    private ColorSensor sensor;
    private HardwareMap hardwareMap;

    private Scalar color;

    public IntakeColorSensor(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        sensor = hardwareMap.get(ColorSensor.class, "intake_color");
        color = new Scalar(0, 0, 0);
    }

    @Override
    public void periodic() {
        color = new Scalar(sensor.red(), sensor.green(), sensor.blue());

    }

    public Scalar getColor() {
        return color;
    }

}

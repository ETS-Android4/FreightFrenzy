package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Kicker implements Subsystem {
    // Hardware Map
    private HardwareMap hardwareMap;

    public Servo kicker;

    private boolean in = true;

    public enum TARGETS {
        IN,
        OUT;

        public double getTarget() {
            switch (this) {
                case IN:    return 0.5;
                case OUT:   return 1.0;
                default:    return 1.0;
            }
        }
    }

    public Kicker(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        kicker = hardwareMap.get(Servo.class, "kicker");
    }

    @Override
    public void periodic() {
        kicker.setPosition(in ? TARGETS.IN.getTarget() : TARGETS.OUT.getTarget());
    }

    public double getCurrentPos() {
        return kicker.getPosition();
    }

    public void in() {
        in = true;
    }

    public void out() {
        in = false;
    }

    public void togglePos() {
        in = !in;
    }
}

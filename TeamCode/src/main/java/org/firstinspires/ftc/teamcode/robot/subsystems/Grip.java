package org.firstinspires.ftc.teamcode.robot.subsystems;


import static org.firstinspires.ftc.teamcode.CommandDrive.STOP_USING_GRIPPER;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Grip implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private Servo grip;

    private boolean open = false;

    public enum TARGETS {
        OPEN,
        CLOSE;

        public double getTarget() {
            switch (this){
                case OPEN:  return 1;
                case CLOSE: return 0;
                default:    return 0;
            }
        }
    }
    public Grip(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        grip = hardwareMap.get(Servo.class, "grip");

        if(STOP_USING_GRIPPER) return;

        grip.setPosition(TARGETS.CLOSE.getTarget());
    }

    @Override
    public void periodic() {
        if(STOP_USING_GRIPPER) return;

        grip.setPosition(open ? TARGETS.OPEN.getTarget() : TARGETS.CLOSE.getTarget());
    }

    public double getCurrentPos() {
        return grip.getPosition();
    }

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
    }

    public void togglePos() {
        open = !open;
    }
}

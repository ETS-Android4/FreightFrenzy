package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Grip;

public class SoloTeleOpGripControl implements Command {
    private Grip grip;
    private Gamepad gamepad;

    private boolean gripToggleCheck;
    public static boolean tellArmGipToggled = false;
    public static boolean gripOpen;

    public SoloTeleOpGripControl(Grip grip, Gamepad gamepad){
        this.gamepad = gamepad;
        this.grip = grip;
    }

    @Override
    public void start() {
        grip.close();
    }

    @Override
    public void periodic() {
        gripOpen = grip.getCurrentPos() == Grip.TARGETS.OPEN.getTarget();

        if(SoloTeleOpArmControl.tellGripToToggle && !tellArmGipToggled) {
            grip.open();
            tellArmGipToggled = true;
        }
        if(!SoloTeleOpArmControl.tellGripToToggle) {
            tellArmGipToggled = false;
        }

        if(gamepad.a && gripToggleCheck) {
            grip.togglePos();
            gripToggleCheck = false;
        } else if(!gamepad.a && !gripToggleCheck){
            gripToggleCheck = true;
        }


    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

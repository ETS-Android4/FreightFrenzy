package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Grip;

public class TeleOpGripControl implements Command {
    private Grip grip;
    private Gamepad gamepad;

    private boolean gripToggleCheck;
    public static boolean tellArmGipToggled = false;
    public static boolean gripOpen;

    public TeleOpGripControl(Grip grip, Gamepad gamepad){
        this.gamepad = gamepad;
        this.grip = grip;
    }

    @Override
    public void start() {
        grip.close();
    }

    @Override
    public void periodic() {
        boolean toggleGrip = gamepad.a;
        boolean encoderIn = gamepad.y;

        gripOpen = grip.getCurrentPos() == Grip.TARGETS.OPEN.getTarget();

        if(TeleOpArmControl.tellGripToToggle && !tellArmGipToggled) {
            grip.open();
            tellArmGipToggled = true;
        }
        if(!TeleOpArmControl.tellGripToToggle) {
            tellArmGipToggled = false;
        }

        if(encoderIn) {
            grip.close();
        }

        if(toggleGrip && gripToggleCheck) {
            grip.togglePos();
            gripToggleCheck = false;
        } else if(!toggleGrip && !gripToggleCheck){
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

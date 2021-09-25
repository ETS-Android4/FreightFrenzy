package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Kicker;

public class TeleOpKickerControl implements Command {
    private Kicker kicker;
    private Gamepad gamepad;

    public TeleOpKickerControl(Kicker kicker, Gamepad gamepad){
        this.kicker = kicker;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        kicker.in();
    }

    @Override
    public void periodic() {
        if(gamepad.b){
            kicker.in();
        } else {
            kicker.out();
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

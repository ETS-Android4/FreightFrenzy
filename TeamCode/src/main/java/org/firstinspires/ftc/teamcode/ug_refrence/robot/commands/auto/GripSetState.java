package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Grip;

public class GripSetState implements Command {
    private Grip grip;

    private double state;

    public GripSetState(Grip grip, double state) {
        this.grip = grip;
        this.state = state;
    }

    public GripSetState(Grip grip, boolean toggle) {
        this.grip = grip;
        this.state = -1;

        grip.togglePos();
    }

    @Override
    public void start() {
        if(state == 0) {
            grip.close();
        }
        if(state == 1) {
            grip.open();
        }

    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}

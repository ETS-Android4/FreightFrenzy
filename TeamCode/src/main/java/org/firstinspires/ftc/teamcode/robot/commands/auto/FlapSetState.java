package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.Flap;

@Deprecated
public class FlapSetState implements Command {
    private Flap flap;

    private Flap.POSITION state;

    public FlapSetState(Flap flap, Flap.POSITION state) {
        this.flap = flap;
        this.state = state;
    }

    public FlapSetState(Flap flap, boolean toggle) {
        this.flap = flap;
        this.state = null;
    }


    @Override
    public void start() {
        if(state != null && state == Flap.POSITION.CLOSE) {
            flap.close();
        }
        if(state != null && state == Flap.POSITION.OPEN) {
            flap.close();
        }
        if(state == null) {
            flap.toggle();
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

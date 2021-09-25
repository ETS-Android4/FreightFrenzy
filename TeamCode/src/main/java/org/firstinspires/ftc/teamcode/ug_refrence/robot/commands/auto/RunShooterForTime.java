package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Shooter;

public class RunShooterForTime implements Command {
    private Shooter shooter;

    private ElapsedTime timer;
    private double seconds;
    private double power;
    private boolean stop;
    private Shooter.MODE mode;

    public RunShooterForTime(Shooter shooter, double seconds, double power, Shooter.MODE mode){
        this.shooter = shooter;
        this.seconds = seconds;
        this.power = power;
        this.mode = mode;

        stop = true;

        timer = new ElapsedTime();
    }

    public RunShooterForTime(Shooter shooter, boolean stopAtEnd, double power, Shooter.MODE mode){
        this.shooter = shooter;
        this.power = power;
        seconds = 0;
        this.mode = mode;

        stop = false;

        timer = new ElapsedTime();
    }

    public RunShooterForTime(Shooter shooter, double seconds, double power){
        this(shooter, seconds, power, Shooter.MODE.POWER);
    }

    public RunShooterForTime(Shooter shooter, boolean stopAtEnd, double power){
        this(shooter, stopAtEnd, power, Shooter.MODE.POWER);
    }

    @Override
    public void start() {
        timer.reset();
        if(mode.getType())
            shooter.setPower(-power);
        else
            shooter.setVelocity(-power);
        shooter.setShoot(true);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        if(stop) {shooter.setPower(0); shooter.setVelocity(0); shooter.setShoot(false);}
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}

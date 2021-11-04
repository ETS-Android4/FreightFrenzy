package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class StrafeByTimer implements Command {
    private Drive drive;

    private ElapsedTime timer;
    private double seconds;
    private double power;

    public StrafeByTimer(Drive drive, double seconds, double power){
        timer = new ElapsedTime();

        this.drive = drive;
        this.seconds = seconds;
        this.power = power;
    }

    @Override
    public void start(){
        timer.reset();
        drive.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setDiagonalPower(power,-power);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop(){
        drive.setDiagonalPower(0,0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}

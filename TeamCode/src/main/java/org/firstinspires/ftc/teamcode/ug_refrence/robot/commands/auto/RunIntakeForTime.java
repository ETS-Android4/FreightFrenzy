package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Intake;

public class RunIntakeForTime implements Command {
    private Intake intake;

    private ElapsedTime timer;
    private double seconds;
    private double power;

    public RunIntakeForTime(Intake intake, double seconds, double power){
        this.intake = intake;
        this.seconds = seconds;
        this.power = power;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setPower(power);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        intake.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}

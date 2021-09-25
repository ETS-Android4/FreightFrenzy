package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

public class RunTransferForTime implements Command {
    private Transfer transfer;

    private ElapsedTime timer;
    private double seconds;
    private double power;

    public RunTransferForTime(Transfer transfer, double seconds, double power){
        this.transfer = transfer;
        this.seconds = seconds;
        this.power = power;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
        transfer.setDirection(DcMotorSimple.Direction.FORWARD);
        transfer.setPower(power);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        transfer.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}

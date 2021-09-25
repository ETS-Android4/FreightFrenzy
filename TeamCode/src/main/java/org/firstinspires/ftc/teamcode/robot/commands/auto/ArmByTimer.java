package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

public class ArmByTimer implements Command {
    private Arm wobble;

    private ElapsedTime timer;
    private double seconds;
    private double power;

    public ArmByTimer(Arm wobble, double seconds, double power){
        timer = new ElapsedTime();

        this.wobble = wobble;
        this.seconds = seconds;
        this.power = power;
    }

    @Override
    public void start(){
        timer.reset();
        wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wobble.setPower(power);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop(){
        wobble.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}

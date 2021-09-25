package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

@Deprecated
public class TurnByGyro implements Command {
    private Drive drive;

    private ElapsedTime timer;
    private Telemetry telemetry;
    private double timeout;
    private double power;

    private double target;

    private double newPower;

    private final double K_P = 0.012;

    public TurnByGyro(Drive drive, double degrees, double power, double timeout, Telemetry telemetry){
        this.drive = drive;
        this.target = degrees;
        this.power = power;
        this.timeout = timeout;

        timer = new ElapsedTime();
        this.telemetry = telemetry;
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void periodic() {
        double error = normalize180(target - drive.heading());
        double correction = error * K_P;

        newPower = power * correction;
        newPower = Range.clip(newPower,-1,1);
        drive.setPower(-newPower, newPower);

        telemetry.addData("drive.heading()", drive.heading());
        telemetry.addData("power", power);
        telemetry.addData("newPower", newPower);
        telemetry.update();
    }

    @Override
    public void stop() {
        drive.setPower(0,0);
    }

    @Override
    public boolean isCompleted() {
        return Range.clip(drive.heading(),target+1,target-1) == drive.heading() || timer.seconds() > timeout;
    }



    public double normalize180(double angle) {
        while(angle > 180) {
            angle -= 360;
        }
        while(angle <= -180) {
            angle += 360;
        }
        return angle;
    }
}

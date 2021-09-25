package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class StrafeByEncoder implements Command {
    private Drive drive;

    private ElapsedTime timer;
    private double power;
    private int counts;
    private double timeout;

    private final double DEFAULT_TIMEOUT = 5.0;
    private DcMotor.RunMode prevRunMode;

    private final double DRIVE_SPEED = 0.6;
    private final double MAX_DRIVE_SPEED = Math.min(DRIVE_SPEED + 0.1, 1.0);
    private final double K_P = 0.03; // Proportional coefficient for gyro-controlled driving

    double target;
    double heading;
    double error;
    double correction;
    double newFlRrPower;
    double newFrRlPower;

    // Created by Blake to hold reference to gamepad for cancelling commmand (see isCompleted() definition)
//    private Gamepad gamepad;

    public StrafeByEncoder(Drive drive, int counts, double target, double power, double timeout){
        timer = new ElapsedTime();

        this.drive = drive;
        this.power = power;
        this.counts = counts;
        this.timeout = timeout;
    }

    public StrafeByEncoder(Drive drive, int counts, double target, double power){
        timer = new ElapsedTime();

        this.drive = drive;
        this.power = power;
        this.counts = counts;
        this.timeout = DEFAULT_TIMEOUT;
    }

    // Created by Blake to pass in gamepad inputs for terminating the command (see isCompleted() definition)
    public StrafeByEncoder(Drive drive, int counts, double target, double power, Gamepad gamepad) {
        timer = new ElapsedTime();

        this.drive = drive;
        this.power = power;
        this.counts = counts;
        this.timeout = DEFAULT_TIMEOUT;

//        this.gamepad = gamepad;
    }

    @Override
    public void start(){
        timer.reset();

        int[] currentPos = drive.getCurrentPositions();
        prevRunMode = drive.getRunMode();
        drive.setTargets(
                currentPos[0] + counts,
                currentPos[1] - counts,
                currentPos[2] - counts,
                currentPos[3] + counts
        );
        drive.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.setDiagonalPower(power,power);
    }

    @Override
    public void periodic() {
        heading = drive.heading();
        error   = target - heading;
        correction = error * K_P;
        newFlRrPower = Math.min((power + correction), MAX_DRIVE_SPEED);
        newFrRlPower = Math.min((power - correction), MAX_DRIVE_SPEED);
//        drive.setPower(newFlRrPower, newFrRlPower); Controller is malfunctioning; temporarily disabled
    }

    @Override
    public void stop(){

        drive.setRunMode(prevRunMode);
        drive.setPower(0,0);
    }

    @Override
    public boolean isCompleted() {
        return (!drive.driveIsBusy() || timer.seconds() > timeout /*|| gamepad.a*/);    // FIXME: remove gamepad.a condition before competition --Blake
    }
}

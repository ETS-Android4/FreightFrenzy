package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Drive;

public class DriveByEncoder implements Command {
    private Drive drive;

    private ElapsedTime timer;
    private double power;
    private int counts;
    private double timeout;

    private static final double DEFAULT_TIMEOUT = 5.0;
    private DcMotor.RunMode prevRunMode;

    private final double DRIVE_SPEED = 0.6;
    private final double MAX_DRIVE_SPEED = Math.min(DRIVE_SPEED + 0.1, 1.0);
    private final double K_P = 0.01; // Proportional coefficient for gyro-controlled driving

    double target;
    double heading;
    double error;
    double correction;
    double newLeftPower;
    double newRightPower;

    private Telemetry telemetry;


    public DriveByEncoder(Drive drive, int counts, double target, double power, double timeout, Telemetry telemetry){
        timer = new ElapsedTime();

        this.drive = drive;
        this.power = power; //* -1.0;  // Negate the power to drive the correct direction
        this.counts = counts;
        this.target = target;
        this.timeout = timeout;
        this.telemetry = telemetry;
    }

    public DriveByEncoder(Drive drive, int counts, double target, double power) {
        this(drive, counts, target, power, DEFAULT_TIMEOUT, null);
    }

    public DriveByEncoder(Drive drive, int counts, double target, double power, double timeout) {
        this(drive, counts, target, power, timeout, null);
    }

    public DriveByEncoder(Drive drive, int counts, double target, double power, Telemetry telemetry) {
        this(drive, counts, target, power, DEFAULT_TIMEOUT, telemetry);
    }

    @Override
    public void start(){
        timer.reset();

        int[] currentPos = drive.getCurrentPositions();
        prevRunMode = drive.getRunMode();
        drive.setTargets(
                currentPos[0] + counts,
                currentPos[1] + counts,
                currentPos[2] + counts,
                currentPos[3] + counts
        );
        drive.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPower(power,power);

        if(telemetry != null) {
            for(int i = 0; i < drive.getCurrentPositions().length; i++)
                telemetry.addData("Target " + i, drive.getCurrentPositions()[i]);
            telemetry.addData("Counts", counts);
            telemetry.addData("Prev Run Mode", prevRunMode);
            telemetry.addData("Curr Run Mode", drive.getRunMode());
            telemetry.addData("Power", power);
            telemetry.update();
        }
    }

    @Override
    public void periodic() {
        heading = drive.heading();
        error   = target - heading;
        correction = error * K_P;
        newLeftPower = Math.min((power - correction), MAX_DRIVE_SPEED);
        newRightPower = Math.min((power + correction), MAX_DRIVE_SPEED);
        drive.setPower(newLeftPower,newRightPower);

        if(telemetry != null) {
            telemetry.addData("Timer", timer);
            telemetry.addData("Heading", heading);
            telemetry.addData("Target", target);
            telemetry.addData("Error", error);
            telemetry.addData("Correction", correction);
            telemetry.addData("New Left", newLeftPower);
            telemetry.addData("New Right", newRightPower);
            telemetry.addData("Max Drive", MAX_DRIVE_SPEED);
            telemetry.update();
        }
    }

    @Override
    public void stop(){
        //drive.reverseFlDrive();

//        drive.setRunMode(prevRunMode);
        drive.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.setPower(0,0);
    }

    @Override
    public boolean isCompleted() {
        return (!drive.driveIsBusy() || timer.seconds() > timeout);
    }
}



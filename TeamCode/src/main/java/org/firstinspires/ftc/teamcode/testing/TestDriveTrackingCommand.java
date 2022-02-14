package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner_util.Encoder;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.swampbots_util.TrackRobotPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

public class TestDriveTrackingCommand implements Command {
    private Drive drive;
    private Gamepad gamepad;
    private Telemetry telemetry;
    private TrackRobotPosition tracker;

    private ArrayList<Encoder> encoders = new ArrayList<>(4);


    // Constructor
    public TestDriveTrackingCommand(Drive drive, Gamepad gamepad, Telemetry telemetry) {
        this.drive = drive;
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        for(DcMotorEx m : drive.getMotors()) {
            encoders.add(new Encoder(m));
        }

        tracker = new TrackRobotPosition(drive, this.telemetry);
    }
    public TestDriveTrackingCommand(Drive drive, Gamepad gamepad) {
        this(drive ,gamepad, null);
    }

    @Override
    public void start() {
        drive.setPower(0,0);

        tracker.start();

    }

    @Override
    public void periodic() {
        double speed = gamepad.right_trigger - gamepad.left_trigger;
        double turn = gamepad.left_stick_x;
        boolean goSlow = gamepad.left_bumper || gamepad.right_bumper;

        drive.setRocketLeaguePower(speed, turn, goSlow);

        double[] velocities = encoders.stream().
                flatMapToDouble(encoder -> DoubleStream.of(encoder.getCorrectedVelocity()))
                .toArray();

        tracker.periodic(velocities, drive.heading());

        if(telemetry != null) {
            telemetry.addLine("Drive Telemetry:");
            telemetry.addData("speed:", speed);
            telemetry.addData("turn:", turn);
            telemetry.addData("goSlow?", goSlow);
            telemetry.addLine();
            telemetry.addData("FL Target:", drive.getCurrentPositions()[0]);
            telemetry.addData("FR Target:", drive.getCurrentPositions()[1]);
            telemetry.addData("RL Target:", drive.getCurrentPositions()[2]);
            telemetry.addData("RR Target:", drive.getCurrentPositions()[3]);
            telemetry.addLine();
            telemetry.addData("FL Velo:", drive.getVelocities()[0]);
            telemetry.addData("FR Velo:", drive.getVelocities()[1]);
            telemetry.addData("RL Velo:", drive.getVelocities()[2]);
            telemetry.addData("RR Velo:", drive.getVelocities()[3]);
            telemetry.addLine();
            telemetry.addData("heading:", drive.heading());
            telemetry.update();

        }

    }

    @Override
    public void stop() {
        drive.setPower(0,0);
    }

    @Override
    public boolean isCompleted(){
        return false;
    }


















}

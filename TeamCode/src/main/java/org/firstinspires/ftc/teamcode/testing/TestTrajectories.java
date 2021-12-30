package org.firstinspires.ftc.teamcode.testing;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.arcrobotics.ftclib.command.RamseteCommand;
import com.arcrobotics.ftclib.controller.PController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.RamseteController;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.ChassisSpeeds;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.DifferentialDriveKinematics;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.DifferentialDriveWheelSpeeds;
import com.arcrobotics.ftclib.trajectory.Trajectory;
import com.arcrobotics.ftclib.trajectory.TrajectoryConfig;
import com.arcrobotics.ftclib.trajectory.TrajectoryGenerator;
import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTrajectory;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.swampbots_util.Units;

import java.util.ArrayList;

@Autonomous(name = "Test Trajectories", group = "testing")
public class TestTrajectories extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;
    private RamseteController controller;
    private DifferentialDriveKinematics driveKinematics;
    private PIDFController pidfController = new PIDFController(0.35, 0.02, 0.5, 0);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);

        controller = new RamseteController(1,0.1);
        driveKinematics = new DifferentialDriveKinematics(Units.feetToMeters(12.9));

        Pose2d start = new Pose2d(Units.feetToMeters(2.0), Units.feetToMeters(3.5), Rotation2d.fromDegrees(90));
        Pose2d basicMove = new Pose2d(Units.feetToMeters(1.4), Units.feetToMeters(9.7), Rotation2d.fromDegrees(-30));

        ArrayList<Translation2d> interiorWaypoints = new ArrayList<>();
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(-4.3), Units.feetToMeters(2.6)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(4.2), Units.feetToMeters(5)));

        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(5), Units.feetToMeters(5));
        config.setReversed(true);

        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                start,
                interiorWaypoints,
                basicMove,
                config
        );

        commander.init();

        sleep(500);

        commander.runCommand(new DriveByTrajectory(drive, trajectory, controller, telemetry));

//
//        Trajectory.State goal = trajectory.sample(1.2);
//        ChassisSpeeds adjSpeeds = controller.calculate(start, goal);
//        DifferentialDriveWheelSpeeds wheelSpeeds = driveKinematics.toWheelSpeeds(adjSpeeds);
//
//        double leftSpeed = wheelSpeeds.leftMetersPerSecond;
//        double rightSpeed = wheelSpeeds.rightMetersPerSecond;
//
//        double leftPower = pidfController.calculate(leftSpeed);
//        double rightPower = pidfController.calculate(rightSpeed);

    }
}

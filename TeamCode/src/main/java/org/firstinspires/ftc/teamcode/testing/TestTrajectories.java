package org.firstinspires.ftc.teamcode.testing;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.acmerobotics.dashboard.config.Config;
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

@Config
@Autonomous(name = "Test Trajectories", group = "testing")
public class TestTrajectories extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;
    private RamseteController controller;

    public static double ax0 = 0.0;
    public static double ay0 = 0.0;
    public static double atheta0 = 0.0;
    public static double bx1 = 1.0;
    public static double by1 = 0.0;
    public static double cxf = 2.0;
    public static double cyf = 0.0;
    public static double cthetaf = -90.0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);


        controller = new RamseteController(0.3,0.1);




        Pose2d start = new Pose2d(Units.feetToMeters(ax0), Units.feetToMeters(ay0), Rotation2d.fromDegrees(atheta0));
        Pose2d end = new Pose2d(Units.feetToMeters(cxf), Units.feetToMeters(cyf), Rotation2d.fromDegrees(cthetaf));

        ArrayList<Translation2d> interiorWaypoints = new ArrayList<>();
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(bx1), Units.feetToMeters(by1)));

        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(50), Units.feetToMeters(50));
        config.setReversed(false);

        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                start,
                interiorWaypoints,
                end,
                config);

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.addData("Total time", trajectory.getTotalTimeSeconds());
        telemetry.update();

        waitForStart();

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

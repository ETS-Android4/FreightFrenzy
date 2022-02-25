package org.firstinspires.ftc.teamcode.testing;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.wpilibcontroller.RamseteController;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
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
import java.util.List;

@Config
@Autonomous(name = "Test Trajectories", group = "testing")
public class TestTrajectories extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;
    private RamseteController controller;

    public static double ax0 = 0.0;
    public static double ay0 = 0.0;
    public static double atheta0 = 0.0;
    public static double bx1 = 2.0;
    public static double by1 = 0.0;
    public static double cxf = 4.0;
    public static double cyf = 0.0;
    public static double cthetaf = 0.0;
    public static double maxAcc = 2.0;
    public static double maxAngAcc = 1.0;
    public static double rBeta = 0.3;
    public static double rZeta = 0.1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);


        controller = new RamseteController(rBeta, rZeta);




        Pose2d start = new Pose2d(Units.feetToMeters(ax0), Units.feetToMeters(ay0), Rotation2d.fromDegrees(atheta0));
        Pose2d end = new Pose2d(Units.feetToMeters(cxf), Units.feetToMeters(cyf), Rotation2d.fromDegrees(cthetaf));

        List<Translation2d> interiorWaypoints = new ArrayList<>();
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(bx1), Units.feetToMeters(by1)));

        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(maxAcc), Units.feetToMeters(maxAngAcc));
        config.setReversed(false);

        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                start,
                interiorWaypoints,
                end,
                config);

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.addData("Total time:", trajectory.getTotalTimeSeconds());
        telemetry.addData("States:",trajectory.getStates());
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

        commander.stop();

    }
}

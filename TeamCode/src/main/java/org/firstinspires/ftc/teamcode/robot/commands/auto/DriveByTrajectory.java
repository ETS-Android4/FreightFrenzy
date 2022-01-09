package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.arcrobotics.ftclib.controller.wpilibcontroller.RamseteController;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.ChassisSpeeds;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.DifferentialDriveWheelSpeeds;
import com.arcrobotics.ftclib.trajectory.Trajectory;
import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

import java.util.List;
import java.util.Locale;

public class DriveByTrajectory implements Command {
    private Drive drive;

    private ElapsedTime timer;
    private double t0;

    private Trajectory trajectory;
    private Telemetry telemetry;
    private RamseteController controller;

    public DriveByTrajectory(Drive drive, Trajectory trajectory, RamseteController controller, Telemetry telemetry) {
        this.drive = drive;
        this.trajectory = trajectory;
        this.controller = controller;
        this.telemetry = telemetry;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        drive.setPower(0, 0);

        timer.reset();
        t0 = timer.seconds();

        if(telemetry != null) {
            telemetry.addData("traj @ t=0", trajectory.sample(0));
            telemetry.update();
        }
    }

    @Override
    public void periodic() {
        double t1 = timer.seconds();

        Pose2d currentPose = trajectory.sample(t0).poseMeters;
        Trajectory.State goalState = trajectory.sample(t1);
        ChassisSpeeds adjSpeeds = controller.calculate(currentPose, goalState);

        DifferentialDriveWheelSpeeds wheelSpeeds = drive.getKinematics().toWheelSpeeds(adjSpeeds);
        drive.setFeedforwardPower(wheelSpeeds);


        if(telemetry != null) {
            telemetry.addLine("Drive by Trajectory Telemetry:");
            telemetry.addLine(String.format(Locale.ENGLISH, "current (x,y,Θ) = (%.2f, %.2f, %.2f)", currentPose.getX(), currentPose.getY(), currentPose.getRotation().getDegrees()));
            telemetry.addLine(String.format(Locale.ENGLISH, "target (x,y,Θ) = (%.2f, %.2f, %.2f)", goalState.poseMeters.getX(), goalState.poseMeters.getY(), goalState.poseMeters.getRotation().getDegrees()));
            telemetry.addLine(String.format(Locale.ENGLISH, "target (v,a,κ) = (%.3f, %.3f, %.3f)", goalState.velocityMetersPerSecond, goalState.accelerationMetersPerSecondSq, goalState.curvatureRadPerMeter));
            telemetry.addLine();
            telemetry.addLine(String.format(Locale.ENGLISH, "Adjusted Speeds (l,r) = (%.4f, %.4f)", wheelSpeeds.leftMetersPerSecond, wheelSpeeds.rightMetersPerSecond));
            telemetry.addLine();
            telemetry.addLine(String.format(Locale.ENGLISH, "Time (t0,t1,Δt) = (%.5f, %.5f, %.5f)", t0, t1, (t1 - t0)));
            telemetry.update();

        }

        t0 = t1;
    }

    @Override
    public void stop() {

        drive.setPower(0, 0);
    }

    @Override
    public boolean isCompleted() {
        Trajectory.State currentState = trajectory.sample(timer.seconds());
        List<Trajectory.State> allStates = trajectory.getStates();
        Trajectory.State lastState = allStates.get(allStates.size() - 1);


        return lastState.equals(currentState) || timer.seconds() >= trajectory.getTotalTimeSeconds();
    }
}

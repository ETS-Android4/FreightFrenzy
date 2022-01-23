package org.firstinspires.ftc.teamcode.swampbots_util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.disnodeteam.dogecommander.Command;
import com.sun.tools.javac.util.Pair;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.swampbots_util.Units;

import java.util.Arrays;
import java.util.Locale;

@Config
public class TrackRobotPosition {
    private Drive drive;
    private Pose2d initialPos;
    private Telemetry telemetry;

    private Pose2d currentPos;
    private double t0;
    private Rotation2d theta;
    private double t1;
    private double velocity;
    private Pair<int[], int[]> encoderPositions;

    public static double scaleVelocity = 3.0;

    public TrackRobotPosition(Drive drive, Pose2d initialPos, Telemetry telemetry) {
        this.drive = drive;
        this.initialPos = initialPos;
        this.telemetry = telemetry;

    }

    public TrackRobotPosition(Drive drive, Pose2d initialPos) {
        this(drive, initialPos, null);
    }

    public TrackRobotPosition(Drive drive, Telemetry telemetry) {
        this(drive, new Pose2d(new Translation2d(0,0), new Rotation2d(0)), telemetry);
    }

    public TrackRobotPosition(Drive drive) {
        this(drive, new Pose2d(new Translation2d(0,0), new Rotation2d(0)), null);
    }

    public void start() {
        currentPos = initialPos;
        theta = initialPos.getRotation();

        encoderPositions = new Pair<>(drive.getCurrentPositions(), new int[]{0, 0, 0, 0});

        t0 = 0;
    }

    /**
     * Update the robot to track it's position
     *
     * @param time timestamp of when the data was pushed
     * @param velocity current velocity of the robot
     * @param angle angle of the robot in Radians
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void periodic(double time, double velocity, float angle) {
        double deltaT = time - t0;

        // Change in angle of rotation
        theta = new Rotation2d(angle);
        Rotation2d deltaTheta = theta.minus(currentPos.getRotation());

        // Parameterize velocity into x and y components
        double vx = velocity * theta.getCos();
        double vy = velocity * theta.getSin();
        double deltaX = deltaT * vx;
        double deltaY = deltaT * vy;
        Translation2d deltaPos = new Translation2d(deltaX, deltaY);

//        currentPos.plus(new Transform2d(deltaPos, deltaTheta));
        currentPos = new Pose2d(currentPos.getTranslation().plus(deltaPos), currentPos.getRotation().plus(deltaTheta));

        if(telemetry != null) {
            telemetry.addLine("Robot Tracker Data:");
            telemetry.addLine(String.format(Locale.ENGLISH, "current (x,y,Θ) = (%.2f, %.2f, %.2f)", currentPos.getX(), currentPos.getY(), currentPos.getRotation().getDegrees()));
            telemetry.addLine(String.format(Locale.ENGLISH, "velocity (v,vx,vy) = (%.4f, %.4f, %.4f)", velocity, vx, vy));
            telemetry.addLine(String.format(Locale.ENGLISH, "change (Δx,Δy,ΔΘ) = (%.4f, %.4f, %.4f)", deltaX, deltaY, deltaTheta.getDegrees()));
            telemetry.addData("theta.sin()", theta.getSin());
            telemetry.addData("theta.cos()", theta.getCos());
            telemetry.addData("deltaTheta", deltaTheta);
            telemetry.addData("deltaPos", deltaPos);
            telemetry.addLine();
            telemetry.addLine(String.format(Locale.ENGLISH, "Time (t0,t1,Δt) = (%.5f, %.5f, %.5f)", t0, t1, deltaT));
            telemetry.addLine(); // We don't update telemetry here because we update it in main program
        }
        t0 = time;
    }

    /**
     * Update the robot to track it's position
     *
     * @param time timestamp of when the data was pushed
     * @param encoderPos current encoder values of the robot [fl, fr, rl, rr]
     * @param angle angle of the robot in Radians
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void periodic(double time, int[] encoderPos, float angle) {
        double deltaT = time - t0;

        int[] deltaEncoder = new int[4];
        for (int i = 0; i < 4; i++) {
            // Swap array values
            encoderPositions.fst[i] = encoderPositions.snd[i];
            encoderPositions.snd[i] = encoderPos[i];

            deltaEncoder[i] = encoderPositions.snd[i] - encoderPositions.fst[i];
        }

        velocity = Arrays.stream(deltaEncoder).sum() / Drive.NUMBER_OF_ENCODERS / deltaT; // Average encoders correspond to velocity (Counts / Sec)
        velocity = Units.inchesToMeters(velocity / Drive.COUNTS_PER_INCH_EMPIRICAL * scaleVelocity); // Fix units (Counts / Sec => Inch / Sec => m/s)

        periodic(time, velocity, angle);
    }

    public void stop() {

    }

    public boolean isCompleted() {
        return false;
    }

    public Pose2d getPose() {
        return currentPos;
    }

    public Translation2d getXY() {
        return currentPos.getTranslation();
    }

    public Rotation2d getTheta() {
        return currentPos.getRotation();
    }

    public double getX() {
        return currentPos.getX();
    }

    public double getY() {
        return currentPos.getY();
    }
}

package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.swampbots_util.SynchronousPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;


/**
 * This command turns the robot to a given target heading.
 * The motor power is regulated by a PID controller with
 * error calculated from the current robot heading.
 *
 * @author Blake (mentor-coded gang)
 */
@Config
public class TurnByGyroPID implements Command {

    // Subsystem(s)
    private Drive drive;

    // Private members
    private Telemetry telemetry;
    private Gamepad gamepad;

    private double target;
    private double power;



    // PID controller
    public static double MAX_SPEED = 0.6;    // Maximum motor power
    public static double kP = 0.005;          // Proportional coefficient
    public static double kI = 0.0;            // Integral coefficient
    public static double kD = 0.0;            // Derivative coefficient
    public static double TOLERANCE = 2.7;    // Allowable error ("close-enough" factor) for when we can stop
    SynchronousPID pid = new SynchronousPID(kP, kI, kD);

    private double error = TOLERANCE + 1.0; // Default value to clear first isCompleted() check (otherwise is 0.0, so isCompleted() returns true immediately)

    // Constructors
    public TurnByGyroPID(Drive drive, Telemetry telemetry, /*Gamepad gamepad,*/ double target, double power) {
        this.drive = drive;

        this.telemetry = telemetry;
//        this.gamepad = gamepad;
        
        this.target = target;
        this.power = power;
    }

    public TurnByGyroPID(Drive drive, Telemetry telemetry, Gamepad gamepad, double target, double power,
                         double p, double i, double d) {
        this.drive = drive;

        this.telemetry = telemetry;
        this.gamepad = gamepad;

        this.target = target;
        this.power = power;

        pid.setPID(p, i, d);
    }

    public TurnByGyroPID(Drive drive, double target, double power,
                         double p, double i, double d) {
        this.drive = drive;

        this.target = target;
        this.power = power;

        pid.setPID(p, i, d);
    }
    public TurnByGyroPID(Drive drive, double target, double power) {
        this.drive = drive;

        this.target = target;
        this.power = power;
    }
    

    // Overridden methods from Command interface
    @Override
    public void start() {
        pid.setSetpoint(target);                     // Set target final heading
        pid.setOutputRange(-MAX_SPEED, MAX_SPEED);   // Set maximum motor power
        pid.setDeadband(TOLERANCE);                  // Set how far off you can safely be from your target

        drive.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void periodic() {
        error = drive.normalize180(target - drive.heading());
        power = pid.calculateGivenError(error);

        telemetry.addLine("Turning...");
        telemetry.addLine("Press Y to stop.");
        telemetry.addLine();
        telemetry.addData("Target", target);
        telemetry.addData("Current error", error);
        telemetry.addData("Current power", power);
        telemetry.addLine();
        telemetry.addData("Drive run mode", drive.getRunMode());

        drive.setPower(-power, power);

        telemetry.update();
    }

    @Override
    public void stop() {
        drive.setPower(0, 0);
    }

    @Override
    public boolean isCompleted() {  // FIXME: remove gamepad.y condition before competition
        return Math.abs(error) < TOLERANCE /*|| (gamepad !=null && gamepad.y)*/;
    }
}

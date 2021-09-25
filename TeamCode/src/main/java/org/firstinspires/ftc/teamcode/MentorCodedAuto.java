package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

/**
 * Autonomous for meet 2
 *
 * @author Blake (Mentor-coded gang)
 */
@Disabled
@Autonomous(name = "Autonomous (mentor-coded gang)", group = "_Competition")
public class MentorCodedAuto extends LinearOpMode implements DogeOpMode {
    private boolean POWER_SHOT = false;
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap,true);
        Kicker kicker           = new Kicker(hardwareMap);
        Shooter shooter         = new Shooter(hardwareMap);
        Arm arm                 = new Arm(hardwareMap);
        Grip grip               = new Grip(hardwareMap);
        Intake intake           = new Intake(hardwareMap);
        Transfer transfer       = new Transfer(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(shooter);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(transfer);


        commander.init();

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();



        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget()));





        telemetry.addLine("Driving to shooting line");
        telemetry.update();
        commander.runCommandsParallel(
                new RunShooterForTime(shooter,false, 0.85),
                new DriveByEncoder(drive, InchToCount(60), 0, 0.5, telemetry)
                );
        sleep(200);

        telemetry.addLine("Turning and shooting");
        telemetry.update();
        double turnPower = 0.45;
        commander.runCommand(new TurnByGyroPID(drive, telemetry, 10, turnPower));
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 15, turnPower));
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 20, turnPower));
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);

        telemetry.addLine("Powering down shooter");
        telemetry.update();
        commander.runCommandsParallel(
                new RunShooterForTime(shooter, true, 0.0),
                new StrafeByEncoder(drive, InchToCount(40), 0,.4));

        // Put arm out
        telemetry.addLine("Wobble Goal");
        telemetry.update();
        sleep(100); // Pause to settle before taking drive.heading() reading

        commander.runCommand(new DriveByEncoder(drive, InchToCount(16.0), drive.heading(),  0.4));

//        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
//        sleep(1000);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
        sleep(500);

//        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
//        sleep(1000);

        commander.stop();
    }

    private int InchToCount(double inch){return (int)(inch*Drive.COUNTS_PER_INCH_EMPIRICAL);}
}

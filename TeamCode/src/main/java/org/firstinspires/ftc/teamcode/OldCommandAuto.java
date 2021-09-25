package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByEncoder;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

@Disabled
@Deprecated
@Autonomous(name = "Old Command Auto", group = "old")
public class OldCommandAuto extends LinearOpMode implements DogeOpMode {
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

        telemetry.addLine("Started");
        telemetry.update();

        // Drive to shooting position
        commander.runCommandsParallel(
                new RunShooterForTime(shooter,false, 0.85),
                new DriveByEncoder(drive, InchToCount(60), 0, 0.5, telemetry)
                );

        sleep(200);


        telemetry.addLine("Shot 1");
        telemetry.update();
        //commander.runCommand(new TurnByGyro(drive, 20, .5, 5, telemetry));
        commander.runCommand(new StrafeByEncoder(drive, -InchToCount(16), 0,.35));
        sleep(1200);
//        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
//        sleep(500);
//        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
//        sleep(500);

        telemetry.addLine("Shot 2");
        telemetry.update();
        //commander.runCommand(new TurnByGyro(drive, 35, .3, 5, telemetry));
        commander.runCommand(new StrafeByEncoder(drive, -InchToCount(7.5), 0,.3));
        sleep(1200);
//        sleep(1500);
//        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
//        sleep(500);
//        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));


        telemetry.addLine("Shot 3");
        telemetry.update();
//        commander.runCommand(new TurnByGyro(drive, 50, .3, 5, telemetry));
        commander.runCommand(new StrafeByEncoder(drive, -InchToCount(6.5), 0,.3));
        sleep(1200);
//        sleep(1500);
//        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
//        sleep(500);
//        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        commander.runCommandsParallel(
                new RunShooterForTime(shooter, true, 0.0),
                new StrafeByEncoder(drive, InchToCount(40), 0,.4)); // Power down shooter




//        commander.runCommand(new TurnByGyro(drive, -50, .3, 5, telemetry));
        sleep(500);
        // Put arm out
        telemetry.addLine("Wobble Goal");
        telemetry.update();

        commander.runCommand(new ArmByEncoder(arm, 0, Arm.TARGETS.DOWN.getTarget(), 1.0));  // Bring wobble arm up)
        sleep(2000);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
        sleep(300);

        // Drive back to line
        commander.runCommand(new DriveByTimer(drive, 0.5,0.3));

        commander.stop();
    }

    private int InchToCount(double inch){return (int)(inch*Drive.COUNTS_PER_INCH_EMPIRICAL);}
}

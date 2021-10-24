package org.firstinspires.ftc.teamcode.ug_refrence.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Transfer;

@Disabled
@Autonomous
public class TestDogeAuto extends LinearOpMode implements DogeOpMode {
    public void runOpMode(){
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

        // Run before start
//        commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget()));
//        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget(), 10.0));

        telemetry.addLine("ready");
        telemetry.update();

        waitForStart();

        telemetry.addLine("after start");
        telemetry.update();

        commander.runCommandsParallel(
                new RunShooterForTime(shooter,false, 0.75),
                new DriveByTimer(drive,1,-0.3, telemetry)
        );
        telemetry.addLine("after drive");
        telemetry.update();
//        commander.runCommand(new DriveByTimer(drive,0.1,0));
//
//        telemetry.addLine("after 2nd drive");
//        telemetry.update();

        sleep(2000);




        commander.stop();
    }



}

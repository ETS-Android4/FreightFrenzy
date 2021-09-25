package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByTimer;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

@Disabled
@Autonomous
public class TestAutoStrafe extends LinearOpMode implements DogeOpMode {
    //private DcMotor shooterMotor;
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);
//        Kicker kicker = new Kicker(hardwareMap);
//        Shooter shooter         = new Shooter(hardwareMap);
        //Arm arm = new Arm(hardwareMap);
        //Grip grip = new Grip(hardwareMap);
        //Intake intake = new Intake(hardwareMap);
        //Transfer transfer = new Transfer(hardwareMap);

        commander.registerSubsystem(drive);
//        commander.registerSubsystem(kicker);
//        commander.registerSubsystem(shooter);
        //commander.registerSubsystem(arm);
        //commander.registerSubsystem(grip);
        //commander.registerSubsystem(intake);
        //commander.registerSubsystem(transfer);

        //shooterMotor = hardwareMap.get(DcMotor.class, "shooter");

        commander.init();

        waitForStart();

//        telemetry.addData("Fl0",drive.getCurrentPositions()[0]);
//        telemetry.addData("Fr0",drive.getCurrentPositions()[1]);
//        telemetry.addData("Rl0",drive.getCurrentPositions()[2]);
//        telemetry.addData("Rr0",drive.getCurrentPositions()[3]);
//        telemetry.update();
//
//        telemetry.addData("Fl0",drive.getCurrentPositions()[0]);
//        telemetry.addData("Fr0",drive.getCurrentPositions()[1]);
//        telemetry.addData("Rl0",drive.getCurrentPositions()[2]);
//        telemetry.addData("Rr0",drive.getCurrentPositions()[3]);
//        telemetry.addLine();
        commander.runCommand(new StrafeByTimer(drive,0.7,0.3));

//        telemetry.addData("Fl0",drive.getCurrentPositions()[0]);
//        telemetry.addData("Fr0",drive.getCurrentPositions()[1]);
//        telemetry.addData("Rl0",drive.getCurrentPositions()[2]);
//        telemetry.addData("Rr0",drive.getCurrentPositions()[3]);
//        telemetry.update();
//        commander.runCommand(new RunShooterForTime(shooter,false,.8));
//        sleep(3000);
//
//        commander.runCommand(new KickerSetState(kicker,1,2));
//        sleep(3000);
//        commander.runCommand(new KickerSetState(kicker,0,2));
//        sleep(3000);
//
//        commander.runCommand(new KickerSetState(kicker,1,2));
//        sleep(3000);
//        commander.runCommand(new KickerSetState(kicker,0,2));
//        sleep(3000);
//
//        commander.runCommand(new KickerSetState(kicker,1,2));
//        sleep(3000);
//        commander.runCommand(new KickerSetState(kicker,0,2));
//        sleep(1000);
//
//        commander.runCommand(new RunShooterForTime(shooter,0,0));

        sleep(20000);
        commander.stop();
    }
}

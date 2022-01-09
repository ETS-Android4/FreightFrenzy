package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

@Autonomous(name = "Red Only Park", group = "finalized")
public class AutoRedJustPark extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander = new DogeCommander(this);

    private Drive drive;
    private Arm arm;
    private Slides slides;
    private Intake intake;
    private Carousel carousel;

    private final double turnPower = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Drive(hardwareMap, true);
        arm = new Arm(hardwareMap);
        slides = new Slides(hardwareMap);
        intake = new Intake(hardwareMap);
        carousel = new Carousel(hardwareMap);

        telemetry.addLine("Registering subsystems");
        telemetry.update();

        commander.registerSubsystem(drive);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(intake);
//        commander.registerSubsystem(carousel);

        // *** Camera code goes here ***

        telemetry.addLine("Initializing commander");
        telemetry.update();

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();

//        commander.runCommandsParallel(
//                new ArmSetState(arm, Arm.POSITION.MIDDLE),
//                new DriveByTimer(drive, 0.8, -0.25, telemetry));
//        sleep(500);
//
//        commander.runCommand(new SlidesByEncoder(slides, Slides.TARGETS.OUT, 0.4, DEFAULT_TIMEOUT, telemetry));
//        sleep(2000);
//        commander.runCommand(new ArmSetState(arm, Arm.POSITION.DEPOSIT));
//        sleep(2000);
//
//        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
//        sleep(2000);
//
//        commander.runCommandsParallel(new SlidesByEncoder(slides, Slides.TARGETS.IN, 0.4));
//
//        sleep(2300);
//        commander.runCommand(new ArmSetState(arm, Arm.POSITION.INTAKE));
//        sleep(1000);
//
//        commander.runCommand(new TurnByGyroPID(drive, telemetry, 80.0, turnPower));
//        sleep(500);
//
//        commander.runCommand(new DriveByTimer(drive, 0.7, 0.6));
//        commander.runCommand(new StrafeByTimer(drive, 0.8, 0.5));
//        commander.runCommand(new DriveByTimer(drive, 0.9, 0.7));
//        commander.runCommand(new StrafeByTimer(drive, 0.6, -0.6));
        commander.runCommand(new DriveByTimer(drive, 2, 0.5));



        commander.stop();
    }



}

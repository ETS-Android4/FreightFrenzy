package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

@Deprecated
@Disabled
@Autonomous(name = "Duck", group = "testing")
public class AutoDuck extends LinearOpMode implements DogeOpMode {
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
//        arm = new Arm(hardwareMap);
//        slides = new Slides(hardwareMap);
//        intake = new Intake(hardwareMap);
        carousel = new Carousel(hardwareMap);

        telemetry.addLine("Registering subsystems");
        telemetry.update();

//        commander.registerSubsystem(drive);
//        commander.registerSubsystem(arm);
//        commander.registerSubsystem(slides);
//        commander.registerSubsystem(intake);
//        commander.registerSubsystem(carousel);

        // *** Camera code goes here ***

//        telemetry.addLine("Initializing commander");
//        telemetry.update();
//
//        commander.init();
        drive.initHardware();
        carousel.initHardware();

        telemetry.addLine("Ready!");
//        telemetry.addData("heading", drive.heading());
        telemetry.update();
        waitForStart();

        drive.setPower(0.3, 0.3);
        drive.periodic();
        sleep(700);
        drive.setPower(0,0);
        drive.periodic();
//        commander.runCommand(new DriveByEncoder(drive, drive.inchToCounts(1.0), drive.heading(),0.3,3, telemetry));

        sleep(100);
        carousel.setPower(turnPower);
        carousel.periodic();
        sleep(5000);
        carousel.setPower(0.5);
        carousel.periodic();
        sleep(100);
        //spin carousel

        drive.setPower(-0.7, -0.7);
        drive.periodic();
        sleep(2000);
        drive.setPower(0,0);
        drive.periodic();

        commander.stop();
    }
}

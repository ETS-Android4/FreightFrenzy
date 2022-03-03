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

@Autonomous(name = "Only Park", group = "finalized")
public class AutoDriveForwardAndPark extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander = new DogeCommander(this);

    private Drive drive;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Drive(hardwareMap, true);

        telemetry.addLine("Registering subsystems");
        telemetry.update();

        commander.registerSubsystem(drive);

        telemetry.addLine("Initializing commander");
        telemetry.update();

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();

        commander.runCommand(new DriveByTimer(drive, 2, 0.5));

        commander.stop();
    }



}

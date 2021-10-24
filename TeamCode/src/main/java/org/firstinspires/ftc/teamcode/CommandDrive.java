package org.firstinspires.ftc.teamcode;

import android.app.slice.Slice;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpCarouselControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpFlapControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpSlideControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Flap;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

@TeleOp(name = "Command Drive", group = "TeleOp")
public class CommandDrive extends LinearOpMode implements DogeOpMode {
    public static final float TRIGGER_THRESHOLD = 0.7f;
    public static final double DEFAULT_TIMEOUT = 5.0;

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Arm arm = new Arm(hardwareMap);
        Drive drive = new Drive(hardwareMap);
        Flap flap = new Flap(hardwareMap);
        Slides slides = new Slides(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);
        Intake intake = new Intake(hardwareMap);

        commander.registerSubsystem(arm);
        commander.registerSubsystem(drive);
        commander.registerSubsystem(flap);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(carousel);
        commander.registerSubsystem(intake);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpArmControl(arm, gamepad1),
                new TeleOpDriveControl(drive, gamepad1),
                new TeleOpFlapControl(flap, gamepad1),
                new TeleOpSlideControl(slides, gamepad1, telemetry),
                new TeleOpCarouselControl(carousel, gamepad2),
                new TeleOpIntakeControl(intake, gamepad2)
        );
    }
}

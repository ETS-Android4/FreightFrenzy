package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArcadeControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideKickerControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideKickerControlPlus;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpCapstoneControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpCarouselControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeColorSensor;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControlPlus;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControlPlusV2;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpLineColorSensor;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpSlideControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Cap;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeDistanceSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.LineColorSensor;
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
        Slides slides = new Slides(hardwareMap);
        Kicker kicker = new Kicker(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        IntakeDistanceSensor intakeDistanceSensor = new IntakeDistanceSensor(hardwareMap);
        Cap cap = new Cap(hardwareMap);

//        LineColorSensor lineColorSensor = new LineColorSensor(hardwareMap);
//        IntakeColorSensor intakeColorSensor = new IntakeColorSensor(hardwareMap);

        commander.registerSubsystem(cap);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(drive);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(carousel);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(intakeDistanceSensor);


//        commander.registerSubsystem(lineColorSensor);
//        commander.registerSubsystem(intakeColorSensor);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpCapstoneControl(cap, gamepad1),
                new TeleOpArcadeControl(drive, gamepad1),
                new TeleOpCarouselControl(carousel, gamepad2),
                new TeleOpIntakeControlPlusV2(intake, intakeDistanceSensor, gamepad2),
                new TeleOpArmSlideKickerControlPlus(arm, slides, kicker, intakeDistanceSensor, gamepad2, telemetry)


//                new TeleOpLineColorSensor(lineColorSensor, true)
//                new TeleOpIntakeColorSensor(intakeColorSensor, true)
        );
    }
}

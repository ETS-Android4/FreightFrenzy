package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.SlidesByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;
import org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement;

import androidx.annotation.NonNull;

import static org.firstinspires.ftc.teamcode.CommandDrive.DEFAULT_TIMEOUT;

@Deprecated
@Disabled
@Autonomous(name = "Red Park w\\ Cam", group = "finalized")
public class AutoRedParkWithCam extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander = new DogeCommander(this);

    private Drive drive;
    private Arm arm;
    private Slides slides;
    private Intake intake;
    private Carousel carousel;
    private AutoCameraControl cam;

    private final double turnPower = 0.2;

    private final int CAM_OVERRIDE = -1;

    @Override
    public void runOpMode() throws InterruptedException {
        drive =     new Drive(hardwareMap, true);
        arm =       new Arm(hardwareMap);
        slides =    new Slides(hardwareMap);
        intake =    new Intake(hardwareMap);
        carousel =  new Carousel(hardwareMap);

        if(CAM_OVERRIDE == -1)cam = new AutoCameraControl(new Camera(hardwareMap, telemetry), gamepad1, gamepad2, telemetry);

        telemetry.addLine("Registering subsystems");
        telemetry.update();

        commander.registerSubsystem(drive);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(intake);
//        commander.registerSubsystem(carousel);

        if(CAM_OVERRIDE == -1) {
            while (!cam.getCamera().hasInitialized && !isStarted() && !isStopRequested()); // Ensure camera has initialized

            while (!isStarted() && !isStopRequested()) {
                cam.periodic();
            }
        }


        telemetry.addLine("Initializing commander");
        telemetry.update();

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();

        if(CAM_OVERRIDE == -1) {
            switch (choosePlacement(cam)) {
                case LEFT:
                    runPathBottom();
                    break;
                case CENTER:
                    runPathMiddle();
                    break;
                case RIGHT:
                    runPathTop();
                    break;
                case UNKNOWN: //Run most point path if something goes wrong
                    runPathTop();
                    break;
            }
        } else {
            switch (CAM_OVERRIDE) {
                case 0:
                    runCommonPathBeforeSplit();
                case 1:
                    runPathBottom();
                    break;
                case 2:
                    runPathMiddle();
                    break;
                case 3:
                    runPathTop();
                    break;
                default:
                    break;
            }
        }


    }

    private void runCommonPathBeforeSplit() {
        telemetry.addLine("common path 1");
        telemetry.update();


    }

    private void runPathBottom() {
        runCommonPathBeforeSplit();
        telemetry.addLine("bottom path");
        telemetry.update();

        commander.runCommandsParallel(
                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new DriveByTimer(drive, 0.5, -0.30, telemetry));  //TODO: get correct (time,power)
        sleep(500);

//        commander.runCommand(new SlidesByEncoder(slides, Slides.TARGETS.BOTTOM_PLACE, 0.4, DEFAULT_TIMEOUT, telemetry));
        sleep(2000);


        runCommonPathAfterSplit();
    }

    private void runPathMiddle() {
        runCommonPathBeforeSplit();
        telemetry.addLine("middle path");
        telemetry.update();

        commander.runCommandsParallel(
                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new DriveByTimer(drive, 0.6, -0.42, telemetry));  //TODO: get correct (time,power)
        sleep(500);

//        commander.runCommand(new SlidesByEncoder(slides, Slides.TARGETS.MID_PLACE, 0.4, DEFAULT_TIMEOUT, telemetry));
        sleep(2000);


        runCommonPathAfterSplit();
    }

    private void runPathTop() {
        runCommonPathBeforeSplit();
        telemetry.addLine("top path");
        telemetry.update();


        commander.runCommandsParallel(
                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new DriveByTimer(drive, 0.48, -0.4, telemetry));
        sleep(500);

        commander.runCommand(new SlidesByEncoder(slides, Slides.TARGETS.OUT, 0.8, DEFAULT_TIMEOUT, telemetry));
        sleep(2000);


        runCommonPathAfterSplit();
    }

    private void runCommonPathAfterSplit() {
        telemetry.addLine("common path 2");
        telemetry.update();

        commander.runCommand(new ArmSetState(arm, Arm.POSITION.DEPOSIT));
        sleep(2000);

        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
        sleep(2000);

        commander.runCommandsParallel(new SlidesByEncoder(slides, Slides.TARGETS.IN, 0.4));

        sleep(2300);
        commander.runCommand(new ArmSetState(arm, Arm.POSITION.INTAKE));
        sleep(1000);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 80.0, turnPower));
        sleep(500);

        commander.runCommand(new DriveByTimer(drive, 0.7, 0.6));
        commander.runCommand(new StrafeByTimer(drive, 0.8, 0.5));
        commander.runCommand(new DriveByTimer(drive, 0.9, 0.7));
        commander.runCommand(new StrafeByTimer(drive, 0.6, -0.6));


        commander.stop();
    }

    @NonNull
    private DuckPlacement choosePlacement(@NonNull AutoCameraControl cam) {
        DuckPlacement placement = cam.getPlacement();
        if(!placement.name().equals(DuckPlacement.UNKNOWN.name())) {
            cam.periodic();
            return choosePlacement(cam);
        }

        return placement;
    }
}

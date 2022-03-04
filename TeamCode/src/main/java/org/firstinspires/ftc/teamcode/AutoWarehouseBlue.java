package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterDelay;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionUntilStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.IntakeSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunCarouselForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.SlidesByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.CarouselColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;
import org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

import java.util.Date;
import java.util.function.BooleanSupplier;

@Config
@Autonomous(name = "Warehouse Blue", group = "finalized")
public class AutoWarehouseBlue extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;

    private Drive               drive;
    private Arm                 arm;
    private Slides              slides;
    private Kicker              kicker;
    private Intake              intake;
    private AutoCameraControl   cam;

    private SwampbotsUtil util = new SwampbotsUtil();

    public static double CAM_OVERRIDE = -1;

    private final boolean EXTRA_WAIT_TIMES = true;

    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        drive =     new Drive(hardwareMap, true);
        arm =       new Arm(hardwareMap);
        slides =    new Slides(hardwareMap);
        kicker =    new Kicker(hardwareMap);
        intake =    new Intake(hardwareMap);
        cam =       new AutoCameraControl(new Camera(hardwareMap, telemetry), gamepad1, gamepad2, telemetry);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(intake);

        // Wait for cam to initialize
        while (!cam.getCamera().hasInitialized && !opModeIsActive() && !isStopRequested());

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("pre-init @", new Date().getTime());
            cam.periodic();
            if(isStopRequested()) {
                cam.getCamera().stop();
            }
        }

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();
        telemetry.addLine("Started");
        telemetry.update();

        if(CAM_OVERRIDE == -1) {
            switch (choosePlacement(cam)) {
                case LEFT:
                    runPathTop();
                    break;
                case CENTER:
                    runPathMiddle();
                    break;
                case RIGHT:
                    runPathBottom();
                    break;
                case UNKNOWN: //Run most point path if something goes wrong
                    runPathTop();
                    break;
            }
        } else {
            switch (((int) CAM_OVERRIDE)) {
                case 0:
                    runPathBottom();
                    break;
                case 1:
                    runPathMiddle();
                    break;
                case 2:
                    runPathTop();
                    break;
                case 4:
                    runCommonPathBeforeSplit();
                    break;
                case 5:
                    runCommonPathAfterSplit();
                    break;
            }
        }

        commander.stop();
    }

    private void runCommonPathBeforeSplit() {
        telemetry.addLine("common path 1");
        telemetry.update();
        cam.getCamera().stop();
        if(EXTRA_WAIT_TIMES) sleep(1000);

        commander.runCommandsParallel(
                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new IntakeSetState(intake, Intake.LIFT_POSITIONS.IN));

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-4), 0, 0.3, telemetry));
        sleep(300);
        commander.runCommand(new TurnByGyroPID(drive, telemetry, -30, 0.3));
        sleep(300);
    }

    private void runPathBottom() {
        runCommonPathBeforeSplit();
        telemetry.addLine("bottom path");
        telemetry.update();
        if(EXTRA_WAIT_TIMES) sleep(1000);

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-7), -30, 0.3, telemetry));
        commander.runCommandsParallel(
                new SlidesByEncoder(slides, Slides.TARGETS.LOW_SHARED, 0.7),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.LOW_HUB),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.AUTO_MID.getTargets(), 10);
                            }
                        }
                )
        );
        sleep(1500);
        commander.runCommandsParallel(new SlidesByEncoder(slides, Slides.TARGETS.BOTTOM_HUB, 0.7));
        sleep(500);

        runCommonPathAfterSplit();
    }

    private void runPathMiddle() {
        runCommonPathBeforeSplit();
        telemetry.addLine("middle path");
        telemetry.update();
        if(EXTRA_WAIT_TIMES) sleep(1000);

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-7), -30, 0.3, telemetry));
        commander.runCommandsParallel(
                new SlidesByEncoder(slides, Slides.TARGETS.LOW_SHARED, 0.7),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.MIDDLE_HUB),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.AUTO_MID.getTargets(), 10);
                            }
                        }
                )
        );
        sleep(1500);
        commander.runCommandsParallel(new SlidesByEncoder(slides, Slides.TARGETS.BOTTOM_HUB, 0.7));
        sleep(500);

        runCommonPathAfterSplit();
    }

    private void runPathTop() {
        runCommonPathBeforeSplit();
        telemetry.addLine("top path");
        telemetry.update();
        if(EXTRA_WAIT_TIMES) sleep(1000);

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-7), -30, 0.3, telemetry));
        commander.runCommandsParallel(
                new SlidesByEncoder(slides, Slides.TARGETS.LOW_SHARED, 0.7),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.TOP_HUB),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.AUTO_MID.getTargets(), 10);
                            }
                        }
                )
        );
        sleep(1500);
        commander.runCommandsParallel(new SlidesByEncoder(slides, Slides.TARGETS.BOTTOM_HUB.getTargets() - 40, 0.7));
        sleep(500);

        runCommonPathAfterSplit();
    }

    private void runCommonPathAfterSplit() {
        telemetry.addLine("common path 2");
        telemetry.update();
        if(EXTRA_WAIT_TIMES) sleep(1000);

        commander.runCommand(new KickerSetState(kicker, Kicker.POSITION.CLOSE));
        sleep(300);

        commander.runCommandsParallel(
                new SlidesByEncoder(slides, Slides.TARGETS.LOW_SHARED, 0.5),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.MIDDLE),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.LOW_SHARED.getTargets(), 50);
                            }
                        }
                )
        );
        sleep(1000);

        commander.runCommandsParallel(
                new DriveByEncoder(drive, SwampbotsUtil.inchToCount(7), -30, 0.3, telemetry),
                new SlidesByEncoder(slides, Slides.TARGETS.IN, 0.7)
        );
        sleep(1000);
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(22), -70, 0.3, telemetry));
//        sleep(1000);
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(26), -85, 0.3, telemetry));
    }

    @Nullable
    private DuckPlacement choosePlacement(@NonNull AutoCameraControl cam) {
        DuckPlacement placement = cam.getPlacement();
        if(placement.name().equals(DuckPlacement.UNKNOWN.name())) {
            telemetry.addData("choosing @", new Date().getTime());
            cam.periodic();
            if(isStopRequested()) {
                cam.getCamera().stop();
                return null;
            }
            return choosePlacement(cam);
        }

        return placement;
    }

}

package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement.CENTER;
import static org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement.LEFT;
import static org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement.RIGHT;
import static org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement.UNKNOWN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterDelay;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionUntilStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunCarouselForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.SlidesByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.CarouselColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;
import org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

import java.util.Date;
import java.util.function.BooleanSupplier;

@Config
@Autonomous(name = "Duck Blue", group = "finalized")
public class AutoDuckBlue extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;

    private Drive drive;
    private Carousel carousel;
    private CarouselColorSensor carouselColorSensor;
    private Arm arm;
    private Slides slides;
    private Kicker kicker;
    private AutoCameraControl cam;

    private SwampbotsUtil util = new SwampbotsUtil();

    public static double CAM_OVERRIDE = -1;

    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        drive = new Drive(hardwareMap, true);
        carousel = new Carousel(hardwareMap);
        carouselColorSensor = new CarouselColorSensor(hardwareMap, telemetry);
        arm = new Arm(hardwareMap);
        slides = new Slides(hardwareMap);
        kicker = new Kicker(hardwareMap);
        cam = new AutoCameraControl(new Camera(hardwareMap, telemetry), gamepad1, gamepad2, telemetry);


        commander.registerSubsystem(drive);
        commander.registerSubsystem(carousel);
//        commander.registerSubsystem(carouselColorSensor);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(kicker);

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
        
//        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
//        telemetry.addData("pos", arm.getTargetPos());
//        telemetry.update();
//sleep(5000);
//        runCommonPathBeforeSplit();
//
//        commander.runCommandsParallel(
////                new ArmSetState(arm, Arm.POSITION.MIDDLE),
//                new ActionAfterDelay(
//                        new SlidesByEncoder(slides, Slides.TARGETS.OUT, 0.7),
//                        0.5
//                ),
//                new ActionAfterStatement(
//                        new ArmSetState(arm, Arm.POSITION.DEPOSIT),
//                        new BooleanSupplier() {
//                            @Override
//                            public boolean getAsBoolean() {
//                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets() - 50, 10);
//                            }
//                        }
//                ));
//        sleep(500);
//        commander.runCommand(new KickerSetState(kicker, Kicker.POSITION.CLOSE));
//        sleep(300);
//        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
//        sleep(1000);
//        commander.runCommandsParallel(
//                new SlidesByEncoder(slides, Slides.TARGETS.IN, 0.5)//,
////                new ActionAfterStatement(
////                        new ArmSetState(arm, Arm.POSITION.INTAKE),
////                        new BooleanSupplier() {
////                            @Override
////                            public boolean getAsBoolean() {
////                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50);
////                            }
////                        }
////                )
//        );
//        sleep(1000);
//
//        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(25.0), 50, 0.3, telemetry));
//        sleep(1000);
//
//        runCommonPathAfterSplit();

        commander.stop();
    }

    public void runCommonPathBeforeSplit() {
        telemetry.addLine("common path 1");
        telemetry.update();
        cam.getCamera().stop();
        sleep(1000);

        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-19), 0, 0.3, telemetry));
        sleep(300);
        commander.runCommand(new TurnByGyroPID(drive, telemetry, 50, 0.3));
        sleep(300);
    }

    public void runPathBottom() {
        runCommonPathBeforeSplit();
        telemetry.addLine("bottom path");
        telemetry.update();
        sleep(1000);

        commander.runCommandsParallel(
                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new ActionAfterDelay(
                        new SlidesByEncoder(slides, Slides.TARGETS.BOTTOM_HUB, 0.7),
                        0.5
                ),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.LOW_HUB),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 10);
                            }
                        }
                ));
        sleep(500);
    }

    public void runPathMiddle() {
        runCommonPathBeforeSplit();
        telemetry.addLine("middle path");
        telemetry.update();
        sleep(1000);

        commander.runCommandsParallel(
                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new ActionAfterDelay(
                        new SlidesByEncoder(slides, Slides.TARGETS.OUT, 0.7),
                        0.5
                ),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.MIDDLE_HUB),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 10);
                            }
                        }
                ));
        sleep(500);
    }

    public void runPathTop() {
        runCommonPathBeforeSplit();
        telemetry.addLine("top path");
        telemetry.update();
        sleep(1000);

        commander.runCommandsParallel(
                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new ActionAfterDelay(
                        new SlidesByEncoder(slides, Slides.TARGETS.OUT, 0.7),
                        1.0
                ),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.DEPOSIT),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 10);
                            }
                        }
                ));
        sleep(500);


    }

    public void runCommonPathAfterSplit() {
        telemetry.addLine("common path 2");
        telemetry.update();
        sleep(1000);

        commander.runCommand(new KickerSetState(kicker, Kicker.POSITION.CLOSE));
        sleep(300);
        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
        sleep(700);
        commander.runCommandsParallel(
                new SlidesByEncoder(slides, Slides.TARGETS.IN, 0.5),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.INTAKE),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50);
                            }
                        }
                )
        );
        sleep(1000);

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(25.0), 50, 0.3, telemetry));
        sleep(1000);

        commander.runCommand(new ActionUntilStatement(
                new DriveByTimer(drive, 1, 0.2),
                new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() {
                        return carouselColorSensor.doesSensorSeeAnything();
                    }
                }
        ));
        commander.runCommand(new RunCarouselForTime(carousel, 2.6, -0.35));
        sleep(500);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 0, 0.2));
        sleep(500);

//        commander.runCommand(new DriveByTimer(drive, 1, -0.2));

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-24.0), 0, 0.3, telemetry));

    }

    @Nullable
    private DuckPlacement choosePlacement(@NonNull AutoCameraControl cam) {
        DuckPlacement placement = cam.getPlacement();
        if(placement.name().equals(DuckPlacement.UNKNOWN.name())) {
            telemetry.addData("choosing at", new Date().getTime());
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

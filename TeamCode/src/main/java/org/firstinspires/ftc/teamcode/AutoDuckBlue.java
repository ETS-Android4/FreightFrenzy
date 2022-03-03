package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterDelay;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionUntilStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunCarouselForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.SlidesByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.CarouselColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

import java.util.function.BooleanSupplier;

@Autonomous(name = "Duck Blue", group = "finalized")
public class AutoDuckBlue extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;

    private Drive drive;
    private Carousel carousel;
    private CarouselColorSensor carouselColorSensor;
    private Arm arm;
    private Slides slides;
    private Kicker kicker;

    private SwampbotsUtil util = new SwampbotsUtil();

    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        drive = new Drive(hardwareMap, true);
        carousel = new Carousel(hardwareMap);
        carouselColorSensor = new CarouselColorSensor(hardwareMap, telemetry);
        arm = new Arm(hardwareMap);
        slides = new Slides(hardwareMap);
        kicker = new Kicker(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(carousel);
//        commander.registerSubsystem(carouselColorSensor);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(kicker);

        telemetry.addLine("3");
        telemetry.update();

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();
        telemetry.addLine("Started");
        telemetry.update();

//        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
//        telemetry.addData("pos", arm.getTargetPos());
//        telemetry.update();
//sleep(5000);
        runCommonPathBeforeSplit();

        commander.runCommandsParallel(
//                new ArmSetState(arm, Arm.POSITION.MIDDLE),
                new ActionAfterDelay(
                        new SlidesByEncoder(slides, Slides.TARGETS.OUT, 0.7),
                        0.5
                ),
                new ActionAfterStatement(
                        new ArmSetState(arm, Arm.POSITION.DEPOSIT),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets() - 50, 10);
                            }
                        }
                ));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker, Kicker.POSITION.CLOSE));
        sleep(300);
        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
        sleep(1000);
        commander.runCommandsParallel(
                new SlidesByEncoder(slides, Slides.TARGETS.IN, 0.5)//,
//                new ActionAfterStatement(
//                        new ArmSetState(arm, Arm.POSITION.INTAKE),
//                        new BooleanSupplier() {
//                            @Override
//                            public boolean getAsBoolean() {
//                                return util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50);
//                            }
//                        }
//                )
        );
        sleep(1000);

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(25.0), 50, 0.3, telemetry));
        sleep(1000);

        runCommonPathAfterSplit();

        commander.stop();
    }

    public void runCommonPathBeforeSplit() {
        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-19), 0, 0.3, telemetry));
        sleep(300);
        commander.runCommand(new TurnByGyroPID(drive, telemetry, 50, 0.3));
        sleep(300);
    }

    public void runLowPath() {
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
    }

    public void runMiddlePath() {
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
    }

    public void runHighPath() {
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
    }

    public void runCommonPathAfterSplit() {
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

}

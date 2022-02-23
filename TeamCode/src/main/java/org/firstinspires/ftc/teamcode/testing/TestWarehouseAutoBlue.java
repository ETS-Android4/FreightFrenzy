package org.firstinspires.ftc.teamcode.testing;

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
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

import java.util.function.BooleanSupplier;

@Autonomous(name = "test warehouse blue", group = "testing")
public class TestWarehouseAutoBlue extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;

    private Drive drive;
    private Arm arm;
    private Slides slides;
    private Kicker kicker;
    private Intake intake;

    private SwampbotsUtil util = new SwampbotsUtil();

    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        drive = new Drive(hardwareMap, true);
        arm = new Arm(hardwareMap);
        slides = new Slides(hardwareMap);
        kicker = new Kicker(hardwareMap);
        intake = new Intake(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(intake);

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
        commander.runCommand(new ArmSetState(arm, Arm.POSITION.MIDDLE));
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-4), 0, 0.3, telemetry));
        sleep(300);
        commander.runCommand(new TurnByGyroPID(drive, telemetry, -25, 0.3));
        sleep(300);
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-4), -25, 0.3, telemetry));
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

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(6), -25, 0.3, telemetry));
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(12), -80, 0.3, telemetry));
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(30), -90, 0.3, telemetry));

        commander.stop();
    }

}

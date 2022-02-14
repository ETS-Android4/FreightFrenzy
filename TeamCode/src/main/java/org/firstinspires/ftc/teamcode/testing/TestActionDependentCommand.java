package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterDelay;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionAfterStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionUntilStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ActionWhileStatement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunCarouselForTime;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.GlobalTimer;

import java.util.function.BooleanSupplier;

@Autonomous(name = "Test actions", group = "testing")
public class TestActionDependentCommand extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;

    private Drive drive;
    private Carousel carousel;
    private GlobalTimer globalTimer;

    private int testToRun = -1;

    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        drive = new Drive(hardwareMap);
        carousel = new Carousel(hardwareMap);
        globalTimer = new GlobalTimer();

        commander.registerSubsystem(drive);
        commander.registerSubsystem(carousel);
        commander.registerSubsystem(globalTimer);

        commander.init();

        while(!isStarted() && !isStopRequested()) {
            if(gamepad1.a) testToRun = 0;
            if(gamepad1.b) testToRun = 1;
            if(gamepad1.x) testToRun = 2;
            if(gamepad1.y) testToRun = 3;
            if(gamepad1.right_stick_button) testToRun = -1;


            telemetry.addLine("Ready!");
            telemetry.addData("running", testToRun);
            telemetry.update();
        }

        waitForStart();

        switch (testToRun) {
            case -1:
                test();
                break;
            case 0:
                after();
                break;
            case 1:
                until();
                break;
            case 2:
                whil();
                break;
            case 3:
                delay();
                break;

        }

        commander.stop();
    }

    private void after() {
        globalTimer.reset();
        commander.runCommandsParallel(
                new DriveByTimer(drive, 5, 0.2),
                new ActionAfterStatement(
                        new RunCarouselForTime(carousel, 4, 1),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return drive.getCurrentPositions()[0] > 100 || globalTimer.getSeconds() > 10.0;
                            }
                        }
                )
        );

    }

    private void delay() {
        commander.runCommandsParallel(
                new ActionAfterDelay(
                        new RunCarouselForTime(carousel, 4, 1), 5
                )
        );
    }

    private void until() {
        commander.runCommandsParallel(
                new DriveByTimer(drive, 5, 0.2),
                new ActionUntilStatement(
                        new RunCarouselForTime(carousel, 10, 1),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return drive.getCurrentPositions()[0] > 100;
                            }
                        }
                )
        );
    }

    private void whil() {
        commander.runCommandsParallel(
                new DriveByTimer(drive, 5, 0.2),
                new ActionWhileStatement(
                        new RunCarouselForTime(carousel, 10, 1),
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() {
                                return drive.getCurrentPositions()[0] < 100;
                            }
                        }
                )
        );
    }

    private void test() {
        commander.runCommand(new DriveByTimer(drive, 5, 0.2));
    }
}

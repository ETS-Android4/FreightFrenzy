package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpCarouselControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControlPlus;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.CarouselColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeColorSensor;

@TeleOp(name = "Test Intake Color", group = "testing")
public class TestIntakeColorSensor extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        IntakeColorSensor intakeColorSensor = new IntakeColorSensor(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        // Subsystem sub = new Subsystem()

        commander.registerSubsystem(intakeColorSensor);
        commander.registerSubsystem(intake);
        // commander.registerSubsystem(sub);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpIntakeControlPlus(intake, intakeColorSensor, gamepad1, telemetry)
                // new CommandToTest(sub...)
        );
    }
}

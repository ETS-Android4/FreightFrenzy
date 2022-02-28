package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControlPlus;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeColorSensor;

@Disabled
@TeleOp(name = "test intake", group = "testing")
public class TestIntake extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Intake intake = new Intake(hardwareMap);
        IntakeColorSensor intakeColorSensor = new IntakeColorSensor(hardwareMap);

        commander.registerSubsystem(intake);
        commander.registerSubsystem(intakeColorSensor);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpIntakeControlPlus(intake, intakeColorSensor, gamepad1, telemetry)
        );
    }
}

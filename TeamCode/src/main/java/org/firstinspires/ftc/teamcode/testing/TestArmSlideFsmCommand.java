package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideKickerControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideKickerControlPlus;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

@TeleOp(name = "Test FSM", group = "testing")
public class TestArmSlideFsmCommand extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Arm arm = new Arm(hardwareMap);
        Slides slides = new Slides(hardwareMap);
        Kicker kicker = new Kicker(hardwareMap);
        IntakeColorSensor intakeColorSensor = new IntakeColorSensor(hardwareMap);

        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(intakeColorSensor);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                 new TeleOpArmSlideKickerControlPlus(arm, slides, kicker, intakeColorSensor, gamepad1, telemetry)
        );
    }
}

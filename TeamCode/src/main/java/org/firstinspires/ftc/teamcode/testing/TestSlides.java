package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpSlideControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

@Disabled
@TeleOp(name = "Test Slides", group = "testing")
public class TestSlides extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Slides slides = new Slides(hardwareMap);
        Arm arm = new Arm(hardwareMap);

        commander.registerSubsystem(slides);
        commander.registerSubsystem(arm);

        commander.init();

        waitForStart();

        commander.runCommandsParallel(new TeleOpSlideControl(slides, gamepad1, telemetry),
                new TeleOpArmControl(arm, gamepad1));
    }
}

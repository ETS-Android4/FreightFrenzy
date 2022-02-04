package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmSlideKickerControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
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

        commander.registerSubsystem(arm);
        commander.registerSubsystem(slides);
        commander.registerSubsystem(kicker);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                 new TeleOpArmSlideKickerControl(arm, slides, kicker, gamepad1, telemetry)
        );
    }
}

package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpCarouselControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;

@TeleOp(name = "Test Carousel", group = "testing")
public class TestCarousel extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Carousel carousel = new Carousel(hardwareMap);

        commander.registerSubsystem(carousel);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpCarouselControl(carousel, gamepad1, telemetry)
        );
    }
}

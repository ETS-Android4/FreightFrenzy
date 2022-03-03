package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpCarouselControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.CarouselColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;

@Disabled
@TeleOp(name = "Test Carousel Color", group = "testing")
public class TestCarouselColorSensor extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        CarouselColorSensor carouselColorSensor = new CarouselColorSensor(hardwareMap, telemetry);
        Carousel carousel = new Carousel(hardwareMap);
        Kicker kicker = new Kicker(hardwareMap);
        // Subsystem sub = new Subsystem()

        commander.registerSubsystem(carouselColorSensor);
        commander.registerSubsystem(carousel);
        // commander.registerSubsystem(sub);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpCarouselControl(carousel, gamepad1)
                // new CommandToTest(sub...)
        );
    }
}

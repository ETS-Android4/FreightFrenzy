package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;

@Disabled
@Autonomous(name = "test cam", group = "testing")
public class TestCamera extends LinearOpMode implements DogeOpMode {
    private AutoCameraControl cam;

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        cam = new AutoCameraControl(new Camera(hardwareMap), gamepad1, gamepad2, telemetry);

        while (opModeIsActive() && !isStopRequested()) {
            cam.periodic();
        }

        commander.init();
        waitForStart();

    }
}

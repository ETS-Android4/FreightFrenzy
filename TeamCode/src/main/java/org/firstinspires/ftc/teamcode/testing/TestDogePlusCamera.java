package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RingPlacement;
import org.firstinspires.ftc.teamcode.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;

@Disabled
@Autonomous(name = "Test Doge+Cam",group = "testing")
public class TestDogePlusCamera extends LinearOpMode implements DogeOpMode {
    private AutoCameraControl cam;
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        //Drive drive             = new Drive(hardwareMap);


        //commander.registerSubsystem(drive);

        cam = new AutoCameraControl(new Camera(hardwareMap), gamepad1, gamepad2, telemetry);

        while (!opModeIsActive()) {
            cam.periodic();
        }

        //commander.init();
        waitForStart();

        switch (choosePath(cam.getPlacement())) {
            case 0:
                runPath0();
                break;
            case 1:
                runPath1();
                break;
            case 4:
                runPath4();

                break;
            default:
                runPath0();
                break;
        }
        sleep(2000);

//        commander.runCommandsParallel(
//                new DriveByTimer(drive, 2, 0.3)
//        );


        commander.stop();
    }

    private void runCommonPathBeforeSplit() {
        telemetry.addLine("Running Common Path");
        telemetry.update();

        sleep(2000);
    }

    private void runPath0() {
        runCommonPathBeforeSplit();
        telemetry.addLine("Running Path 0");
        telemetry.update();

    }

    private void runPath1() {
        runCommonPathBeforeSplit();
        telemetry.addLine("Running Path 1");
        telemetry.update();

    }

    private void runPath4() {
        runCommonPathBeforeSplit();
        telemetry.addLine("Running Path 4");
        telemetry.update();

    }

    private int choosePath(RingPlacement placement) {
        switch (placement) {
            case ZERO_RINGS:
                return 0;
            case ONE_RING:
                return 1;
            case FOUR_RINGS:
                return 4;
            case UNKNOWN:
                cam.periodic();
                return choosePath(cam.getPlacement());
            default:
                cam.periodic();
                return choosePath(cam.getPlacement());
        }
    }
}

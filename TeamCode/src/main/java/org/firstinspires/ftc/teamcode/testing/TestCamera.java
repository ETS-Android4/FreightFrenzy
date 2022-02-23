package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.swampbots_util.DuckPatternPipeline;
import org.firstinspires.ftc.teamcode.swampbots_util.DuckPlacement;

import java.util.Date;

@Autonomous(name = "Test cam", group = "testing")
public class TestCamera extends LinearOpMode implements DogeOpMode {
    private AutoCameraControl cam;

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        cam = new AutoCameraControl(new Camera(hardwareMap, telemetry), gamepad1, gamepad2, telemetry);
        
        while (!cam.getCamera().hasInitialized && !opModeIsActive() && !isStopRequested());

        telemetry.addLine("started control");
        telemetry.update();

//        sleep(3000);

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("pre-init at", new Date().getTime());
            cam.periodic();
            if(isStopRequested()) {
                cam.getCamera().stop();
            }
        }

        telemetry.addData("start", isStarted());
        telemetry.addData("stop", isStopRequested());
        telemetry.addData("active", opModeIsActive());
        telemetry.update();

        commander.init();
        waitForStart();

        telemetry.addLine("ran");
        telemetry.addData("placement", cam.getPlacement());
        telemetry.update();
//        sleep(3000);

        switch (choosePlacement(cam)) {
            case LEFT:
                runPathTop();
                break;
            case CENTER:
                runPathMiddle();
                break;
            case RIGHT:
                runPathBottom();
                break;
            case UNKNOWN: //Run most point path if something goes wrong
                runPathTop();
                break;
        }

    }

    private void runCommonPathBeforeSplit() {
        telemetry.addLine("common path 1");
        telemetry.update();
        cam.getCamera().stop();
        sleep(1000);


    }

    private void runPathBottom() {
        runCommonPathBeforeSplit();
        telemetry.addLine("bottom path");
        telemetry.update();
        sleep(1000);

        runCommonPathAfterSplit();
    }

    private void runPathMiddle() {
        runCommonPathBeforeSplit();
        telemetry.addLine("middle path");
        telemetry.update();
        sleep(1000);


        runCommonPathAfterSplit();
    }

    private void runPathTop() {
        runCommonPathBeforeSplit();
        telemetry.addLine("top path");
        telemetry.update();
        sleep(1000);

        runCommonPathAfterSplit();
    }

    private void runCommonPathAfterSplit() {
        telemetry.addLine("common path 2");
        telemetry.update();
        sleep(1000);

    }

    private DuckPlacement choosePlacement(AutoCameraControl cam) {
        DuckPlacement placement = cam.getPlacement();
        if(placement.name().equals(DuckPlacement.UNKNOWN.name())) {
            telemetry.addData("choosing at", new Date().getTime());
            cam.periodic();
            if(isStopRequested()) {
                cam.getCamera().stop();
                return null;
            }
            return choosePlacement(cam);
        }

        return placement;
    }
}

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

@Autonomous(name = "test cam", group = "testing")
public class TestCamera extends LinearOpMode implements DogeOpMode {
    private AutoCameraControl cam;

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        cam = new AutoCameraControl(new Camera(hardwareMap, telemetry), gamepad1, gamepad2, telemetry);
        
        while (!cam.getCamera().hasInitialized && !isStarted() && !isStopRequested());

        telemetry.addLine("started control");
        telemetry.update();

        sleep(3000);

        while (!isStarted() && !isStopRequested()) {
            cam.periodic();
        }

        commander.init();
        waitForStart();

        telemetry.addLine("ran");
        telemetry.addData("placement", cam.getPlacement());
        telemetry.update();
        sleep(3000);

        switch (choosePlacement(cam)) {
            case LEFT:
                runPathBottom();
                break;
            case CENTER:
                runPathMiddle();
                break;
            case RIGHT:
                runPathTop();
                break;
            case UNKNOWN: //Run most point path if something goes wrong
                runPathTop();
                break;
        }

    }

    private void runCommonPathBeforeSplit() {
        telemetry.addLine("common path 1");
        telemetry.update();


    }

    private void runPathBottom() {
        runCommonPathBeforeSplit();
        telemetry.addLine("bottom path");
        telemetry.update();

        runCommonPathAfterSplit();
    }

    private void runPathMiddle() {
        runCommonPathBeforeSplit();
        telemetry.addLine("middle path");


        runCommonPathAfterSplit();
    }

    private void runPathTop() {
        runCommonPathBeforeSplit();
        telemetry.addLine("top path");


        runCommonPathAfterSplit();
    }

    private void runCommonPathAfterSplit() {
        telemetry.addLine("common path 2");

    }

    private DuckPlacement choosePlacement(AutoCameraControl cam) {
        DuckPlacement placement = cam.getPlacement();
        if(!placement.name().equals(DuckPlacement.UNKNOWN.name())) {
            cam.periodic();
            return choosePlacement(cam);
        }

        return placement;
    }
}

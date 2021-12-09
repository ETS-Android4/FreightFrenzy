package org.firstinspires.ftc.teamcode.testing;

import android.annotation.SuppressLint;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeColorSensor;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpLineColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeColorSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.LineColorSensor;


@TeleOp(name = "Test Color Sensors", group = "testing")
public class TestColorSensors extends LinearOpMode implements DogeOpMode {

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        LineColorSensor line = new LineColorSensor(hardwareMap);
        IntakeColorSensor intake = new IntakeColorSensor(hardwareMap);

        commander.registerSubsystem(line);
        commander.registerSubsystem(intake);

//        line.initHardware();
//        intake.initHardware();
//
//        while (!isStarted() && !isStopRequested()) {
//            line.periodic();
//            intake.periodic();
//            double[] val = line.getColorLeft().val;
//            telemetry.addLine(String.format("l: [r,g,b] = [%.2f, %.2f, %.2f]", val[0], val[1], val[2]));
//            val = line.normalizeByMax(line.getColorLeft(), 255);
//            telemetry.addLine(String.format("|l|: [r,g,b] = [%.2f, %.2f, %.2f]", val[0], val[1], val[2]));
//            val = line.getColorRight().val;
//            telemetry.addLine(String.format("r: [r,g,b] = [%.2f, %.2f, %.2f]", val[0], val[1], val[2]));
//            val = line.normalizeByMax(line.getColorRight(), 255);
//            telemetry.addLine(String.format("|r|: [r,g,b] = [%.2f, %.2f, %.2f]", val[0], val[1], val[2]));
//            telemetry.addLine();
//
//            val = intake.getColor().val;
//            telemetry.addLine(String.format("i: [r,g,b] = [%.2f, %.2f, %.2f]", val[0], val[1], val[2]));
//
//
//
//            telemetry.update();
//        }

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpLineColorSensor(line, true),
                new TeleOpIntakeColorSensor(intake, true)
        );
    }
}

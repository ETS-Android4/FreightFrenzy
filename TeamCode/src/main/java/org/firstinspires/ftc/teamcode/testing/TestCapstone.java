package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpCapstoneControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapArm;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapGrip;


@TeleOp(name = "Test Cap", group = "testing")
public class TestCapstone extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);


        CapArm capArm = new CapArm(hardwareMap);
        CapGrip capGrip = new CapGrip(hardwareMap);

        commander.registerSubsystem(capArm);
        commander.registerSubsystem(capGrip);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
            new TeleOpCapstoneControl(capArm, capGrip, gamepad1, telemetry)
        );
    }
}

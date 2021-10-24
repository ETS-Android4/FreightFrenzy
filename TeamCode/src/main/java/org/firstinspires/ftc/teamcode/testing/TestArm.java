package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

@TeleOp(name = "Test Arm", group = "testing")
public class TestArm extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Arm arm = new Arm(hardwareMap);

        commander.registerSubsystem(arm);

        commander.init();

        waitForStart();

        commander.runCommandsParallel(new TeleOpArmControl(arm, gamepad1, telemetry));
    }
}

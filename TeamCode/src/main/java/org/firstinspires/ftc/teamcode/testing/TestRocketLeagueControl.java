package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpDriveCurrentTracking;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpRocketLeagueDriveControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

@Disabled
@TeleOp(name = "Test RL Control", group = "testing")
public class TestRocketLeagueControl extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpRocketLeagueDriveControl(drive, gamepad1, telemetry),
                new TeleOpDriveCurrentTracking(drive)
                // new CommandToTest(sub...)
        );
    }
}

package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

@Autonomous(name = "Test Tracking Consts", group = "testing")
public class TestExactTrackingConstants extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);


        commander.registerSubsystem(drive);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TestDriveTrackingCommand(drive, telemetry),
                new DriveByEncoder(drive, SwampbotsUtil.inchToCount(24), 0, 0.3));

        sleep(10000);

        commander.stop();
    }
}

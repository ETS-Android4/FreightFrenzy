package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

@Autonomous(name = "test duck blue", group = "testing")
public class TestDuckAutoBlue extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander;

    private Drive drive;

    @Override
    public void runOpMode() throws InterruptedException {
        commander = new DogeCommander(this);

        drive = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);

        commander.init();

        waitForStart();

        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(-19), 0, 0.3, telemetry));
        sleep(3000);
        commander.runCommand(new TurnByGyroPID(drive, telemetry, 50, 0.2));
        sleep(3000);
        commander.runCommand(new DriveByEncoder(drive, SwampbotsUtil.inchToCount(25.0), 50, 0.3, telemetry));
        sleep(5000);

        commander.stop();
    }

}

package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.SavePID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

@Disabled
@Autonomous
public class TestSaveHeadingInAuto extends LinearOpMode implements DogeOpMode {
    public void runOpMode(){
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);

        commander.init();

        waitForStart();

        commander.runCommandsParallel(
                new DriveByTimer(drive, 2, 0.2),
                new SavePID(drive)

        );

        telemetry.addLine(Float.toString(drive.heading()));
        telemetry.update();
        sleep(5000);
        commander.stop();
    }
}

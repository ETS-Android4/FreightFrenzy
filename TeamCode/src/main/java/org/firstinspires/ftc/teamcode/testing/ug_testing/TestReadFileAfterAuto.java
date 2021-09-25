package org.firstinspires.ftc.teamcode.testing.ug_testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Drive;

@Disabled
@Autonomous
public class TestReadFileAfterAuto extends LinearOpMode implements DogeOpMode {
    public void runOpMode(){
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap, true, true);

        commander.registerSubsystem(drive);

        commander.init();

//        float headingOffsets = drive.getHeadingOffset();

//        telemetry.addLine( "" + headingOffsets);

       // telemetry.addLine(drive.getException().toString());
        telemetry.update();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpDriveControl(drive, gamepad1)
        );


        commander.stop();

    }
}

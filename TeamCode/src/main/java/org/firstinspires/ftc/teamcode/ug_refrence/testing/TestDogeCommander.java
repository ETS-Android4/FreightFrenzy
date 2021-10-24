package org.firstinspires.ftc.teamcode.ug_refrence.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Drive;

@Disabled
@TeleOp
public class TestDogeCommander extends LinearOpMode implements DogeOpMode {
    public void runOpMode(){
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap, true);
        Arm arm                 = new Arm(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(arm);

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpDriveControl(drive, gamepad1),
                new TeleOpArmControl(arm, gamepad2, telemetry)
        );


        commander.stop();

    }



}
/*

[p1,p2,p3,...,pn,del]
set m1 to p1 for 1->n
wait del


while opmodeisactive(){
robotState = 2.dpd
if robotState is normal =>
 commander.runCommandsParallel(
               normal teleop controls
        );
        <
        else if robotState is capping =>
         commander.runCommandsParallel(
                driving controls
                override auxiliary controls & auto cap
        );
        <
}

0) Automation (Autocap)
1) Odometry
sqrt(2)) Camera
2) LED indicators
3) 7-Segment LED display
4)











 */

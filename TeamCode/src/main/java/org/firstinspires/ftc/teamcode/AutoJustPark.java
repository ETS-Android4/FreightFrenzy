package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

@Deprecated
@Disabled
@Autonomous(name = "Only Park", group = "finalized")
public class AutoJustPark extends LinearOpMode{

    private Drive drive;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Drive(hardwareMap, true);

        drive.initHardware();

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();

        drive.setPower(0.8, 0.8);
        sleep(2000);
        drive.setPower(0, 0);
    }



}

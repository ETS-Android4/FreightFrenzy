package org.firstinspires.ftc.teamcode.testing.ug_testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop.SoloTeleOpGripControl;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Grip;

@Disabled
@TeleOp(name = "Test Arm Pos", group = "testing")
public class TestArmPositions extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);


        Arm arm                 = new Arm(hardwareMap);
        Grip grip               = new Grip(hardwareMap);

        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);

        commander.init();

        waitForStart();


        commander.runCommandsParallel(
                new TestArmPositionsCommand(arm,gamepad2, telemetry),
                new SoloTeleOpGripControl(grip,gamepad1)
        );



        commander.stop();
    }
}

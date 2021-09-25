package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

@Disabled
@TeleOp(name = "Test Telemetry", group = "testing")
public class TestTelemetryOpMode extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap,true);
//        Kicker kicker           = new Kicker(hardwareMap);
//        Shooter shooter         = new Shooter(hardwareMap);
//        Arm arm                 = new Arm(hardwareMap);
//        Grip grip               = new Grip(hardwareMap);
//        Intake intake           = new Intake(hardwareMap);
//        Transfer transfer       = new Transfer(hardwareMap);
        TestTelemetrySubsystem teleSub = new TestTelemetrySubsystem(telemetry);
//        TestTelemetryCommand teleComm  = new TestTelemetryCommand(teleSub);

        commander.registerSubsystem(drive);
//        commander.registerSubsystem(kicker);
//        commander.registerSubsystem(shooter);
//        commander.registerSubsystem(arm);
//        commander.registerSubsystem(grip);
//        commander.registerSubsystem(intake);
//        commander.registerSubsystem(transfer);
        commander.registerSubsystem(teleSub);

        commander.init();

//        kicker.kicker.setPosition(Kicker.TARGETS.OUT.getTarget());
        waitForStart();

        //FIXME: Need to rewrite subsystem and command to pass a subsystem into DC and command into runCommandsParallel
        commander.runCommandsParallel(
                new TestTelemetryCommand(teleSub),
                new TestDriveControl(drive,gamepad1, new TestTelemetryCommand(teleSub))
//                new TeleOpKickerControl(kicker,gamepad1),
//                new TeleOpShooterControl(shooter,gamepad2),
//                new TeleOpArmControl(arm,gamepad1),
//                new TeleOpGripControl(grip,gamepad1),
//                new TeleOpIntakeControl(intake,gamepad2),
//                new TeleOpTransferControl(transfer,gamepad2),
        );


        commander.stop();
    }
}

package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpDriveControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpGripControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpIntakeControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpKickerControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpShooterControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpTransferControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpGripControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpKickerControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpShooterControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpTransferControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

@TeleOp(name = "Command Drive", group = "TeleOp")
public class CommandDrive extends LinearOpMode implements DogeOpMode {
    public static final float TRIGGER_THRESHOLD = 0.7f;

    public static final boolean STOP_USING_GRIPPER = false;  // FIXME: Will stop all commands involving the gripper. Implemented b/c servo broke
    public static final boolean ONE_PERSON_CONTROLS = false;

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap,true);
        Kicker kicker           = new Kicker(hardwareMap);
        Shooter shooter         = new Shooter(hardwareMap);
        Arm arm                 = new Arm(hardwareMap);
        Grip grip               = new Grip(hardwareMap);
        Intake intake           = new Intake(hardwareMap);
        Transfer transfer       = new Transfer(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(shooter);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(transfer);

        commander.init();

        kicker.kicker.setPosition(Kicker.TARGETS.OUT.getTarget());
        waitForStart();

        if(ONE_PERSON_CONTROLS) {
            commander.runCommandsParallel(
                    new SoloTeleOpDriveControl(drive,gamepad1, telemetry),
                    new SoloTeleOpKickerControl(kicker,gamepad1),
                    new SoloTeleOpShooterControl(shooter,gamepad1),
                    new SoloTeleOpArmControl(arm,gamepad1),
                    new SoloTeleOpGripControl(grip,gamepad1),
                    new SoloTeleOpIntakeControl(intake,gamepad1),
                    new SoloTeleOpTransferControl(transfer,gamepad1)
            );
        } else {
            commander.runCommandsParallel(
                    new TeleOpDriveControl(drive,gamepad1, telemetry),
                    new TeleOpKickerControl(kicker,gamepad1),
                    new TeleOpShooterControl(shooter,gamepad1),
                    new TeleOpArmControl(arm,gamepad2),
                    new TeleOpGripControl(grip,gamepad2),
                    new TeleOpIntakeControl(intake,gamepad2),
                    new TeleOpTransferControl(transfer,gamepad2)
            );
        }


        commander.stop();
    }
}

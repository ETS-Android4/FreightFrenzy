package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

@Disabled
@Autonomous(name = "Command Auto", group = "finalized")
public class AutoRedPowershot extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander = new DogeCommander(this);

    private Drive drive;
    private Kicker kicker;
    private Shooter shooter;
    private Arm arm;
    private Grip grip;
    private Intake intake;
    private Transfer transfer;

    private double turnPower = 0.45;

    private final int PATH_OVERRIDE = -1; // -1 => no override; 0,1,4 => their respective paths; 5 => only common path

    @Override
    public void runOpMode() throws InterruptedException {
        drive             = new Drive(hardwareMap,true);
        kicker           = new Kicker(hardwareMap);
        shooter         = new Shooter(hardwareMap);
        arm                 = new Arm(hardwareMap);
        grip               = new Grip(hardwareMap);
        intake           = new Intake(hardwareMap);
        transfer       = new Transfer(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(shooter);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(transfer);

        AutoCameraControl cam = new AutoCameraControl(new Camera(hardwareMap), gamepad1, gamepad2, telemetry);

        while (!opModeIsActive() && !isStopRequested()) {
            cam.periodic();
        }

        commander.init();
        waitForStart();


        if(PATH_OVERRIDE == -1) {
            switch (choosePath(cam)) {
                case 0:
                    runPath0();
                    break;
                case 1:
                    runPath1();
                    break;
                case 4:
                    runPath4();

                    break;
                default:
                    runPath0();
                    break;
            }
        } else {
            switch (PATH_OVERRIDE) {
                case 0:
                    runPath0();
                    break;
                case 1:
                    runPath1();
                    break;
                case 4:
                    runPath4();
                    break;
                case 5:
                    runCommonPathBeforeSplit();
                    break;
                default:
                    runPath0();
                    break;
            }
        }



        commander.stop();
    }

    private void runCommonPathBeforeSplit() {
        telemetry.addLine("Running Common Path");
        telemetry.update();


        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget()));

        telemetry.addLine("Driving to shooting line");
        telemetry.update();
        commander.runCommandsParallel(
                new RunShooterForTime(shooter,false, 0.85),
                new DriveByEncoder(drive, InchToCount(60), 0, 0.5, telemetry)
        );
        sleep(200);

        telemetry.addLine("Turning and shooting");
        telemetry.update();

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 10, turnPower));
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 15, turnPower));
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 20, turnPower));
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);

        telemetry.addLine("Powering down shooter");
        telemetry.update();
        commander.runCommand(new RunShooterForTime(shooter, true, 0.0));
    }

    private void runPath0() {
        telemetry.addLine("Rings = 0");
        telemetry.update();
        runCommonPathBeforeSplit();

        telemetry.addLine("Running Path 0");
        telemetry.update();

        commander.runCommand(new TurnByGyroPID(drive, telemetry, -80, turnPower));
        sleep(500);


        telemetry.addLine("Drive to Wobble Goal");
        telemetry.update();
        commander.runCommand(new DriveByEncoder(drive, InchToCount(38.0), -60, 0.4));

        telemetry.addLine("Place Wobble Goal");
        telemetry.update();
        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
        sleep(1000);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
        sleep(500);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
        sleep(500);


    }
    private void runPath1() {
        telemetry.addLine("Rings = 1");
        runCommonPathBeforeSplit();

        telemetry.addLine("Running Path 1");
        telemetry.update();

        commander.runCommand(new StrafeByEncoder(drive, InchToCount(40), 0,.4));

        // Put arm out
        telemetry.addLine("Wobble Goal");
        telemetry.update();
        sleep(100); // Pause to settle before taking drive.heading() reading

        commander.runCommand(new DriveByEncoder(drive, InchToCount(16.0), drive.heading(),  0.4));

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
        sleep(1000);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
        sleep(500);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
        sleep(500);

    }


    private void runPath4() {
        telemetry.addLine("Rings = 4");
        telemetry.update();
        runCommonPathBeforeSplit();

        telemetry.addLine("Running Path 4");
        telemetry.update();

        commander.runCommand(new TurnByGyroPID(drive, telemetry, -30, turnPower));
        sleep(500);

        commander.runCommand(new DriveByEncoder(drive, InchToCount(65.0), drive.heading(), 0.4));
        sleep(200);

        //FIXME: Uncomment below when gripper works
//        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
//        sleep(500);
//
//        // Open grip to drop Wobble Goal
//        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
//        sleep(500);
//
//        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
        sleep(500);

        commander.runCommand(new DriveByEncoder(drive, InchToCount(-48.0), drive.heading(), 0.5));

    }

    private int choosePath(AutoCameraControl cam) {
        switch (cam.getPlacement()) {
            case ZERO_RINGS:
                return 0;
            case ONE_RING:
                return 1;
            case FOUR_RINGS:
                return 4;
            case UNKNOWN:
                cam.periodic();
                return choosePath(cam);
            default:
                cam.periodic();
                return choosePath(cam);
        }
    }

    private int InchToCount(double inch){return (int)(inch*Drive.COUNTS_PER_INCH_EMPIRICAL);}

}


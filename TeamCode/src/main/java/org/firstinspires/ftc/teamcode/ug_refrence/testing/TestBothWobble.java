package org.firstinspires.ftc.teamcode.ug_refrence.testing;


import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.ug_refrence.RingPlacement;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.ArmByEncoder;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.StrafeByEncoder;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Transfer;

@Config
@Autonomous(name = "Test Both Wobble", group = "testing")
public class TestBothWobble extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander = new DogeCommander(this);

    private Drive drive;
    private Kicker kicker;
    private Shooter shooter;
    private Arm arm;
    private Grip grip;
    private Intake intake;
    private Transfer transfer;

    private double turnPower = 0.45;

    public static int PS_WAIT_TIME1 = 1250;
    public static int PS_WAIT_TIME2 = 250;

    public static double PATH_OVERRIDE = -1; // -1 => no override; 0,1,4 => their respective paths; 5 => only common path; 6 => manual selection
    private RingPlacement placement;

    private final boolean RUN_SHOOTER = true;  //FIXME: Set true when kicker works

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

        commander.init();

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        if(PATH_OVERRIDE != 6) {
            while (!opModeIsActive() && !isStopRequested()) {
                cam.periodic();
            }
        } else {
            while (!opModeIsActive() && !isStopRequested()) {
                if(gamepad1.a) placement = RingPlacement.ZERO_RINGS;
                if(gamepad1.b) placement = RingPlacement.ONE_RING;
                if(gamepad1.x) placement = RingPlacement.FOUR_RINGS;
                if(gamepad1.y) placement = RingPlacement.UNKNOWN;

                telemetry.addData("Running placement", placement == RingPlacement.UNKNOWN ? "Common Path" : placement); // Unary if statement to write meaningful telemetry
                telemetry.update();
            }
        }

        waitForStart();

        // Select the path to run with
        if(PATH_OVERRIDE == -1) {
            choosePath(cam);
        } else {
            switch ((int)PATH_OVERRIDE) {
                case 0:
                    placement = RingPlacement.ZERO_RINGS;
                    break;
                case 1:
                    placement = RingPlacement.ONE_RING;
                    break;
                case 4:
                    placement = RingPlacement.FOUR_RINGS;
                    break;
                case 5:
                    placement = RingPlacement.UNKNOWN;
                    break;
                case 6:
                    break;
                default:
                    placement = RingPlacement.ZERO_RINGS;
                    break;
            }
        }

        //
        switch (placement) {
            case ZERO_RINGS:
                runPath0();
                break;
            case ONE_RING:
                runPath1();
                break;
            case FOUR_RINGS:
                runPath4();
                break;
            case UNKNOWN:   // Note: the is only reachable by PATH_OVERRIDE == 5
                runCommonPathBeforeSplit();
                break;
            default:
                runPath0();
                break;
        }


        commander.stop();
    }

    private void runCommonPathBeforeSplit() {
        telemetry.addLine("Running Common Path");
        telemetry.update();

        // Lock Arm in place
        arm.setPower(1);
        arm.setTargetPos(Arm.TARGETS.UP.getTarget());
        arm.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Move Kicker out and close Gripper
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget())); //FIXME


        telemetry.addLine("Driving to shooting line");
        telemetry.update();
        commander.runCommandsParallel(
                new RunShooterForTime(shooter, false, Shooter.VELO_LEVELS.POWER_SHOT.getVelo(), Shooter.MODE.VELOCITY),                  // Turn on Shooter to .85 power
                new DriveByEncoder(drive, InchToCount(60), 0, 0.5, telemetry)   // Drive to Shooting line
        );
        sleep(200);

//        transfer.setPower(-0.5);
//        transfer.periodic();

        telemetry.addLine("Turning and shooting");
        telemetry.update();
        // Turn to face first Power Shot
        commander.runCommand(new TurnByGyroPID(drive, telemetry, 10, turnPower));

        if (RUN_SHOOTER) {
            // Shoot first Power Shot
            commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
            sleep(PS_WAIT_TIME1);
            commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
            sleep(PS_WAIT_TIME2);
        }

        // Turn to face second Power Shot
        commander.runCommand(new TurnByGyroPID(drive, telemetry, 15, turnPower));

        if (RUN_SHOOTER) {
            // Shoot second Power Shot
            commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
            sleep(PS_WAIT_TIME1);
            commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
            sleep(PS_WAIT_TIME2);
        }
            // Turn to face third Power Shot
            commander.runCommand(new TurnByGyroPID(drive, telemetry, 20, turnPower));

        if (RUN_SHOOTER) {
            // Shoot third Power Shot
            commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
            sleep(PS_WAIT_TIME1);
            commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
            sleep(PS_WAIT_TIME2);
        }

//        transfer.setPower(0);
//        transfer.periodic();


        telemetry.addLine("Powering down shooter");
        telemetry.update();
        // Turn off shooter
        commander.runCommand(new RunShooterForTime(shooter, true, 0.0));
    }

    private void runPath0() {
        telemetry.addLine("Rings = 0");
        telemetry.update();
        runCommonPathBeforeSplit();

        telemetry.addLine("Running Path 0");
        telemetry.update();

        // Turn to Drop Zone
        commander.runCommand(new TurnByGyroPID(drive, telemetry, -65, turnPower));
        sleep(500);


        telemetry.addLine("Drive to Wobble Goal");
        telemetry.update();
        // Drive in front of Drop Zone
        commander.runCommand(new DriveByEncoder(drive, InchToCount(36.0), drive.heading(), 0.4));


        telemetry.addLine("Place Wobble Goal");
        telemetry.update();
        // Put Arm out
        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 3, telemetry));
        sleep(1000);

        // Drop Wobble Goal
        commander.runCommand(new GripSetState(grip, true));
        sleep(500);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 0.4, 3));
        sleep(300);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, -172, turnPower));
        sleep(100);

        commander.runCommandsParallel(
                new DriveByEncoder(drive, InchToCount(44.0), drive.heading(), 0.4),
                new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 3, telemetry)
        );
        sleep(200);

        commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget()));
        sleep(500);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 0.4, 4, telemetry));
        sleep(300);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, -20, turnPower));
        sleep(100);

        commander.runCommand(new DriveByEncoder(drive, InchToCount(33.0), 0, 0.5));
        sleep(100);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 4, telemetry));
        sleep(400);

        commander.runCommand(new GripSetState(grip, Grip.TARGETS.OPEN.getTarget()));
        sleep(600);

        commander.runCommandsParallel(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 2),
                new StrafeByEncoder(drive, InchToCount(-25), 0, 0.4));

        commander.runCommand(new DriveByEncoder(drive, InchToCount(5), 0, 0.8));
        // TODO: Tune grabbing wobble
    }
    private void runPath1() {
        telemetry.addLine("Rings = 1");
        runCommonPathBeforeSplit();

        telemetry.addLine("Running Path 1");
        telemetry.update();

        // Strafe in front of Drop Zone
        commander.runCommand(new StrafeByEncoder(drive, InchToCount(40.0), 0,.4));

        telemetry.addLine("Wobble Goal");
        telemetry.update();
        sleep(100); // Pause to settle before taking drive.heading() reading

        // Drive to Drop Zone
        commander.runCommand(new DriveByEncoder(drive, InchToCount(16.0), drive.heading(),  0.4));

        // Put Arm out
        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 0.5));
        sleep(1000);

        // Release Wobble Goal
        commander.runCommand(new GripSetState(grip, true));
        sleep(500);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, -180, turnPower));
        sleep(100);

        commander.runCommandsParallel(
                new DriveByEncoder(drive, InchToCount(60.0), drive.heading(), 0.5),
                new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 3, telemetry)
        );
        sleep(200);

        commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget()));
        sleep(600);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 0.4, 4, telemetry));
        sleep(300);

        commander.runCommand(new TurnByGyroPID(drive, telemetry, 20, turnPower));
        sleep(100);

        commander.runCommand(new DriveByEncoder(drive, InchToCount(60.0), drive.heading(), 0.8));
        sleep(200);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 2, telemetry));
        sleep(100);

        commander.runCommand(new GripSetState(grip, Grip.TARGETS.OPEN.getTarget()));
        sleep(300);

        commander.runCommand(new DriveByEncoder(drive, InchToCount(-10.0), 0, 0.8, telemetry));

        // TODO: Tune 2nd Wobble goal
    }

    private void runPath4() {
        telemetry.addLine("Rings = 4");
        telemetry.update();
        runCommonPathBeforeSplit();

        telemetry.addLine("Running Path 4");
        telemetry.update();

        // Turn to face Drop Zone
        commander.runCommand(new TurnByGyroPID(drive, telemetry, -30, turnPower));
        sleep(500);

        // Drive to Drop zone
        commander.runCommand(new DriveByEncoder(drive, InchToCount(65.0), drive.heading(), 0.4));
        sleep(200);

        // Put Arm out
        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 0.5));  // Bring wobble arm up)
        sleep(500);

        // Drop Wobble Goal
        commander.runCommand(new GripSetState(grip, true));
        sleep(500);

        // Drive back to parking line
        commander.runCommand(new DriveByEncoder(drive, InchToCount(-48.0), drive.heading(), 0.5));

        // TODO: Drive to and get second Wobble Goal

    }

    /**
     * Sets the correct placement of the Rings
     *
     * @param cam The Camera object to get the placement from
     */
    private void choosePath(AutoCameraControl cam) {
        placement = cam.getPlacement();
        if(placement == RingPlacement.UNKNOWN) {
            cam.periodic();
            choosePath(cam);
        }
    }


    private int InchToCount(double inch){return (int)(inch*Drive.COUNTS_PER_INCH_EMPIRICAL);}

}


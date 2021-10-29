package org.firstinspires.ftc.teamcode.ug_refrence.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.ug_refrence.RingPlacement;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.ArmByEncoder;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.AutoCameraControl;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Transfer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Config
@Disabled
@Autonomous(name = "AAATest Roadrunner Path", group = "testing")
public class TestRoadrunnerAutoPath extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander = new DogeCommander(this);

    private Kicker kicker;
    private Shooter shooter;
    private Arm arm;
    private Grip grip;
    private Intake intake;
    private Transfer transfer;

    private RingPlacement placement;


    public static double shootingTurn1 = 7;  // Degrees
    public static double shootingTurn12 = -6;  // Degrees
    public static double shootingTurn2 = 7;  // Degrees
    public static double shootingTurn22 = -4;  // Degrees

    public static double PATH_OVERRIDE = -1; // -1 => no override; 0,1,4 => their respective paths; 5 => only common path; 6 => manual selection

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        AutoCameraControl cam = new AutoCameraControl(new Camera(hardwareMap), gamepad1, gamepad2, telemetry);

        kicker              = new Kicker(hardwareMap);
        shooter             = new Shooter(hardwareMap);
        arm                 = new Arm(hardwareMap);
        grip                = new Grip(hardwareMap);
        intake              = new Intake(hardwareMap);
        transfer            = new Transfer(hardwareMap);

        commander.registerSubsystem(kicker);
        commander.registerSubsystem(shooter);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(transfer);

        Pose2d startPose = new Pose2d(-64,-50, 0.0);

        drive.setPoseEstimate(startPose);

        Trajectory commonPath = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.3, ()->{
                    // Lock Arm in place
                    arm.setPower(1);
                    arm.setTargetPos(Arm.TARGETS.UP.getTarget());
                    arm.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

                    grip.close(); // FIXME: Disabled gripper until fixed
                    kicker.out();
                    commander.runCommand(new RunShooterForTime(shooter, false, Shooter.VELO_LEVELS.POWER_SHOT.getVelo(), Shooter.MODE.VELOCITY));
                })
                .splineToLinearHeading(new Pose2d(-2,-53, Math.toRadians(32.0)), 0)

                .build();
        // --------- Case 0 --------- \\
        Trajectory zeroRingGet2 = drive.trajectoryBuilder(commonPath.end().plus(new Pose2d(0, 0, Math.toRadians(120))))
                .splineToSplineHeading(new Pose2d(-53, -40, Math.toRadians(90)), Math.toRadians(90),
                        new MinVelocityConstraint(Arrays.asList(
                                new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL * 3/4),
                                new MecanumVelocityConstraint(DriveConstants.MAX_VEL * 7/11, DriveConstants.TRACK_WIDTH)
                        )),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker((dist)->0.15*dist, ()->{
                    grip.open(); // FIXME: Disabled gripper until fixed
                    commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 4));
                })
                .splineToConstantHeading(new Vector2d(-53, -36.4), Math.toRadians(90),
                        new MinVelocityConstraint(Arrays.asList(
                            new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                            new MecanumVelocityConstraint(10, DriveConstants.TRACK_WIDTH)
                        )),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory zeroRingDrop2 = drive.trajectoryBuilder(zeroRingGet2.end())
                .splineToSplineHeading(new Pose2d(8, -44, Math.toRadians(-90)), Math.toRadians(-90))
                .build();

        Trajectory zeroRingToLine = drive.trajectoryBuilder(zeroRingDrop2.end())
                .splineToSplineHeading(new Pose2d(15, -20, 0), Math.toRadians(90))
                .build();

        // --------- Case 1 --------- \\

        Trajectory oneRingDrop1 = drive.trajectoryBuilder(commonPath.end().plus(new Pose2d(0,0, Math.toRadians(shootingTurn1 + shootingTurn12 + shootingTurn2 + shootingTurn22))))
                .splineToSplineHeading(new Pose2d(27, -43, Math.toRadians(20)), Math.toRadians(30))
                .build();

        Trajectory oneRingGet2 = drive.trajectoryBuilder(oneRingDrop1.end())
                .splineToSplineHeading(new Pose2d(0, -12, Math.toRadians(-160.5)), Math.toRadians(-160))
                .addDisplacementMarker((dist)->0.2*dist, ()->{
                    grip.open(); // FIXME: Disabled gripper until fixed
                    commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 4));
                })

                .splineToConstantHeading(new Vector2d(-31.1,-19.2), Math.toRadians(-160),
                        new MinVelocityConstraint(Arrays.asList(
                                new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                new MecanumVelocityConstraint(20, DriveConstants.TRACK_WIDTH)
                        )),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))

                .build();

        Trajectory oneRingDrop2 = drive.trajectoryBuilder(oneRingGet2.end())
                .splineToSplineHeading(new Pose2d(24, -35, 0), Math.toRadians(-20))
                .build();

        Trajectory oneRingToLine = drive.trajectoryBuilder(oneRingDrop2.end())
                .splineToConstantHeading(new Vector2d(15, -24), Math.toRadians(140))
                .build();

        // --------- Case 4 --------- \\

        Trajectory fourRingDrop1 = drive.trajectoryBuilder(commonPath.end().plus(new Pose2d(0,0, Math.toRadians(shootingTurn1 + shootingTurn12 + shootingTurn2 + shootingTurn22))))
                .splineToSplineHeading(new Pose2d(48, -58, 0), Math.toRadians(-50))
                .build();

        Trajectory fourRingStrafeOffWall = drive.trajectoryBuilder(fourRingDrop1.end())
                .strafeTo(new Vector2d(46,-53))
                .build();
//
        Trajectory fourRingGet2 = drive.trajectoryBuilder(new Pose2d(46, -53, Math.toRadians(160)))
                .splineToSplineHeading(new Pose2d(0, -12, Math.toRadians(11.5-180)), Math.toRadians(-160))
                .addDisplacementMarker((dist)->0.2*dist, ()->{
                    grip.open(); // FIXME: Disabled gripper until fixed
                    commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 4));
                })
//                .addDisplacementMarker(20, ()->{
//                    commander.runCommandsParallel(1
//                            new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 4, telemetry),
//                            new GripSetState(grip, Grip.TARGETS.OPEN.getTarget())
//                    );
//                })
                .splineToConstantHeading(new Vector2d(-31.1,-19.2), Math.toRadians(-160),
                        new MinVelocityConstraint(Arrays.asList(
                                new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                new MecanumVelocityConstraint(20, DriveConstants.TRACK_WIDTH)
                        )),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))

//                .addDisplacementMarker(()->{
//                    commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget()));
//                    sleep(500);
//
//                    commander.runCommandsParallel(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 2));
//                })
            .build();

        Trajectory fourRingDrop2 = drive.trajectoryBuilder(fourRingGet2.end())
                .splineToSplineHeading(new Pose2d(44, -54, Math.toRadians(-30)), Math.toRadians(-30))
                .build();

        Trajectory fourRingToLine = drive.trajectoryBuilder(fourRingDrop2.end())
                .strafeTo(new Vector2d(40,-50))
                .splineToSplineHeading(new Pose2d(15,-24, 0), Math.toRadians(140))
                .build();

        telemetry.addLine("Ready!");
        telemetry.update();


        commander.init();

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

        if (isStopRequested()) return;

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

        switch (placement) {
            case ZERO_RINGS:
                runCommonPath(drive, commonPath);
                zeroRingPath(drive, zeroRingGet2, zeroRingDrop2, zeroRingToLine);
                break;
            case ONE_RING:
                runCommonPath(drive, commonPath);
                oneRingPath(drive, oneRingDrop1, oneRingGet2, oneRingDrop2, oneRingToLine);
                break;
            case FOUR_RINGS:
                runCommonPath(drive, commonPath);
                fourRingPath(drive, fourRingDrop1, fourRingStrafeOffWall, fourRingGet2, fourRingDrop2, fourRingToLine);
                break;
            case UNKNOWN:
                runCommonPath(drive, commonPath);
                break;
            default:
                break;
        }

        commander.stop();


    }

    private void runCommonPath(@NotNull SampleMecanumDrive drive, @NotNull Trajectory ...paths) {
        drive.followTrajectory(paths[0]); // Common Path

        shoot();

        drive.turn(Math.toRadians(shootingTurn1));
        drive.turn(Math.toRadians(shootingTurn12));

        shoot();

        drive.turn(Math.toRadians(shootingTurn2));
        drive.turn(Math.toRadians(shootingTurn22));

        commander.runCommand(new RunShooterForTime(shooter, false, Shooter.VELO_LEVELS.POWER_SHOT_FAR.getVelo(), Shooter.MODE.VELOCITY));
        sleep(250);

        shoot();

        commander.runCommand(new RunShooterForTime(shooter, 0,0));
    }

    private void zeroRingPath(@NotNull SampleMecanumDrive drive, @NotNull Trajectory ...paths) {
        drive.turn(Math.toRadians(-1 * (30 + shootingTurn1 + shootingTurn12 + shootingTurn2 + shootingTurn22)));

        place();

        drive.turn(Math.toRadians(150));

        drive.followTrajectory(paths[0]); // Get 2

        sleep(400);

        commander.runCommand(new GripSetState(grip,Grip.TARGETS.CLOSE.getTarget()));
        sleep(500); //FIXME: Disabled gripper until fixed

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(500);

        drive.followTrajectory(paths[1]); // Drop 2

        place();

        drive.followTrajectory(paths[2]); // Park
    }

    private void oneRingPath(@NotNull SampleMecanumDrive drive, @NotNull Trajectory ...paths) {
        drive.followTrajectory(paths[0]); // Drop 1

        place();

        drive.followTrajectory(paths[1]); // Get 2

        sleep(400);

        commander.runCommand(new GripSetState(grip,Grip.TARGETS.CLOSE.getTarget()));
        sleep(500); //FIXME: Disabled gripper until fixed

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(500);

        drive.followTrajectory(paths[2]); // Drop 2

        place();

        drive.followTrajectory(paths[3]); // Park
    }

    private void fourRingPath(@NotNull SampleMecanumDrive drive, @NotNull Trajectory ...paths) {
        drive.followTrajectory(paths[0]);   // Drop 1

        place();

        drive.followTrajectory(paths[1]);    // Strafe off wall
        drive.turn(Math.toRadians(160));

        drive.followTrajectory(paths[2]); // Get 2

        sleep(400);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.CLOSE.getTarget()));
        sleep(500); //FIXME: Disabled gripper until fixed

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(500);

        drive.followTrajectory(paths[3]);  // Drop 2

        place();

        drive.followTrajectory(paths[4]); // Park
    }

    private void shoot() {
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);
    }

    private void place() {
        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(1000);

//         Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
        sleep(500); //FIXME: Disabled gripper until fixed

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(500);
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
}

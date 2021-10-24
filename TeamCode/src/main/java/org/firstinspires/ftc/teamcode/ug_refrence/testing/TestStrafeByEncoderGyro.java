package org.firstinspires.ftc.teamcode.ug_refrence.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.StrafeByEncoder;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Drive;

/**
 * This OpMode tests the gyro correction in the StrafeByEncoder command.
 *
 * @author Blake (mentor-coded gang)
 */
@Disabled
@Autonomous(name = "Test: Strafe by Encoder - gyro-corrected", group = "Testing")
public class TestStrafeByEncoderGyro extends LinearOpMode implements DogeOpMode {

    private final double STRAFE_POWER = 0.35;
    private final double TARGET_HEADING = 0.0;  // 0.0 is straight forward relative to the initial heading of the robot

    @Override
    public void runOpMode() {

        telemetry.addLine("Initializing...");
        telemetry.update();

        // Set up commander and robot subsystems
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);

        commander.init();

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();

        telemetry.addLine("Controls: ");
        telemetry.addData("x", "    Strafe left");
        telemetry.addData("b", "    Strafe right");
        telemetry.addData("a", "    Cancel current strafe");
        telemetry.update();

        // Await driver input
        while(opModeIsActive()) {
            // FIXME: gamepad2.a was added into the isCompleted() function of the StrafeByEncoder command. This will need to be removed after testing.
            if(gamepad2.x) {        // Strafe left
                commander.runCommandsParallel(new StrafeByEncoder(drive, Integer.MIN_VALUE / 2, TARGET_HEADING, STRAFE_POWER, gamepad2));
            } else if(gamepad2.b) { // Strafe right
                commander.runCommandsParallel(new StrafeByEncoder(drive, Integer.MAX_VALUE / 2, TARGET_HEADING, STRAFE_POWER, gamepad2));
            }
        }

        commander.stop();
    }
}

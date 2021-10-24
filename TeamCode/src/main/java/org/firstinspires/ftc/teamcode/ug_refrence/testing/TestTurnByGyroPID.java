package org.firstinspires.ftc.teamcode.ug_refrence.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.swampbots_util.GamepadCooldowns;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.auto.TurnByGyroPID;
import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Drive;

/**
 * This OpMode tests the TurnByGyroPID command.
 *
 * @author Blake
 */
@Disabled
@Autonomous(name = "Test: Turn by Gyro - PID controlled", group = "testing")
public class TestTurnByGyroPID extends LinearOpMode implements DogeOpMode {

    private final double TURN_POWER = 0.4;


    // Button cooldowns
    GamepadCooldowns cooldowns = new GamepadCooldowns();

    //Variable for thresholding LT and RT inputs, e.g. if(gamepad1.left_trigger > TRIGGER_THRESHOLD)
    public final double TRIGGER_THRESHOLD = 0.7;

    // Local runtime snapshot
    private double runtime;

    // PID coefficients
    private double kP = 0.0;
    private double kI = 0.0;
    private double kD = 0.0;

    private final double K_STEP = 0.005;

    @Override
    public void runOpMode() {

        telemetry.addLine("Initializing...");
        telemetry.update();

        // Set up commander and robot subsystems
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);

        commander.init();


        // Set cooldown length
        cooldowns.setCooldown(0.150); // 150 milliseconds

        // Done initializing
        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();

        // Await driver input
        while(opModeIsActive()) {
            /* --------------------------------- Gamepad 1 --------------------------------- */
            telemetry.addLine("Controls: (Gamepad 1)");
            telemetry.addLine("Increase / Decrease");
            telemetry.addData("Up/Dn", "    kP");
            telemetry.addData("Rt/Lf", "    kI");
            telemetry.addData("LB/LT", "    kD");
            telemetry.addData("kP", kP);
            telemetry.addData("kI", kI);
            telemetry.addData("kD", kD);

            //--------------------------------------------------------------------------------------
            // START PID COEFFICIENT CONTROLS
            //--------------------------------------------------------------------------------------
            runtime = getRuntime();


            // Proportional coefficient-------------------------------------------------------------
            if(gamepad1.dpad_up && cooldowns.dpUp.ready(runtime)) {
                kP += K_STEP;
                cooldowns.dpUp.updateSnapshot(runtime);
            }

            if(gamepad1.dpad_down && cooldowns.dpDown.ready(runtime)) {
                if(kP < K_STEP) kP = 0.0;
                else            kP -= K_STEP;
                cooldowns.dpDown.updateSnapshot(runtime);
            }


            // Integral coefficient-----------------------------------------------------------------
            if(gamepad1.dpad_right && cooldowns.dpRight.ready(runtime)) {
                kI += K_STEP;
                cooldowns.dpRight.updateSnapshot(runtime);
            }

            if(gamepad1.dpad_left && cooldowns.dpLeft.ready(runtime)) {
                if(kI < K_STEP) kI = 0.0;
                else            kI -= K_STEP;
                cooldowns.dpLeft.updateSnapshot(runtime);
            }


            // Derivative coefficient---------------------------------------------------------------
            if(gamepad1.left_bumper && cooldowns.lb.ready(runtime)) {
                kD += K_STEP;
                cooldowns.lb.updateSnapshot(runtime);
            }

            if(gamepad1.left_trigger > TRIGGER_THRESHOLD && cooldowns.lt.ready(runtime)) {
                if(kD < K_STEP) kD = 0.0;
                else            kD -= K_STEP;
                cooldowns.lt.updateSnapshot(runtime);
            }

            //--------------------------------------------------------------------------------------
            // END PID COEFFICIENT CONTROLS
            //--------------------------------------------------------------------------------------

            /* --------------------------------- Gamepad 2 --------------------------------- */
            telemetry.addLine("Controls: (Gamepad 2)");
            telemetry.addData("a", "    Turn to 0");
            telemetry.addData("b", "    Turn to 15");
            telemetry.addData("x", "    Turn to 90");
            telemetry.addData("y", "    Terminate current command");

            if(gamepad2.a) {        // Turn to 0
                commander.runCommandsParallel(new TurnByGyroPID(drive, telemetry, gamepad2, 0, TURN_POWER, kP, kI, kD));
            } else if(gamepad2.b) { // Turn to 45
                commander.runCommandsParallel(new TurnByGyroPID(drive, telemetry, gamepad2, 15, TURN_POWER, kP, kI, kD));
            } else if(gamepad2.x) { // Turn to 90
                commander.runCommandsParallel(new TurnByGyroPID(drive, telemetry, gamepad2, 90, TURN_POWER, kP, kI, kD));
            }

            telemetry.update();
        }

        commander.stop();
    }

}

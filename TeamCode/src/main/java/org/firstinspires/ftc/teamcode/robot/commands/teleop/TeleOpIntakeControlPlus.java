package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import static org.firstinspires.ftc.teamcode.CommandDrive.TRIGGER_THRESHOLD;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeColorSensor;

import java.util.Locale;


public class TeleOpIntakeControlPlus implements Command {
    private Gamepad gamepad;
    private Intake intake;
    private IntakeColorSensor sensor;
    private Telemetry telemetry;

    private final double POWER_SCALAR = 0.69;

    private boolean liftToggle = true;

    private ElapsedTime timer;
    private double t0;
    private double misfireTimer = -1.0;
    private final double MISFIRE_TIMEOUT = 2.5;

    private double overrideTimer = -1.0;
    private final double OVERRIDE_TIMEOUT = 2.5;

    private double delayUntilOverrideAgainTimer = -1.0;
    private final double DELAY_UNTIL_OVERRIDE_AGAIN_TIMEOUT = 4.0;

    public TeleOpIntakeControlPlus(Intake intake, IntakeColorSensor sensor, Gamepad gamepad, Telemetry telemetry) {
        this.intake = intake;
        this.gamepad = gamepad;
        this.sensor = sensor;
        this.telemetry = telemetry;

        timer = new ElapsedTime();
    }

    public TeleOpIntakeControlPlus(Intake intake, IntakeColorSensor sensor, Gamepad gamepad) {
        this(intake, sensor, gamepad, null);
    }

    @Override
    public void start() {
        intake.setPower(0);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

//        intake.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset encoder for Arm // Not needed anymore b/c of switch to normal servos
//        intake.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        timer.reset();
        t0 = timer.seconds();

        intake.out();
    }

    @Override
    public void periodic() {
        double deltaT = timer.seconds() - t0;
        t0 = timer.seconds();

        // Two Person Controls:
        // In: RB   Out: RT
        intake.setPower(
                (       gamepad.left_trigger > TRIGGER_THRESHOLD   ? 1.0 :
                        gamepad.right_trigger > TRIGGER_THRESHOLD  ? -1.0 : 0.0
                ) * POWER_SCALAR);

        // Turn off intake when autodetected
        if(overrideTimer < OVERRIDE_TIMEOUT) {
            if(gamepad.left_trigger > TRIGGER_THRESHOLD && gamepad.right_trigger > TRIGGER_THRESHOLD) { // Cancel the override if both triggers are held down
                overrideTimer = OVERRIDE_TIMEOUT;
            } else { // Otherwise follow thru with the override
                intake.setPower(0.0);
            }
        }

        boolean toggleLift = gamepad.guide;
        if(toggleLift) {
            if(liftToggle) {
                    if(intake.getPosition() == Intake.LIFT_POSITIONS.IN) {
                        misfireTimer = 0;
                    }
                intake.toggle();
                liftToggle = false;
            }
        } else {
            liftToggle = true;
        }

        if(sensor.doesSensorSeeAnything() && intake.getPosition() == Intake.LIFT_POSITIONS.OUT) {
                if(misfireTimer > MISFIRE_TIMEOUT) {
                    intake.setPosition(Intake.LIFT_POSITIONS.IN);
                    misfireTimer = 0;
                }
                if(overrideTimer > OVERRIDE_TIMEOUT && delayUntilOverrideAgainTimer > DELAY_UNTIL_OVERRIDE_AGAIN_TIMEOUT) {
                    overrideTimer = 0;
                    delayUntilOverrideAgainTimer = 0;
                }
        }


        misfireTimer += deltaT;
        overrideTimer += deltaT;
        delayUntilOverrideAgainTimer += deltaT;

        if(telemetry != null) {
            telemetry.addLine("Intake Telemetry:");
            telemetry.addData("Power:", intake.getPower());
            telemetry.addData("Direction:", intake.getDirection());
            telemetry.addLine();
            telemetry.addData("Position:", intake.getPosition());
            telemetry.addLine();

            telemetry.addData("deltaT=", deltaT);
            telemetry.addData("t0:", t0);
            telemetry.addData("misfire timer:", misfireTimer);
            telemetry.addData("override timer:", overrideTimer);
            telemetry.addData("delay timer:", delayUntilOverrideAgainTimer);

            sensor.toTelemetry(telemetry);
            telemetry.update();
        }
    }

    @Override
    public void stop() {
        intake.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

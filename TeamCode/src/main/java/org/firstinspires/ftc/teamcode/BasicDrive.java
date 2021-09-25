package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.BasicDriveHardware.INTAKE_MAX_POWER;
import static org.firstinspires.ftc.teamcode.BasicDriveHardware.KICKER_SPEED_MAX;
import static org.firstinspires.ftc.teamcode.BasicDriveHardware.KICKER_SPEED_MIN;
import static org.firstinspires.ftc.teamcode.BasicDriveHardware.SHOOTER_MAX_POWER;
import static org.firstinspires.ftc.teamcode.BasicDriveHardware.SWEEPER_RANGE_MAX;
import static org.firstinspires.ftc.teamcode.BasicDriveHardware.SWEEPER_RANGE_MIN;
import static org.firstinspires.ftc.teamcode.BasicDriveHardware.TRANSFER_MAX_POWER;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Mecanum Driver Control", group = "old")
public class BasicDrive extends OpMode {
    BasicDriveHardware hardware = new BasicDriveHardware();


    double intakePower   = 0;
    double transferPower = 0;
    double shooterPower  = 0;


    public void init() {
        hardware.init(hardwareMap);


        hardware.kicker.    setPower(KICKER_SPEED_MIN);
        hardware.sweeper.   setPosition(SWEEPER_RANGE_MIN);




        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {


        // Do the math
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double twist = -gamepad1.right_stick_x;

         intakePower     = gamepad1.a ? 1.0 * INTAKE_MAX_POWER : 0.0;
         transferPower   = gamepad1.a ? 1.0 * TRANSFER_MAX_POWER : 0.0;

         shooterPower    = gamepad1.b ? 1.0 * SHOOTER_MAX_POWER : 0.0;

        // Set the power
        hardware.setMecanumPower(drive, strafe, twist, .6);

        hardware.intake.setPower(intakePower);
        hardware.transfer.setPower(transferPower);

        hardware.shooter.setPower(shooterPower);

        hardware.kicker.setPower(gamepad1.y ? KICKER_SPEED_MAX : KICKER_SPEED_MIN);
        hardware.sweeper.setPosition(gamepad1.x ? SWEEPER_RANGE_MAX : SWEEPER_RANGE_MIN);


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Wheel driver speed mod", .6);
        telemetry.addLine();
    }


}

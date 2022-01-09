package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CommandDrive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;

public class TeleOpCarouselControl implements Command {
    private Gamepad gamepad;
    private Carousel carousel;
    private Telemetry telemetry;

    private final double POWER_SCALAR = 1.0;
    private final double BASE_VELO = 1.0;
    private final double ACC = 0.0;

    private double velo = 0;

    private ElapsedTime timer;
    private double t0;

    public TeleOpCarouselControl(Carousel carousel, Gamepad gamepad, Telemetry telemetry) {
        this.carousel = carousel;
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        timer = new ElapsedTime();
        t0 = timer.seconds();
    }

    public TeleOpCarouselControl(Carousel carousel, Gamepad gamepad) {
        this(carousel, gamepad, null);
    }

    @Override
    public void start() {
        carousel.setPower(0);
        carousel.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        // Two Person Controls:
        // In: RB   Out: RT
        boolean lt = gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean rt = gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;

        double deltaT = timer.seconds() - t0;
        t0 = timer.seconds();

        if(lt || rt) {
            velo += ACC * deltaT;
        } else {
            velo = 0;
        }

        carousel.setPower(
                (velo + BASE_VELO) *
                (       lt  ?  1.0 :
                        rt  ? -1.0 : 0.0
                ) * POWER_SCALAR);

        if(telemetry != null) {
            telemetry.addLine("Carousel Data:");
            telemetry.addData("acc:", ACC);
            telemetry.addData("velo:", velo);
            telemetry.addData("deltaT:", deltaT);
            telemetry.addData("acc * delta t", ACC *deltaT);
            telemetry.addData("delta v / 1 sec", ACC /deltaT);
            telemetry.addLine();
            telemetry.addData("Power:", carousel.getPower());
            telemetry.addData("Direction:", carousel.getDirection());
            telemetry.addLine();
            telemetry.addData("Power Scalar:", POWER_SCALAR);
            telemetry.update();
        }
    }

    @Override
    public void stop() {
         carousel.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

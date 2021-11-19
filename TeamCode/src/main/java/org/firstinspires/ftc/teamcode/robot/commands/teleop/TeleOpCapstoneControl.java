package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapArm;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapGrip;

public class TeleOpCapstoneControl implements Command {
    private CapGrip grip;
    private CapArm arm;
    private Gamepad gamepad;
    private Telemetry telemetry;

    private ElapsedTime timer;
    private double t0;

    private final double GRIP_TIME = 1.6; //TODO: Tune this once Capstone works

    private double grippingTimer = -1;
    private boolean gripToggle = true;
    private boolean prepareToMoveArmUp = false;

    public TeleOpCapstoneControl(CapArm arm, CapGrip grip, Gamepad gamepad, Telemetry telemetry) {
        this.arm = arm;
        this.grip = grip;
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        timer = new ElapsedTime();
    }

    public TeleOpCapstoneControl(CapArm arm, CapGrip grip, Gamepad gamepad) {
        this(arm, grip, gamepad, null);
    }

    @Override
    public void start() {
        grip.place();
        arm.bottom();

        timer.reset();
        t0 = timer.seconds();

        grippingTimer = GRIP_TIME;
    }

    @Override
    public void periodic() {
        boolean gripAndGoMid = gamepad.y;
        boolean gotoTop = gamepad.b;
        boolean place = gamepad.a;
        boolean ungripAndGoDown = gamepad.x;


        double deltaT = timer.seconds() - t0;
        t0 = timer.seconds();


        if(
                gripAndGoMid &&
                !prepareToMoveArmUp &&
//                grip.getTargetPos() == CapGrip.POSITION.PLACE &&
                arm.getTargetPos() == CapArm.POSITION.BOTTOM
        ) {
            grip.grip();
            prepareToMoveArmUp = true;
            grippingTimer = 0.0;
        }
        if(prepareToMoveArmUp && grippingTimer > GRIP_TIME) {
            arm.middle();
            prepareToMoveArmUp = false;
        }

        if(gotoTop) {
            arm.top();
        }

        if(place && gripToggle) {
            grip.toggle();
            gripToggle = false;
        } else if(!place && !gripToggle) {
            gripToggle = true;
        }

        if(ungripAndGoDown) {
            arm.bottom();
            grip.place();
        }


//        boolean gripperGrip = gamepad.a;
//        boolean gripperPlace = gamepad.b;
//        boolean armDown = gamepad.x;
//        boolean armUp = gamepad.y;
//
//        if(gripperGrip) {
//            grip.grip();
//        } else if(gripperPlace) {
//            grip.place();
//        }
//
//        if(armUp) {
//            arm.top();
//        } else if(armDown) {
//            arm.bottom();
//        }

        grippingTimer += deltaT;


        if(telemetry != null) {
            telemetry.addLine("Cap telemetry:");
//            telemetry.addData("grip grip?", gripperGrip);
//            telemetry.addData("grip place?", gripperPlace);
//            telemetry.addData("arm down?", armDown);
//            telemetry.addData("arm up?", armUp);
            telemetry.addData("grip & up?", gripAndGoMid);
            telemetry.addData("arm top?", gotoTop);
            telemetry.addData("grip place?", place);
            telemetry.addData("ungrip & down?", ungripAndGoDown);
            telemetry.addLine();
            telemetry.addData("grip target pos", grip.getTargetPos());
            telemetry.addData("arm target pos", arm.getTargetPos());
            telemetry.addLine();
            telemetry.addData("timer", timer.seconds());
            telemetry.addData("delta t", deltaT);
            telemetry.addData("gripping timer", grippingTimer);
            telemetry.addData("prepare to move arm up?", prepareToMoveArmUp);
            telemetry.update();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeDistanceSensor;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

import java.util.Locale;

public class TeleOpArmSlideKickerControlPlus implements Command {
    private Arm arm;
    private Slides slides;
    private Kicker kicker;
    private IntakeDistanceSensor distanceSensor;
    private Gamepad gamepad;
    private Telemetry telemetry;

    private ElapsedTime timer;
    private double t0;

    private SwampbotsUtil util;

    private boolean autoMode = true;
    private double kickerTimer = -1;
    private double intakeTimer = -1;
    private boolean goingDown = true;

    private final double DROP_TIME = 1.0;   //seconds
    private final double IN_TIME = 0.8;
    private final double POWER_SCALAR = 1.0;
    private final double LONG_DROP_POWER_SCALAR = 0.6;

    private States currentState;
    private double currentTimeout = -1;

    private boolean cancelMove = false;

    private boolean kickerToggle = true;
    private String armToggle = "intake";

    private double armTimer = -1;
    private final double ARM_TIMEOUT = 1;

    private boolean autoIntake = false;
    private double autoIntakeTimer = -1;

    private enum States {
        INTAKE,
        LOW_SHARED,
        HIGH_SHARED,
        DEPOSIT,
        MOVING,
        MANUAL_CONTROL;


    }

    private enum TIMEOUTS {
        ARM_INTAKE_TO_MIDDLE,
        KICKER,
        DELAY_FOR_AUTO_INTAKE;

        public double getTimeout() { // TODO: Check values
            switch (this) {
                case ARM_INTAKE_TO_MIDDLE:
                    return 1.3;
                case KICKER:
                    return 0.4; //TODO: Decreased from 0.6 -> 0.4
                case DELAY_FOR_AUTO_INTAKE:
                    return 5.0;
                default:
                    return 0.0;
            }
        }
    }

    public TeleOpArmSlideKickerControlPlus(Arm arm, Slides slides, Kicker kicker, IntakeDistanceSensor distanceSensor, Gamepad gamepad, Telemetry telemetry) {
        this.arm = arm;
        this.slides = slides;
        this.kicker = kicker;
        this.distanceSensor = distanceSensor;
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        timer = new ElapsedTime();
        util = new SwampbotsUtil();
    }

    public TeleOpArmSlideKickerControlPlus(Arm arm, Slides slides, Kicker kicker, IntakeDistanceSensor distanceSensor, Gamepad gamepad) {
        this(arm, slides, kicker, distanceSensor, gamepad, null);
    }

    @Override
    public void start() {
        arm.intake();
        kicker.open();

        slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slides.setPower(0.0);

        timer.reset();
        t0 = timer.seconds();
        kickerTimer = DROP_TIME;
        intakeTimer = IN_TIME;
        autoIntakeTimer = TIMEOUTS.DELAY_FOR_AUTO_INTAKE.getTimeout();

        currentState = States.INTAKE;

        autoMode = true;
        slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setPower(POWER_SCALAR);
    }

    @Override
    public void periodic() {
        boolean intake      = false;//gamepad.a;    //intake
        boolean middle      = false;//gamepad.x;    //middle
//        boolean deposit     = false;//gamepad.b;    //deposit
        boolean powerIn     = false;//gamepad.dpad_up;      //gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean powerOut    = false;//gamepad.dpad_down;    //gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean encoderIn   = false;//gamepad.left_bumper;
        boolean encoderOut  = false;//gamepad.x; //gamepad.right_bumper;

        boolean setAutoOn   = gamepad.dpad_left;
        boolean setAutoOff  = gamepad.dpad_right;


        boolean dropAndReturn = gamepad.left_bumper;
        boolean cancelMoveAndReturn = gamepad.right_bumper;
        boolean deposit = gamepad.a;
        boolean lowShared = gamepad.y;
        boolean highShared = gamepad.b;
        boolean toggleKicker = false;//gamepad.y;

        if(setAutoOn) {
            autoMode = true;
            slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            slides.setPower(POWER_SCALAR);
        }
        if(setAutoOff) {
            autoMode = false;
        }

        double deltaT = timer.seconds() - t0;
        t0 = timer.seconds();

        if(autoMode) {

            //TODO: Make sure this all works
            switch (currentState) {
                case INTAKE:
                    if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.IN.getTargets(), 10) && !armToggle.equals("intake")) {
                        kicker.open();
                        armToggle = "intake";
                        autoIntake = false;
                    }

                    // Allow toggling the kicker when we are in
                    if(toggleKicker) {
                        if(kickerToggle) {
                            kicker.toggle();
                            kickerToggle = false;
                        }
                    } else {
                        kickerToggle = true;
                    }

                    if(oneOfThree(deposit, lowShared, highShared) && goingDown) { // Only one is pressed
                        kicker.hold();
                        goingDown = false;

                        intakeTimer = 0.0;
//                        slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        arm.middle();
                    }

                    if(!goingDown && cancelMoveAndReturn) {
                        goingDown = true;
                        kicker.open();
                        arm.intake();
                    }

                    if(intakeTimer > IN_TIME) {
                        setTarget(deposit, lowShared, highShared);
                    }

                    if(distanceSensor.doesSensorSeeAnything() && !autoIntake && autoIntakeTimer > TIMEOUTS.DELAY_FOR_AUTO_INTAKE.getTimeout()) {
                        kicker.hold();
                        goingDown = false;

                        intakeTimer = 0.0;
                        arm.middle();

                        autoIntake = true;
                    }
                    if(cancelMoveAndReturn && autoIntake) {
                        kicker.open();
                        arm.intake();
                        goingDown = true;
                        autoIntake = false;
                    }


                    break;
                case DEPOSIT:
                    if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets(), 10) && !armToggle.equals("deposit")) {
                        arm.deposit();
                        armToggle = "deposit";
                    }

                    if(cancelMoveAndReturn && !goingDown) {
                        cancelMove = true;
                        arm.middle(); // Prob need a fix for if you cancel and are too close to 'middle' s.t. arm hits robot
//                        slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        armTimer = 0;
                    }
                    if(cancelMove && armTimer > ARM_TIMEOUT) {
                        slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                    }

                    if(cancelMove && slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets() - 50) { // -50 b/c slides get more negative as they go further out
                        if(oneOfThree(deposit, lowShared, highShared)) {
                            cancelMove = false;

                            setTarget(deposit, lowShared, highShared);
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50)) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            kicker.open();
                            currentState = States.INTAKE;
                        }
                    }

                    if(dropAndReturn && !cancelMove) {
                        kicker.close();
                        //Drop what is used to drop
                        kickerTimer = 0.0;
                        armTimer = 0;
                        goingDown = true;
                    }

                    if(goingDown) {
                        if(kickerTimer > TIMEOUTS.KICKER.getTimeout()) {
                            arm.middle();
                            slides.setPower(LONG_DROP_POWER_SCALAR); //TODO: Added this and line below to allow slides to decrease while arm moves
                            slides.setTargetPos(Slides.TARGETS.LOW_SHARED.getTargets());

                            if(armTimer > ARM_TIMEOUT + TIMEOUTS.KICKER.getTimeout()) {
                                slides.setPower(LONG_DROP_POWER_SCALAR);
                                slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());

                            }
//                            slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets())) {
                            arm.intake();
                            slides.setPower(POWER_SCALAR);
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(lowShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.LOW_SHARED.getTargets());
                        arm.lowShared();
                        currentState = States.LOW_SHARED;
                    }

                    if(highShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.HIGH_SHARED.getTargets());
                        arm.highShared();
                        currentState = States.HIGH_SHARED;
                    }

                    break;
                case HIGH_SHARED:
                    if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.HIGH_SHARED.getTargets(), 10) && !armToggle.equals("high")) {
                        arm.highShared();

                        armToggle = "high";
                    }

                    if(cancelMoveAndReturn && !goingDown) {
                        cancelMove = true;
                        arm.middle(); // Prob need a fix for if you cancel and are too close to 'middle' s.t. arm hits robot
//                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                        armTimer = 0;
                    }
                    if(cancelMove && armTimer > ARM_TIMEOUT) {
                        slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                    }

                    if(cancelMove && slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets() - 50) {
                        if(oneOfThree(deposit, lowShared, highShared)) {
                            cancelMove = false;

                            setTarget(deposit, lowShared, highShared);
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50)) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(dropAndReturn && !cancelMove) {
                        kicker.close();
                        //Drop what is used to drop
                        kickerTimer = 0.0;
                        armTimer = 0;
                        goingDown = true;
                    }

                    if(goingDown) {
                        if(kickerTimer > TIMEOUTS.KICKER.getTimeout()) {
                            arm.middle();

                            if(armTimer > ARM_TIMEOUT + TIMEOUTS.KICKER.getTimeout()+0.4)  {
                                slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                                kicker.open();
                            }
//                            slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets())) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }


                    if(deposit && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
                        arm.deposit();
                        currentState = States.LOW_SHARED;
                    }

                    if(lowShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.LOW_SHARED.getTargets());
                        arm.lowShared();
                        currentState = States.LOW_SHARED;
                    }

                    break;
                case LOW_SHARED:
                    if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.LOW_SHARED.getTargets(), 10) && !armToggle.equals("low")) {
                        arm.lowShared();
                        armToggle = "low";
                    }

                    if(cancelMoveAndReturn && !goingDown) {
                        cancelMove = true;
                        arm.middle(); // Prob need a fix for if you cancel and are too close to 'middle' s.t. arm hits robot
//                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                        armTimer = 0;
                    }
                    if(cancelMove && armTimer > ARM_TIMEOUT) {
                        slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                    }

                    if(cancelMove && slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets() - 50) {
                        if(oneOfThree(deposit, lowShared, highShared)) {
                            cancelMove = false;

                            setTarget(deposit, lowShared, highShared);
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50)) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(dropAndReturn && !cancelMove) {
                        kicker.close();
                        //Drop what is used to drop
                        kickerTimer = 0.0;
                        armTimer = 0;
                        goingDown = true;
                    }

                    if(goingDown) {
                        if(kickerTimer > TIMEOUTS.KICKER.getTimeout()) {
                            arm.middle();

                            if(armTimer > ARM_TIMEOUT + TIMEOUTS.KICKER.getTimeout() + 0.4) {
                                slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                                kicker.open();
                            }
//                            slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets())) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(deposit && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
                        arm.deposit();
                        currentState = States.LOW_SHARED;
                    }

                    if(highShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.HIGH_SHARED.getTargets());
                        arm.highShared();
                        currentState = States.HIGH_SHARED;
                    }

                    break;
            }

            // Arm pos = f(slide pos) = {intake if slide pos > middle, middle if slideMax < slide pos < middle, out only by input}
//            if(encoderIn ^ encoderOut) {
//                slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//                slides.setPower(POWER_SCALAR);
//                if(encoderOut) {
//                    goingDown = false;
//
//                    intakeTimer = 0.0;
//                }
//            }
//
//            if(slides.getRunMode() == DcMotor.RunMode.RUN_TO_POSITION) {
//                if (goingDown) { // Coming in
//                    if(droppingTimer >= DROP_TIME) {
//                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());
//
//                        if (slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets()) {
//                            arm.intake();
//                        } else {
//                            arm.middle();
//                        }
//
//                    }
//                } else { // Going out
//                    arm.middle();
//
//                    if (intakeTimer >= IN_TIME) {
//                        slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
//
//                        if (encoderIn && util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets(), 30)) {
//                            arm.deposit();
//
//                            droppingTimer = 0.0;
//
//                            goingDown = true;
//                        } else if(encoderIn && !util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets(), 600)) {
//                            goingDown = true;
//                        }
//                    } else if(encoderIn) {
//                        goingDown = true;
//                    }
//
//                }
//
//
////                if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets()) ||
////                        util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.IN.getTargets())) {
////                    slides.setPower(1);
////                }
//            }

            kickerTimer += deltaT;
            intakeTimer += deltaT;
            armTimer += deltaT;
            autoIntakeTimer += deltaT;



        } else {
            intake      = gamepad.a;    //intake
            middle      = gamepad.x;    //middle
            deposit     = gamepad.b;    //deposit
            powerIn     = gamepad.dpad_up;      //gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
            powerOut    = gamepad.dpad_down;    //gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;
            encoderIn   = gamepad.left_bumper;
            encoderOut  = gamepad.x; //gamepad.right_bumper;
            lowShared   = gamepad.dpad_left;    // Remove button collisions
            highShared  = gamepad.dpad_right;   // Remove button collisions

            if(middle) {
                arm.middle();
            } else if(intake) {
                arm.intake();
            } else if(deposit) {
                arm.deposit();
            } else if(lowShared) {
                arm.lowShared();
            } else if(highShared) {
                arm.highShared();
            }
            if(powerIn || powerOut) {
                slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slides.setPower(((powerIn ? 1 : 0) - (powerOut ? 1 : 0)) * POWER_SCALAR);
            } else if(encoderIn ^ encoderOut) {
                slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
                slides.setPower(POWER_SCALAR);
                if(encoderIn) {
                    slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                }
                if(encoderOut) {
                    slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
                }
            } else /*if(slides.getRunMode() != DcMotor.RunMode.RUN_TO_POSITION)*/{ //TODO: Uncomment once we finish testing
                slides.setPower(0);
            }
        }




        if(telemetry != null) {
            telemetry.addLine("Arm telemetry:");
            telemetry.addData("intake?", intake);
            telemetry.addData("middle?", middle);
            telemetry.addData("deposit?", deposit);
            telemetry.addData("low shared?", lowShared);
            telemetry.addData("high shared?", highShared);
            telemetry.addLine();
            telemetry.addData("target pos:", arm.getTargetPos());
            telemetry.addLine();

            telemetry.addLine("Slide Telemetry:");
            telemetry.addData("power in?", powerIn);
            telemetry.addData("power out?",powerOut);
            telemetry.addData("encoder in?", encoderIn);
            telemetry.addData("encoder out?",encoderOut);
            telemetry.addLine();
            telemetry.addData("power:", slides.getPower());
            telemetry.addData("current pos:", slides.getCurrentPos());
            telemetry.addData("target pos:", slides.getTargetPos());
            telemetry.addData("runMode:", slides.getRunMode());
            telemetry.addLine();

            telemetry.addLine("Kicker Telemetry:");
            telemetry.addData("toggle?", toggleKicker);
            telemetry.addData("can toggle?", kickerToggle);
            telemetry.addLine();
            telemetry.addData("raw position:", kicker.getPosition());
            telemetry.addData("real position:", kicker.getPosition().getPosition());
            telemetry.addData("direction:", kicker.getDirection());
            telemetry.addLine();

            telemetry.addLine("Color Sensor Telemetry:");
            telemetry.addData("see anything?", distanceSensor.doesSensorSeeAnything());

            telemetry.addLine();

            telemetry.addLine("FSM Telemetry:");
            telemetry.addData("fsm active?", autoMode);
            telemetry.addData("going down?", goingDown);
            telemetry.addData("cancelling action?", cancelMoveAndReturn);
            telemetry.addData("auto intake?", autoIntake);
            telemetry.addLine();
            telemetry.addData("current state:", currentState);
            telemetry.addLine();
            telemetry.addData("delta t=", String.format("%.3f",deltaT*1000));
            telemetry.addData("t0:", String.format("%.3f",t0*1000));
            telemetry.addData("dropping timer:", kickerTimer);
            telemetry.addData("intake timer:", intakeTimer);
            telemetry.addData("arm timer:", armTimer);
            telemetry.addData("arm toggle", armToggle);

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

    private boolean oneOfThree(boolean a, boolean b, boolean c) {
        return (a ? 1 : 0) + (b ? 1 : 0) + (c ? 1 : 0) == 1;
    }

    private void setTarget(boolean deposit, boolean lowShared, boolean highShared) {
        if(deposit) {
            slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
            currentState = States.DEPOSIT;
        }
        if(lowShared) {
            slides.setTargetPos(Slides.TARGETS.LOW_SHARED.getTargets());
            currentState = States.LOW_SHARED;
        }
        if(highShared) {
            slides.setTargetPos(Slides.TARGETS.HIGH_SHARED.getTargets());
            currentState = States.HIGH_SHARED;
        }
    }
}

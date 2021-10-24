package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

public class ArmSetState implements Command {
    private Arm arm;

    private Arm.POSITION state;
    private boolean toggleDirection;

    public ArmSetState(Arm arm, Arm.POSITION state) {
        this.arm = arm;
        this.state = state;
    }

    /**
     * Note about toggle:
     * True increases level, false decreases level. Change is circular.
     *   For true: Intake -> Middle -> Deposit -> Intake -> etc.
     *   False is opposite direction
     *
     * Full table:
     *                              Toggle
     *                           True   |  False
     *                Deposit | Intake  | Middle
     * Current Level  Middle  | Deposit | Intake
     *                Intake  | Middle  | Deposit
     */
    public ArmSetState(Arm arm, boolean toggle) {
        this.arm = arm;
        this.state = null;
        toggleDirection = toggle;
    }


    @Override
    public void start() {
        if(state != null && state == Arm.POSITION.DEPOSIT) {
            arm.deposit();
        }
        if(state != null && state == Arm.POSITION.MIDDLE) {
            arm.middle();
        }
        if(state != null && state == Arm.POSITION.INTAKE) {
            arm.intake();
        }
        if(state == null) {
            switch (arm.getTargetPos()) {
                case DEPOSIT:
                    if(toggleDirection) {
                        arm.intake();
                    } else {
                        arm.middle();
                    }
                    break;
                case MIDDLE:
                    if(toggleDirection) {
                        arm.deposit();
                    } else {
                        arm.intake();
                    }
                    break;
                case INTAKE:
                    if(toggleDirection) {
                        arm.middle();
                    } else {
                        arm.deposit();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}

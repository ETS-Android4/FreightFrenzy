package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class BasicDriveHardware {

    // Motors
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor rearLeft;
    public DcMotor rearRight;

    public DcMotor intake;
    public DcMotor transfer;

    public DcMotor shooter;



    // Servos
    public CRServo kicker;
    public Servo sweeper;


    // IMU
    BNO055IMU imu;

    // Drive encoder variables
    public final double COUNTS_PER_REV_HD_20    = 560; // REV HD Hex 20:1 motor
    public final double DRIVE_GEAR_REDUCTION    = 20.0 / 26.0; // 15 tooth on motor shaft to 15 tooth on wheel shaft
    public final double WHEEL_DI_INCHES         = 90.0 / 25.4; // 90mm diameter wheel divided by 25.4(in/mm)
    //    public final double COUNTS_PER_INCH         = (COUNTS_PER_REV_HD_20 * DRIVE_GEAR_REDUCTION) / (WHEEL_DI_INCHES * Math.PI);
    public final double COUNTS_PER_INCH_EMPIRICAL = 1000 / 24.0;    // 1000 counts divided by 24.0 inches; determined through testing

    // Servo-specific variables
    public static final double KICKER_SPEED_MAX = .8;
    public static final double KICKER_SPEED_MIN = 0.2;
    public static final double SWEEPER_RANGE_MAX    = 1.0;
    public static final double SWEEPER_RANGE_MIN    = 0.0;

    // Speed modifier variables
    public static final double SLOW     = 0.35;
    public static final double NORMAL   = 0.8;
    public static final double FAST     = 1.0;

    // Motor modifier variables
    public static final double INTAKE_MAX_POWER     = 1.0;
    public static final double TRANSFER_MAX_POWER   = 1.0;
    public static final double SHOOTER_MAX_POWER    = 0.7;

    // PID variables
    public final double MAX_SPEED = 0.3;
    public final double P = 0.025;
    public final double I = 0.000;
    public final double D = 0.000;
    public final double TOLERANCE = 1.0;

//    public final double DV_TOLERANCE = 0.2;


    // Timeout variable for looping methods
    public static final double TIMEOUT = 2.0;  // 2 seconds

    // Camera variables
    public OpenCvCamera webcam;
    public RingPatternPipeline vision;

    public void init(HardwareMap hwMap) {
        init(hwMap, false, false); // Default is to not initialize the camera or the IMU
    }


    public void init(HardwareMap hardwareMap, boolean initCamera, boolean initIMU) {
        /*frontLeft   = hardwareMap.dcMotor.get("fl_drive");
        frontRight  = hardwareMap.dcMotor.get("fr_drive");
        rearLeft    = hardwareMap.dcMotor.get("rl_drive");
        rearRight   = hardwareMap.dcMotor.get("rr_drive");

        intake      = hardwareMap.dcMotor.get("intake");
        transfer    = hardwareMap.dcMotor.get("transfer");

        shooter     = hardwareMap.dcMotor.get("shooter");

        kicker      = hardwareMap.crservo.get("kicker");
        sweeper     = hardwareMap.servo.get("sweeper");


        frontLeft.  setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.   setDirection(DcMotorSimple.Direction.REVERSE);





        frontLeft.      setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.     setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.       setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.      setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake.         setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transfer.       setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter.        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/



        /*
            We are using Actuonix PQ-12 linear actuators for the grippers as of Dec 12 2019. They
            take a signal between 1.0 milliseconds (extend; 1000 microseconds) and 2.0 milliseconds
            (retract; 2000 microseconds) according to Actuonix's datasheet. REV Smart Robot Servos,
            for comparison, take a signal between 500 microseconds (-90 degrees) and 2500
            microseconds (90 degrees), so a 2500 microseconds signal is sent when setPosition(1.0)
            is called. To correct the range for the linear actuator servos, We are setting them to
            0.4 (1000 / 2500) and 0.8 (2000 / 2500).
         */
//        gripper.scaleRange(0.2, 0.8);




        if(initCamera) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam1"), cameraMonitorViewId);
            webcam.openCameraDevice();
            vision = new RingPatternPipeline();
            webcam.setPipeline(vision);
            webcam.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_RIGHT);
        }


    }


    // Movement methods
    public void setLeftPower(double power) {
        frontLeft.setPower(power);
        rearLeft.setPower(power);
    }

    public void setRightPower(double power) {
        frontRight.setPower(power);
        rearRight.setPower(power);
    }


    public void setMecanumPower(double drive, double strafe, double twist) {
        setMecanumPower(drive,strafe,twist,1);
    }

    public void setMecanumPower(double drive, double strafe, double twist, double speedMod) {
        frontLeft.setPower( (drive + strafe + twist) * speedMod);
        frontRight.setPower((drive - strafe - twist) * speedMod);
        rearLeft.setPower(  (drive - strafe + twist) * speedMod);
        rearRight.setPower( (drive + strafe - twist) * speedMod);
    }

    public void mecanumDriveFieldCentric(double x, double y, double twist, double heading) {
        // Account for the gyro heading in the drive vector
        double[] rotated = rotateVector(x, y, -1 * heading);    // Heading is negated to correct vector rotation

        // We are making seperate variables here for clarity's sake
        double xIn = rotated[0];
        double yIn = rotated[1];

        // Put motor powers in an array to be normalized to largest value
        double[] motorSpeeds = {
                yIn + xIn + twist,  // Front left
                yIn - xIn - twist,  // Front right
                yIn - xIn + twist,  // Rear left
                yIn + xIn - twist   // Rear right
        };

        // Normalize so largest speed is 1.0
        normalize(motorSpeeds);


        // Set motor powers
        frontLeft  .setPower(motorSpeeds[0]);
        frontRight .setPower(motorSpeeds[1]);
        rearLeft   .setPower(motorSpeeds[2]);
        rearRight  .setPower(motorSpeeds[3]);
    }
    protected static double[] rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (Math.PI / 180.0));
        double sinA = Math.sin(angle * (Math.PI / 180.0));
        double[] out = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        return out;
    }
    protected static void normalize(double[] wheelSpeeds) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        for (int i = 1; i < wheelSpeeds.length; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) {
                maxMagnitude = temp;
            }
        }
        if (maxMagnitude > 1.0) {
            for (int i = 0; i < wheelSpeeds.length; i++) {
                wheelSpeeds[i] /= maxMagnitude;
            }
        }
    }

    public void setDriveCounts(int counts) {
        frontLeft.setTargetPosition    (frontLeft.getCurrentPosition()    + counts);
        frontRight.setTargetPosition   (frontRight.getCurrentPosition()   + counts);
        rearLeft.setTargetPosition     (rearLeft.getCurrentPosition()     + counts);
        rearRight.setTargetPosition    (rearRight.getCurrentPosition()    + counts);
    }

    public void setStrafeCounts(int counts) {
        frontLeft.setTargetPosition    (frontLeft.getCurrentPosition()    + counts);
        frontRight.setTargetPosition   (frontRight.getCurrentPosition()   - counts);
        rearLeft.setTargetPosition     (rearLeft.getCurrentPosition()     - counts);
        rearRight.setTargetPosition    (rearRight.getCurrentPosition()    + counts);
    }


    // Gyroscope methods
    public double normalize180(double angle) {
        while(angle > 180) {
            angle -= 360;
        }
        while(angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    public float heading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }






    // OVERRIDE METHODS
    public void stopAllMotors() {
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        rearLeft.setPower(0.0);
        rearRight.setPower(0.0);


    }


}

package org.firstinspires.ftc.teamcode.subsystems;


import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Bot {

    public OpMode opMode;
    public static Bot instance;

    public Bucket bucket;

    public Slides slides;
    public Claw claw;

    public Wrist wrist;
    public Linkage linkage;

    private final DcMotorEx FL, FR, BL, BR;

    public boolean fieldCentricRunMode = false;


    public static Bot getInstance(OpMode opMode) {
        if (instance == null) {
            return instance = new Bot(opMode);
        }
        instance.opMode = opMode;
        return instance;
    }
    private void enableAutoBulkRead() {
        for (LynxModule mod : opMode.hardwareMap.getAll(LynxModule.class)) {
            mod.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }


    public Bot(OpMode opMode) {
        this.opMode = opMode;
        enableAutoBulkRead();
        try {
            fieldCentricRunMode = false;
        } catch (Exception e) {
            fieldCentricRunMode = false;

        }

        FL = opMode.hardwareMap.get(DcMotorEx.class, "fl");
        FR = opMode.hardwareMap.get(DcMotorEx.class, "fr");
        BL = opMode.hardwareMap.get(DcMotorEx.class, "bl");
        BR = opMode.hardwareMap.get(DcMotorEx.class, "br");

        prepMotors();


        this.slides = new Slides(opMode);
        this.claw= new Claw(opMode);
        this.bucket = new Bucket(opMode);
        this.linkage = new Linkage(opMode);
        this.wrist= new Wrist(opMode);
    }


    public void prepMotors(){
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        FL.setMode(RUN_USING_ENCODER);
        FR.setMode(RUN_USING_ENCODER);
        BL.setMode(RUN_USING_ENCODER);
        BR.setMode(RUN_USING_ENCODER);
        //note: need to plug in encoders for this to work

    }

    public void driveRobotCentric(double strafeSpeed, double forwardBackSpeed, double turnSpeed) {
        double[] speeds = {
                forwardBackSpeed - strafeSpeed - turnSpeed,
                forwardBackSpeed + strafeSpeed + turnSpeed,
                forwardBackSpeed + strafeSpeed - turnSpeed,
                forwardBackSpeed - strafeSpeed + turnSpeed
        };
        double maxSpeed = 0;
        for (int i = 0; i < 4; i++) {
            maxSpeed = Math.max(maxSpeed, speeds[i]);
        }
        if (maxSpeed > 1) {
            for (int i = 0; i < 4; i++) {
                speeds[i] /= maxSpeed;
            }
        }
        FL.setPower(speeds[0]);
        FR.setPower(speeds[1]);
        BL.setPower(speeds[2]);
        BR.setPower(speeds[3]);
    }

    public void resetEverything(){
        resetEncoder();
      //  prepMotors();
//        slides.runToStorage();
//        slides.resetEncoder();
//        slides.resetProfiler();
      //  wrist.reset();
        claw.open();
        bucket.stopIntake();
        bucket.flipIn();
        linkage.retract();

    }

    private void resetEncoder() {
        FL.setMode(STOP_AND_RESET_ENCODER);
        FR.setMode(STOP_AND_RESET_ENCODER);
        BR.setMode(STOP_AND_RESET_ENCODER);
        BL.setMode(STOP_AND_RESET_ENCODER);
    }



  /*  private void transfer_and_outtake(boolean isDiffyTransfer, double claw_pos) {

        // sync claw pos to kinematics class, so that it knows what length to extend slides
        if(claw.getPos() != 1) {
            claw.open();
        }
        if(isDiffyTransfer){
            wrist.transferPos();
        }
        claw.close();
        wrist.rotate(0.25); // I KNOW THIS LINE IS PROB WRONG
    }

   */

}

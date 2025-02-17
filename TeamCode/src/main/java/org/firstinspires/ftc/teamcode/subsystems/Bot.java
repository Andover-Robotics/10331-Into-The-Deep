package org.firstinspires.ftc.teamcode.subsystems;


import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Bot {
    public OpMode opMode;
    public static Bot instance;
    //Subsystems
   // public Bucket bucket;
    public IntakeArm arm;
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
       // this.bucket = new Bucket(opMode);
        this.arm = new IntakeArm(opMode);
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
//        bucket.stopIntake();
//        bucket.flipIn();
        linkage.retract();

    }

    private void resetEncoder() {
        FL.setMode(STOP_AND_RESET_ENCODER);
        FR.setMode(STOP_AND_RESET_ENCODER);
        BR.setMode(STOP_AND_RESET_ENCODER);
        BL.setMode(STOP_AND_RESET_ENCODER);
    }

    //ACTIONS
    public class slidesPeriodic implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            slides.periodic();
            return true;
        }
    }
    public Action slidesPeriodic() {
        return new slidesPeriodic();
    }

    //Bucket Outtake (Auto): move wrist, slides up, claw open; reset all back to storage. specify 1 for low, 2 for high
    public SequentialAction bucketOuttakeAction(int level) {
        switch(level) {
            case 1:
                return new SequentialAction(
                       // new InstantAction(() -> wrist.bucketOuttakePos()),
                        new SleepAction(0.1),
                        new InstantAction(() -> slides.runToLowBucket()),
                        new InstantAction(() -> claw.open()),
                        new SleepAction(0.5),
                        //reset position:
                        new InstantAction(() -> claw.close()),
                        new SleepAction(0.1),
                        new InstantAction(() -> wrist.storage()),
                        new SleepAction(0.1),
                        new InstantAction(() -> slides.runToStorage()),
                        new SleepAction(0.5)
                );
            case 2:
                return new SequentialAction(
                       // new InstantAction(() -> wrist.bucketOuttakePos()),
                        new SleepAction(0.1),
                        new InstantAction(() -> slides.runToTopBucket()),
                        new InstantAction(() -> claw.open()),
                        new SleepAction(0.1),
                        //reset position:
                        new InstantAction(() -> claw.close()),
                        new SleepAction(0.1),
                        new InstantAction(() -> wrist.storage()),
                        new SleepAction(0.1),
                        new InstantAction(() -> slides.runToStorage()),
                        new SleepAction(0.5)
                );
            default:
                return null;
        }
    }
}

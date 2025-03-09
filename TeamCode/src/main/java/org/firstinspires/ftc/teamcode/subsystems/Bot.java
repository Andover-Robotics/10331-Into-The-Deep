package org.firstinspires.ftc.teamcode.subsystems;


import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    private final MotorEx FL, FR, BL, BR;
    public boolean fieldCentricRunMode = false;

    public enum BotState {
        INTAKE,
        TRANSFER,
        OUTTAKE
    }
    public BotState state;

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

        FL = new MotorEx(opMode.hardwareMap, "fl", Motor.GoBILDA.RPM_435);
        FR = new MotorEx(opMode.hardwareMap, "fr", Motor.GoBILDA.RPM_435);
        BL = new MotorEx(opMode.hardwareMap, "bl", Motor.GoBILDA.RPM_435);
        BR = new MotorEx(opMode.hardwareMap, "br", Motor.GoBILDA.RPM_435);

        prepMotors();

        this.slides = new Slides(opMode);
        this.claw= new Claw(opMode);
        this.arm = new IntakeArm(opMode);
        this.linkage = new Linkage(opMode);
        this.wrist= new Wrist(opMode);
    }

    public void prepMotors(){
        FR.setInverted(true);
        BR.setInverted(true);
        FL.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        FL.setRunMode(Motor.RunMode.RawPower);
        FR.setRunMode(Motor.RunMode.RawPower);
        BL.setRunMode(Motor.RunMode.RawPower);
        BR.setRunMode(Motor.RunMode.RawPower);
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
        FL.set(speeds[0]);
        FR.set(speeds[1]);
        BL.set(speeds[2]);
        BR.set(speeds[3]);
    }

    public void resetEverything(){
        claw.close();
        wrist.reset();
        arm.reset();
        linkage.retract();
        slides.resetEncoder();
        slides.resetProfiler();
       // slides.runToStorage(); - uncomment when slides storage pos works
    }

//    private void resetEncoder() {
//        FL.setMode(STOP_AND_RESET_ENCODER);
//        FR.setMode(STOP_AND_RESET_ENCODER);
//        BR.setMode(STOP_AND_RESET_ENCODER);
//        BL.setMode(STOP_AND_RESET_ENCODER);
//    }

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

    //TELEOP ACTIONS:
    public SequentialAction clawToTransfer() {
        return new SequentialAction(
                new SleepAction(0.1),
                new InstantAction(() -> wrist.intermediate()),
                new SleepAction(0.2),
                new InstantAction(() -> claw.open()),
                new SleepAction(0.2),
                new InstantAction(() -> wrist.transfer()),
                new SleepAction(0.2),
                new InstantAction(() -> claw.close()),
                new SleepAction(0.2)
        );
    }

    public SequentialAction bucketOuttake(int lvl) {
        switch(lvl) {
            case 1:
                return new SequentialAction(
                        new SleepAction(0.1),
                        new InstantAction(() -> wrist.bucketOuttake()),
                        new SleepAction(0.1),
                        new InstantAction(() -> slides.runToLowBucket()),
                        new SleepAction(0.1),
                        new InstantAction(() -> claw.open())
                );
            case 2:
                return new SequentialAction(
                        new SleepAction(0.1),
                        new InstantAction(() -> wrist.bucketOuttake()),
                        new SleepAction(0.1),
                        new InstantAction(() -> slides.runToTopBucket()),
                        new SleepAction(0.1),
                        new InstantAction(() -> claw.open())
                );
            default:
                return null;
        }
    }

    public SequentialAction specOuttake() {
        return new SequentialAction(
                new SleepAction(0.1),
                new InstantAction(() -> wrist.specOuttake()),
                new SleepAction(0.1),
                new InstantAction(() -> slides.runToTopRung())
        );
        //let the driver choose the positioning and then bring down the slides to storage position for slides clipping
    }

    public SequentialAction armToTransfer() {
        return new SequentialAction(
                new InstantAction(() -> arm.closeClaw()),
                new SleepAction(0.1),
                new InstantAction(() -> arm.transferPos()),
                new SleepAction(0.1),
                new InstantAction(() -> linkage.retract())
        );
    }

    public SequentialAction intakeAction() {
        return new SequentialAction(
                new InstantAction(() -> linkage.extend()),
                new SleepAction(0.1),
                new InstantAction(()-> arm.intakePos())
        );
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

    public ParallelAction goBack(){
        return new ParallelAction(
                new SleepAction(0.3),
                new InstantAction(() -> FL.set(-0.1)),
                new InstantAction(() -> BL.set(-0.1)),
                new InstantAction(() -> FR.set(-0.1)),
                new InstantAction(() -> BR.set(-0.1))
        );
    }

    public void goBack(ElapsedTime time){
        while(time.seconds()<0.01){
            FL.set(0.1);
            BL.set(0.1);
            FR.set(0.1);
            BR.set(0.1);
        }
    }
    public void stopRobot(){
            FL.set(0);
            BL.set(0);
            FR.set(0);
            BR.set(0);
    }

    public void distanceSensorGoBack(){



    }



}

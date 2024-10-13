package org.firstinspires.ftc.teamcode.subsystems;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvWebcam;


public class Bot {

    public OpMode opMode;
    public static Bot instance;
    public Slides slides;
    public DiffyClaw diffyClaw;
    public Bucket bucket;
    public Linkage linkage;
    public OpenCvWebcam camera;
    private ElapsedTime time= new ElapsedTime();;

    private final DcMotorEx FL, FR, BL, BR;

    public boolean fieldCentricRunMode = false;

    public static Bot getInstance(OpMode opMode) {
        if (instance == null) {
            return instance = new Bot(opMode);
        }
        instance.opMode = opMode;
        return instance;
    }

    public Bot(OpMode opMode) {
        this.opMode = opMode;
        enableAutoBulkRead();
        try {
            fieldCentricRunMode = false;
        } catch (Exception e) {
            fieldCentricRunMode = false;

        }

        WebcamName camName = opMode.hardwareMap.get(WebcamName.class, "webcam");
        camera = OpenCvCameraFactory.getInstance().createWebcam(camName);
        FL = opMode.hardwareMap.get(DcMotorEx.class, "fl");
        FR = opMode.hardwareMap.get(DcMotorEx.class, "fr");
        BL = opMode.hardwareMap.get(DcMotorEx.class, "bl");
        BR = opMode.hardwareMap.get(DcMotorEx.class, "br");

        prepMotors();

        this.slides = new Slides(opMode);
        this.diffyClaw = new DiffyClaw(opMode);
        this.bucket = new Bucket(opMode);
        this.linkage = new Linkage(opMode);
    }

    public void prepMotors(){
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

    }

    private void enableAutoBulkRead() {
        for (LynxModule mod : opMode.hardwareMap.getAll(LynxModule.class)) {
            mod.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }


    //Any motion that needs to be incorporated into trajectory -> should be method returning Action
    //incorporate any action with camera here (in Action method)

    public Action intakeSense(boolean allianceBlue) {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                linkage.extend();
                bucket.flipOut();
                bucket.intakeSense(allianceBlue);
                bucket.stopIntake();
                bucket.flipIn();
                linkage.retract();
                diffyClaw.transferPos();
                return false;
            }
        };
    }

    public Action intakeNoSense() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                time.reset();
                time.startTime();
                linkage.extend();
                bucket.flipOut();
                while(time.seconds()<3){
                    bucket.intakeNoSense();
                }
                bucket.stopIntake();
                bucket.flipIn();
                linkage.retract();
                diffyClaw.transferPos();
                return false;
            }
        };
    }

    public Action slidesTopBucket() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                slides.runToTopBucket();
                return false;
            }
        };
    }

    public Action slidesTopRung() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                slides.runToTopRung();
                return false;
            }
        };
    }



}
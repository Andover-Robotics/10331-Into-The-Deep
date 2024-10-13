package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


public class Bucket {

    private final CRServo tubingServo1;
    private final CRServo tubingServo2;

    private final Servo flip;
    private final Servo flip2;
    private final Servo flip3;
    private final Servo flip4;
    private final ColorSensor colorSensor;
    public static final double storagePos=0;
    public static final double intakePos=1;
    public static  double currentPos=0;

    private final float[] hsvValues = {0, 0, 0};
    //values need to be tuned
    private final double SCALE_FACTOR = 255;

    public Bucket(OpMode opMode) {

        tubingServo1 = opMode.hardwareMap.crservo.get("tubing servo 1");
        tubingServo2 = opMode.hardwareMap.crservo.get("tubing servo 2");
        flip = opMode.hardwareMap.servo.get("flip servo");
        flip2 = opMode.hardwareMap.servo.get("flip servo 2");
        flip3 = opMode.hardwareMap.servo.get("flip servo 3");
        flip4 = opMode.hardwareMap.servo.get("flip servo 4");
        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "sensor_color_distance");
    }


    public boolean intakeSense(boolean allianceBlue){
        tubingServo1.setDirection(DcMotorSimple.Direction.FORWARD);
        tubingServo2.setDirection(DcMotorSimple.Direction.FORWARD);
        prepColorSensor();

        while(hsvValues[0]<5 && hsvValues[1] <5 && hsvValues[2]<5){
            tubingServo1.setPower(1);
            tubingServo2.setPower(1);
            prepColorSensor();
        }

        if((allianceBlue && colorSensor.red()>100) || (!allianceBlue && colorSensor.blue()>100)){
            reverseIntake();
            intakeSense(allianceBlue);
        }

        return true;
    }

    public void intakeNoSense(){
        tubingServo1.setDirection(DcMotorSimple.Direction.FORWARD);
        tubingServo2.setDirection(DcMotorSimple.Direction.FORWARD);
        tubingServo1.setPower(1);
        tubingServo2.setPower(1);
    }

    public void reverseIntake(){
        tubingServo1.setDirection(DcMotorSimple.Direction.REVERSE);
        tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        tubingServo1.setPower(1);
        tubingServo2.setPower(1);
    }

    public void stopIntake(){
        tubingServo1.setPower(0);
        tubingServo2.setPower(0);
    }

    public void flipIn(){
        flip.setPosition(storagePos);
        flip2.setPosition(storagePos);
        flip3.setPosition(storagePos);
        flip4.setPosition(storagePos);
    }

    public void flipOut() {
        flip.setPosition(intakePos);
        flip2.setPosition(intakePos);
        flip3.setPosition(intakePos);
        flip4.setPosition(intakePos);
    }

    private void prepColorSensor() {
        Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
                (int) (colorSensor.green() * SCALE_FACTOR),
                (int) (colorSensor.blue() * SCALE_FACTOR),
                hsvValues);

        telemetry.addData("Red  ", colorSensor.red() *SCALE_FACTOR);
        telemetry.addData("Green", colorSensor.green()*SCALE_FACTOR);
        telemetry.addData("Blue ", colorSensor.blue()*SCALE_FACTOR);
    }
}
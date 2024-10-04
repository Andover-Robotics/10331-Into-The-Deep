package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Note that the REV Robotics Color-Distance incorporates two sensors into one device.
 * It has an IR proximity sensor which is used to calculate distance and an RGB color sensor.

 * For V1/V2, the light/distance sensor saturates at around 2" (5cm).  This means that targets that are 2"
 * or closer will display the same value for distance/light detected.

 * For V3, the distance sensor as configured can handle distances between 0.25" (~0.6cm) and 6" (~15cm).
 * Any target closer than 0.25" will display as 0.25" and any target farther than 6" will display as 6".

 */

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

        //when its black (no sample in bucket)
        while(hsvValues[0]<5 && hsvValues[1] <5 && hsvValues[2]<5){
            tubingServo1.setPower(1);
            tubingServo2.setPower(1);
            prepColorSensor();
        }
        //after sample in bucket:
        if((allianceBlue && colorSensor.red()>100) || (!allianceBlue && colorSensor.blue()>100)){
            reverseIntake();
            intakeSense(allianceBlue);
            //need to be careful, it is possible to enter infinite recursion
        }

        //atp: correct colored block is in intake -> return true
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
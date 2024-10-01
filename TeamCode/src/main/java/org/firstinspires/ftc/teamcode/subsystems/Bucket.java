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

    private final CRServo wheelServo1;
    private final CRServo wheelServo2;
    private final Servo flip;
    private final ColorSensor colorSensor;

    //needs to be tuned
    private final double storagePos=0;
    private final double intakePos=1;

    private final float[] hsvValues = {0, 0, 0};
    private final double SCALE_FACTOR = 255;

    public Bucket(OpMode opMode) {

        wheelServo1 = opMode.hardwareMap.crservo.get("wheel servo");
        wheelServo2 = opMode.hardwareMap.crservo.get("wheel servo");
        flip = opMode.hardwareMap.servo.get("flap servo");
        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "sensor_color_distance");
    }


    public void intakeSense(boolean allianceBlue){
        wheelServo1.setDirection(DcMotorSimple.Direction.FORWARD);
        wheelServo2.setDirection(DcMotorSimple.Direction.FORWARD);
        prepColorSensor();

        //when its black (no sample in bucket) -> values needs to be tuned
        while(hsvValues[0]<5 && hsvValues[1] <5 && hsvValues[2]<5){
            wheelServo1.setPower(1);
            wheelServo2.setPower(1);
            prepColorSensor();
        }
        if((allianceBlue && colorSensor.red()>100) || (!allianceBlue && colorSensor.blue()>100)){
            reverseIntake();
            intakeSense(allianceBlue);
        }
    }

    public void intakeNoSense(boolean allianceBlue){
        wheelServo1.setDirection(DcMotorSimple.Direction.FORWARD);
        wheelServo2.setDirection(DcMotorSimple.Direction.FORWARD);
        wheelServo1.setPower(1);
        wheelServo2.setPower(1);
    }

    public void reverseIntake(){
        wheelServo1.setDirection(DcMotorSimple.Direction.REVERSE);
        wheelServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        wheelServo1.setPower(1);
        wheelServo2.setPower(1);
    }

    public void stopIntake(){
        wheelServo1.setPower(0);
        wheelServo2.setPower(0);
    }

    public void flipIn(){
        flip.setPosition(storagePos);
    }

    public void flipOut() {
        flip.setPosition(intakePos);
    }

    private void prepColorSensor(){

        Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
                (int) (colorSensor.green() * SCALE_FACTOR),
                (int) (colorSensor.blue() * SCALE_FACTOR),
                hsvValues);

        telemetry.addData("Red  ", colorSensor.red() *SCALE_FACTOR);
        telemetry.addData("Green", colorSensor.green()*SCALE_FACTOR);
        telemetry.addData("Blue ", colorSensor.blue()*SCALE_FACTOR);
    }

}
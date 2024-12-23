package org.firstinspires.ftc.teamcode.subsystems;



import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;



import java.util.Locale;

/**
 * Note that the REV Robotics Color-Distance incorporates two sensors into one device.
 * It has an IR proximity sensor which is used to calculate distance and an RGB color sensor.

 * For V1/V2, the light/distance sensor saturates at around 2" (5cm).  This means that targets that are 2"
 * or closer will display the same value for distance/light detected.

 * For V3, the distance sensor as configured can handle distances between 0.25" (~0.6cm) and 6" (~15cm).
 * Any target closer than 0.25" will display as 0.25" and any target farther than 6" will display as 6".

**/

public class Bucket {
    public final CRServo tubingServo2;
    public final Servo flip;
    double distance=10;
    public final ColorSensor colorSensor;
    public final DistanceSensor distanceSensor;
    public static final double transferPos = 1;
    //tune
    public static final double intakePos = 0.13;
    private final float[] hsvValues = {0, 0, 0};
    public String color="nothing";
    double hue=0, saturation=0, value=0;
    private ElapsedTime time;

    public Bucket(OpMode opMode) {
        tubingServo2 = opMode.hardwareMap.crservo.get("intake left");
        flip = opMode.hardwareMap.servo.get("flip right");
        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "color");
        distanceSensor = opMode.hardwareMap.get(DistanceSensor.class, "color");
        time = new ElapsedTime();
    }

    public void intakeSense(boolean allianceBlue){
        color="nothing";
        tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        distance= distanceSensor.getDistance(DistanceUnit.CM);
        telemetry.update();

        while(distance>4){
            tubingServo2.setPower(0.7);
            distance= distanceSensor.getDistance(DistanceUnit.CM);
        }
        while(distance>1.8){
            tubingServo2.setPower(0.2);
            distance= distanceSensor.getDistance(DistanceUnit.CM);
            runColorSensor();
        }
        stopIntake();
        telemetry.update();

        if((allianceBlue && color.equals("red")) || !allianceBlue && color.equals("blue")){
            runColorSensor();
            reverseIntake();
            intakeSense(allianceBlue);
        }
    }

    public void intakeNoSense(){
        time.reset();
        tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        tubingServo2.setPower(0.7);
    }
    public void intake(double power){
        time.reset();
        tubingServo2.setDirection(DcMotorSimple.Direction.FORWARD);
        tubingServo2.setPower(power);
    }

    public void reverseIntake(){
    //    time.reset();
        tubingServo2.setDirection(DcMotorSimple.Direction.FORWARD);
    //    while(time.seconds() < 2) {
        tubingServo2.setPower(0.7);
     //   }
    }

    public void stopIntake(){
        time.reset();
        tubingServo2.setPower(0);
    }

    public void flipIn(){
        flip.setPosition(transferPos);
       // flip2.setPosition(transferPos);

    }

    public void flipOut() {
        flip.setPosition(intakePos);
     //   flip2.setPosition(intakePos);

    }

    private void prepColorSensor() {
        Color.RGBToHSV((colorSensor.red()),
                (colorSensor.green()),
                (colorSensor.blue()),
                hsvValues);

        hue= hsvValues[0];
        saturation= hsvValues[1] * 255;
        value= hsvValues[2] * 255;
    }

    private void runColorSensor() {
        prepColorSensor();
        if((hue>20 && hue<70) && (saturation>170 && saturation<240)){
            color="yellow";
        }
        else if((hue>15 && hue<50) && (saturation>100 && saturation<180)){
            color= "red";
        }
        else if((hue>170 && hue<260) && (saturation>70 && saturation<256)){
            color = "blue";
        }
        else{
            color= "nothing";
        }
    }

    public void moveFlipRight(double pos){
        flip.setPosition(pos);
    }

    public void reset() {
        moveFlipRight(intakePos);
    }
}




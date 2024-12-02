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

**/

public class Bucket {

      public final CRServo tubingServo1;
      public final CRServo tubingServo2;

     public final Servo flip;
    //public final Servo flip2;
    public final ColorSensor colorSensor;
    public static final double transferPos = 1;

    //tune
    public static final double intakePos = 0.09;

    private final float[] hsvValues = {0, 0, 0};
    String color="nothing";
    double hue=0, saturation=0, value=0;

    public Bucket(OpMode opMode) {

        tubingServo1 = opMode.hardwareMap.crservo.get("intake right");
        tubingServo1.setDirection(DcMotorSimple.Direction.FORWARD);
        tubingServo2 = opMode.hardwareMap.crservo.get("intake left");
        flip = opMode.hardwareMap.servo.get("flip right");
        tubingServo1.setDirection(DcMotorSimple.Direction.REVERSE);
        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "color");
    }

    public void intakeSense(boolean allianceBlue){
        tubingServo1.setDirection(DcMotorSimple.Direction.REVERSE);
        tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        runColorSensor();

        while(color.equals("nothing")){
            tubingServo1.setPower(1);
            tubingServo2.setPower(1);
            runColorSensor();
        }

        if((allianceBlue && color.equals("red")) || !allianceBlue && color.equals("blue")){
            reverseIntake();
            intakeSense(allianceBlue);
        }
        stopIntake();
    }


    public void intakeNoSense(){
        tubingServo1.setDirection(DcMotorSimple.Direction.FORWARD);
        tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        tubingServo1.setPower(1);
        tubingServo2.setPower(1);
    }

    public void reverseIntake(){
        tubingServo1.setDirection(DcMotorSimple.Direction.REVERSE);
        tubingServo2.setDirection(DcMotorSimple.Direction.FORWARD);
        tubingServo1.setPower(1);
        tubingServo2.setPower(1);
    }

    public void stopIntake(){
        tubingServo1.setPower(0);
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
        if((hue>30 && hue<80) && (saturation>230 && saturation<270)){
            color="red";
        }
        else if((hue>20 && hue<50) && (saturation>140 && saturation<190)){
            color= "yellow";
        }
        else if((hue>150 && hue<200) && (saturation>230 && saturation<270)){
            color = "blue";
        }
        else{
            color= "nothing";
        }

       /*
        if((hue>-1 && hue<70) && (saturation>100 && saturation<260)){
            color="red";
        }
        else if((hue>50 && hue<90) && (saturation>140 && saturation<220)){
            color= "yellow";
        }
        else if((hue>100 && hue<220) && (saturation>60 && saturation<180)){
            color = "blue";
        }
        else{
            color= "nothing";
        }

       */
    }



    public void moveFlipRight(double pos){
        flip.setPosition(pos);
    }
    public void move(double pos){
        flip.setPosition(pos);
       // flip2.setPosition(pos);
    }


    public void runBackwards() {
        reverseIntake();
    }
    public void runForwards() {
        intakeNoSense();
    }

    public void reset() {
        move(intakePos);
    }
}




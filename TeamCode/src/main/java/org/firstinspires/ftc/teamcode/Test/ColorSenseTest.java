package org.firstinspires.ftc.teamcode.Test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

import java.util.Locale;


@TeleOp(name = "Color Sense Test", group = "Test")
public class ColorSenseTest extends LinearOpMode {

    private GamepadEx gp1;
    private final float[] hsvValues = {0, 0, 0};
    private String color="nothing";
    double hue=0, saturation=0, value=0;
    double distance=1;

    boolean allianceBlue=false;

    private ColorSensor colorSensor;

    private DistanceSensor distanceSensor;

    private CRServo tubingServo2;
    private CRServo tubingServo1;

    @Override
    public void runOpMode() throws InterruptedException {
        gp1 = new GamepadEx(gamepad1);
        gp1.readButtons();
        initColorSensor(this);

    /*    bot.bucket.stopIntake();
        bot.linkage.retract();
        bot.bucket.flipIn();

     */

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
          //  bot.bucket.flipOut();
            gp1.readButtons();

            if(gp1.wasJustPressed(GamepadKeys.Button.B)) {
                tubingServo1.setPower(0);
                tubingServo2.setPower(0);
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.A)) {
                tubingServo1.setPower(0.3);
                tubingServo2.setPower(0.3 );
            }

            runColorSensor();
     /*       if (gp1.wasJustPressed(GamepadKeys.Button.X)) {
                bot.bucket.reverseIntake();
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.A)){
                bot.bucket.flipOut();
                bot.linkage.extend();
                intakeSense(allianceBlue);
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.Y)){
                bot.bucket.stopIntake();
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)){
                allianceBlue=true;
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.DPAD_UP)){
                allianceBlue=false;
            }

      */

            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", distanceSensor.getDistance(DistanceUnit.CM)));
            telemetry.addData("Hue  ", hue);
            telemetry.addData("Saturation", saturation);
            telemetry.addData("Value ", value);
            telemetry.addData("color", color);
            telemetry.addData("alliance blue ", allianceBlue);
            telemetry.update();

   /*         if((hue>-1 && hue<70) && (saturation>100 && saturation<260)){
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
    }



/*    public void intakeSense(boolean allianceBlue){
        color="nothing";
        bot.bucket.tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
        telemetry.update();

        while(distance>4){
            bot.bucket.tubingServo2.setPower(0.7);
            distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
        }
        while(distance>1.8){
            bot.bucket.tubingServo2.setPower(0.2);
            distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
            runColorSensor();
        }
        bot.bucket.stopIntake();
        telemetry.update();

        if((allianceBlue && color.equals("red")) || !allianceBlue && color.equals("blue")){
            runColorSensor();
            bot.bucket.reverseIntake();
            intakeSense(allianceBlue);
        }
        bot.bucket.stopIntake();
        bot.linkage.retract();
        bot.bucket.flipIn();
    }

 */
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
        if((hue>40 && hue<70) && (saturation>170 && saturation<240)){
            color="yellow";
        }
        else if((hue>15 && hue<40) && (saturation>180 && saturation<220)){
            color= "red";
            //30 and 204
            //
        }
        else if((hue>110 && hue<220) && (saturation>70 && saturation<256)){
            color = "blue";
        }
        else{
            color= "nothing";
        }

        telemetry.addData("Distance (cm)",
                String.format(Locale.US, "%.02f", distanceSensor.getDistance(DistanceUnit.CM)));
        telemetry.addData("Hue  ", hue);
        telemetry.addData("Saturation", saturation);
        telemetry.addData("Value ", value);
        telemetry.addData("color", color);
        telemetry.addData("alliance blue ", allianceBlue);
        telemetry.update();
    }
    private void initColorSensor(OpMode opMode) {
        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "color");
        distanceSensor = opMode.hardwareMap.get(DistanceSensor.class, "color");
        tubingServo2 = opMode.hardwareMap.crservo.get("intake left");
        tubingServo1 = opMode.hardwareMap.crservo.get("intake right");
        tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

}
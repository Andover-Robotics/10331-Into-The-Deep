package org.firstinspires.ftc.teamcode.Test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

import java.util.Locale;


@TeleOp(name = "Color Sense Test", group = "Test")
public class ColorSenseTest extends LinearOpMode {

    Bot bot;

    private GamepadEx gp2;
    private final float[] hsvValues = {0, 0, 0};
    String color="nothing";
    double hue=0, saturation=0, value=0;
    double distance=10;

    boolean isIntaking=false;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad1);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            bot.bucket.flipOut();
            gp2.readButtons();

            runColorSensor();
            if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                isIntaking=false;
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.B)) {
                isIntaking=true;
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.X)) {
                bot.bucket.reverseIntake();
            }
            if(!isIntaking){
             //  intakeSense(true);
            }

            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", bot.bucket.distanceSensor.getDistance(DistanceUnit.CM)));
            telemetry.addData("Hue  ", hue);
            telemetry.addData("Saturation", saturation);
            telemetry.addData("Value ", value);
            telemetry.addData("color", color);
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

    public void intakeSense(boolean allianceBlue){
        color="nothing";
        bot.bucket.tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
        telemetry.update();

        while(distance>1.5){
            bot.bucket.tubingServo2.setPower(0.7);
            distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
            runColorSensor();
        }
        bot.bucket.stopIntake();
        runColorSensor();

      /*  if((allianceBlue && color.equals("red")) || !allianceBlue && color.equals("blue")){
            bot.bucket.reverseIntake();
            intakeSense(allianceBlue);
        }

       */
    }
    private void prepColorSensor() {
        Color.RGBToHSV((bot.bucket.colorSensor.red()),
                (bot.bucket.colorSensor.green()),
                (bot.bucket.colorSensor.blue()),
                hsvValues);

        hue= hsvValues[0];
        saturation= hsvValues[1] * 255;
        value= hsvValues[2] * 255;
    }

    private void runColorSensor() {
        prepColorSensor();
        if((hue>35 && hue<70) && (saturation>170 && saturation<210)){
            color="yellow";
        }
        else if((hue>15 && hue<30) && (saturation>100 && saturation<170)){
            color= "red";
        }
        else if((hue>150 && hue<200) && (saturation>30 && saturation<80)){
            color = "blue";
        }
        else{
            color= "nothing";
        }

        telemetry.addData("Distance (cm)",
                String.format(Locale.US, "%.02f", bot.bucket.distanceSensor.getDistance(DistanceUnit.CM)));
        telemetry.addData("Hue  ", hue);
        telemetry.addData("Saturation", saturation);
        telemetry.addData("Value ", value);
        telemetry.addData("color", color);
        telemetry.update();
    }

}
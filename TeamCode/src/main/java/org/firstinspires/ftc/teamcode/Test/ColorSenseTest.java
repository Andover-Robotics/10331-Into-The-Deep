package org.firstinspires.ftc.teamcode.Test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Bot;


@TeleOp(name = "Color Sense Test", group = "Test")
public class ColorSenseTest extends LinearOpMode {

    Bot bot;

    private GamepadEx gp1;
    private final float[] hsvValues = {0, 0, 0};
    String color="nothing";
    double hue=0, saturation=0, value=0;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);

        waitForStart();
        gp1.readButtons();

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("Current Color:");
            testColorSensor();

             /* hsvValues[0] => hue
                hsvValues[1] => saturation
                hsvValues[2] => value
                rgb values from image:
                blue: middle: 32,56,190; highlights:86, 142,238, shadows: 3,5,79
                red: middle: 218,47,43; highlights:  219, 88, 87, shadows: 112,1,4
                yellow: middle: 243,175,16; highlights: 241, 193, 76; shadows: 221,107,0
                hsv:
                blue: highlights: 218, 65.9%, 93.3%; middle: 231, 83.2, 74.5; shadows: 238, 96.2, 31
                red: highlights: 0, 60.3, 85.9; middle: 1, 80.3, 85.5; shadows: 358, 99.1, 43.9
                yellow: highlights: 43, 68.5, 94.5; middle: 42, 93.4, 95.3; shadows: 29, 100, 86.7
              */


            if((hue>10 && hue<80) && (saturation>100 && saturation<160)){
                color="red";
            }
            else if((hue>30 && hue<70) && (saturation>140 && saturation<200)){
                color= "yellow";
            }
            else if((hue>80 && hue<130) && (saturation>80 && saturation<130)){
                color = "blue";
            }
            else{
                color= "nothing";
            }

            telemetry.addData("color", color);
            telemetry.update();

        }
    }

    private void testColorSensor() {
        Color.RGBToHSV((bot.bucket.colorSensor.red()),
                (bot.bucket.colorSensor.green()),
                (bot.bucket.colorSensor.blue()),
                hsvValues);

        hue= hsvValues[0];
        saturation= hsvValues[1] * 255;
        value= hsvValues[2] * 255;

        telemetry.addData("Hue  ", hue);
        telemetry.addData("Saturation", saturation);
        telemetry.addData("Value ", value);

    }
}
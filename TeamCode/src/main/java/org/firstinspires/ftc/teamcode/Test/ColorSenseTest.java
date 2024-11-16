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
    private double SCALE_FACTOR = 255;

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


             /*   hsvValues[0] => hue
                hsvValues[1] => saturation
                hsvValues[2] => value

              */

            if(hue<180 && hue>89 && saturation<188 && saturation>64 && value>73 && value<255){
                color="red";
            }
            else if(hue>18 && hue<85 && saturation<224 && saturation>77 && value<255 && value>76){
                color = "yellow";
            }
            else if(hue>45 && hue<180 && saturation>0 && saturation<164 && value<200 && value>57){
                color= "blue";
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
        saturation= hsvValues[1] * SCALE_FACTOR;
        value= hsvValues[2] * SCALE_FACTOR;

        telemetry.addData("Hue  ", hue);
        telemetry.addData("Saturation", saturation);
        telemetry.addData("Value ", value);

    }
}
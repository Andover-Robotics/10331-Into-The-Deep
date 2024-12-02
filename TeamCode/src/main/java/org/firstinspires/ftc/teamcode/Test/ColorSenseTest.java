package org.firstinspires.ftc.teamcode.Test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.subsystems.Bot;


@TeleOp(name = "Color Sense Test", group = "Test")
public class ColorSenseTest extends LinearOpMode {

    Bot bot;

    private GamepadEx gp2;
    private final float[] hsvValues = {0, 0, 0};
    String color="nothing";
    double hue=0, saturation=0, value=0;

    boolean isIntaking=false;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad1);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            bot.bucket.flipOut();
            telemetry.addLine("Current Color:");
            gp2.readButtons();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)){
                if (isIntaking) {
                    bot.bucket.stopIntake();
                    isIntaking=false;
                }
                else{
                    intakeSense(true);
                }
            }


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
        telemetry.update();
        bot.bucket.tubingServo1.setDirection(DcMotorSimple.Direction.REVERSE);
        bot.bucket.tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        runColorSensor();

        while(color.equals("nothing")){
            bot.bucket.tubingServo1.setPower(1);
            bot.bucket.tubingServo2.setPower(1);
            runColorSensor();
            telemetry.update();
        }

        if((allianceBlue && color.equals("red")) || !allianceBlue && color.equals("blue")){
            bot.bucket.reverseIntake();
            intakeSense(allianceBlue);
        }
        bot.bucket.stopIntake();
    }

    private void prepColorSensor() {
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
        telemetry.addData("color", color);
        telemetry.update();

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
        telemetry.addData("color", color);
        telemetry.update();
    }

}
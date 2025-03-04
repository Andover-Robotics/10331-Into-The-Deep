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

    Bot bot;
    private GamepadEx gp1;
    private final float[] hsvValues = {0, 0, 0};
    private String color="nothing";
    double hue=0, saturation=0, value=0;
    boolean allianceBlue=false;
    double distance =3.3;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;


    @Override
    public void runOpMode() throws InterruptedException {
        gp1 = new GamepadEx(gamepad1);
        gp1.readButtons();
        initColorSensor(this);
        bot = Bot.getInstance(this);
        bot.arm.intakePos();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            gp1.readButtons();

            if(gp1.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)){
                allianceBlue=true;
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.DPAD_UP)){
                allianceBlue=false;
            }

            // runColorSensor();
            intakeSense(allianceBlue);
        }
    }

    public void intakeSense(boolean allianceBlue){
        color="nothing";
        distance= distanceSensor.getDistance(DistanceUnit.CM);
        telemetry.update();

        while(distance>4){
         distance= distanceSensor.getDistance(DistanceUnit.CM);
        }
        while(distance<3.3){
          distance= distanceSensor.getDistance(DistanceUnit.CM);
          runColorSensor();
            telemetry.update();

            if((!allianceBlue && color.equals("red")) || allianceBlue && color.equals("blue")){
                //close claw
                bot.arm.closeClaw();
                return;
            }
        }



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
        if((hue>85 && hue<100) && (saturation>135 && saturation<160)){
            color="yellow";
        }
        else if((hue>80 && hue<97) && (saturation>110 && saturation<135)){
            color= "red";
        }
        else if((hue>100 && hue<220) && (saturation>70 && saturation<256)){
            color = "blue";
        }
        else{
            color= "nothing";
        }

        telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", distanceSensor.getDistance(DistanceUnit.CM)));
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
    }

}


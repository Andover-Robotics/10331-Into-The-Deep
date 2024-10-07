package org.firstinspires.ftc.teamcode.Test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;



import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class ColorSenseTest extends LinearOpMode {

    OpMode opmode;
    private GamepadEx gp1;
    private final float[] hsvValues = {0, 0, 0};
    private double SCALE_FACTOR = 255;
    private final ColorSensor colorSensor= opmode.hardwareMap.get(ColorSensor.class, "sensor_color_distance");


    @Override
    public void runOpMode() throws InterruptedException {
        gp1 = new GamepadEx(gamepad1);

        waitForStart();
        gp1.readButtons();

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("Current Position");
            testColorSensor();
            if(gp1.wasJustPressed(GamepadKeys.Button.A)){

                /*hsv[0] => hue
                hsv[1] => saturation
                hasv[2] => value

                 */

                if(hsvValues[0]<30 && hsvValues[1]>100 && hsvValues[2]>100){
                    telemetry.addLine("red");
                }
                if(hsvValues[0]<80 && hsvValues[0]>40 && hsvValues[1]>80 && hsvValues[2]>80){
                    telemetry.addLine("yellow");
                }
                if(hsvValues[0]>200 && hsvValues[1]<150 && hsvValues[2]<150){
                    telemetry.addLine("blue");
                }
            }
            telemetry.update();

        }
    }

    private void testColorSensor() {
        Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
                (int) (colorSensor.green() * SCALE_FACTOR),
                (int) (colorSensor.blue() * SCALE_FACTOR),
                hsvValues);

        telemetry.addData("Hue  ", hsvValues[0]);
        telemetry.addData("Saturation", hsvValues[1]);
        telemetry.addData("Value ", hsvValues[2]);
    }
}

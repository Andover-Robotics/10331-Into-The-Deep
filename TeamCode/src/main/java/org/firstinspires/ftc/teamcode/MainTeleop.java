package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Bot;
import org.firstinspires.ftc.teamcode.subsystems.Slides;
import java.lang.Math;



@TeleOp
public class MainTeleop extends LinearOpMode {
    Bot bot;
    private GamepadEx gp1;
    private GamepadEx gp2;
    private double driveSpeed = 1;

    private boolean isManual = false;
    private boolean isLinkageRetracted=true;
    private boolean isIntaking=true;
    private boolean isBucketFlipped=true;
    private boolean isDiffyTransferPos = true;
    private boolean isClawOpen= true;
    private boolean isAllianceBlue=false;
    private final float[] hsvValues = {0, 0, 0};
    String color="nothing";
    double hue=0, saturation=0, value=0;
    double distance=10;


    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);
        gp2 = new GamepadEx(gamepad2);

        bot.resetEverything();
        waitForStart();

        gp1.readButtons();
        if (gp1.wasJustPressed(GamepadKeys.Button.A)) {
            isAllianceBlue=true;
        }

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("TeleOp has started");
            gp1.readButtons();
            gp2.readButtons();

            drive();

            bot.slides.periodic();

            bot.slides.runSlides(gp2.getRightY());

            if (gp2.wasJustPressed(GamepadKeys.Button.START)) {
                isManual=!isManual;
            }

            //automatic control:
            if(!isManual) {
                if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                    bot.wrist.reset();
                    bot.claw.open();
                    //below code creates space for flip to occur
                    bot.wrist.setPitch(bot.wrist.pitchSetpoint - 20);
                    automaticIntake();
                    bot.bucket.stopIntake();
                    bot.linkage.retract();
                    bot.bucket.flipIn();
                    bot.wrist.reset();
                    //possibly move surgical tubing a bit HERE
                    bot.claw.grasp();
                }
                if (gp2.wasJustPressed(GamepadKeys.Button.B)) {
                    //after moves slide
                    bot.wrist.bucket();
                    bot.claw.open();
                    bot.claw.close();
                    bot.wrist.storage();
                    bot.slides.runToStorage();
                    bot.claw.open();
                    bot.wrist.reset();
                }
            }

            //manual control (all controls are on GP2):
            else {
                //linkage control (X)
                if (gp2.wasJustPressed(GamepadKeys.Button.X)) {
                    if(isLinkageRetracted){
                        bot.bucket.flipOut();
                        bot.linkage.extend();
                        isBucketFlipped=false;
                        isLinkageRetracted=false;
                    }
                    else{
                        bot.wrist.reset();
                        bot.wrist.setPitch(bot.wrist.pitchSetpoint - 20);
                        bot.linkage.retract();
                        bot.bucket.flipIn();
                        isBucketFlipped=true;
                        isLinkageRetracted=true;
                    }
                }

                //bucket claw control
                if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                    if(!isClawOpen){
                        bot.claw.open();
                        isClawOpen=true;
                    }
                    else{
                        //is never closed -> hopefully can still go from outtake-> storage (TEST)
                        bot.claw.grasp();
                        isClawOpen=false;
                    }
                }

                if (gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                    if(isDiffyTransferPos){
                        bot.wrist.bucket();
                        isDiffyTransferPos=false;
                    }
                    else{
                        bot.wrist.storage();
                        isDiffyTransferPos=true;
                    }
                }

                //bucket noodles control (B)
                if (gp2.wasJustPressed(GamepadKeys.Button.B)) {
                    if(isIntaking){
                        bot.bucket.stopIntake();
                        isIntaking=false;
                    }
                    else{
                        bot.bucket.intakeNoSense();
                        isIntaking=true;
                    }
                }

                //Right Bumper to reverse intake
                if(gp2.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)){
                    bot.bucket.reverseIntake();
                }
            }

            //Slides Preset Positions (GP2):
            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                switch(bot.slides.getState()){
                    case GROUND:
                        bot.slides.runToLowBucket();
                        break;
                    case BUCKET1:
                        bot.slides.runToTopBucket();
                }
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                switch(bot.slides.getState()){
                    case BUCKET2:
                        bot.slides.runToLowBucket();
                        break;
                    case BUCKET1:
                        bot.slides.runToStorage();
                }
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                switch(bot.slides.getState()){
                    case CHAMBER_HIGH:
                        bot.slides.runToLowRung();
                        break;
                    case CHAMBER_LOW:
                        bot.slides.runToStorage();
                        break;
                }
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                switch(bot.slides.getState()){
                    case GROUND:
                        bot.slides.runToLowRung();
                        break;
                    case CHAMBER_LOW:
                        bot.slides.runToTopRung();
                        break;
                }
            }
        }
    }

    private void automaticIntake(){
        color="nothing";
        bot.bucket.tubingServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
        telemetry.update();

        while(distance>4){
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                break;
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                automaticIntake();
            }
            bot.bucket.tubingServo2.setPower(0.7);
            distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
        }
        while(distance>1.8){
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                break;
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                automaticIntake();
            }
            bot.bucket.tubingServo2.setPower(0.2);
            distance= bot.bucket.distanceSensor.getDistance(DistanceUnit.CM);
            runColorSensor();
        }
        bot.bucket.stopIntake();

        if((isAllianceBlue && color.equals("red")) || !isAllianceBlue && color.equals("blue")){
            runColorSensor();
            bot.bucket.reverseIntake();
            automaticIntake();

        }

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


    private void drive() {
        gp1.readButtons();
        bot.prepMotors();
        driveSpeed = 1;
        driveSpeed *= 1 - 0.9 * gp1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        driveSpeed = Math.max(0, driveSpeed);

        Vector2d driveVector = new Vector2d(gp1.getLeftX(), -gp1.getLeftY()),
                turnVector = new Vector2d(
                        gp1.getRightX(), 0);

        bot.driveRobotCentric(driveVector.getX() * driveSpeed,
                driveVector.getY() * driveSpeed,
                turnVector.getX() * driveSpeed / 1.7
        );
    }
}





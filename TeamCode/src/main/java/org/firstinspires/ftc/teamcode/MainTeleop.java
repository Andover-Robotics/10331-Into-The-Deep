package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;
import org.firstinspires.ftc.teamcode.subsystems.Slides;
import java.lang.Math;


/*
@TeleOp
public class MainTeleop extends LinearOpMode {
    private double delta_L; //slide extension length needed to get to position
    private double claw_angle; //this is the angle between claw and horizontal
    private double slides_angle;  // angle between slides and horizontal
    private final double robot_height = 0.21; // (height of base (not including slides) in meters, CHANGE LATER)
    private final double claw_length = 0.05; //CHANGE LATER
    private final double TICK_TO_SLIDES_MOVEMENT_CONV = 1; //CHANGE LATER
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

    /*

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

            bot.slides.runSlides(gp2.getRightY());

            if (gp2.wasJustPressed(GamepadKeys.Button.START)) {
                isManual=!isManual;
            }

            //automatic control:
            if(!isManual) {
                if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                    automaticIntake();
                }
                if (gp2.wasJustPressed(GamepadKeys.Button.B)) {
                    //after moves slide
                    bot.wrist.outtakePos();
                    bot.claw.open();
                    bot.claw.close();
                    bot.wrist.transferPos();
                    bot.slides.runToStorage();
                }
            }

            //manual control (all controls are on GP2):
            else {
                //linkage control (X)
                if (gp2.wasJustPressed(GamepadKeys.Button.X)) {
                    if(isLinkageRetracted){
                        bot.linkage.extend();
                        bot.bucket.flipOut();
                        isBucketFlipped=false;
                        isLinkageRetracted=false;
                    }
                    else{
                        bot.linkage.retract();
                        bot.bucket.flipIn();
                        isBucketFlipped=true;
                        isLinkageRetracted=true;
                    }
                }

                //bucket flip control (A)
                if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                    if(!isClawOpen){
                        bot.claw.open();
                        isClawOpen=true;
                    }
                    else{
                        bot.claw.close();
                        isClawOpen=false;
                    }
                }

                if (gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                    if(isDiffyTransferPos){
                        bot.wrist.outtakePos();
                        isDiffyTransferPos=false;
                    }
                    else{
                        bot.wrist.transferPos();
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
                        bot.slides.runToMidBucket();
                        break;
                    case BUCKET2:
                        bot.slides.runToTopBucket();
                }
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                switch(bot.slides.getState()){
                    case BUCKET3:
                        bot.slides.runToMidBucket();
                        break;
                    case BUCKET2:
                        bot.slides.runToLowBucket();
                        break;
                    case BUCKET1:
                        bot.slides.runToStorage();
                }
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                switch(bot.slides.getState()){
                    case RUNG2:
                        bot.slides.runToLowRung();
                        break;
                    case RUNG1:
                        bot.slides.runToStorage();
                        break;
                }
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                switch(bot.slides.getState()){
                    case GROUND:
                        bot.slides.runToLowRung();
                        break;
                    case RUNG1:
                        bot.slides.runToTopRung();
                        break;
                }
            }
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

    private void automaticIntake(){
        bot.bucket.flipOut();
        bot.linkage.extend();
        while(!bot.bucket.intakeSense(isAllianceBlue)){
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                break;
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                automaticIntake();
            }
        }
        bot.bucket.stopIntake();
        bot.linkage.retract();
        bot.bucket.flipIn();
        bot.wrist.transferPos();
        bot.claw.close();
        //then driver moves slides
    }



}

 */





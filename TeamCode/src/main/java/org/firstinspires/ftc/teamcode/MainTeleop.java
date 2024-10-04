package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

/*

To Do (in order of priority):
- kinematics!!! For diffy claw
- test color sensor (can be done independently with control hub)
- test all servo values (after build)
-


 */




@TeleOp
public class MainTeleop extends LinearOpMode {
    Bot bot;
    private GamepadEx gp1;
    private GamepadEx gp2;
    private double driveSpeed = 1;

    private boolean isManual = false;
    private boolean isLinkageRetracted=true;
    private boolean isDiffyOuttake=true;
    private boolean isIntaking=true;
    private boolean isBucketFlipped=true;


    private boolean isAllianceBlue=false;

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
            rotateClaw();
            runSlides(gp2.getRightY());

            if(!isManual) {
                if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                    automaticIntake();
                }
                if (gp2.wasJustPressed(GamepadKeys.Button.B)) {
                    bot.diffyClaw.outtakePos();
                }
            }

            else {
                if (gp2.wasJustPressed(GamepadKeys.Button.X)) {
                    if(isLinkageRetracted){
                        bot.linkage.extend();
                        isLinkageRetracted=false;
                    }
                    else{
                        bot.linkage.retract();
                        isLinkageRetracted=true;
                    }
                }
                if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                    if(isBucketFlipped){
                        bot.bucket.flipOut();
                        isBucketFlipped=false;
                    }
                    else{
                        bot.bucket.flipIn();
                        isBucketFlipped=true;
                    }
                }
                if (gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                    if(isDiffyOuttake){
                        bot.diffyClaw.transferPos();
                        isDiffyOuttake=false;
                    }
                    else{
                        bot.diffyClaw.outtakePos();
                        isDiffyOuttake=true;
                    }
                }
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

                if(gp2.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)){
                    bot.bucket.reverseIntake();
                }




            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {;
                bot.slides.runToTop();
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.slides.runToStorage();
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                bot.slides.runToLow();
            }
            else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                bot.slides.runToMid();}


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

    private void rotateClaw() {
        gp2.readButtons();
        bot.diffyClaw.rotate(gp2.getLeftX());
    }
    private void automaticIntake(){
        bot.linkage.extend();
        bot.bucket.flipOut();
        while(!bot.bucket.intakeSense(isAllianceBlue)){
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                break;
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                automaticIntake();
            }
        }
        bot.bucket.stopIntake();
        bot.bucket.flipIn();
        bot.linkage.retract();
        bot.diffyClaw.transferPos();
    }


}




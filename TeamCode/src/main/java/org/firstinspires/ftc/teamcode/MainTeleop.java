/*package org.firstinspires.ftc.teamcode;


# MUST UDDATE ROBOT CONTROLLER SDK FOR RR -> And Get Ladybug

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;
import org.firstinspires.ftc.teamcode.subsystems.Slides;



To Do (in order of priority):
- kinematics!!! For diffy claw
- test color sensor (can be done independently with control hub)
- test all servo values (after build)
-



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
//            gp1.readButtons();
//            gp2.readButtons();
            //potential source for error: having reading buttons within the while loop and within the individual methods

            drive();
            rotateClaw(gp1.getLeftX());
            runSlides(gp2.getRightY());

            //automatic control:
            if(!isManual) {
                if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                    automaticIntake();
                }
                if (gp2.wasJustPressed(GamepadKeys.Button.B)) {
                    bot.diffyClaw.outtakePos();
                }
            }

            //manual control (all controls are on GP2):
            else {
                //linkage control (X)
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

                //bucket flip control (A)
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

                //diffy control (Y)
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

    private void rotateClaw(double pos) {
      //  gp2.readButtons();
        bot.diffyClaw.rotate(pos);
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

    private void runSlides(double power) {
        bot.slides.runToManual(power);
        bot.slides.periodic();
    }
}
*/
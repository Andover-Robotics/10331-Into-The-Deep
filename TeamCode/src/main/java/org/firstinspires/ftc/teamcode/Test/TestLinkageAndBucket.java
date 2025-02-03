package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class TestLinkageAndBucket extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    private GamepadEx gp1;
    private double driveSpeed = 1;

    int times = 0;

    boolean isAllianceBlue=true;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        gp1 = new GamepadEx(gamepad1);
        double pos=0;

        waitForStart();

        bot.bucket.stopIntake();
        bot.linkage.retract();
        bot.bucket.flipIn();
        bot.claw.open();



        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            if(gp2.wasJustPressed(GamepadKeys.Button.START)) {
                isAllianceBlue= !isAllianceBlue;
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                bot.bucket.stopIntake();
                bot.linkage.retract();
                bot.bucket.flipIn();

            }
            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.bucket.flipOut();
                bot.linkage.extend();
                bot.bucket.intakeNoSense();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {

            }

/*

            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                times++;
                if(times % 2 == 0)
                    bot.bucket.runForwards();
                else
                    bot.bucket.runBackwards();
            }


 */


            //bucket testing: flip
            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.bucket.flipIn();
            } else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.bucket.flipOut();
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

}




package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class TestLinkageAndBucket extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    int times = 0;

    boolean isAllianceBlue=true;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        double pos=0;

        waitForStart();

        bot.bucket.stopIntake();
        bot.linkage.retract();
        bot.bucket.flipIn();

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
                bot.bucket.flipOut();
                bot.linkage.extend();
                bot.bucket.intakeSense(isAllianceBlue);
                bot.bucket.stopIntake();
                bot.linkage.retract();
                bot.bucket.flipIn();
            }


            /*
            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                times++;
                if(times % 2 == 0)
                    bot.bucket.runForwards();
                else
                    bot.bucket.runBackwards();
            }



            //bucket testing: flip
            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.bucket.flipIn();
            } else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.bucket.flipOut();
            }

             */


        }
    }

}

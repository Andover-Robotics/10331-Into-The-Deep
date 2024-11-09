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

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        double pos=0;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            //boolean compound = false;
            //linkage testing
            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                bot.linkage.retract();
                //later will change to reset if retract is jank
            } else if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.linkage.extend();
            }

            //bucket testing: intake
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


        }
    }
}


package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp(name = "Tune Bucket", group = "Test")
public class TuneBucket extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    private double axlePos=0;
    private boolean isForward;


    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);

        waitForStart();

        double pos=0.0;
      //  bot.bucket.flip1.setPosition(0);
       // bot.bucket.flip2.setPosition(0);
        //pos = bot.bucket.flip1.getPosition();

        while (opModeIsActive() && !isStopRequested()) {

            gp2.readButtons();
//
//            telemetry.addData("Current Position Bucket Flip Right", bot.bucket.flip1.getPosition());
//            telemetry.addData("Current Position Bucket Flip left", bot.bucket.flip2.getPosition());
//            telemetry.update();

            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                if(pos > 0)
                    pos = pos-0.1;
//
//                bot.bucket.flip1.setPosition(pos);
//                bot.bucket.flip2.setPosition(pos);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                if(pos < 1)
                    pos=pos+0.1;
                //goes towards the bot
//
//                bot.bucket.flip1.setPosition(pos);
//                bot.bucket.flip2.setPosition(pos);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
//                bot.bucket.flipIn();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
//                bot.bucket.flipOut();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
//                bot.bucket.reset();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                //start intake
//                bot.bucket.intakeNoSense();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                //stop intake
//                bot.bucket.stopIntake();
            }



        }
    }
}








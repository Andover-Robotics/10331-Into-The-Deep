package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

//Tester class for slides - uncomment code when ready
@TeleOp
public class SlidesTest extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    @Override
    public void runOpMode() throws InterruptedException {
      //  bot.slides.periodic


        //Slides joystick control


        //Slides Preset Positions (GP2):
        if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
//            switch(bot.slides.getState()){
//                case GROUND:
//                    bot.slides.runToLowBucket();
//                    break;
//                case BUCKET1:
//                    bot.slides.runToMidBucket();
//                    break;
//                case BUCKET2:
//                    bot.slides.runToTopBucket();
//            }
        }
        else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
//            switch(bot.slides.getState()){
//                case BUCKET3:
//                    bot.slides.runToMidBucket();
//                    break;
//                case BUCKET2:
//                    bot.slides.runToLowBucket();
//                    break;
//                case BUCKET1:
//                    bot.slides.runToStorage();
//            }
        }
        else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
//            switch(bot.slides.getState()){
//                case RUNG2:
//                    bot.slides.runToLowRung();
//                    break;
//                case RUNG1:
//                    bot.slides.runToStorage();
//                    break;
//            }
        }
        else if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
//            switch(bot.slides.getState()){
//                case GROUND:
//                    bot.slides.runToLowRung();
//                    break;
//                case RUNG1:
//                    bot.slides.runToTopRung();
//                    break;
//            }
        }
    }

    void runSlides(double power) {
//        bot.slides.runToManual(power);
//        bot.slides.periodic();
    }
}

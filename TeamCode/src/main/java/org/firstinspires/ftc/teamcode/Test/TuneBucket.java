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

        double pos=0.2;
        while (opModeIsActive() && !isStopRequested()) {

            gp2.readButtons();

            telemetry.addData("Current Position Bucket Flip Right", bot.bucket.flip.getPosition());
            telemetry.update();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)){
                //decrement right - WORKING
                if(pos>0){
                    pos=pos-0.1;
                }

                //flip 1: decrement
                //flip 2:
                bot.bucket.moveFlipRight(pos);
            }


            if(gp2.wasJustPressed(GamepadKeys.Button.B)){
                //increment right: towards robot
                if(pos<1){
                    pos=pos+0.1;
                }

                //flip 1: increment
                //flip 2:
                bot.bucket.moveFlipRight(pos);
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                bot.bucket.intake(0.3);
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.bucket.stopIntake();
            }
/*

            if(gp2.wasJustPressed(GamepadKeys.Button.A)){
                if(pos>0){
                    pos=pos-0.1;
                }
                bot.bucket.move(pos);
            }


            if(gp2.wasJustPressed(GamepadKeys.Button.B)){
                if(pos<1){
                    pos=pos+0.1;
                }

                bot.bucket.move(pos);
            }



            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                //decrement left : direction towards robot

                if(posLeft>0){
                    posLeft=posLeft-0.1;
                }

                bot.bucket.moveFlipLeft(posLeft);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                //increment left : towards field
                if(posLeft<1){
                    posLeft=posLeft+0.1;
                }
                bot.bucket.moveFlipLeft(posLeft);
            }



            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.bucket.reset();
            }

 */

        }
    }
}








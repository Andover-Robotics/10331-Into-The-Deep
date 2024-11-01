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

        double pos=0;
        while (opModeIsActive() && !isStopRequested()) {

            gp2.readButtons();

            telemetry.addData("Current Position Bucket Flip Right", bot.bucket.flip.getPosition());
            telemetry.addData("Current Position Bucket Flip Left", bot.bucket.flip2.getPosition());
            telemetry.update();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)){
                if(pos>0){
                    pos=pos-0.1;
                }
                bot.bucket.move(pos, 0);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)){
                if(pos<1){
                    pos=pos+0.1;
                }
                bot.bucket.move(pos,0);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                bot.bucket.runBackwards();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                bot.bucket.runForwards();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.bucket.resetFlip();
            }

        }
    }
}

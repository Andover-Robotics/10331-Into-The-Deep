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

            telemetry.addData("Current Position", pos);

            if(gp2.getRightY()>0){
                pos=bot.bucket.flipIncrement(gp2.getRightY());
            }
            if(gp2.getRightY()<0){
                pos=bot.bucket.flipDecrement(-gp2.getRightY());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                bot.bucket.runBackwards();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                bot.bucket.runForwards();
            }

        }
    }
}

package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
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

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("Current Position");

            gp2.readButtons();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)){
                bot.bucket.flipIncrement();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.B)){
                bot.bucket.flipDecrement();
            }


        }
    }
}

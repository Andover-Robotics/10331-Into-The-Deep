package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class TestClipping extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    @Override
    public void runOpMode() throws InterruptedException {
        //claw to spec pos, raise slides, lower slides
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.wrist.specOuttake();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                bot.slides.runToLowRung();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                bot.slides.runToStorage();
            }
        }
    }
}

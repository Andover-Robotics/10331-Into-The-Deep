package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

public class TestWrist extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        double pos = 0.0;

        waitForStart();

        //sync servos to 0
        bot.wrist.wrist_l.setPosition(0);
        bot.wrist.wrist_r.setPosition(0);

        while(opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            telemetry.addData("Current Pos: Left wrist", bot.wrist.wrist_l.getPosition());
            telemetry.addData("Current Pos: Right wrist", bot.wrist.wrist_r.getPosition());
            telemetry.update();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                //increment position by 0.1
                pos = pos <= 0.9 ? pos+0.1 : 1;
                bot.wrist.wrist_l.setPosition(pos);
                bot.wrist.wrist_r.setPosition(pos);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                pos = pos >= 0.1 ? pos-0.1 : 0;
                bot.wrist.wrist_l.setPosition(pos);
                bot.wrist.wrist_r.setPosition(pos);
            }
        }
    }
}

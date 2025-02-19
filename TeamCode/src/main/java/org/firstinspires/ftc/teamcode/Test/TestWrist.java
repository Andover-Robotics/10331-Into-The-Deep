package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;


@TeleOp(name="test wrist")
public class TestWrist extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        double pos = 0;

        waitForStart();

        //sync servos to 0
        bot.wrist.setPitch(0);

        while(opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            telemetry.addData("Current Pos: Left wrist", bot.wrist.wrist_l.getPosition());
            telemetry.addData("Current Pos: Right wrist", bot.wrist.wrist_r.getPosition());
            telemetry.update();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                //increment position by 0.1
                pos = pos <= 0.95 ? pos+0.05 : 1;
                bot.wrist.wrist_l.setPosition(pos);
                bot.wrist.wrist_r.setPosition(pos);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                pos = pos >= 0.05 ? pos-0.05 : 0;
                bot.wrist.wrist_l.setPosition(pos);
                bot.wrist.wrist_r.setPosition(pos);
            }


            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                bot.wrist.bucketOuttake();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.wrist.specOuttake();
            }
        }
    }
}

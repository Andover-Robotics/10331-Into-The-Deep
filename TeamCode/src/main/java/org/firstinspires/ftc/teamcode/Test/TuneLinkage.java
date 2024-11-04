package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;


@TeleOp(name = "Tune Linkage", group = "Test")
public class TuneLinkage extends LinearOpMode {

    Bot bot;
    private GamepadEx gp2;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        double pos=0;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();
            telemetry.addData("Current Position Linkage Right", bot.linkage.linkage1.getPosition());
            telemetry.addData("Current Position Linkage Left", bot.linkage.linkage2.getPosition());
            telemetry.update();

            if(gp2.wasJustPressed(GamepadKeys.Button.A)){
                bot.linkage.retract();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.B)){
                bot.linkage.extend();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                if(pos>0){
                    pos=pos-0.01;
                }
                bot.linkage.move(pos);
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                if(pos<1){
                    pos=pos+0.01;
                }
                bot.linkage.move(pos);
            }

        }
    }
}

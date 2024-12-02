package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp(name = "Tune Claw", group = "Test")
public class TuneClaw extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    private double axlePos=0;
    private boolean isForward;


    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);

        waitForStart();

        double pos=bot.claw.claw.getPosition();
        while (opModeIsActive() && !isStopRequested()) {

            gp2.readButtons();

            telemetry.addData("Current Position", bot.claw.claw.getPosition());
            telemetry.update();

            if(gp2.wasJustPressed(GamepadKeys.Button.X)){
                if(pos>0){
                    pos=pos-0.01;
                }
                bot.claw.move(pos);
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)){
                if(pos<1){
                    pos=pos+0.01;
                }
                bot.claw.move(pos);
            }

        }
    }
}

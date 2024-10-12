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
            telemetry.addData("Current Position", pos);

            if(gp2.getRightY()>0){
                pos=bot.linkage.extendIncrementally(gp2.getRightY());
            }
            if(gp2.getRightY()<0){
                pos=bot.linkage.retractIncrementally(-gp2.getRightY());
            }

        }
    }
}

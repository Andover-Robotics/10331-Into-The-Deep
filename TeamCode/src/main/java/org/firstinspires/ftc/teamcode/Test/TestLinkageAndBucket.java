package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

public class TestLinkageAndBucket extends LinearOpMode {
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


        }
    }
}


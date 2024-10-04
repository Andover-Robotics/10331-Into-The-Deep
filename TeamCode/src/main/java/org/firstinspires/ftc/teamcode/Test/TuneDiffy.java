package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

//tester class to tune methods of differential claw
@TeleOp(name= "tuneDiffy")
public class TuneDiffy extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("Current Position");

        }

    }
}

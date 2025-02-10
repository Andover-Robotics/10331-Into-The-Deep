package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

//Tester class for slides - uncomment code when ready
@TeleOp
public class SlidesTest extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);

        waitForStart();

        bot.slides.periodic();

        //Slides joystick control
        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            bot.slides.runSlides(-gp2.getRightY());
            //when joystick down -> telemetry is negative (

            telemetry.addData("GP2 position (value for runTo i think?)", gp2.getRightY());
            telemetry.addData("Slides Left position", -bot.slides.leftMotor.getCurrentPosition());
            telemetry.addData("Slides Right position", -bot.slides.rightMotor.getCurrentPosition());
            telemetry.update();

            //slides preset positions
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.slides.runToTopBucket();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.slides.runToLowBucket();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                bot.slides.runToTopRung();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                bot.slides.runToLowRung();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.slides.runToStorage();
            }

            bot.slides.periodic();

        }
    }
}
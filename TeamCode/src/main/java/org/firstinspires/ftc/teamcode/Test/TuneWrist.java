package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp(name = "TuneWrist", group = "Test")
public class TuneWrist extends LinearOpMode {
    public Bot bot;
    private GamepadEx gp1;
    @Override
    public void runOpMode() throws InterruptedException {

        Bot.instance = null;
        bot = Bot.getInstance(this);

        gp1 = new GamepadEx(gamepad1);

        bot.wrist.vertical();

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {

            gp1.readButtons();

            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.wrist.setPitch(bot.wrist.pitchSetpoint - 20);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                bot.wrist.setRoll(bot.wrist.rollSetpoint - 10);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.wrist.setPitch(bot.wrist.pitchSetpoint + 20);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                bot.wrist.setRoll(bot.wrist.rollSetpoint + 10);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.A)) {
                bot.wrist.setPitch(bot.wrist.PITCH_MAX - bot.wrist.PITCH_MID);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.B)) {
                bot.wrist.setPitch(bot.wrist.PITCH_MIN - bot.wrist.PITCH_MID);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.X)) {
                bot.wrist.diffyLeft.setPosition(0);
                bot.wrist.diffyRight.setPosition(0);
            }

            telemetry.addData("Right Servo Position", bot.wrist.diffyRight.getAngle());
            telemetry.addData("Left Servo Position", bot.wrist.diffyLeft.getAngle());
            telemetry.addData("Right Servo Position", bot.wrist.diffyRight.getPosition());
            telemetry.addData("Left Servo Position", bot.wrist.diffyLeft.getPosition());

            telemetry.addData("Current Roll Position (dpad L/R)", bot.wrist.rollSetpoint);
            telemetry.addData("Current Pitch Position (dpad Up/Down)", bot.wrist.pitchSetpoint);

            telemetry.update();

        }
    }
}

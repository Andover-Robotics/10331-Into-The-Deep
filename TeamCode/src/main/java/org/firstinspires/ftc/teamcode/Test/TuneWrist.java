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
                bot.wrist.setRoll(bot.wrist.rollSetpoint - 20);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.wrist.setPitch(bot.wrist.pitchSetpoint + 20);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                bot.wrist.setRoll(bot.wrist.rollSetpoint + 20);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.START)) {
                bot.wrist.setPitch(bot.wrist.PITCH_MAX);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.BACK)) {
                bot.wrist.setRollPitch(-60, bot.wrist.PITCH_MIN);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.X)) {
                bot.wrist.setRollPitch(140, 420);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.Y)) {
                //bot.wrist.setRollPitch(100, 230);
                bot.wrist.setPitch(90);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.A)) {
               // bot.wrist.setRollPitch(70, 500);
                bot.wrist.setRoll(90);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.B)) {
               // bot.wrist.setRollPitch(0, 220);
                bot.wrist.setRollPitch(0,-45);
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

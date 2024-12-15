package org.firstinspires.ftc.teamcode.Test;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Bot;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TuneWrist", group = "Test")
public class TuneWrist extends LinearOpMode {
    public Bot bot;
    private GamepadEx gp1;
    private ElapsedTime time;
    @Override
    public void runOpMode() throws InterruptedException {

        time = new ElapsedTime();
        bot = Bot.getInstance(this);

        /*
roll, pitch
Sequence 1:
        1) (100,520) => TRANSFER
        2) (100,480)
        3) (320,480)
        4) (320,-40)
        5) (80,-40) => OUTTAKE


        1) 80,-40 -> outtake
        2) 240,160
        3) 240. 520
        4) 100. 520 -> transfer
   */

        gp1 = new GamepadEx(gamepad1);

        bot.wrist.vertical();
     //   bot.wrist.setRoll(100);
      //  bot.wrist.setPitch(520);

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
            if (gp1.wasJustPressed(GamepadKeys.Button.A)) {
                bot.wrist.setRoll(100);
                bot.wrist.setPitch(520);
             //   bot.wrist.setRollPitch(100,520);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.B)) {
                bot.wrist.setRoll(100);
                bot.wrist.setPitch(480);
             //   bot.wrist.setRollPitch(100,480);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.wrist.setRoll(320);
                bot.wrist.setPitch(480);
             //   bot.wrist.setRollPitch(320,480);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.X)) {
                bot.wrist.setRoll(320);
                bot.wrist.setPitch(-40);
            //    bot.wrist.setRollPitch(320,-40);
            }
            if (gp1.wasJustPressed(GamepadKeys.Button.BACK)) {
                bot.wrist.setRoll(80);
                bot.wrist.setPitch(-40);
            }

            if (gp1.wasJustPressed(GamepadKeys.Button.START)) {
                time.reset();
                while(time.seconds() < 2) {
                    bot.wrist.setRoll(100);
                    bot.wrist.setPitch(520);
                }
                time.reset();
                while(time.seconds() < 2) {
                    bot.wrist.setRoll(100);
                    bot.wrist.setPitch(480);
                }
                time.reset();
                while(time.seconds() < 2) {
                    bot.wrist.setRoll(320);
                    bot.wrist.setPitch(480);
                }
                time.reset();
                while(time.seconds() < 2) {
                    bot.wrist.setRoll(320);
                    bot.wrist.setPitch(-40);
                }
                bot.wrist.setRoll(80);
                bot.wrist.setPitch(-40);
            }

            /*
        1) (100,520) => TRANSFER
        2) (100,480)
        3) (320,480)
        4) (320,-40)
        5) (80,-40) => OUTTAKE
             */



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

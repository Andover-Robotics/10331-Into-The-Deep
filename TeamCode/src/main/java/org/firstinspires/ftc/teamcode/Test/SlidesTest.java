package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

//Tester class for slides - uncomment code when ready
@TeleOp
public class SlidesTest extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    private MotorEx motor;
    private MotorEx motor2;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        motor= bot.slides.rightMotor;
        motor2=  bot.slides.leftMotor;

        waitForStart();

        // bot.slides.periodic();

        //Slides joystick control
        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            bot.slides.runSlides(-gp2.getRightY());
            //when joystick down -> telemetry is negative (
            telemetry.addData("Slides position", -gp2.getRightY());

            telemetry.addLine("Motor Data")
                  //  .addData("Type", motor.getMotorType().getName())
                 //   .addData("Power", "%.3f", motor.getPower())
                    .addData("Current Pos", "%d", motor.getCurrentPosition());
                  //  .addData("Target Pos", "%d", motor.getTargetPosition())
                   // .addData("Busy?", motor.isBusy() ? "Yes" : "No");

            telemetry.addLine("Motor2 Data")
                //    .addData("Type", motor2.getMotorType().getName())
                 //   .addData("Power", "%.3f", motor2.getPower())
                    .addData("Current Pos", "%d", motor2.getCurrentPosition());
                  //  .addData("Target Pos", "%d", motor2.getTargetPosition())
                   // .addData("Busy?", motor2.isBusy() ? "Yes" : "No");


            telemetry.update();

            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
           //     bot.slides.runToLowBucket();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
             //   bot.slides.runToStorage();
            }
        }
    }


    void runSlides ( double power){
        bot.slides.runToManual(power);
        bot.slides.periodic();
    }
}
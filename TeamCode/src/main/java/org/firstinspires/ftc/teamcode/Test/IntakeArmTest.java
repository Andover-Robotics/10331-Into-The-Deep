package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class IntakeArmTest extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    double pos = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);

        waitForStart();
        bot.arm.rotateWrist(0);
        bot.arm.setPitch(0);
        bot.arm.closeClaw();

        while(opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();
            telemetry.addData("Current Position Intake Claw: ", bot.arm.clawOpenClose.getPosition());
            telemetry.addData("Current Position Claw Right Wrist: ", bot.arm.wrist_r.getPosition());
            telemetry.addData("Current Position Claw Left Wrist: ", bot.arm.wrist_l.getPosition());
            telemetry.addData("Current Position Wrist Rotation Servo: ", bot.arm.wristRotationServo.getPosition());
            telemetry.update();
            //tune rotation:

            //tune open/close:

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                pos = pos+0.1 < 1 ? pos+0.1 : pos;
                bot.arm.setClawPos(pos);
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                pos = pos - 0.1 > 0 ? pos-0.1 : pos;
                bot.arm.setClawPos(pos);
            }

            //tune wrist:
            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                pos = pos+0.1 < 1 ? pos+0.1 : pos;
                bot.arm.setPitch(pos);
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                pos = pos - 0.1 > 0 ? pos-0.1 : pos;
                bot.arm.setPitch(pos);
            }
        }
    }
}

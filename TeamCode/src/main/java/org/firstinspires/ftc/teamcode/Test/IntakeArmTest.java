package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class IntakeArmTest extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    double pitchPos = 0;
    double rotatePos = 0;
    double openPos = 0;
    double inc = 0;
    ElapsedTime timer = new ElapsedTime();
    double updateInterval = 0.1;

    boolean toggleIntake = true;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);

        waitForStart();
        bot.wrist.transfer();
        bot.linkage.retract();
        bot.arm.transferPos();

        //intermediate arm pos (pitch): 0.65
        //rotation pos: 0.65
        //intake arm pos: 0.85
        //transfer arm pos: 0.25

        while(opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();
            telemetry.addData("Current Position Intake Claw: ", bot.arm.clawOpenClose.getPosition());
            telemetry.addData("Current Position Claw Right Wrist: ", bot.arm.wrist_r.getPosition());
            telemetry.addData("Current Position Claw Left Wrist: ", bot.arm.wrist_l.getPosition());
            telemetry.addData("Current Position Wrist Rotation Servo: ", bot.arm.wristRotationServo.getPosition());
            telemetry.update();
            //tune rotation:
            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                //rotatePos = openPos+0.05 < 1 ? openPos+0.05 : openPos;
               // rotatePos = openPos+0.05;
                //bot.arm.rotateWrist(0.75);
                bot.claw.open();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                //rotatePos =  openPos-0.05 > 0 ? openPos-0.05 : openPos;
//                rotatePos =  openPos-0.05;
               //    bot.arm.rotateWrist(0.5);\
                bot.claw.close();
            }

            //tune open/close:
//            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
////                openPos = openPos+0.1 < 1 ? openPos+0.1 : openPos;
////                bot.arm.setClawPos(openPos);
//                rotatePos =  rotatePos+0.05 < 1 ? rotatePos+0.05 : rotatePos;
//                bot.arm.rotateWrist(rotatePos);
//            }
//            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
////                openPos = openPos - 0.1 > 0 ? openPos-0.1 : openPos;
////                bot.arm.setClawPos(openPos);
//                rotatePos =  rotatePos-0.05 > 0 ? rotatePos-0.05 : rotatePos;
//                bot.arm.rotateWrist(rotatePos);
//            }

            rotatePos = gp2.getLeftX();
            if (rotatePos != 0) {
                double temp = gp2.getLeftX();
                if(Math.abs(temp) > Math.abs(rotatePos)) {
                    rotatePos = temp;
                }
                bot.arm.rotateWrist(rotatePos);
            }



            if(gp2.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
                toggleIntake = !toggleIntake;
            }

            //tune wrist:
            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                pitchPos = pitchPos+0.05 < 1 ? pitchPos+0.05 : pitchPos;
                bot.arm.setPitch(pitchPos);
               // bot.arm.intakePos();
            }
//
//            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
//                bot.arm.closeClaw();
//            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                pitchPos = pitchPos - 0.05 > 0 ? pitchPos-0.05 : pitchPos;
                bot.arm.setPitch(pitchPos);
            }
        }
    }
}

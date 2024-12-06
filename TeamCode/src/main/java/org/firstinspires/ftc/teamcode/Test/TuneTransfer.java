package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class TuneTransfer extends LinearOpMode {
    Bot bot;
    private GamepadEx gp1;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);

        waitForStart();

        bot.bucket.stopIntake();
        bot.linkage.retract();
        bot.bucket.flipIn();

        double clawPos=bot.claw.claw.getPosition();
        double bucketPos=bot.bucket.flip.getPosition();
        double linkagePos= bot.linkage.retractPos;

        while (opModeIsActive() && !isStopRequested()) {
            gp1.readButtons();

            if(gp1.wasJustPressed(GamepadKeys.Button.A)){
                if(bucketPos>0){
                    bucketPos=bucketPos-0.05;
                }
                bot.bucket.moveFlipRight(bucketPos);
            }

            if(gp1.wasJustPressed(GamepadKeys.Button.B)){
                if(bucketPos<1){
                    bucketPos=bucketPos+0.05;
                }
                bot.bucket.moveFlipRight(bucketPos);
            }

            if(gp1.wasJustPressed(GamepadKeys.Button.X)){
                if(clawPos>0){
                    clawPos=clawPos-0.01;
                }
                bot.claw.move(clawPos);
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.Y)){
                if(clawPos<1){
                    clawPos=clawPos+0.01;
                }
                bot.claw.move(clawPos);
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)){
                if(linkagePos>0){
                    linkagePos=linkagePos-0.01;
                }
                bot.linkage.move(linkagePos);
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)){
                if(linkagePos<1){
                    linkagePos=linkagePos+0.01;
                }
                bot.linkage.move(linkagePos);
            }
            ;
            telemetry.addData("Current Position Bucket", bot.bucket.flip.getPosition());
            telemetry.addData("Current Position Claw", bot.claw.claw.getPosition());
            telemetry.addData("Current Position Linkage Right", bot.linkage.linkage1.getPosition());
            telemetry.addData("Current Position Linkage Left", bot.linkage.linkage2.getPosition());
            telemetry.update();


        }
    }

}
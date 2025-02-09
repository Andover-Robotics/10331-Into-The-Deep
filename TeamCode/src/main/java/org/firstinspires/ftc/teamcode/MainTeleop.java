package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Bot;
import org.firstinspires.ftc.teamcode.subsystems.Slides;
import java.lang.Math;

@TeleOp

//FULL MANUAL

public class MainTeleop extends LinearOpMode {
    Bot bot;
    private GamepadEx gp1;
    private GamepadEx gp2;
    private double driveSpeed = 1;

    private boolean isLinkageRetracted = true;
    private boolean isIntaking = true;
    private boolean isBucketFlipped = true;
    private boolean isDiffyTransferPos = true;
    private boolean isClawOpen = true;
    private boolean isAllianceBlue = false;
    private ElapsedTime time;

    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);
        gp2 = new GamepadEx(gamepad2);
        time = new ElapsedTime();

        waitForStart();

        gp1.readButtons();
        if (gp1.wasJustPressed(GamepadKeys.Button.START)) {
            isAllianceBlue = true;
        }

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("TeleOp has started");
            gp1.readButtons();
            gp2.readButtons();

            drive();

            bot.slides.periodic();
            bot.slides.runSlides(-gp2.getRightY());

            //open close claw
            if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                if (!isClawOpen) {
                    bot.claw.open();
                    isClawOpen = true;
                } else {
                    bot.claw.grasp();
                    isClawOpen = false;
                }

            }

            //increment wrist forward
            if (gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                double curr_pos = bot.wrist.wrist_r.getPosition();
                if (curr_pos > 0) {
                    curr_pos -= 0.01;
                }
                bot.wrist.setPitch(curr_pos);
            }

            //increment wrist backward
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                double curr_pos = bot.wrist.wrist_r.getPosition();
                if (curr_pos < 1) {
                    curr_pos += 0.01;
                }
                bot.wrist.setPitch(curr_pos);
            }

            //increment linkage forward
            if (gp2.wasJustPressed(GamepadKeys.Button.X)) {
                double curr_pos = bot.linkage.linkage1.getPosition();
                if (curr_pos < 1) {
                    curr_pos += 0.01;
                }
              //  bot.linkage.move(curr_pos);
            }

            //increment linkage backward
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                double curr_pos = bot.linkage.linkage1.getPosition();
                if (curr_pos > 0) {
                    curr_pos -= 0.01;
                }
                bot.linkage.linkage1.setPosition(curr_pos);
            }

            //noodles
            /*
            if(gp2.getLeftY() > 0.01) {
                bot.bucket.intake(0.5);
            } else if(gp2.getLeftY() < -0.01) {
                bot.bucket.reverseIntake(0.5);
            }
            */





            //run noodles forward
            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                if(isIntaking) {
                    bot.bucket.stopIntake();
                    isIntaking = false;
                } else {
                    bot.bucket.intake(0.5);
                    isIntaking = true;
                }
            }

            //run noodles backward
            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                if(isIntaking) {
                    bot.bucket.stopIntake();
                    isIntaking = false;
                } else {
                    bot.bucket.reverseIntake(0.5);
                    isIntaking = true;
                }
            }
        }
    }

    private void drive() {
        gp1.readButtons();
        bot.prepMotors();
        driveSpeed = 1;
        driveSpeed *= 1 - 0.9 * gp1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        driveSpeed = Math.max(0, driveSpeed);

        Vector2d driveVector = new Vector2d(gp1.getLeftX(), -gp1.getLeftY()),
                turnVector = new Vector2d(
                        gp1.getRightX(), 0);

        bot.driveRobotCentric(driveVector.getX() * driveSpeed,
                driveVector.getY() * driveSpeed,
                turnVector.getX() * driveSpeed / 1.7
        );
    }
}








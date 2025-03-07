package org.firstinspires.ftc.teamcode.Test;


import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

@TeleOp
public class DriveTest extends LinearOpMode {
    Bot bot;
    private GamepadEx gp1;
    private double driveSpeed = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);
        gp1.readButtons();
//
//        bot.bucket.stopIntake();
//        bot.linkage.retract();
//        bot.bucket.flipIn();


        waitForStart();

        while(opModeIsActive() & !isStopRequested()) {
//           bot.bucket.stopIntake();
//            bot.linkage.retract();
//            bot.bucket.flipIn();


            gp1.readButtons();
            drive();
        }
    }
    private void drive() {
        bot.prepMotors();
        driveSpeed = 0.7;
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






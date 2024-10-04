package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;


@TeleOp
public class MainTeleop extends LinearOpMode {
    Bot bot;
    private GamepadEx gp1;
    private GamepadEx gp2;
    private double driveSpeed = 1;
    private boolean isIntake = false;
    private boolean isOuttakePosition = false;
    private boolean isIncrementFourbar = true;
    private boolean intakeUp = false;

    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);
        gp2 = new GamepadEx(gamepad2);

        bot.resetEverything();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("TeleOp has started");
            gp1.readButtons();
            gp2.readButtons();

            drive();
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




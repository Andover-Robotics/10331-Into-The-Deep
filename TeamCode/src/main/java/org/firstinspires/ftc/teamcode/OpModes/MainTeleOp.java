package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

import java.lang.Math;

@TeleOp
public class MainTeleOp extends LinearOpMode {

    private GamepadEx gp1;

    Bot bot;

    @Override

    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("TeleOp has started");
            //drivetrain movement works
            drive();
        }




    }

    private void drive() {
        gp1.readButtons();

        double driveSpeed = 1;

        driveSpeed *= 1 - 0.9 * gp1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        driveSpeed = Math.max(0, driveSpeed);

        Vector2d driveVector = new Vector2d(gp1.getLeftX(), -gp1.getLeftY()),
                turnVector = new Vector2d(
                        gp1.getRightX(), 0);

        bot.driveRobotCentric(-driveVector.component1() * driveSpeed,
                driveVector.component2() * driveSpeed,
                turnVector.component1() * driveSpeed / 1.7
        );
    }
}

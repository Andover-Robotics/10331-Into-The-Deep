package org.firstinspires.ftc.teamcode.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

import java.util.ArrayList;
import java.util.List;

@TeleOp
public class TestLinkageAndBucket extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    private GamepadEx gp1;
    private double driveSpeed = 1;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
       // gp1 = new GamepadEx(gamepad1);
        TelemetryPacket packet = new TelemetryPacket();

        double pos=0;

        waitForStart();
        bot.bucket.flipIn();
        bot.linkage.extend();
        bot.bucket.flipOut();
        bot.wrist.intermediate();
        bot.claw.open();
        bot.wrist.storage();

        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                bot.bucket.intakeNoSense();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                bot.bucket.stopIntake();
            }

            //bucket testing: flip
            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.bucket.flipIn();
            } else if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                bot.bucket.flipOut();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                bot.linkage.retract();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.linkage.extend();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.bucket.reverseIntake();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                runningActions.add(bucketOuttakeAction());
            }

            //boilerplate for actions (?pls work)
            List<Action> newActions = new ArrayList<>();
            for (Action action : runningActions) {
                action.preview(packet.fieldOverlay());
                if (action.run(packet)) {
                    newActions.add(action);
                }
            }
            runningActions = newActions;

            dash.sendTelemetryPacket(packet);
        }

    }

    public SequentialAction bucketOuttakeAction() {
        return new SequentialAction(
                // new InstantAction(() -> wrist.bucketOuttakePos()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.wrist.intermediate()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.claw.open()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.storage()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.bucketOuttake())
        );
    }
//    private void drive() {
//        gp1.readButtons();
//        bot.prepMotors();
//        driveSpeed = 1;
//        driveSpeed *= 1 - 0.9 * gp1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
//        driveSpeed = Math.max(0, driveSpeed);
//
//        Vector2d driveVector = new Vector2d(gp1.getLeftX(), -gp1.getLeftY()),
//                turnVector = new Vector2d(
//                        gp1.getRightX(), 0);
//
//        bot.driveRobotCentric(driveVector.getX() * driveSpeed,
//                driveVector.getY() * driveSpeed,
//                turnVector.getX() * driveSpeed / 1.7
//        );
//    }

}




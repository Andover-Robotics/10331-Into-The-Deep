package org.firstinspires.ftc.teamcode;


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

import org.firstinspires.ftc.teamcode.subsystems.Bot;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

@TeleOp
public class MainTeleOp extends LinearOpMode {
    Bot bot;
    private GamepadEx gp1;
    private GamepadEx gp2;
    private double driveSpeed = 1;
    private boolean isIntaking = true;
    private boolean isClawOpen = true;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);
        gp2 = new GamepadEx(gamepad2);
        TelemetryPacket packet = new TelemetryPacket();

        waitForStart();
        bot.resetEverything();

        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addLine("TeleOp has started");
            gp1.readButtons();
            gp2.readButtons();

            drive();

            bot.slides.periodic();
            bot.slides.runSlides(-gp2.getRightY());


            //intake movements:
            if (gp2.wasJustPressed(GamepadKeys.Button.X)) {
                if(bot.state == Bot.BotState.INTAKE) {
                   // bot.arm.intakePos();
                    runningActions.add(bot.intakeAction());
                    bot.state = Bot.BotState.TRANSFER;
                } else if (bot.state == Bot.BotState.TRANSFER){
                    runningActions.add(bot.armToTransfer());
                    runningActions.add(bot.clawToTransfer());
                    bot.state = Bot.BotState.OUTTAKE;
                    //MAKE SURE both actions don't run at once
                }
                //if current state is intake: go to intake pos
                //if current state is transfer: go to transfer pos
            }

            //outtake:
            //bucket->
            if (gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                runningActions.add(bot.bucketOuttake(1));
                bot.state = Bot.BotState.INTAKE;
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                runningActions.add(bot.bucketOuttake(2));
                bot.state = Bot.BotState.INTAKE;
            }

            //spec->
            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                runningActions.add(bot.specOuttake());
                bot.state = Bot.BotState.INTAKE;
            }


            //linkage extend and retract - we can keep it on driver 1 for now
            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.linkage.extend();
            }

            if (gp1.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.linkage.retract();
            }

            //slides code goes here:
            //slides manual control:
            bot.slides.runSlides(-gp2.getRightY());

            //slides preset positions:
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                bot.slides.runToTopBucket();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                bot.slides.runToLowBucket();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                bot.slides.runToTopRung();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                bot.slides.runToLowRung();
            }
            if (gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.slides.runToStorage();
            }

            bot.slides.periodic();

            //run actions:
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

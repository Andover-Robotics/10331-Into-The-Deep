package org.firstinspires.ftc.teamcode.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Bot;

import java.util.ArrayList;
import java.util.List;


@TeleOp
public class BotOverview extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();


    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        TelemetryPacket packet = new TelemetryPacket();

        waitForStart();

        while(isStarted() && !isStopRequested()) {
            //INTAKE TO OUTTAKE ACTIONS (X->Y->B->A)
            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                runningActions.add(intakeAction());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                runningActions.add(confirmIntake());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                runningActions.add(clawOuttakeAction());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                runningActions.add(finalOuttake());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.BACK)) {
                runningActions.add(retryIntake());
            }

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
    public SequentialAction clawOuttakeAction() {
        return new SequentialAction(
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.5),
                new InstantAction(() -> bot.arm.openClaw()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.slidesIntermediate())
        );
    }

    public SequentialAction retryIntake() {
        return new SequentialAction(
                new InstantAction(() -> bot.linkage.retract()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.arm.intakePos()),
                new SleepAction(0.4),
                //INCREASE SLEEP ACTION TIME IF LAUNCHING OCCURS
                new InstantAction(() -> bot.arm.openClaw())
        );
    }

    public SequentialAction finalOuttake() {
        return new SequentialAction(
                new InstantAction(() -> bot.wrist.bucketOuttake()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.claw.open()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.slidesIntermediate())
        );
    }

    public SequentialAction intakeAction() {
        return new SequentialAction(
                new InstantAction(() -> bot.linkage.extend()),
                new SleepAction(0.1),
                new InstantAction(()->bot.arm.intakePos())
        );
    }

    public SequentialAction confirmIntake() {
        return new SequentialAction(
                new InstantAction(() -> bot.arm.closeClaw()),
                new SleepAction(0.8),
                new InstantAction(() -> bot.arm.transferPos()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.linkage.retract()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.wrist.intermediate()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.claw.open()),
                new SleepAction(0.5),
                new InstantAction(() -> bot.wrist.transfer())
        );
    }
}

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

@TeleOp(name = "Test Wrist + Claw")
public class TestWristAndClaw extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();
    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        TelemetryPacket packet = new TelemetryPacket();
       // packet.put("this is not null", "hello");
      //  double pos = 0.0;

        waitForStart();
        bot.wrist.wrist_r.setPosition(0.2);
        bot.wrist.wrist_l.setPosition(0.2);
        bot.claw.close();

        while(opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();

            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                bot.wrist.storage();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.claw.open();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                bot.claw.close();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.wrist.specOuttake();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
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

        //control flow: go to intermediate position (0.1), open claw, go to storage, close claw, then go to outtake pos.
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
}

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
public class TestIntakeAndLinkage extends LinearOpMode {
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

        //initial positions:
        bot.linkage.extend();
        bot.claw.close();
        bot.wrist.intermediate();
        bot.arm.rotateWrist(0);
        bot.arm.setPitch(0);
        bot.arm.closeClaw();

        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();
            //testing individual functions:
            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                bot.arm.intakePos();
            } else if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                bot.arm.transferPos();
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
                bot.linkage.retract();
            }
            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.linkage.extend();
            }

            //testing full actions:

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
               runningActions.add(intakeAction());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
              //  bot.bucket.reverseIntake();
                runningActions.add(confirmIntake());
              //  runningActions.add(clawOuttakeAction());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                runningActions.add(clawOuttakeAction());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                runningActions.add(clawToTransfer());
            }

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

    public SequentialAction clawOuttakeAction() {
        return new SequentialAction(
                new SleepAction(0.1),
                new InstantAction(() -> bot.wrist.intermediate()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.claw.open()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.transfer()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.arm.openClaw()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.bucketOuttake())
        );
    }

    public SequentialAction clawToTransfer() {
        return new SequentialAction(
                new InstantAction(() -> bot.claw.open()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.2)
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
                new SleepAction(0.2)
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

    //intake actions to pick up sample:
            /*
        intake action:
        - extend linkage
        - intake arm to pos parallel with the field while rotating arm 180 degrees
        - open intake claw

        intake action pt 2:
        - close intake claw
        - go to transfer position
        - retract linkage

        claw (transfer - outtake) action:
        - open claw
        - claw go to transfer pos
        - close claw
        - claw go to outtake pos
         */

}

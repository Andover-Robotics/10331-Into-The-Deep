package org.firstinspires.ftc.teamcode.Test;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
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
import java.util.ArrayList;
import java.util.List;

@Config
@TeleOp
public class MainTeleOp extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    private GamepadEx gp1;
    private double driveSpeed = 1;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();
    public double rotatePos = 0.525;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        gp1 = new GamepadEx(gamepad1);
        TelemetryPacket packet = new TelemetryPacket();

        waitForStart();

        //initial positions (ensure these are correct):
//        bot.claw.close();
//        bot.wrist.intermediate();
//        bot.linkage.retract();
//        bot.arm.transferPos();
        bot.resetEverything();

        while (opModeIsActive() && !isStopRequested()) {
            gp1.readButtons();
            gp2.readButtons();

            //slides periodic:
            bot.slides.periodic();

            //drive:
            drive();

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

            // EXTRA CONTROLS FOR INTAKE CUSTOMIZATION:
            //bot intake wrist control:
            if(gp1.wasJustPressed(GamepadKeys.Button.RIGHT_STICK_BUTTON)) {
                rotatePos = rotatePos+0.05 < 1 ? rotatePos+0.05 : rotatePos;
                bot.arm.rotateWrist(rotatePos);
            }

            if(gp1.wasJustPressed(GamepadKeys.Button.LEFT_STICK_BUTTON)) {
                rotatePos = rotatePos-0.05 > 0 ? rotatePos-0.05 : rotatePos;
                bot.arm.rotateWrist(rotatePos);
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.BACK)) {
                runningActions.add(retryIntake());
            }


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
            if (gp2.wasJustPressed(GamepadKeys.Button.START)) {
                bot.slides.runToStorage();
            }

            //slides manual control:
            bot.slides.runSlides(-gp2.getRightY());


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
                new SleepAction(0.5),
                new InstantAction(() -> bot.wrist.transfer()),
                new SleepAction(0.9),
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.5),
                new InstantAction(() -> bot.arm.openClaw()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.slidesIntermediate())
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
                new SleepAction(0.2)
        );
    }

    public SequentialAction resetLinkage() {
        return new SequentialAction(
                new InstantAction(() -> bot.linkage.extend()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.linkage.retract()),
                new SleepAction(0.2)
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
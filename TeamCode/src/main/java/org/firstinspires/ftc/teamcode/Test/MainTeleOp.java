package org.firstinspires.ftc.teamcode.Test;
import android.graphics.Color;

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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Bot;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@TeleOp
public class MainTeleOp extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;
    private GamepadEx gp1;
    private double driveSpeed = 0.5;

    double distance =10;

    ElapsedTime time = new ElapsedTime();
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();
    public double rotatePos = 0.525;
    public double pos = 0.5;
    public double addPosLinkage = 0;

    private DistanceSensor distanceSensor;

    private final float[] hsvValues = {0, 0, 0};
    private String color="nothing";
    double hue=0, saturation=0, value=0;
    boolean allianceBlue=false;
    private ColorSensor colorSensor;
    private DistanceSensor colorDistanceSensor;


    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        gp1 = new GamepadEx(gamepad1);
        initSensor(this);
        initColorSensor(this);
        TelemetryPacket packet = new TelemetryPacket();

        waitForStart();
        bot.resetEverything();

        while (opModeIsActive() && !isStopRequested()) {
            gp1.readButtons();
            gp2.readButtons();
            runColorSensor();

            if(gp1.wasJustPressed(GamepadKeys.Button.B)){
                allianceBlue=!allianceBlue;
            }



            //drive:
            drive();



            if(gp2.wasJustPressed(GamepadKeys.Button.LEFT_STICK_BUTTON)) {
                runningActions.add(intakeBack());
            }

            //INTAKE TO OUTTAKE ACTIONS (X->Y->B->A)
            if(gp2.wasJustPressed(GamepadKeys.Button.X)) {
               runningActions.add(intakeAction());
               bot.state = Bot.BotState.INTAKE;
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.state = Bot.BotState.TRANSFER;
                runningActions.add(confirmIntake());
                //note 2 self: if needed, adjust pitch of the outtake arm here
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.B)) {
                bot.state = Bot.BotState.OUTTAKE;
                runningActions.add(clawOuttakeAction());
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.A)) {
                runningActions.add(finalOuttake());
            }

            // EXTRA CONTROLS FOR INTAKE CUSTOMIZATION:
            //bot intake wrist control:
            if(gp2.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
                rotatePos = rotatePos+0.05 < 1 ? rotatePos+0.05 : rotatePos;
                bot.arm.rotateWrist(rotatePos);
                telemetry.addData("pressed right stick button", "");
            }

            if(gp2.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) {
                rotatePos = rotatePos-0.05 > 0 ? rotatePos-0.05 : rotatePos;
                bot.arm.rotateWrist(rotatePos);
                telemetry.addData("pressed left stick button", "");
            }

//            if(gp2.wasJustPressed(GamepadKeys.Button.LEFT_STICK_BUTTON)) {
//                pos = pos >= 0.05 ? pos-0.05 : 0;
//                bot.wrist.setPitch(pos);
//            }
//
//            if(gp2.wasJustPressed(GamepadKeys.Button.RIGHT_STICK_BUTTON)) {
//                pos = pos <= 0.95 ? pos+0.05 : 1;
//                bot.wrist.setPitch(pos);
//            }



//            if(gp1.wasJustPressed(GamepadKeys.Button.Y)) {
//                runningActions.add(closeClaw());
//            }

            if(gp2.wasJustPressed(GamepadKeys.Button.BACK)) {
                runningActions.add(retryIntake());
                bot.state = Bot.BotState.INTAKE;
            }
//
//            if((bot.state == Bot.BotState.INTAKE && allianceBlue) && (color.equalsIgnoreCase("blue") || color.equalsIgnoreCase("yellow"))) {
//                bot.arm.closeClaw();
//            }
//
//            if((bot.state == Bot.BotState.INTAKE && !allianceBlue) && (color.equalsIgnoreCase("red") || color.equalsIgnoreCase("yellow"))) {
//                bot.arm.closeClaw();
//            }

            if(gp1.wasJustPressed(GamepadKeys.Button.A)) {
                distance= distanceSensor.getDistance(DistanceUnit.CM);
                while(distance>6.5){
                    time.reset();
                    time.startTime();
                    bot.goBack(time);
                    distance= distanceSensor.getDistance(DistanceUnit.CM);
                    telemetry.addData("distance: ", distance);
                    telemetry.update();
                    dash.sendTelemetryPacket(packet);
                }
                bot.stopRobot();
            }

            if(gp1.wasJustPressed(GamepadKeys.Button.B)) {
                distance= distanceSensor.getDistance(DistanceUnit.CM);
                while(distance>6){
                    time.reset();
                    time.startTime();
                    bot.goBack(time);
                    distance= distanceSensor.getDistance(DistanceUnit.CM);
                    telemetry.addData("distance: ", distance);
                    telemetry.update();
                    dash.sendTelemetryPacket(packet);
                }
                bot.stopRobot();
            }
            if(gp1.wasJustPressed(GamepadKeys.Button.X)) {
                bot.wrist.wrist_l.setPosition(bot.wrist.wrist_l.getPosition()+0.05);
                bot.wrist.wrist_r.setPosition(bot.wrist.wrist_r.getPosition()+0.05);
            }

            if(gp1.wasJustPressed(GamepadKeys.Button.Y)) {
                bot.wrist.wrist_l.setPosition(bot.wrist.wrist_l.getPosition()-0.05);
                bot.wrist.wrist_r.setPosition(bot.wrist.wrist_r.getPosition()-0.05);
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
            telemetry.addData("slides setpoint: ", bot.slides.target);
            telemetry.addData("error ", bot.slides.controller.getPositionError());
            telemetry.update();
//            packet.addLine("is this working???");
            dash.sendTelemetryPacket(packet);

            //slides periodic:
            bot.slides.periodic();
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

    public SequentialAction finalOuttake() {
        return new SequentialAction(
                new InstantAction(() -> bot.wrist.bucketOuttake()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.claw.open()),
                new SleepAction(0.3)
        );
    }

    public SequentialAction intakeAction() {
        return new SequentialAction(
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.2),
                new InstantAction(() -> bot.wrist.slidesIntermediate()),
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

    public SequentialAction intakeBack() {
        return new SequentialAction(
                new InstantAction(() -> bot.arm.closeClaw()),
                new SleepAction(0.8),
                new InstantAction(() -> bot.arm.transferPos()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.linkage.retract()),
                new SleepAction(0.1)
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

    public SequentialAction openClawSpec() {
        return new SequentialAction(
                new SleepAction(0.1),
                new InstantAction(() -> bot.wrist.bucketOuttake()),
                new SleepAction(0.1),
                new InstantAction(() -> bot.claw.move(0.1)),
                new SleepAction(0.5)

        );
    }

    public SequentialAction closeClaw() {
        return new SequentialAction(
                new SleepAction(0.1),
                new InstantAction(() -> bot.claw.close()),
                new SleepAction(0.2)
        );
    }

    private void drive() {
        gp1.readButtons();
        bot.prepMotors();
        driveSpeed = 1;
        driveSpeed *= 1 - 0.9 * gp1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        driveSpeed = Math.max(0, driveSpeed) * 0.6;

        Vector2d driveVector = new Vector2d(gp1.getLeftX(), -gp1.getLeftY()),
                turnVector = new Vector2d(
                        gp1.getRightX(), 0);

        bot.driveRobotCentric(driveVector.getX() * driveSpeed,
                driveVector.getY() * driveSpeed,
                turnVector.getX() * driveSpeed / 1.7
        );
    }

    private void initSensor(OpMode opMode) {
        distanceSensor = opMode.hardwareMap.get(DistanceSensor.class, "distanceSensor");
    }

    private void initColorSensor(OpMode opMode) {
        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "color");
    }

    private void prepColorSensor() {
        Color.RGBToHSV((colorSensor.red()),
                (colorSensor.green()),
                (colorSensor.blue()),
                hsvValues);

        hue= hsvValues[0];
        saturation= hsvValues[1] * 255;
        value= hsvValues[2] * 255;
    }


    private void runColorSensor() {
        prepColorSensor();
        if((hue>85 && hue<100) && (saturation>135 && saturation<160)){
            color="yellow";
        }
        else if((hue>80 && hue<97) && (saturation>110 && saturation<135)){
            color= "red";
        }
        else if((hue>100 && hue<220) && (saturation>70 && saturation<256)){
            color = "blue";
        }
        else{
            color= "nothing";
        }

        telemetry.addData("Hue  ", hue);
        telemetry.addData("Saturation", saturation);
        telemetry.addData("Value ", value);
        telemetry.addData("color", color);
        telemetry.addData("alliance blue ", allianceBlue);
        telemetry.update();
    }
}
package org.firstinspires.ftc.teamcode.Test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@TeleOp(name = "Color Sense Test", group = "Test")
public class DistanceSenseTest extends LinearOpMode {

    Bot bot;
    private GamepadEx gp1;
    double distance =10;
    double wall= 7;

    boolean isMoving;
    private DistanceSensor distanceSensor;
    private FtcDashboard dash = FtcDashboard.getInstance();
    ElapsedTime time = new ElapsedTime();
    private List<Action> runningActions = new ArrayList<>();


    @Override
    public void runOpMode() throws InterruptedException {
        gp1 = new GamepadEx(gamepad1);
        gp1.readButtons();
        TelemetryPacket packet = new TelemetryPacket();
        initSensor(this);
        bot = Bot.getInstance(this);


        waitForStart();


        while (opModeIsActive() && !isStopRequested()) {
            gp1.readButtons();
            isMoving=false;

            if(gp1.wasJustPressed(GamepadKeys.Button.A)) {
                distance= distanceSensor.getDistance(DistanceUnit.CM);
                while(distance>wall){
                    time.reset();
                    time.startTime();
                    //runningActions.add(bot.goBack());
                    bot.goBack(time);
                    isMoving=true;
                    distance= distanceSensor.getDistance(DistanceUnit.CM);
                    telemetry.addData("distance: ", distance);
                    telemetry.addData("isMoving: ", isMoving);
                    telemetry.update();
                    dash.sendTelemetryPacket(packet);
                }
                bot.stopRobot();

            }
            isMoving=false;


            List<Action> newActions = new ArrayList<>();
            for (Action action : runningActions) {
                action.preview(packet.fieldOverlay());
                if (action.run(packet)) {
                    newActions.add(action);
                }
            }

            runningActions = newActions;
            telemetry.addData("distance: ", distance);
            telemetry.addData("isMoving: ", isMoving);
            telemetry.update();
            dash.sendTelemetryPacket(packet);


        }
    }


    private void initSensor(OpMode opMode) {
        distanceSensor = opMode.hardwareMap.get(DistanceSensor.class, "distanceSensor");
    }
}

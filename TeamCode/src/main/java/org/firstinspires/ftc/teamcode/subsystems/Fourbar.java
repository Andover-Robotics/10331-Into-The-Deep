package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Fourbar {
    public final Servo fourbar;
    private final Servo angleBoxServo;


    private static double outtakeBox=0.2, storageBox = 0.7, outtake = 0.7, storage = 0.1;

    public static boolean isOuttakePosition;

    public Fourbar(OpMode opMode) {
        fourbar = opMode.hardwareMap.servo.get("fourBarServo");
        angleBoxServo= opMode.hardwareMap.servo.get("boxAngleServo");
        fourbar.setDirection(Servo.Direction.FORWARD);
        isOuttakePosition= false;
    }


    public Action storage() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                angleBoxServo.setPosition(storageBox);
                fourbar.setPosition(storage);
                isOuttakePosition = false;
                return false;
            }
        };
    }

    public Action outtake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                fourbar.setPosition(outtake);
                angleBoxServo.setPosition(outtakeBox);
                isOuttakePosition = true;
                return false;
            }
        };
    }


}

package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class DiffyClaw {
    public final Servo diffy1;
    private final Servo diffy2;


    private static double outtakeBox=0.2, storageBox = 0.7, outtake = 0.7, storage = 0.1;

    public static boolean isOuttakePosition;

    public DiffyClaw(OpMode opMode) {
        diffy1 = opMode.hardwareMap.servo.get("leftServo");
        diffy2 = opMode.hardwareMap.servo.get("rightServo");
    }


    //for rotatating, both servos would rotate in the same direction. we need to figure out the directions by testing
    public void rotate(boolean isLeft, double pos) {
        if(isLeft) {
            diffy1.setDirection(Servo.Direction.FORWARD);
            diffy2.setDirection(Servo.Direction.FORWARD);
        } else {
            diffy1.setDirection(Servo.Direction.REVERSE);
            diffy2.setDirection(Servo.Direction.REVERSE);
        }

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }

    //both servos are moving in different directions to move the claw up or down. which direction each servo should be needs to be discovered
    //by tuning
    public void move(boolean isUp, double pos) {
        if(isUp) {
            diffy1.setDirection(Servo.Direction.REVERSE);
            diffy2.setDirection(Servo.Direction.FORWARD);
        } else {
            diffy1.setDirection(Servo.Direction.FORWARD);
            diffy2.setDirection(Servo.Direction.REVERSE);
        }

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }




}

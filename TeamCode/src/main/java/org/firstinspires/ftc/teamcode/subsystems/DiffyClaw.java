package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class DiffyClaw {
    public final Servo diffy1;
    private final Servo diffy2;

    private final Servo openCloseServo;

    public static final double transferClawPos = 0.7, outtakeClawPos = 0.7, diffyOpen = 0, diffyClose = 1;


    public DiffyClaw(OpMode opMode) {
        diffy1 = opMode.hardwareMap.servo.get("diffy1");
        diffy2 = opMode.hardwareMap.servo.get("diffy2");
        openCloseServo = opMode.hardwareMap.servo.get("openCloseDiffy");
    }

    public void rotate(double pos) {
        diffy1.setDirection(Servo.Direction.FORWARD);
        diffy2.setDirection(Servo.Direction.FORWARD);

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }

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

    public void openCloseDiffy(boolean open) {
        if(open) {
            openCloseServo.setPosition(diffyOpen);
        } else {
            openCloseServo.setPosition(diffyClose);
        }
    }

    public void transferPos() {
        openCloseDiffy(true);
        move(false, transferClawPos);
        openCloseDiffy(false);
    }

    public void outtakePos() {
        openCloseDiffy(false);
        move(false, outtakeClawPos);
    }


}

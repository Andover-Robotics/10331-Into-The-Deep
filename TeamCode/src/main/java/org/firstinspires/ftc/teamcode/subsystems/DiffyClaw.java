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
        diffy1 = opMode.hardwareMap.servo.get("fourBarServo");
        diffy2= opMode.hardwareMap.servo.get("boxAngleServo");
    }


}

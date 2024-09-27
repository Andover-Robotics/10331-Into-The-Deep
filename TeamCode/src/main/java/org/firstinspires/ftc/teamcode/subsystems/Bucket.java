package org.firstinspires.ftc.teamcode.subsystems;



import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;



public class Bucket {

    public final CRServo wheelServo1;
    public final CRServo wheelServo2;
    public final Servo flip;
    private final DigitalChannel colorSensor;


    public Bucket(OpMode opMode) {

        wheelServo1 = opMode.hardwareMap.crservo.get("wheel servo");
        wheelServo2 = opMode.hardwareMap.crservo.get("wheel servo");
        flip = opMode.hardwareMap.servo.get("flap servo");
        colorSensor = opMode.hardwareMap.get(DigitalChannel.class, "colorSensor");
        colorSensor.setMode(DigitalChannel.Mode.INPUT);
    }

}
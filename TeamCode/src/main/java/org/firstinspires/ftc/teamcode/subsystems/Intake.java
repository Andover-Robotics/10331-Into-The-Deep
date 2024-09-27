package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


public class Intake {

    public final Bucket bucket;
    public final Servo linkage1;
    public final Servo linkage2;
    private final OpMode opMode;
    private boolean isIntake;


    public Intake(OpMode opMode) {
        this.opMode = opMode;
        linkage1 = opMode.hardwareMap.get(Servo.class, "extensionLeft");
        linkage2 = opMode.hardwareMap.get(Servo.class, "extensionRight");
        bucket = new Bucket(opMode);
    }

}





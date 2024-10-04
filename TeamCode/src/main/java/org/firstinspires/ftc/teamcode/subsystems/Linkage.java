package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


public class Linkage {

    public final Bucket bucket;
    public final Servo linkage1;
    public final Servo linkage2;

    public static final double extendPos=1;
    public static final double retractPos=0;


    public Linkage(OpMode opMode) {
        linkage1 = opMode.hardwareMap.get(Servo.class, "extensionLeft");
        linkage2 = opMode.hardwareMap.get(Servo.class, "extensionRight");
        bucket = new Bucket(opMode);
    }

    public void extend(){
        linkage1.setPosition(extendPos);
        linkage2.setPosition(extendPos);
    }

    public void retract(){
        linkage1.setPosition(retractPos);
        linkage1.setPosition(retractPos);
    }

}





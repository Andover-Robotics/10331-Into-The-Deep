package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


public class IntakeSystem {

    public final Bucket bucket;
    public final Servo linkage1;
    public final Servo linkage2;

    private final double extendPos=1;
    private final double retractPos=0;


    public IntakeSystem(OpMode opMode) {
        linkage1 = opMode.hardwareMap.get(Servo.class, "extensionLeft");
        linkage2 = opMode.hardwareMap.get(Servo.class, "extensionRight");
        bucket = new Bucket(opMode);
    }

    public void intake(boolean isAllianceBlue){
        extend();
        bucket.intake(isAllianceBlue);
        retract();
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





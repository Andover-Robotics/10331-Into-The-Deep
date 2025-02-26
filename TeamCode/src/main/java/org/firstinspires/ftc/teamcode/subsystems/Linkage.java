package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


public class Linkage {

    //public final Bucket bucket;
    public final Servo linkage1;
    public final Servo linkage2;

    public static final double max_1 = 0.42;
    public static final double min_1 = 0.145;
    //original: 0.09
    //new orig: 0.11

    public static final double max_2 = 0.39;
    public static final double min_2 = 0.195;
    //original: 0.14
    //new orig: 0.16



    public Linkage(OpMode opMode) {
        linkage1 = opMode.hardwareMap.get(Servo.class, "right linkage");
        linkage2 = opMode.hardwareMap.get(Servo.class, "left linkage");
        linkage2.setDirection(Servo.Direction.REVERSE);
      //  bucket = new Bucket(opMode);
    }
    //linkage 1:
    //min = 0.09
    //max = 0.42

    //linkage2:
    //min = 0.17
    //max = 0.37

    public void extend(){
        linkage1.setPosition(max_1);
        linkage2.setPosition(max_2);
    }

    public void retract(){
        linkage1.setPosition(min_1);
        linkage2.setPosition(min_2);
    }


}








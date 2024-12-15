package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {

    public final Servo claw;

    public static final double openPos=0.55;
    public static final double graspPos=0.52;

    public static final double closePos=0.46;



    public Claw(OpMode opMode) {
        claw = opMode.hardwareMap.get(Servo.class, "claw");

    }

    public void open(){
        claw.setPosition(openPos);
    }
    public void close(){
        claw.setPosition(closePos);
    }

    public void grasp(){
        claw.setPosition(graspPos);
    }
    public void move(double pos){
        claw.setPosition(pos);
    }

}




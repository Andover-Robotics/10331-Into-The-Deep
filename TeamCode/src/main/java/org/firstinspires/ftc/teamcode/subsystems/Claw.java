package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;



public class Claw {

    public final Servo claw;
    public static final double openPos=0.85;
    public static final double graspPos=0.78;
    public static final double closePos=0.79;
    //Note: all values MUST BE CHANGED (receive from TeleOp)

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






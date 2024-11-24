package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Wrist {

    public  Servo diffy1;
    public Servo diffy2;

    private final double transferPos=0;
    private final double outtakePos = 0.7;



    //opposite directions -> rotates
    //same direction -> turns/moves
    //*** Only direction matters not position
    //while turning and rotating -> only one gear moves (which gear depends on direction of motion)
    public Wrist(OpMode opMode) {
        diffy1 = opMode.hardwareMap.servo.get("diffyRight");
        diffy2 = opMode.hardwareMap.servo.get("diffyLeft");
    }

    public void rotate(boolean clockwise, double pos) {
        if(clockwise) {
            diffy1.setDirection(Servo.Direction.FORWARD);
            diffy2.setDirection(Servo.Direction.REVERSE);
        }
        else{
            diffy1.setDirection(Servo.Direction.REVERSE);
            diffy2.setDirection(Servo.Direction.FORWARD);
        }

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }

    public void move(boolean goingUp, double pos) {
        if (goingUp) {
            diffy1.setDirection(Servo.Direction.FORWARD);
            diffy2.setDirection(Servo.Direction.FORWARD);
        } else {
            diffy1.setDirection(Servo.Direction.REVERSE);
            diffy2.setDirection(Servo.Direction.REVERSE);
        }
        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }
    public void moveTest(boolean goingUp, double pos) {
        if (goingUp) {
            diffy1.setDirection(Servo.Direction.FORWARD);
            diffy1.setPosition(pos);
        } else {
            diffy2.setDirection(Servo.Direction.REVERSE);
            diffy2.setPosition(pos);
        }
    }

    public void transferPos() {
        move(false, transferPos);
    }

    public void outtakePos(){
        move(true, outtakePos);
    }}

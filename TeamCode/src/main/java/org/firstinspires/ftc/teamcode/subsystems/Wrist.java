package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Wrist {

    public  Servo diffy1;
    private Servo diffy2;
    //decide through testing whether these servos need to be converted to CRservos.


    public static final double transferClawPos = 0.7, outtakeClawPos = 0.7, diffyOpen = 0, diffyClose = 1;
    //will need to tune these values

    public static boolean isOuttakePosition;

    public Wrist(OpMode opMode) {
        diffy1 = opMode.hardwareMap.servo.get("diffyRight");
        diffy2 = opMode.hardwareMap.servo.get("diffyLeft");
    }

    //for rotating, both servos would rotate in the same direction
    //direction of servo needs to be discovered by testing
    public void rotate(double pos) {
        diffy1.setDirection(Servo.Direction.FORWARD);
        diffy2.setDirection(Servo.Direction.FORWARD);

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }

    //both servos are moving in different directions to move the claw up or down
    //direction of servo needs to be discovered by testing
    public void move(boolean isUp, double pos) {
        if (isUp) {
            diffy1.setDirection(Servo.Direction.REVERSE);
            diffy2.setDirection(Servo.Direction.FORWARD);
        } else {
            diffy1.setDirection(Servo.Direction.FORWARD);
            diffy2.setDirection(Servo.Direction.REVERSE);
        }

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }


    public void transferPos() {
        move(false, transferClawPos);
        //check over this - I'm not entirely sure that just opening claw, bringing it to transfer claw pos, and then closing it is going to work.
    }

    public void outtakePos() {
        move(false, outtakeClawPos);
    }
}

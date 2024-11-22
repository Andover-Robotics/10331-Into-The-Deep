package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Wrist {

    public  Servo diffy1;
    private Servo diffy2;
    //decide through testing whether these servos need to be converted to CRservos.


    private final boolean isDiffyTransfer = false;
    private final boolean isDiffyPickup = false;
    private final boolean isDiffyRung = false;
    private final boolean isDiffyBucket = false;


    // tune values
    private final double TRANSFER_POS = 0;
    private final double PICKUP_POS = 0.2;
    private final double RUNG_POS = 0.5;
    private final double BUCKET_POS = 0.7;


    public Wrist(OpMode opMode) {
        diffy1 = opMode.hardwareMap.servo.get("diffyRight");
        diffy2 = opMode.hardwareMap.servo.get("diffyLeft");
    }

    //for rotating, both servos would rotate in the same direction

    public void rotate(double pos) {
        diffy1.setDirection(Servo.Direction.FORWARD);
        diffy2.setDirection(Servo.Direction.FORWARD);

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }

    //both servos are moving in different directions to move the claw up or down
    //direction of servo needs to be discovered by testing
    public void move(boolean goingUp, double pos) {
        if (goingUp) {
            diffy1.setDirection(Servo.Direction.REVERSE);
            diffy2.setDirection(Servo.Direction.FORWARD);
        } else {
            diffy1.setDirection(Servo.Direction.FORWARD);
            diffy2.setDirection(Servo.Direction.REVERSE);
        }

        diffy1.setPosition(pos);
        diffy2.setPosition(pos);
    }


    public void transfer() {
        move(true, TRANSFER_POS);
    }

    public void pickupPos() {
        move(true, PICKUP_POS);
    }

    public void rungPos() {
        move(false, RUNG_POS);
    }

    public void bucketPos() {
        move(false, BUCKET_POS);
    }
}

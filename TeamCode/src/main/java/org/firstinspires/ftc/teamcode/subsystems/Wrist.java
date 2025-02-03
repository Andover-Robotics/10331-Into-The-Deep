package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Wrist {
    public ServoEx wrist_l;
    public ServoEx wrist_r;

    public final double MIN_ANGLE = -100, MAX_ANGLE = 900;
    //public final double PITCH_MIN = -100; //0
    //public final double PITCH_MAX = 750; //180 // 700
    //public final double PITCH_MID = 90; //200
    public final double STORAGE_POS = 0.6; //test to find value
    public final double VERT_POS = 0.3; //test to find value, this should be like the pitch_min position
    public final double REST = 0.0; //also test
    public final double OFFSET = 0.0; //in case there is offset between the two servos and
                                      //we don't want to reset/reprogram the servos, we can use this;

    //private ElapsedTime time= new ElapsedTime();
    //just some notes:
    // right port 1 -
    // left port 3

    public Wrist(OpMode opMode) {
        wrist_l = new SimpleServo(opMode.hardwareMap, "wrist_l", MIN_ANGLE, MAX_ANGLE, AngleUnit.DEGREES);
        wrist_r = new SimpleServo(opMode.hardwareMap, "wrist_r", MIN_ANGLE, MAX_ANGLE, AngleUnit.DEGREES);
    }
    public void storage() {
        setPitch(STORAGE_POS);
    }

   public void reset() {
        setPitch(REST);
    }

    public void vertical() {
       setPitch(VERT_POS);
    }

    public void setPitch(double pos) {
        wrist_l.setPosition(pos);
        wrist_r.setPosition(pos-OFFSET);

    }

}



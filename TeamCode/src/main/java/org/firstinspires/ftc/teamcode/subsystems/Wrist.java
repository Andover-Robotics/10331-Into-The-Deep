package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Wrist {
    public Servo wrist_l;
    public Servo wrist_r;
    private double intermediate_transfer_pos = 0.3;
    private double spec_outtake_pos = 0.72;
    private double initial_outtake_pos = 0.67;
    private double transfer_pos = 0.05;

    public Wrist(OpMode opMode) {
        wrist_l = opMode.hardwareMap.servo.get("left wrist");
        wrist_r = opMode.hardwareMap.servo.get("right wrist");
      //  wrist_l.setDirection(Servo.Direction.REVERSE); orig
        wrist_r.setDirection(Servo.Direction.REVERSE);
    }
    public void storage() {
       // setPitch(STORAGE_POS);
        setPitch(transfer_pos);
    }

    public void setPitch(double pos) {
        wrist_l.setPosition(pos);
        wrist_r.setPosition(pos);
    }

    public void intermediate() {
        wrist_l.setPosition(intermediate_transfer_pos);
        wrist_l.setPosition(intermediate_transfer_pos);
    }

    public void bucketOuttake() {
        wrist_l.setPosition(initial_outtake_pos);
        wrist_r.setPosition(initial_outtake_pos);
    }

    public void specOuttake() {
        wrist_l.setPosition(spec_outtake_pos);
        wrist_r.setPosition(spec_outtake_pos);
    }

}



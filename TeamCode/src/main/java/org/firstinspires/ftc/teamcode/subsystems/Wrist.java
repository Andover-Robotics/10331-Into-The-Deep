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
    private double intermediate = 0.3;
    private double spec_outtake_pos = 0.72;
    private double bucketPos = 0.67;
    private double initial_outtake_pos = 0.6;
    private double transfer_pos = 0.04;
    //orignal 0.065

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
        setPitch(intermediate);
    }

    public void transfer() {
        setPitch(transfer_pos);
    }


    public void bucketOuttake() {
        setPitch(bucketPos);
    }
    public void slidesIntermediate() {
        setPitch(initial_outtake_pos);
    }

    public void specOuttake() {
        setPitch(spec_outtake_pos);
    }

    public void reset() {
        slidesIntermediate();
    }
}



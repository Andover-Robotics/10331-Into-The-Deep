package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Wrist {
    public ServoEx diffyLeft, diffyRight;


      /*

        1) (260,280) => TRANSFER
        2) (420,260)
        3)(480,160)
        4) (480,-140)
        5) (240,-140) => OUTTAKE


         */

    //these are in degrees
    public final double MIN_ANGLE = -100, MAX_ANGLE = 900;
  //  public final double ROLL_MID = 240;
    public final double ROLL_MIN = 0; //-100
    public final double ROLL_MAX = 480; //360
    public final double PITCH_MIN = -100; //0
    public final double PITCH_MAX = 750; //180 // 700
    public final double PITCH_MID = 90; //200

    private ElapsedTime time= new ElapsedTime();;

    public double pivotAngleDegrees = 0;
    public double currentPitch = 0, currentRoll = 0, pitchSetpoint = 0, rollSetpoint = 0;

    public Wrist(OpMode opMode) {
        diffyLeft= new SimpleServo(opMode.hardwareMap, "diffyRight", MIN_ANGLE, MAX_ANGLE, AngleUnit.DEGREES);
        diffyRight = new SimpleServo(opMode.hardwareMap, "diffyLeft", MIN_ANGLE, MAX_ANGLE, AngleUnit.DEGREES);
        diffyRight.setInverted(true);
    }
    public void storage() {

        setRoll(0);
        setPitch(20);
    /*    time.reset();
        while(time.seconds() < 1) {
            setRoll(0);
            setPitch(20);
        }
        time.reset();
        while(time.seconds() < 1) {
            setRoll(100);
            setPitch(480);
        }
        time.reset();
        while(time.seconds() < 1) {
            setRoll(320);
            setPitch(480);
        }
        time.reset();
        while(time.seconds() < 1) {
            setRoll(320);
            setPitch(-40);
        }
        setRoll(80);
        setPitch(-40);

     */
    }
    public void clip() {
     /*   time.reset();
        while(time.seconds() < 1) {
            setRoll(80);
            setPitch(-40);
        }
        time.reset();
        while(time.seconds() < 1) {
            setRoll(240);
            setPitch(160);
        }
        time.reset();
        while(time.seconds() < 1) {
            setRoll(240);
            setPitch(520);
        }
        setRoll(100);
        setPitch(520);

      */

        setRoll(0);
        setPitch(-100);
    }
   /* public void reset() {
        setRoll(80);
        setPitch(-40);
    }

    */
    public void vertical() {
        setRollPitch(ROLL_MIN, PITCH_MIN);
    }

    // set both roll and pitch at the same time
    public void setRollPitch(double roll, double pitch) {

        pitchSetpoint = pitch;
        rollSetpoint = roll;

        pitch = (pitch % 360) + PITCH_MID - pivotAngleDegrees + 10;
        pitch = pitch % 360;

        // Clamp roll and pitch to their respective ranges
        roll = Math.max(ROLL_MIN, Math.min(ROLL_MAX, roll));
        pitch = Math.max(PITCH_MIN, Math.min(PITCH_MAX, pitch));

        // Calculate servo angles for combined roll and pitch
        double leftAngle = pitch + (roll);
        double rightAngle = pitch + (ROLL_MAX - (roll));

        // Set servos to the calculated angles
        diffyLeft.turnToAngle(leftAngle);
        diffyRight.turnToAngle(rightAngle);

        // Update the current roll and pitch values
        currentRoll = roll;
        currentPitch = pitch;
    }

    public void setRoll(double roll) {
        rollSetpoint = roll;
        // Clamp roll to its range
        roll += 10;
        roll = Math.max(ROLL_MIN, Math.min(ROLL_MAX, roll));

        // Maintain the current pitch while setting roll
        double leftAngle = currentPitch + (roll);
        double rightAngle = currentPitch + (ROLL_MAX - (roll));

        // Set the servo angles for roll
        diffyLeft.turnToAngle(leftAngle);
        diffyRight.turnToAngle(rightAngle);

        // Update the current roll value
        currentRoll = roll;
    }

    public void setPitch(double pitch) {
        pitchSetpoint = pitch;
        pitch = pitch + PITCH_MID - pivotAngleDegrees;

        // Clamp pitch to its range
        pitch = Math.max(PITCH_MIN, Math.min(PITCH_MAX, pitch));

        // Maintain the current roll while setting pitch
        double leftAngle = pitch + (currentRoll);
        double rightAngle = pitch + (ROLL_MAX - (currentRoll));

        // Set the servo angles for pitch
        diffyLeft.turnToAngle(leftAngle);
        diffyRight.turnToAngle(rightAngle);

        // Update the current pitch value
        currentPitch = pitch;
    }

}

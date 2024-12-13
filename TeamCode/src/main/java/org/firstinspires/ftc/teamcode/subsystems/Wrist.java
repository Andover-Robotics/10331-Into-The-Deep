package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.arcrobotics.ftclib.hardware.ServoEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Wrist {
    public ServoEx diffyLeft, diffyRight;


    //these are in degrees
    public final double MIN_ANGLE = -100, MAX_ANGLE = 900;
    public final double ROLL_MID = 45;
    public final double ROLL_MIN = 0;
    public final double ROLL_MAX = 450;
    public final double PITCH_MIN = 0;
    public final double PITCH_MAX = 800;
    public final double PITCH_MID = 90;

  //  current val + mid = constant


    /*


**Min < pitch + Pitch Mid < Pitch Max

min-mid < setpoint < max-mid


//

     */

    public double pivotAngleDegrees = 0;
    public double currentPitch = 0, currentRoll = 0, pitchSetpoint = 0, rollSetpoint = 0;

    double pitchStorage, pitchRung, pitchBucket, pitchWallPickUp;
    double rollStorage, rollRung, rollBucket, rollWallPickUp, rollVertical=ROLL_MAX;

    public Wrist(OpMode opMode) {
        diffyLeft= new SimpleServo(opMode.hardwareMap, "diffyRight", MIN_ANGLE, MAX_ANGLE, AngleUnit.DEGREES);
        diffyRight = new SimpleServo(opMode.hardwareMap, "diffyLeft", MIN_ANGLE, MAX_ANGLE, AngleUnit.DEGREES);
        diffyRight.setInverted(true);
    }
    public void storage() {
        setRollPitch(rollStorage, pitchStorage);
    }
    public void wallPickUp() {
        setRollPitch(rollWallPickUp, pitchWallPickUp);
    }
    public void rung() {
        setRollPitch(rollRung, pitchRung);
    }
    public void bucket() {
        setRollPitch(rollBucket, pitchBucket);
    }

    public void vertical() {
        setRollPitch(rollVertical, PITCH_MIN- PITCH_MID);
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

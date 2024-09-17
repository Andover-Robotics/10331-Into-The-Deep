package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Bot {

    //for FSM
    public enum BotState {
        INTAKE,
        TRANSFER,
        DRIVING,
        OUTTAKE
    }
    public OpMode opMode;
    public BotState currentState = BotState.DRIVING;
    public static Bot instance;
    private final DcMotorEx FL, FR, BL, BR;
    public boolean fieldCentricRunMode;
    private double distanceFromBackdrop;
    private final double optimalDistanceFromBackdrop = 10;

    public static Bot getInstance() {
        if (instance == null) {
            throw new IllegalStateException("tried to getInstance of Bot when uninitialized");
        }
        return instance;
    }

    public static Bot getInstance(OpMode opMode) {
        if (instance == null) {
            return instance = new Bot(opMode);
        }
        instance.opMode = opMode;
        return instance;
    }

    private Bot(OpMode opMode) {
        this.opMode = opMode;

        try {
            fieldCentricRunMode = true;
        } catch (Exception e) {
            fieldCentricRunMode = false;
        }

        FL = opMode.hardwareMap.get(DcMotorEx.class, "fl");
        FR = opMode.hardwareMap.get(DcMotorEx.class, "fr");
        BL = opMode.hardwareMap.get(DcMotorEx.class, "bl");
        BR = opMode.hardwareMap.get(DcMotorEx.class, "br");

        FL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        FL.setMode(RUN_USING_ENCODER);
        FR.setMode(RUN_USING_ENCODER);
        BL.setMode(RUN_USING_ENCODER);
        BR.setMode(RUN_USING_ENCODER);

        //subsystems code goes here

    }

    public void driveRobotCentric(double strafeSpeed, double forwardBackSpeed, double turnSpeed) {
        double[] speeds = {
                forwardBackSpeed - strafeSpeed - turnSpeed,
                forwardBackSpeed + strafeSpeed + turnSpeed,
                forwardBackSpeed + strafeSpeed - turnSpeed,
                forwardBackSpeed - strafeSpeed + turnSpeed
        };
        double maxSpeed = 0;
        for (int i = 0; i < 4; i++) {
            maxSpeed = Math.max(maxSpeed, speeds[i]);
        }
        if (maxSpeed > 1) {
            for (int i = 0; i < 4; i++) {
                speeds[i] /= maxSpeed;
            }
        }
        FL.setPower(speeds[0]);
        FR.setPower(speeds[1]);
        BL.setPower(speeds[2]);
        BR.setPower(speeds[3]);

    }

    public void resetEncoders() {
        FL.setMode(STOP_AND_RESET_ENCODER);
        FR.setMode(STOP_AND_RESET_ENCODER);
        BR.setMode(STOP_AND_RESET_ENCODER);
        BL.setMode(STOP_AND_RESET_ENCODER);
    }

}

package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


public class Slides {

    private final MotorEx rightMotor;
    private final MotorEx leftMotor;
    private PIDFController controller;
    private MotionProfiler profiler = new MotionProfiler(30000, 20000);


    //needs to be tuned
    private final static double p = 0.015, i = 0 , d = 0, f = 0, staticF = 0.25;

    private final double tolerance = 20, powerUp = 0.1, powerDown = 0.05, manualDivide = 1, powerMin = 0.1;
    private double target = 0 ;
    public double power;
    private double manualPower=0;
    public boolean goingDown=false;

    private double profile_init_time = 0;
    private final OpMode opMode;

    public enum slidesPosition{
        GROUND,
        RUNG1,
        BUCKET1,
        RUNG2,
        BUCKET2,
        BUCKET3
    }
    private slidesPosition position = slidesPosition.GROUND;

    //tune
    // public static int storage =0, top = -2350, mid = -1000, low = -500;
    public static int storage = 0, topBucket = -2350, midBucket = -1000, lowBucket = -500, lowRung = 999, highRung= 777;
    public Slides(OpMode opmode) {
        rightMotor = new MotorEx(opmode.hardwareMap, "slidesRight", Motor.GoBILDA.RPM_312);
        leftMotor = new MotorEx(opmode.hardwareMap, "slidesCenter", Motor.GoBILDA.RPM_312);
        rightMotor.setInverted(true);
        leftMotor.setInverted(false);

        controller = new PIDFController(p, i, d, f);
        controller.setTolerance(tolerance);
        controller.setSetPoint(0);


        rightMotor.setRunMode(Motor.RunMode.RawPower);
        rightMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        leftMotor.setRunMode(Motor.RunMode.RawPower);
        leftMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        this.opMode = opmode;

    }

    public void runTo(double pos) {
        rightMotor.setRunMode(Motor.RunMode.RawPower);
        rightMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        leftMotor.setRunMode(Motor.RunMode.RawPower);
        leftMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        controller = new PIDFController(p, i, d, f);
        controller.setTolerance(tolerance);
        resetProfiler();
        profiler.init(leftMotor.getCurrentPosition(), pos);
        profile_init_time = opMode.time;

        goingDown = pos > target;
        target = pos;
    }


    public void runToTopBucket() {
        runTo(topBucket);
        position = slidesPosition.BUCKET3;
    }


    public void runToMidBucket() {
        runTo(midBucket);
        position = slidesPosition.BUCKET2;
    }


    public void runToLowBucket() {
        runTo(lowBucket);
        position = slidesPosition.BUCKET1;
    }

    public void runToTopRung() {
        runTo(highRung);
        position = slidesPosition.RUNG2;
    }

    public void runToLowRung() {
        runTo(lowRung);
        position = slidesPosition.RUNG1;
    }

    public void runToStorage() {
        position = slidesPosition.GROUND;
        runTo(storage);
    }


    public void runToManual(double manual) {
        if (manual > powerMin || manual < -powerMin) {
            manualPower = manual;
        } else {
            manualPower = 0;
        }
    }

    public int getCurrentPosition() {
        return leftMotor.getCurrentPosition();
    }

    public void resetEncoder() {
        rightMotor.resetEncoder();
        leftMotor.resetEncoder();
    }


    public void periodic() {

        //switch this??
        rightMotor.setInverted(false);
        leftMotor.setInverted(true);
        controller.setPIDF(p, i, d, f);
        double dt = opMode.time - profile_init_time;
        if (!profiler.isOver()) {

            controller.setSetPoint(profiler.profile_pos(dt));
            power = powerUp * controller.calculate(leftMotor.getCurrentPosition());
            if (goingDown) {
                power = powerDown * controller.calculate(leftMotor.getCurrentPosition());
            }
            rightMotor.set(power);
            leftMotor.set(power);


        } else {
            if (profiler.isDone()) {
                profiler = new MotionProfiler(30000, 20000);
                // leftMotor.set(0);
                // rightMotor.set(0);
            }

            if (manualPower != 0) {

                controller.setSetPoint(leftMotor.getCurrentPosition());
                rightMotor.set(manualPower / manualDivide);
                leftMotor.set(manualPower / manualDivide);

            } else {
                power = staticF * controller.calculate(leftMotor.getCurrentPosition());
                rightMotor.set(power);
                leftMotor.set(power);
            }
        }
    }

    public double getCurrent() {
        return leftMotor.motorEx.getCurrent(CurrentUnit.MILLIAMPS) + rightMotor.motorEx.getCurrent(CurrentUnit.MILLIAMPS);
    }

    public slidesPosition getState(){
        return position;
    }

    public void resetProfiler(){
        profiler = new MotionProfiler(30000, 20000);
    }

}

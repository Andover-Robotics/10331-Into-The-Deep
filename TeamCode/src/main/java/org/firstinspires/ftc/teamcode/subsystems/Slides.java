package org.firstinspires.ftc.teamcode.subsystems;


import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


public class Slides {

    public final MotorEx rightMotor;
    public final MotorEx leftMotor;
    private PIDFController controller;
    private MotionProfiler profiler = new MotionProfiler(30000, 20000);

    //** right slides getting caught when moving down


    //high chamber -> 26 inches (2020)
    //low chamber -> 13 inches
    //high basket -> 43 inches (3820)
    //low basket -> 25.75 inches  (1850)

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
        CHAMBER_LOW,
        BUCKET1,
        CHAMBER_HIGH,
        BUCKET2,
    }
    private slidesPosition position = slidesPosition.GROUND;

    //high chamber -> 26 inches (2020)
    //low chamber -> 13 inches
    //high basket -> 43 inches (3820)
    //low basket -> 25.75 inches  (1850)

    public static int storage = 0, topBucket = -3600, lowBucket = -1850, lowRung = -50, highRung= -2000;

    public static int test=-1200;
    public Slides(OpMode opmode) {
        rightMotor = new MotorEx(opmode.hardwareMap, "slidesRight", Motor.GoBILDA.RPM_312);
        leftMotor = new MotorEx(opmode.hardwareMap, "slidesLeft", Motor.GoBILDA.RPM_312);
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


        if (manualPower == 0) {
            resetProfiler();
            profiler.init(leftMotor.getCurrentPosition(), pos);
            profile_init_time = opMode.time;
        }
        goingDown = pos > target;
        target = pos;
    }

    public void runSlides(double power) {
        runToManual(power);
        periodic();
    }


    public void runToTopBucket() {
        runTo(topBucket);
        position = slidesPosition.BUCKET2;
    }

    public void runToLowBucket() {
        runTo(lowBucket);
        position = slidesPosition.BUCKET1;
    }
    public void runToTopRung() {
        runTo(highRung);
        position = slidesPosition.CHAMBER_HIGH;
    }
    public void runToLowRung() {
        runTo(lowRung);
        position = slidesPosition.CHAMBER_LOW;
    }
    public void runToStorage() {
        position = slidesPosition.GROUND;
        runTo(storage);
    }

    public void runToManual(double manual) {
   /*     if (Math.abs(manual) > powerMin) {
            manualPower = manual;
        } else {
            manualPower = 0;
        }

    */

        if (manual > powerMin || manual < -powerMin) {
            manualPower = -manual;
        } else {
            manualPower = 0;
        }
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

    public void resetEncoder() {
        rightMotor.resetEncoder();
        leftMotor.resetEncoder();
    }

    public void resetProfiler(){
        profiler = new MotionProfiler(30000, 20000);
    }

    public double getCurrent() {
        return leftMotor.motorEx.getCurrent(CurrentUnit.MILLIAMPS) + rightMotor.motorEx.getCurrent(CurrentUnit.MILLIAMPS);
    }
    public int getCurrentPosition() {
        return leftMotor.getCurrentPosition();
    }

    public slidesPosition getState(){
        return position;
    }

}





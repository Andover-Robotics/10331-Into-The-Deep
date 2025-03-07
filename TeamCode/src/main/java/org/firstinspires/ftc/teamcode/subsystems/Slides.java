package org.firstinspires.ftc.teamcode.subsystems;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
public class Slides {

    public final MotorEx rightMotor;
    public final MotorEx leftMotor;
    public PIDFController controller;
    private MotionProfiler profiler = new MotionProfiler(30000, 20000);
    public static double p = 0.015, i = 0, d = 0, f = 0, staticF = 0.25;
    private final double tolerance = 20, powerUp = 0.1, powerDown = 0.05, manualDivide = 1, powerMin = 0.1;
    private double manualPower = 0;

    //** right slides getting caught when moving down
    //high chamber -> 26 inches (2020)
    //low chamber -> 13 inches
    //high basket -> 43 inches (3820)
    //low basket -> 25.75 inches  (1850)

    private final OpMode opMode;
    private double target = 0;
    private boolean goingDown = false;
    private double profile_init_time = 0;

    public Slides(OpMode opMode) {
        leftMotor = new MotorEx(opMode.hardwareMap, "slidesLeft", Motor.GoBILDA.RPM_435);
        rightMotor = new MotorEx(opMode.hardwareMap, "slidesRight", Motor.GoBILDA.RPM_435);
        rightMotor.setInverted(true);
        leftMotor.setInverted(false);
        controller = new PIDFController(p, i, d, f);
        controller.setTolerance(tolerance);
        controller.setSetPoint(0);
        leftMotor.setRunMode(Motor.RunMode.RawPower);
        leftMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightMotor.setRunMode(Motor.RunMode.RawPower);
        rightMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.opMode = opMode;
    }

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

    public static int storage = 0, topBucket = -3600, lowBucket = -1375, lowRung = -50, highRung= -1300;

    public static int test=-1200;


    public void runTo(double pos) {
        rightMotor.setRunMode(Motor.RunMode.RawPower);
        rightMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        leftMotor.setRunMode(Motor.RunMode.RawPower);
        leftMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        controller = new PIDFController(p, i, d, f);
        controller.setTolerance(tolerance);


        if (manualPower == 0) {
            resetProfiler();
            profiler.init_new_profile(leftMotor.getCurrentPosition(), pos);
            profile_init_time = opMode.time;
        }
        goingDown = pos > target;
        target = pos;
    }

    public void runSlides(double power) {
        runToManual(power);
        periodic();
    }

    public void runToTest() {
        runTo(test);
        //periodic();
        // position = slidesPosition.BUCKET3;
    }

    public void runToTestPeriodic() {
        runTo(test);
        periodic();
        // position = slidesPosition.BUCKET3;
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
        if (manual > powerMin || manual < -powerMin) {
            manualPower = -manual;
        } else {
            manualPower = 0;
        }
    }

    public void periodic() {
        rightMotor.setInverted(false);
        leftMotor.setInverted(true);
        controller.setPIDF(p, i, d, f);
        double dt = opMode.time - profile_init_time;
        if (!profiler.isOver()) {
            controller.setSetPoint(profiler.motion_profile_pos(dt));
            double power = powerUp * controller.calculate(rightMotor.getCurrentPosition());
            if (goingDown) {
                power = powerDown * controller.calculate(rightMotor.getCurrentPosition());
            }
            leftMotor.set(power);
            rightMotor.set(power);
        } else {
            if (profiler.isDone()) {
                profiler = new MotionProfiler(30000, 20000);
            }
            if (manualPower != 0) {
                controller.setSetPoint(leftMotor.getCurrentPosition());
                leftMotor.set(manualPower / manualDivide);
                rightMotor.set(manualPower / manualDivide);
            } else {
                double power = staticF * controller.calculate(rightMotor.getCurrentPosition());
                leftMotor.set(power);
                rightMotor.set(power);
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






package org.firstinspires.ftc.teamcode.Subsystems;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


public class Noodles {

    public final DcMotorEx noodleMotor;
    public final CRServo counterRoller;
    public final Servo extendOne;
    public final Servo extendTwo;
    private final OpMode opMode;
    private boolean isIntake;


    public double extendOneStoragePosition=1.0, extendTwoStoragePosition=0.5,
            extendOneOuttakePosition=0.625, extendTwoOuttakePosition=0.9;


    public Noodles(OpMode opMode) {
        this.opMode = opMode;
        noodleMotor= opMode.hardwareMap.get(DcMotorEx.class, "noodles motor");
        counterRoller= opMode.hardwareMap.get(CRServo.class, "counter roller");
        extendOne= opMode.hardwareMap.get(Servo.class, "extensionLeft");
        extendTwo= opMode.hardwareMap.get(Servo.class, "extensionRight");
        noodleMotor.setDirection(DcMotorEx.Direction.REVERSE);
        isIntake = false;
    }

    public Action intake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                isIntake=true;
                packet.put("intakePos", isIntake);
                goToIntakePos();
                counterRoller.setDirection(CRServo.Direction.FORWARD);
                noodleMotor.setDirection(DcMotorEx.Direction.REVERSE);
                noodleMotor.setPower(1);
                counterRoller.setPower(1);

                //Code to run repeatedly while the method returns true
                return false;
            }
        };
    }

    public Action reverseIntake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                goToIntakePos();
                noodleMotor.setDirection(DcMotorEx.Direction.FORWARD);
                noodleMotor.setPower(0.5);
                isIntake=false;
                noodleMotor.setPower(0.5);
                counterRoller.setDirection(DcMotorEx.Direction.REVERSE);
                counterRoller.setPower(1);
                return false;
            }
        };
    }


    public Action stop() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                storage();
                isIntake= false;
                noodleMotor.setPower(0);
                counterRoller.setPower(0);
                return false;
            }
        };
    }


    public void storage(){
        extendOne.setPosition(extendOneStoragePosition);
        extendTwo.setPosition(extendTwoStoragePosition);
        isIntake= false;
        noodleMotor.setPower(0);
        counterRoller.setPower(0);
    }

    public void goToIntakePos(){
        extendOne.setPosition(extendOneOuttakePosition);
        extendTwo.setPosition(extendTwoOuttakePosition);
    }




}
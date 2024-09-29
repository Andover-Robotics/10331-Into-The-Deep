package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Slides {

    private DcMotorEx rightMotor;
    private DcMotorEx leftMotor;

    public Slides(OpMode opmode) {
        rightMotor = opmode.hardwareMap.get(DcMotorEx.class, "slides1");
        leftMotor= opmode.hardwareMap.get(DcMotorEx.class, "slides2");
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

}

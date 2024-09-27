package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Slides {

    private DcMotorEx slides1;
    private DcMotorEx slides2;

    public Slides(OpMode opmode) {
        slides1 = opmode.hardwareMap.get(DcMotorEx.class, "slides1");
        slides2= opmode.hardwareMap.get(DcMotorEx.class, "slides2");
        slides1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides1.setDirection(DcMotorSimple.Direction.FORWARD);
        slides2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

}

package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {

    private DcMotorEx slides;

    public Slides(OpMode opmode) {
        slides = opmode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public Action slideUp() {
        return new Action() {
            private boolean initialized = false;

            // actions are formatted via telemetry packets as below
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                // powers on motor, if it is not on
                if (!initialized) {
                    slides.setPower(0.8);
                    initialized = true;
                }

                // checks lift's current position
                double pos = slides.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos < 3000.0) {
                    // true causes the action to rerun
                    return true;
                } else {
                    // false stops action rerun
                    slides.setPower(0);
                    return false;
                }

            }
        };
    }

    public Action slideDown() {
        return new Action() {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    slides.setPower(-0.8);
                    initialized = true;
                }

                double pos = slides.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos > 100.0) {
                    return true;
                } else {
                    slides.setPower(0);
                    return false;
                }
            }
        };
    }
}

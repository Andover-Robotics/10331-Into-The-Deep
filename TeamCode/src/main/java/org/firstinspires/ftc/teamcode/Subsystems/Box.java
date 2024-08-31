package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;



public class Box {

    public final CRServo wheelServo;
    public final CRServo flapServo;

    //imaginary break beam sensor because why not
    private final DigitalChannel breakbeamSensor;
    private boolean boxFull;
    private int timesBroken;
    private TelemetryPacket packet;

    private ElapsedTime time;


    public Box(OpMode opMode) {

        wheelServo = opMode.hardwareMap.crservo.get("wheel servo");
        wheelServo.setDirection(DcMotorSimple.Direction.REVERSE);
        flapServo = opMode.hardwareMap.crservo.get("flap servo");
        breakbeamSensor = opMode.hardwareMap.get(DigitalChannel.class, "breakbeamSensor");
        breakbeamSensor.setMode(DigitalChannel.Mode.INPUT);
        time = new ElapsedTime();
    }

    public Action depositFirstPixel() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                time.reset();
                while(time.seconds() < 1.5) {
                    flapServo.setPower(1);
                }
                flapServo.setPower(0);
                return false;
            }
        };
    }

    public Action depositSecondPixel() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                time.reset();
                while(time.seconds() < 2) {
                    flapServo.setPower(1);
                    wheelServo.setPower(1);
                }
                flapServo.setPower(0);
                wheelServo.setPower(0);
                return false;
            }
        };
    }

    public Action loadPixels() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                wheelServo.setDirection(DcMotorSimple.Direction.REVERSE);
                while(!boxIsFull()){
                    wheelServo.setPower(1);
                }
                resetBox();
                return false;
            }
        };
    }


    public void resetBox(){
        timesBroken= 0;
        flapServo.setPower(0);
        wheelServo.setPower(0);
    }

    public boolean boxIsFull(){
        boolean isBeamBroken = breakbeamSensor.getState();
        if (isBeamBroken) {
            packet.put("Status", "Object detected!");
            timesBroken++;
        } else {
            packet.put("Status", "No object detected");
        }
        if(timesBroken ==2){
            boxFull=true;
        }
        return boxFull;
    }



}
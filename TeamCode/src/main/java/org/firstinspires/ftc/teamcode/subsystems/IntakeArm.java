package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class IntakeArm {
    public Servo wristRotationServo;
    public Servo clawOpenClose;
    public Servo wrist_l;
    public Servo wrist_r;
    private double clawOpenPos = 0.5;
    private double intermediatePitch = 0.4;
    private double intakeRotation = 0.65;
    private double transferArmPos = 0.4;

    public IntakeArm(OpMode opMode) {
        wristRotationServo = opMode.hardwareMap.servo.get("intake_wrist");
        clawOpenClose = opMode.hardwareMap.servo.get("intake_claw");
        wrist_l = opMode.hardwareMap.servo.get("flip_left");
        wrist_l.setDirection(Servo.Direction.REVERSE);
        wrist_r = opMode.hardwareMap.servo.get("flip_right");
    }

    public void rotateWrist(double pos) {
        wristRotationServo.setPosition(pos);
    }

    public void setPitch(double pos) {
        wrist_l.setPosition(pos);
        wrist_r.setPosition(pos);
    }

    public void openClaw() {
        clawOpenClose.setPosition(clawOpenPos);
    }

    public void setClawPos(double pos) {
        clawOpenClose.setPosition(pos);
    }

    public void closeClaw() {
        clawOpenClose.setPosition(0);
    }
    public void intakePos() {
        setPitch(0.87);
        rotateWrist(0.525);
        openClaw();
    }

    public void transferPos() {
        rotateWrist(0.525);
        setPitch(transferArmPos);
    }
    public void intermediatePos() {
        setPitch(intermediatePitch);
        rotateWrist(0.5);
        openClaw();
    }

    public void reset() {
        transferPos();
    }

}

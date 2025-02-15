package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Map;

@TeleOp(name = "Slides Tester Power", group = "Test")
public class TuneSlidesPower extends OpMode {
    private static int TP_DELTA = 5;

    private Selector motorSelector;
    private Selector motorSelector2;
    private InputColumnResponder input = new InputColumnResponderImpl();
    private DcMotor motor;
    private DcMotor motor2;

    double value;

    @Override
    public void init() {
        motorSelector = new Selector(hardwareMap.dcMotor.entrySet().stream().map(Map.Entry::getKey));
        input.register(() -> gamepad1.x, motorSelector::selectNext);
        motorSelector2 = new Selector(hardwareMap.dcMotor.entrySet().stream().map(Map.Entry::getKey));
        input.register(() -> gamepad1.y, motorSelector2::selectNext);
    }

    @Override
    public void init_loop() {
        input.update();
        telemetry.addData("Selected motor", motorSelector.selected());
        telemetry.addLine("Press X to select next motor1");
        telemetry.addData("Selected motor2", motorSelector2.selected());
        telemetry.addLine("Press Y to select next motor2");
    }

    @Override
    public void start() {
        motor = hardwareMap.dcMotor.get(motorSelector.selected());
        motor2 = hardwareMap.dcMotor.get(motorSelector2.selected());
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        input.clearRegistry();

        input.register(() -> gamepad1.dpad_up, () -> motor.setTargetPosition(motor.getTargetPosition() + TP_DELTA))
                .register(() -> gamepad1.dpad_down, () -> motor.setTargetPosition(motor.getTargetPosition() - TP_DELTA));

        input.register(() -> gamepad1.dpad_up, () -> motor2.setTargetPosition(motor2.getTargetPosition() + TP_DELTA))
                .register(() -> gamepad1.dpad_down, () -> motor2.setTargetPosition(motor2.getTargetPosition() - TP_DELTA));
    }

    @Override
    public void loop() {
        telemetry.addLine("Controls")
                .addData("Left stick Y", "Controls Power Value");

        telemetry.addLine("Motor Data")
                .addData("Type", motor.getMotorType().getName())
                .addData("Power", "%.3f", motor.getPower())
                .addData("Current Pos", "%d", motor.getCurrentPosition())
                .addData("Target Pos", "%d", motor.getTargetPosition())
                .addData("Busy?", motor.isBusy() ? "Yes" : "No");

        telemetry.addLine("Motor2 Data")
                .addData("Type", motor2.getMotorType().getName())
                .addData("Power", "%.3f", motor2.getPower())
                .addData("Current Pos", "%d", motor2.getCurrentPosition())
                .addData("Target Pos", "%d", motor2.getTargetPosition())
                .addData("Busy?", motor2.isBusy() ? "Yes" : "No");

        input.update();

        //its minus? Should i invert all slide motors for actual teleop?

        motor.setPower(-gamepad1.left_stick_y);
        motor2.setPower(-gamepad1.left_stick_y);
    }
}
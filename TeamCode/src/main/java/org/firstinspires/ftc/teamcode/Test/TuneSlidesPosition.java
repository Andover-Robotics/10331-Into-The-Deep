package org.firstinspires.ftc.teamcode.Test;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@TeleOp(name = "Slides Tester Position", group = "Test")
public class TuneSlidesPosition extends OpMode {

    private double POWER = 0.4, KP = 0.2, FEEDFORWARD = 0, TOLERANCE = 30;
    private int TARGET = 0;

    private Selector motorSelector, rpmSelector, motorSelector2, rpmSelector2;
    private InputColumnResponder input = new InputColumnResponderImpl();
    private MotorEx motor, motor2;

    @Override
    public void init() {
        motorSelector = new Selector(hardwareMap.dcMotor.entrySet().stream().map(Map.Entry::getKey));
        rpmSelector = new Selector(Arrays.stream(Motor.GoBILDA.values()).map(Enum::name));
        motorSelector2 = new Selector(hardwareMap.dcMotor.entrySet().stream().map(Map.Entry::getKey));
        rpmSelector2 = new Selector(Arrays.stream(Motor.GoBILDA.values()).map(Enum::name));
        input.register(() -> gamepad1.x, motorSelector::selectNext)
                .register(() -> gamepad1.a, rpmSelector::selectNext);
    }

    @Override
    public void init_loop() {
        input.update();

        telemetry.addData("Selected motor", motorSelector.selected());
        telemetry.addData("\nSelected rpm", rpmSelector.selected());
        telemetry.addLine("Press X to select next motor1");
        telemetry.addData("Selected motor2", motorSelector2.selected());
        telemetry.addData("\nSelected rpm", rpmSelector2.selected());
        telemetry.addLine("Press Y to select next motor2");


    }

    @Override
    public void start() {
        //right motor
        motor = new MotorEx(hardwareMap, motorSelector.selected(), Motor.GoBILDA.valueOf(rpmSelector.selected()));
        motor.setRunMode(Motor.RunMode.PositionControl);
        motor.setInverted(true);
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        motor.setPositionTolerance(TOLERANCE);
        motor.setPositionCoefficient(KP);

        //left motor
        motor2 = new MotorEx(hardwareMap, motorSelector2.selected(), Motor.GoBILDA.valueOf(rpmSelector2.selected()));
        motor2.setRunMode(Motor.RunMode.PositionControl);
        motor2.setInverted(false);
        motor2.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        motor2.setPositionTolerance(TOLERANCE);
        motor2.setPositionCoefficient(KP);

        TARGET = motor.getCurrentPosition();


        input.clearRegistry();

        input.register(() -> gamepad1.a, () -> FEEDFORWARD += 0.02)
                .register(() -> gamepad1.b, () -> FEEDFORWARD -= 0.02)
                .register(() -> gamepad1.dpad_up, () -> KP += 0.005)
                .register(() -> gamepad1.dpad_down, () -> KP -= 0.005)
                .register(() -> gamepad1.dpad_left, () -> POWER -= 0.02)
                .register(() -> gamepad1.dpad_right, () -> POWER += 0.02)
                .register(() -> gamepad1.y, () -> POWER += 0.005)
                .register(() -> gamepad1.x, () -> {motor.setTargetPosition(TARGET);});
    }


    @Override
    public void loop() {
        telemetry.addLine("Controls")
                .addData("\nLeft stick Y", "Controls target")
                .addData("\nRight stick Y", "Controls tolerance")
                .addData("\nA", "Increase feedforward")
                .addData("\nB", "Decrease feedforward")
                .addData("\nDpad Up", "Increases kp")
                .addData("\nDpad Down", "Decreases kp")
                .addData("\nDpad Right", "Increases power")
                .addData("\nDpad Left", "Decreases power")
                .addData("\nY", "fine tune power")
                .addData("\nX", "run the thing");

        TARGET -= (int)(gamepad1.left_stick_y * 20);
        TOLERANCE -= gamepad1.right_stick_y * 0.25;
        motor.setPositionTolerance(TOLERANCE);
        motor2.setPositionTolerance(TOLERANCE);

        motor.setPositionCoefficient(KP);
        motor2.setPositionCoefficient(KP);
        POWER = Math.max(0, Math.min(1, POWER));

        if(motor.atTargetPosition()){
            motor.set(FEEDFORWARD);
            motor2.set(FEEDFORWARD);
        }else{
            motor.set(POWER);
            motor2.set(POWER);
        }

        telemetry.addLine("\n\nMotor Data")
                .addData("\nName: ", motorSelector.selected())
                .addData("\nRPM: ", rpmSelector.selected())
                .addData("\nMotor Raw Power", "%.3f", motor.motor.getPower())
                .addData("\neditable========\nTarget Power", "%.3f", POWER)
                .addData("\nKP", "%.3f", KP)
                .addData("\nTolerance", "%.3f", TOLERANCE)
                .addData("\nFeedforward", "%.3f", FEEDFORWARD)
                .addData("\nCurrent Pos", "%d", motor.getCurrentPosition())
                .addData("\nTarget Pos", "%d", TARGET);

        input.update();

    }

    private <T extends Enum> Stream<String> enumOrdinals(T[] values) {
        return Stream.of(values)
                .map(Enum::name);
    }
}
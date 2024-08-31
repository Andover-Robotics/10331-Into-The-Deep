package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TestRRAutoV2 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        // instantiate your MecanumDrive
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, Math.toRadians(90)));

        // make a Lift instance
        SubsystemLift lift = new SubsystemLift(hardwareMap);

        Action trajectoryAction1 = drive.actionBuilder(drive.pose)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3)
                .build();

        Action trajectoryActionCloseOut = drive.actionBuilder(drive.pose)
                .strafeTo(new Vector2d(48, 12))
                .build();

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", 0);
            telemetry.update();
        }
        telemetry.addData("Starting Position", 0);
        telemetry.update();
        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        trajectoryAction1,
                        lift.liftUp(),
                        lift.liftDown(),
                        trajectoryActionCloseOut
                )
        );
    }
}

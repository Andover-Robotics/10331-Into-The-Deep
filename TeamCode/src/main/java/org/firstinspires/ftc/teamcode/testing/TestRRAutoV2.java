package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

public class TestRRAutoV2 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        // instantiate MecanumDrive
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, Math.toRadians(90)));

        Slides slide = new Slides(opMode);

        Action RedtrajectoryAction= drive.actionBuilder(drive.pose)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3)
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
                        RedtrajectoryAction,
                        slide.slideUp(),
                        slide.slideDown()
                )
        );
    }
}

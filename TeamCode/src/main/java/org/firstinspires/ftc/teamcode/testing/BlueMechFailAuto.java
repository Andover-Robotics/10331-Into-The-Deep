package org.firstinspires.ftc.teamcode.testing;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
        import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
//i forgot how to get arcrobotics imports to work

// Non-RR imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

/*
A simple mechanical failure auto for a bot on the blue alliance in the CenterStage 23-24 FTC challenge
Serves as proof of concept for RR 1.0
Didn't use any fancy splines... yet >:)
Score 11 points (if !dropped purple pixel) or 18 points (if dropped purple pixel)
someone pls tell me how to import from arcrobotics as it has completely slipped my mind
 */

@Config
@Autonomous(name = "BLUE_MECHANICAL_FAILURE", group = "Autonomous")
public class BlueMechFailAuto extends LinearOpMode{
    //linear op mode more like linear OPP MODE
    Slides slides = new Slides(hardwareMap);
    @Override
    public void runOpMode() {
        // instantiate your MecanumDrive at a particular pose.

        // dont worry about the errors, i forgot how to import from arcrobotics lib
        boolean dropPurplePixel = true;
        boolean blueClose = true;

        GamepadEx gp1 = new GamepadEx(gamepad1);
        gp1.readButtons();
        if (gp1.wasJustPressed(GamepadKeys.Button.B)) {
            telemetry.addLine("not dropping the purple pixel on spike mark");
            dropPurplePixel = false;
        } else if (gp1.wasJustPressed(GamepadKeys.Button.A)){
            telemetry.addLine("dropping the purple pixel on spike mark");
        }

        if (gp1.wasJustPressed(GamepadKeys.Button.X)) {
            telemetry.addLine("blue far");
            blueClose = false;
        } else if(gp1.wasJustPressed(GamepadKeys.Button.Y)) {
            telemetry.addLine("blue close");
        }

        // different start positions depending on alliance and distance from backdrop
        Pose2d startPoseBlueFar = new Pose2d(-36, 52, Math.toRadians(-90));
        Pose2d startPoseBlueClose = new Pose2d(11.8, 61.7, Math.toRadians(90));

        MecanumDrive drive;

        // new Pose2d(11.8, 61.7, Math.toRadians(90))
        //this is bad code :(
        if(blueClose) {
            drive = new MecanumDrive(hardwareMap, startPoseBlueClose);
        } else {
            drive = new MecanumDrive(hardwareMap, startPoseBlueFar);
        }

        Action spikeMarkAndBackdrop;
        Action onlyBackdrop;

        int posY = blueClose ? 48 : 36;

        spikeMarkAndBackdrop = drive.actionBuilder(drive.pose)
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(posY)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3)
                .build();

        onlyBackdrop = drive.actionBuilder(drive.pose)
                .lineToY(37)
                .setTangent(Math.toRadians(0))
                .lineToX(18)
                .setTangent(Math.toRadians(0))
                .lineToXSplineHeading(46, Math.toRadians(180))
                .waitSeconds(3)
                .build();

        waitForStart();

        if (isStopRequested()) return;

        Action trajectoryActionChosen;
        if (dropPurplePixel) {
            trajectoryActionChosen = spikeMarkAndBackdrop;
        } else {
            trajectoryActionChosen = onlyBackdrop;
        }

        Actions.runBlocking(
                new SequentialAction(
                        trajectoryActionChosen,
                        slides.liftUp(),
                        //claw.open()
                        slides.liftDown()
                )
        );
    }
}

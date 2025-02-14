package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

@Config
@Autonomous(name = "bucket+submersible+park", group = "Autonomous")
public class BucketSubmerPark extends LinearOpMode {

    //1. preloaded sample to bucket lv1
    //2. collect sample from submersible
    //3. sample to bucket
    //4. park
    private Bot bot;
    private GamepadEx gp1;
    enum Alliance {
        RED,
        BLUE
    }
    private Alliance alliance;

    @Override
    public void runOpMode() throws InterruptedException {
        Bot.instance = null;
        bot = Bot.getInstance(this);
        gp1 = new GamepadEx(gamepad1);
        telemetry.setAutoClear(true);
        Pose2d initialPoseRed = new Pose2d(-34, -53, Math.toRadians(90));
        Pose2d initialPoseBlue = new Pose2d(38, 56, Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPoseRed);
        Action red = drive.actionBuilder(drive.pose)
                //drive to bucket pos:
                .strafeToLinearHeading(new Vector2d(-52, -53), Math.toRadians(45))
                //outtake seq action:
                .afterTime(0.5, bot.bucketOuttakeAction(1))

                .turn(Math.toRadians(180)) //i think all these angles and positions need to be tested and changed lol
                .strafeToLinearHeading(new Vector2d(36,0), 0)
                .afterTime(0.5, bot.intakeAction())
                //park
                .strafeToLinearHeading(new Vector2d(56,-60), -Math.atan2(7,108))
                .build();
        Action blue = null;
        while(!isStarted() && !isStopRequested()) {
            gp1.readButtons();
        }
    }





}

package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

@Config
@Autonomous(name = "push close",group="Autonomous")

public class PushBotClose extends LinearOpMode{
    //only intake working
    //1. outtake in netzone
    //2. take from spike mark
    //3. outtake in netzone
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
                .strafeToLinearHeading(new Vector2d(-60,-60), Math.toRadians(45))
                .afterTime(0.1, bot.pushOuttakeAction())
                .strafeToLinearHeading(new Vector2d(-45,-48),Math.toRadians(-135))
                .afterTime(0.1, bot.intakeAction())
                .strafeToLinearHeading(new Vector2d(-60,-60),Math.toRadians(45))
                .afterTime(0.1, bot.pushOuttakeAction())
                .strafeToLinearHeading(new Vector2d(56,-60), -Math.atan2(7,108))
                .build();
        Action blue = null;
        while(!isStarted() && !isStopRequested()) {
            gp1.readButtons();
            if(gp1.wasJustPressed(GamepadKeys.Button.A)) {
                alliance = Alliance.RED;
                telemetry.addData("Alliance: ", "red");
            } else {
                alliance = Alliance.BLUE;
                drive = new MecanumDrive(hardwareMap, initialPoseBlue);
                blue = drive.actionBuilder(drive.pose)
                        .strafeToLinearHeading(new Vector2d(60,60),Math.toRadians(-45))
                        .afterTime(0.1, bot.pushOuttakeAction())
                        .strafeToLinearHeading(new Vector2d(36,-60),Math.toRadians(-135))
                        .afterTime(0.1, bot.intakeAction())
                        .strafeToLinearHeading(new Vector2d(60,60),Math.toRadians(-45))
                        .afterTime(0.1,bot.pushOuttakeAction())
                        .strafeToLinearHeading(new Vector2d(-52,60), Math.toRadians(180)-Math.atan2(8,108))
                        .build();
                telemetry.addData("Alliance: ", "blue");
                //change these values
            }
            telemetry.update();
        }
        if(alliance == Alliance.RED) {
            Actions.runBlocking(
                    new ActionHelper.RaceParallelCommand(
                            //idk if we need slides periodic to be running here but we probably do...
                            bot.slidesPeriodic(),
                            new SequentialAction(
                                    red
                            )
                    )
            );
        } else {
            Actions.runBlocking(
                    new ActionHelper.RaceParallelCommand(
                            //idk if we need slides periodic to be running here but we probably do...
                            bot.slidesPeriodic(),
                            new SequentialAction(
                                    blue
                            )
                    )
            );
        }
    }

}

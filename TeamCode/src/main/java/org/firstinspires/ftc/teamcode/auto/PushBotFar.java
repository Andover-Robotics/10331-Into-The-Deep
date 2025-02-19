package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Bot;

@Config
@Autonomous(name = "push far",group="Autonomous")

public class PushBotFar extends LinearOpMode{
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
        Pose2d initialPoseRed = new Pose2d(-53,12,Math.toRadians(90));
        Pose2d initalPoseBlue = new Pose2d(53,-24,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPoseRed);
        Action redClose = drive.actionBuilder(drive.pose)
                .strafeToLinearHeading(new Vector2d(56,-60), -Math.atan2(7,108))
                .build();
    }

}

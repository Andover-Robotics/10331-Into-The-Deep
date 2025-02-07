package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

//        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
//                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
//                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
//                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-52, -53, 0))
////                        .strafeToLinearHeading(new Vector2d(-10.5, 35), Math.toRadians(-90))
//                        .build());
//
        // Configure the bot
        RoadRunnerBotEntity myBot = new RoadRunnerBotEntity(
                meepMeep,
                new Pose2d(-10, -20, Math.toRadians(0)),
                60, 60, Math.toRadians(180), Math.toRadians(180), 15
        );

        // Define an action sequence using actionBuilder
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(-10, -20, Math.toRadians(0)))
                        .strafeToLinearHeading(new Vector2d(6, 33), Math.toRadians(-90))
                        .strafeToLinearHeading(new Vector2d(32, 35), Math.toRadians(-90))
                        .turnTo(Math.toRadians(-30))
                        .strafeToLinearHeading(new Vector2d(53, 53), Math.toRadians(45))
                        .build()
        );


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
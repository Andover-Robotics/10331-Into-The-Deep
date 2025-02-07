package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Vector;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity blueAlliance = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        blueAlliance.runAction(blueAlliance.getDrive().actionBuilder(new Pose2d(38, 56, Math.toRadians(-90)))
                .waitSeconds(3)
                .strafeToLinearHeading(new Vector2d(56,56), Math.toRadians(225))
                //I HATE TRIG cuz BFFR wtf is this angle :skull:
                .turn(Math.toRadians(-45))
                .strafeToLinearHeading(new Vector2d(-52,60), Math.toRadians(180)-Math.atan2(8,108))
                //I DONT UNDERSTAND THIS SUS MATH BRO
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(blueAlliance)
                .start();
    }
}
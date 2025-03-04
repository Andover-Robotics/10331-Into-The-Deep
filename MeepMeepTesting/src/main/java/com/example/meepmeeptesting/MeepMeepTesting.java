package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class MeepMeepTesting {
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity blueAlliance = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        RoadRunnerBotEntity redAlliance = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        Pose2d blueStartPos= new Pose2d(38, 60, Math.toRadians(0));
        Pose2d redStartPos= new Pose2d(38, -60, Math.toRadians(0));

     /*   //blue park
        blueAlliance.runAction(blueAlliance.getDrive().actionBuilder(blueStartPos)
                .waitSeconds(3)
                .lineToX(-50)
                .build());

        //red park
        redAlliance.runAction(redAlliance.getDrive().actionBuilder(redStartPos)
                .waitSeconds(3)
                .lineToX(-50)
                .build());

      */


        //blue specimen
        blueAlliance.runAction(blueAlliance.getDrive().actionBuilder(blueStartPos)
                .waitSeconds(3)
                .lineToY(40)
                .build());

        //red specimen
        redAlliance.runAction(redAlliance.getDrive().actionBuilder(redStartPos)
                .waitSeconds(3)
                .lineToX(-40)
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(blueAlliance)
                .addEntity(redAlliance)
                .start();
    }
}
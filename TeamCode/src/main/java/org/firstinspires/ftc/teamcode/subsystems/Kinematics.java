package org.firstinspires.ftc.teamcode.subsystems;

import java.lang.Math;
import org.ejml.simple.SimpleBase;

public class Kinematics {
    //all this stuff needs to be changed to be accurate; thinking of everything in cm and degrees
    private final double MAX_ANGLE = 75;
    private final double MIN_ANGLE = 30;
    private final double MAX_EXT = 60;
    private final double MIN_EXT = 15;
    private final double DEG_TO_TICKS = 3; //for slides rotation if its going to be a motor
    private final double LENGTH_TO_TICKS = 3; //for slides extension
    private final double DEG_TO_POS = 3; //change in servo position for rotation in deg of up and down motion of the claw


    //moves the claw to a given target state (x pos, y pos, and angle of claw)
    // returns position/ticks of the motor/servo

    /*
    public void move_to_target(Slides slides, Claw claw) {//maybe move this into main teleop?
        double current_ext = slides.getCurrentPosition();
        //double current_angle = slides.getAngle()
        double extension_length = calc_extension() + current_ext;
        double claw_rotation = calc_claw_angle();
        //double slides_angle =
        slides.runTo(extension_length);
        //slides.rotate(slides_angle) or smth
        //claw.move(claw_rotation);

    }
      */

    public double calc_extension() {//motor

        return 0;
    }

    public double  calc_slides_angle() { //motor

        return 0;
    }

    public double calc_claw_angle() { //servo

        return 0;
    }
}


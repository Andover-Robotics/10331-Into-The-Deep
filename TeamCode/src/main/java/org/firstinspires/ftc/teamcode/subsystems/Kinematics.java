//package org.firstinspires.ftc.teamcode.subsystems;
//
//import java.lang.Math;
//import org.ejml.simple.SimpleBase;
//
//public class Kinematics {
//    //all this stuff needs to be changed to be accurate; thinking of everything in cm and degrees
//    private final double MAX_ANGLE = 75;
//    private final double MIN_ANGLE = 30;
//    private final double MAX_EXT = 60;
//    private final double MIN_EXT = 15;
//    private final double DEG_TO_TICKS = 3; //for slides rotation if its going to be a motor
//    private final double LENGTH_TO_TICKS = 3; //for slides extension
//    private final double DEG_TO_POS = 3; //change in servo position for rotation in deg of up and down motion of the claw
//    private final double HEIGHT_CONST = 1;
//    private final double DIFFY_LENGTH = 0.5;
//    private final double INTAKE_DIFFY_ANGLE = 30;
//    private final double OUTTAKE_DIFFY_ANGLE = -30;
//    private final double SLIDES_ANGLE = 45;
//
//
//    // moves the claw to a given target state (x pos, y pos, and angle of claw)
//    // returns position/ticks of the servo/motor
///*    public double calc_extension(double target_height, double target_angle){
//        double length_change = (target_height - HEIGHT_CONST - DIFFY_LENGTH * Math.sin(target_angle) / Math.sin(SLIDES_ANGLE);
//        return length_change * LENGTH_TO_TICKS;
//    }
//
// */
//
//}
//

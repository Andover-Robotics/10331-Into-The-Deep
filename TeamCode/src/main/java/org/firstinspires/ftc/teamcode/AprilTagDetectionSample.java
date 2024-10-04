package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagPose;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
import java.util.ArrayList;

public class AprilTagDetectionSample extends LinearOpMode {
    // just reuse this code in an actual auto or teleop class, don't use a separate opMode for this

    //
    OpenCvWebcam webcam;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    GamepadEx gp1;
    double fx = 1078.03779;
    double fy = 1084.50988;
    double cx = 580.850545;
    double cy = 245.959325;
    //need to edit these values?

/*
    int OBSERVATION_BLUE = 11;
    int CENTER_BLUE = 12;
    int BUCKET_BLUE = 13;
    int OBSERVATION_RED = 14;
    int CENTER_RED = 15;
    int BUCKET_RED = 16;

 */



    AprilTagDetection bucket_b = null;
    AprilTagDetection obs_b = null;
    AprilTagDetection cntr_b = null;
    AprilTagDetection bucket_r = null;
    AprilTagDetection obs_r = null;
    AprilTagDetection cntr_r = null;

    static final double FEET_PER_METER = 3.28084;
    @Override
    public void runOpMode() throws InterruptedException {
        gp1 = new GamepadEx(gamepad1);

        /*
         * Instantiate an OpenCvCamera object for the webcam
         * CameraMonitorViewId is so that the livestream of camera output is displayed on the DS screen
         */
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        webcam = OpenCvCameraFactory.getInstance()
                .createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        //set to detect april tags
        webcam.setPipeline(new AprilTagDetectionPipeline(0.1016, fx, fy, cx, cy));

        // Timeout for obtaining permission is configurable. Set before opening
        webcam.setMillisecondsPermissionTimeout(5000);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addLine("camera init failed");
            }
        });

        telemetry.addLine("Waiting for start");
        telemetry.update();

        /*
         * Wait for the user to press start on the Driver Station
         */
        waitForStart();

        while (!isStarted() && !isStopRequested()) {

            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(!currentDetections.isEmpty()) { //currently just continuously sensing for the apriltags at the 6 locations and printing out data
                for (AprilTagDetection tag : currentDetections) {
                    switch (tag.id) {
                        case 11: // next to blue observation zone
                            obs_b = tag;
                            telemetry.addLine("Observation zone (blue side) is in sight :D \n\nLocation data:");
                            tagToTelemetry(obs_b);
                            telemetry.update();
                            break;
                        case 12: // in the center in front of the drivers of the blue side
                            cntr_b = tag;
                            telemetry.addLine("Center (blue side) is in sight :D \n\nLocation data:");
                            tagToTelemetry(cntr_b);
                            telemetry.update();
                            break;
                        case 13: // next to buckets on the blue side
                            bucket_b = tag;
                            telemetry.addLine("Bucket (blue side) is in sight :D\n\nLocation data:");
                            tagToTelemetry(bucket_b);
                            telemetry.update();
                            break;
                        case 14: // next to red observation zone
                            obs_r = tag;
                            telemetry.addLine("Observation zone (red side) is in sight :D \n\nLocation data:");
                            tagToTelemetry(obs_r);
                            telemetry.update();
                            break;
                        case 15: // in the center of the wall in front of the drivers on the red side
                            cntr_r = tag;
                            telemetry.addLine("Center (red side) is in sight :D \n\nLocation data:");
                            tagToTelemetry(cntr_r);
                            telemetry.update();
                            break;
                        case 16: // next to the buckets on the red side
                            bucket_r = tag;
                            telemetry.addLine("Bucket (red side) is in sight :D \n\nLocation data:");
                            tagToTelemetry(bucket_r);
                            telemetry.update();
                            break;
                        default:
                            telemetry.addLine("Nothing useful detected D:");
                            telemetry.update();
                            break;
                    }
                }
            } else {
                telemetry.addLine("Nothing detected D:");
                telemetry.update();
            }
            sleep(20);
        }


/*
        while (opModeIsActive()) {
            sleep(20);
            if(gp1.wasJustPressed(GamepadKeys.Button.START)) {
                webcam.stopStreaming();
            }*/




            /*
             * The START command just came in: now work off the latest snapshot acquired
             * during the init loop.
             */

            /* Update the telemetry */
            /*
            if(tagOfInterest != null) {
                telemetry.addLine("Tag snapshot:\n");
                tagToTelemetry(tagOfInterest);
                telemetry.update();
            }
            else {
                telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
                telemetry.update();
            }
            */


            /* Actually do something useful */
            /*
            if(tagOfInterest == null) {
                /*
                 * Insert your autonomous code here, presumably running some default configuration
                 * since the tag was never sighted during INIT

            }
            else {
                //use this logic during teleop for localization
                // + create a method to calculate the robot distance from the april tag (need to do math :sob: )

                //units here are inches ->
                if(tagOfInterest.pose.x <= 20) {
                    // do something

                }
                else if(tagOfInterest.pose.x >= 20 && tagOfInterest.pose.x <= 50) {
                    // do something else
                }
                else if(tagOfInterest.pose.x >= 50) {
                    // do something else
                }
                */
            //}
        //}
    }

    void tagToTelemetry(AprilTagDetection detection) {
        Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);

        telemetry.addData("Detected tag ID", detection.id);
        telemetry.addData("Translation X: ", detection.pose.x*FEET_PER_METER);
        telemetry.addData("Translation Y: ", detection.pose.y * FEET_PER_METER);
        telemetry.addData("Translation Z: ", detection.pose.z * FEET_PER_METER);
        telemetry.addData("Rotation Yaw: ", rot.firstAngle);
        telemetry.addData("Rotation Pitch, ", rot.secondAngle);
        telemetry.addData("Rotation Roll: ", rot.thirdAngle);
    }
}
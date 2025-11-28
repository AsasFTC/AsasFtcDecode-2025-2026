package org.firstinspires.ftc.teamcode.mechanisms;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

public class WebCam {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;


    public void init(HardwareMap hwmap){

        WebcamName webcamName = hwmap.get(WebcamName.class, "Webcam 1");

        // Inicializa o detector de AprilTags
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();

        // Inicializa o VisionPortal com a C920
        visionPortal = new VisionPortal.Builder()
                .setCamera(webcamName)
                .addProcessor(aprilTag)
                .build();
    }
    public double getTagId(){
        List<AprilTagDetection> detections = aprilTag.getDetections();

        for (AprilTagDetection tag : detections){
            return tag.id;
        }
        return Double.NaN;
    }
    public double getTagDistanceCentimeters(){
        List<AprilTagDetection> detections = aprilTag.getDetections();

        if (!detections.isEmpty()) {
            for (AprilTagDetection tag : detections) {
                return tag.ftcPose.range * 2.54;

            }
        } else {
            return Double.NaN;
        }
        return Double.NaN;
    }
    public double getYaw(){
        List<AprilTagDetection> detections = aprilTag.getDetections();

        for(AprilTagDetection tag : detections){
            return tag.ftcPose.yaw;
        }
        return Double.NaN;
    }

    public int getDetectionsNumber(){
        List<AprilTagDetection> detections = aprilTag.getDetections();
        return detections.size();
    }
}
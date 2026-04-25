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
    private boolean cameraDisponivel = false;

    public void init(HardwareMap hwmap){
        try {
            WebcamName webcamName = hwmap.get(WebcamName.class, "Webcam 1");

            aprilTag = new AprilTagProcessor.Builder()
                    .setDrawTagOutline(true)
                    .setDrawAxes(true)
                    .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                    .build();

            visionPortal = new VisionPortal.Builder()
                    .setCamera(webcamName)
                    .addProcessor(aprilTag)
                    .build();

            cameraDisponivel = true;

        } catch (Exception e) {
            cameraDisponivel = false;
            visionPortal = null;
            aprilTag = null;
        }
    }
    public double getTagId(){
        if (!cameraDisponivel || aprilTag == null) {
            return Double.NaN;
        }

        List<AprilTagDetection> detections = aprilTag.getDetections();

        for (AprilTagDetection tag : detections){
            return tag.id;
        }
        return Double.NaN;
    }
    public double getTagDistanceCentimeters(){
        if (!cameraDisponivel || aprilTag == null) {
            return Double.NaN;
        }
        List<AprilTagDetection> detections = aprilTag.getDetections();

        if (!detections.isEmpty()) {
            for (AprilTagDetection tag : detections) {
                if(tag != null) {
                    return tag.ftcPose.range * 2.54;
                }
            }
        } else {
            return Double.NaN;
        }
        return Double.NaN;
    }
    public double getYaw(){
        if (!cameraDisponivel || aprilTag == null) {
            return Double.NaN;
        }
        List<AprilTagDetection> detections = aprilTag.getDetections();

        for(AprilTagDetection tag : detections){
            return tag.ftcPose.yaw;
        }
        return Double.NaN;
    }

    public int getDetectionsNumber(){
        if (!cameraDisponivel || aprilTag == null) {
            return 0;
        }

        List<AprilTagDetection> detections = aprilTag.getDetections();
        return detections.size();
    }
    public double getAimAngle(){
        if (!cameraDisponivel || aprilTag == null) {
            return Double.NaN;
        }
        List<AprilTagDetection> detections = aprilTag.getDetections();

        if (!detections.isEmpty()) {
            for (AprilTagDetection tag : detections) {
                if(tag != null) {
                    return Math.atan2(tag.ftcPose.x, tag.ftcPose.z);
                }
            }
        } else {
            return Double.NaN;
        }
        return Double.NaN;
    }

}
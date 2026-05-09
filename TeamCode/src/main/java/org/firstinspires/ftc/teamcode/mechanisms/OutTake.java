package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.ArrayList;
import java.util.List;

public class OutTake {

    DcMotorEx flywheel, flywheel2;


    public void init(HardwareMap hwmap){
        flywheel = (DcMotorEx) hwmap.get(DcMotor.class, "flywheel");
        flywheel2 = (DcMotorEx) hwmap.get(DcMotor.class, "flywheel2");

        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public String getCurrent(){
        List<Double> info = new ArrayList<>();
        info.add(flywheel.getCurrent(CurrentUnit.AMPS));
        info.add(flywheel2.getCurrent(CurrentUnit.AMPS));
        return info.toString();
    }
    public String getPower(){
        List<Double> info = new ArrayList<>();
        info.add(flywheel.getPower());
        info.add(flywheel2.getPower());
        return info.toString();
    }
    public String getVelocity(){
        List<Double> info = new ArrayList<>();
        info.add(flywheel.getVelocity());
        info.add(flywheel2.getVelocity());
        return info.toString();
    }

    public void setPower(double power){
        flywheel.setPower(power);
        flywheel2.setPower(power);
    }
    public void setVelocity(int velocity){
        flywheel.setVelocity(velocity);
        flywheel2.setVelocity(velocity);
    }

    public void turnOff(){
        flywheel.setPower(0);
        flywheel2.setPower(0);
    }

}

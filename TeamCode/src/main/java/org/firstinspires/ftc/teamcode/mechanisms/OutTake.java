package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class OutTake {

    DcMotorEx flywheel;


    public void init(HardwareMap hwmap){
        flywheel = (DcMotorEx) hwmap.get(DcMotor.class, "flywheel");

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getCurrent(){
        return flywheel.getCurrent(CurrentUnit.AMPS);
    }
    public double getPower(){
        return flywheel.getPower();
    }
    public double getVelocity(){
        return flywheel.getVelocity();
    }

    public void dpadControl(boolean currentDpadUp, boolean currentDpadDown, boolean lastDpadUp, boolean lastDpadDown){
        double flywheelPower = 0;
        if (currentDpadUp && !lastDpadUp){
            flywheelPower += 0.01;
        }
        lastDpadUp = currentDpadUp;

        if (currentDpadDown && !lastDpadDown){
            flywheelPower -= 0.01;
        }
        lastDpadDown = currentDpadDown;
        flywheel.setPower(flywheelPower);
    }

    public void turnOff(){
        flywheel.setPower(0);
    }

    public void setDefaultPower(){
        flywheel.setPower(0.6);
    }



}

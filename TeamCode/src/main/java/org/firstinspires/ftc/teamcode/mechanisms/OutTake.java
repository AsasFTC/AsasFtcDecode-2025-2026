package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.Arrays;

public class OutTake {

    DcMotorEx flywheel;
    DcMotorEx flywheel_2;


    public void init(HardwareMap hwmap){
        flywheel = (DcMotorEx) hwmap.get(DcMotor.class, "flywheel");
        flywheel_2 = (DcMotorEx) hwmap.get(DcMotor.class, "flywheel_2");

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel_2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel_2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public String getCurrent(){
        double[] valores = {flywheel.getCurrent(CurrentUnit.AMPS), flywheel_2.getCurrent(CurrentUnit.AMPS)};
        return Arrays.toString(valores);
    }
    public String getPower(){
        double[] valores = {flywheel.getPower(), flywheel_2.getPower()};
        return Arrays.toString(valores) ;
    }
    public String getVelocity(){
        double[] valores = {flywheel.getVelocity(), flywheel_2.getVelocity()};
        return Arrays.toString(valores);
    }

    public void setPower(double power){
        flywheel.setPower(power);
        flywheel_2.setPower(power);
    }
    public void setVelocity(int velocity){
        flywheel.setVelocity(velocity);
        flywheel_2.setVelocity(velocity);
    }

    public void turnOff(){
        flywheel.setPower(0);
        flywheel_2.setPower(0);
    }

    public void setDefaultPower(){
        flywheel.setPower(0.8);
        flywheel_2.setPower(0.8);
    }
}

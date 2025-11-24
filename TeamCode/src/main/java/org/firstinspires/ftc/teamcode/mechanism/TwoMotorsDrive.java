package org.firstinspires.ftc.teamcode.mechanism;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.ArrayList;
import java.util.List;

public class TwoMotorsDrive {
    DcMotorEx motorLeft;
    DcMotorEx motorRight;
    HardwareMap hwmap;
    public TwoMotorsDrive(HardwareMap hwmap, DcMotorEx mleft, DcMotorEx mright ){
        this.motorLeft = mleft;
        this.motorRight = mright;
        this.hwmap = hwmap;
    }

    public void init(){
        motorLeft = hwmap.get(DcMotorEx.class, "leftDrive");
        motorRight = hwmap.get(DcMotorEx.class, "rightDrive");

        motorRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPowers(double leftPower, double rightPower){
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
    }

    public void stopMotors(){
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    public void arcadeDrive(double forward, double right){
        motorLeft.setPower(forward + right);
        motorRight.setPower(forward - right);
    }

    public void setVelocities(double leftVel, double rightVel){
        motorLeft.setVelocity(leftVel);
        motorRight.setVelocity(rightVel);
    }

    public String getMotorsCurrents(){
        List<Double> currents = new ArrayList<>();
        currents.add(motorLeft.getCurrent(CurrentUnit.AMPS));
        currents.add(motorRight.getCurrent(CurrentUnit.AMPS));
        return currents.toString();
    }


}

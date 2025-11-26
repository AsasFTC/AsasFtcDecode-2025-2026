package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

    public void init(HardwareMap hwmap){
        motorLeft = (DcMotorEx) hwmap.get(DcMotor.class, "leftDrive");
        motorRight = (DcMotorEx) hwmap.get(DcMotor.class, "rightDrive");

        motorLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPowers(double lPower, double rPower){
        motorLeft.setPower(lPower);
        motorRight.setPower(rPower);
    }
    public void stopMotors(){
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
    public String getCurrents(){
        List<Double> currents = new ArrayList<>();
        currents.add(motorLeft.getCurrent(CurrentUnit.AMPS));
        currents.add(motorRight.getCurrent(CurrentUnit.AMPS));
        return currents.toString();
    }
    public List<Double> getPowers(){
        List<Double> powers = new ArrayList<>();
        powers.add(motorLeft.getPower());
        powers.add(motorRight.getPower());
        return powers;
    }

    public void arcadeDrive(double forward, double right){
        motorLeft.setPower(forward + right);
        motorRight.setPower(forward - right);
    }
}

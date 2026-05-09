package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class TwoMotorsDrive {

    DcMotorEx motorLeft;
    DcMotorEx motorRight;
    private double leftPowerFiltered = 0;
    private double rightPowerFiltered = 0;
    private double maxDelta = 0.05;

    public void init(HardwareMap hwmap){
        motorLeft = (DcMotorEx) hwmap.get(DcMotor.class, "leftDrive");
        motorRight = (DcMotorEx) hwmap.get(DcMotor.class, "rightDrive");

        motorLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void setMotorDirections(DcMotorSimple.Direction lDirection, DcMotorSimple.Direction rDirection){
        motorLeft.setDirection(lDirection);
        motorRight.setDirection(rDirection);
    }
    public void setMotorModes(DcMotor.RunMode lMode, DcMotor.RunMode rMode){
        motorLeft.setMode(lMode);
        motorRight.setMode(rMode);
    }
    public void setTargetPositions(int lTarget, int rTarget) {
        motorLeft.setTargetPosition(lTarget);
        motorRight.setTargetPosition(rTarget);
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
        double leftTarget = forward - right;
        double rightTarget = forward + right;

        /*
        leftTarget = Math.max(-1, Math.min(1, leftTarget));
        rightTarget = Math.max(-1, Math.min(1, rightTarget));

        leftPowerFiltered = applySlewRate(leftPowerFiltered, leftTarget);
        rightPowerFiltered = applySlewRate(rightPowerFiltered, rightTarget);


         */
        motorLeft.setPower(leftTarget);
        motorRight.setPower(rightTarget);
    }

    public List<Integer> getPositions(){
        List<Integer> positions = new ArrayList<>();
        positions.add(motorLeft.getCurrentPosition());
        positions.add(motorRight.getCurrentPosition());
        return positions;
    }

    public boolean rightMotorIsBusy(){
        return motorRight.isBusy();
    }
    public boolean leftMotorIsBusy(){
        return motorLeft.isBusy();
    }
    /*
    public void setMaxDelta(double newDelta){
        this.maxDelta = newDelta;
    }
    private double applySlewRate(double current, double target){
        double delta = target - current;
        if (Math.abs(delta) > maxDelta){
            Math.copySign(maxDelta, delta);
        }
        return current + delta;
    }

     */

}

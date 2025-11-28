package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.List;

public class InTake {
    DcMotor coreHex;
    DcMotor coreHex2;

    public void init(HardwareMap hwmap){
        coreHex = hwmap.get(DcMotor.class, "coreHex");
        coreHex2 = hwmap.get(DcMotor.class, "coreHex2");
    }

    public void setCoreHexPowers(double cPower, double c2Power){
        coreHex.setPower(cPower);
        coreHex2.setPower(c2Power);
    }

    public void stopMotors(){
        coreHex.setPower(0);
        coreHex2.setPower(0);
    }
    public void powerAuto(double cPower, double c2Power, double timeout, LinearOpMode opMode) {

        setCoreHexPowers(cPower, c2Power);
        double startTime = opMode.getRuntime();
        while (opMode.opModeIsActive() && (opMode.getRuntime() - startTime < timeout)) {
            opMode.idle();
        }

        stopMotors();
    }
    public List<Double> getPowers(){
        List<Double> powers = new ArrayList<>();
        powers.add(coreHex.getPower());
        powers.add(coreHex2.getPower());
        return powers;
    }


}

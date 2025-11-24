package org.firstinspires.ftc.teamcode.common.Teste;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp
public class Teste extends OpMode {

    DcMotorEx motorTESTE;
    @Override
    public void init() {
        motorTESTE = hardwareMap.get(DcMotorEx.class, "noneDoMotor");
    }

    @Override
    public void loop() {
        telemetry.addData("corrente AMPS", motorTESTE.getCurrent(CurrentUnit.AMPS));
        motorTESTE.setPower(gamepad1.right_trigger);
    }
}
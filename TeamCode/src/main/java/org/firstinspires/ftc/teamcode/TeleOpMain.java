package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.mechanism.TwoMotorsDrive;

public class TeleOpMain extends OpMode {

    DcMotorEx motorLeft;
    DcMotorEx motorRight;
    TwoMotorsDrive drive = new TwoMotorsDrive(hardwareMap, motorLeft, motorRight);

    @Override
    public void init(){
        drive.init();
    }

    @Override
    public void loop(){
        telemetry.addData("Corrente dos motores (Esquerdo/Direito)", drive.getMotorsCurrents());
        drive.arcadeDrive(gamepad1.left_stick_x, gamepad1.left_stick_y);
    }
}

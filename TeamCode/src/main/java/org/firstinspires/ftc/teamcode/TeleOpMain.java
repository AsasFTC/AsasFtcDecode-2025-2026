package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.InTake;
import org.firstinspires.ftc.teamcode.mechanisms.OutTake;
import org.firstinspires.ftc.teamcode.mechanisms.TwoMotorsDrive;

@TeleOp
public class TeleOpMain extends OpMode {

    TwoMotorsDrive drive;
    InTake inTake;

    OutTake outTake;
    DcMotor flywheel;


    @Override
    public void init(){

        drive = new TwoMotorsDrive();
        drive.init(hardwareMap);

        inTake = new InTake();
        inTake.init(hardwareMap);

        outTake = new OutTake();
        outTake.init(hardwareMap);


    }

    @Override
    public void loop(){


        //Controle por gamepad
        drive.arcadeDrive(gamepad1.left_stick_x, gamepad1.left_stick_y);
        inTake.setCoreHexPowers(gamepad1.right_trigger, gamepad1.left_trigger);
        outTake.dpadControl(gamepad1.dpad_up, gamepad2.dpad_down, false, false);
        if (gamepad1.left_bumper){
            outTake.turnOff();
        }
        if (gamepad1.right_bumper){
            outTake.setDefaultPower();
        }


        //Telemetria

        //Movimento
        telemetry.addData("Corrente dos motores de movimento(Esquerdo/Direito)", drive.getCurrents());


        //Out-take
        telemetry.addLine("-----------------------Out-take---------------------");
        telemetry.addData("Potência", outTake.getPower());
        telemetry.addData("Velocidade", outTake.getVelocity());
        telemetry.addData("Corrente", outTake.getCurrent());
        

    }
}

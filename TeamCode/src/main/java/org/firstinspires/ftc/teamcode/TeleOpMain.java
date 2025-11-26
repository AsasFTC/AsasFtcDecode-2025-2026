package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.InTake;
import org.firstinspires.ftc.teamcode.mechanisms.OutTake;
import org.firstinspires.ftc.teamcode.mechanisms.TwoMotorsDrive;
import org.firstinspires.ftc.teamcode.mechanisms.WebCam;

@TeleOp
public class TeleOpMain extends OpMode {

    TwoMotorsDrive drive;
    InTake inTake;

    OutTake outTake;

    WebCam cam;

    boolean lastDpadUp;
    boolean lastDpadDown;

    boolean currentDpadUp;

    boolean currentDpadDown;
    double flywheelPower;

    @Override
    public void init(){
        lastDpadDown = false;
        lastDpadUp = false;

        drive = new TwoMotorsDrive();
        drive.init(hardwareMap);

        inTake = new InTake();
        inTake.init(hardwareMap);

        outTake = new OutTake();
        outTake.init(hardwareMap);

        cam = new WebCam();
        cam.init(hardwareMap);


    }

    @Override
    public void loop(){


        //Controle por gamepad
        drive.arcadeDrive(gamepad1.left_stick_y, gamepad1.left_stick_x);
        inTake.setCoreHexPowers(gamepad1.right_trigger, -gamepad1.left_trigger);
        dpadControl();
        if (gamepad1.left_bumper){
            outTake.turnOff();
        }
        if (gamepad1.right_bumper){
            outTake.setDefaultPower();
        }
        if (gamepad1.a){
            try {
                autoShoot();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        //Telemetria

        //Movimento
        telemetry.addData("Corrente dos motores de movimento(Esquerdo/Direito)", drive.getCurrents());


        //Out-take
        telemetry.addLine("-----------------------Out-take---------------------");
        telemetry.addData("Potência", outTake.getPower());
        telemetry.addData("Velocidade", outTake.getVelocity());
        telemetry.addData("Corrente", outTake.getCurrent());

        //Webcam
        telemetry.addLine("------------------------WebCam----------------------");
        telemetry.addData("Tags detectadas", cam.getDetectionsNumber());
        telemetry.addData("ID da Tag", cam.getTagId());
        telemetry.addData("Distância", cam.getTagDistanceCentimeters());
        telemetry.addData("Ângulo de ajuste", cam.getYaw());

    }
    public void dpadControl(){
        currentDpadUp = gamepad1.dpad_up;
        currentDpadDown = gamepad1.dpad_down;

        if (currentDpadUp && !lastDpadUp){
            flywheelPower += 0.01;
        }
        lastDpadUp = currentDpadUp;

        if (currentDpadDown && !lastDpadDown){
            flywheelPower -= 0.01;
        }
        lastDpadDown = currentDpadDown;
        outTake.setPower(flywheelPower);
    }
    public void autoShoot() throws InterruptedException {
        while (cam.getDetectionsNumber() == 1) {
            while (cam.getTagDistanceCentimeters() > 71){
                drive.setPowers(0.4, 0.4);
            }
            drive.stopMotors();
            if (drive.getPowers().get(0) == 0 && drive.getPowers().get(1) == 0 ){
                sleep(2000);
                outTake.setVelocity(1180);
            }
        }

        if (cam.getDetectionsNumber() > 1) {
            outTake.setVelocity(1180);
            sleep(3000);
            outTake.turnOff();
        }

        drive.stopMotors();
    }
}

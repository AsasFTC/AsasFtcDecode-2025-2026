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
    double coreHexPower;
    double coreHex2Power;
    boolean isOuttakeLocked = false;
    double lockedOuttakePower = 0;


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
        drive.arcadeDrive(gamepad1.left_stick_y, gamepad1.left_stick_x);


        inTake.setCoreHexPowers(coreHexPower, -coreHex2Power);

        dpadControl();
        outTakeTriggerControl();
        coreHexBumperControl();

        if (gamepad1.a) {
            autoShoot();
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

    public void autoShoot(){
        double flywheelVelocity = 0;
        double distance = cam.getTagDistanceCentimeters();
        while (cam.getTagId() == 20){
            telemetry.addLine("Calculando velocidade...");
            flywheelVelocity = Math.pow(distance, 1.16) + distance + 1080 + 45*Math.sin(((double) 1 /13)*distance + 8);
            telemetry.addData("Velocidade Calculada", flywheelVelocity);
            if (flywheelVelocity <= 2340) {
                outTake.setVelocity((int) flywheelVelocity);
            }
            telemetry.addData("Velocidade da flywheel", outTake.getVelocity());

            if (gamepad1.a){
                outTake.turnOff();
                return;
            }
        }
        return;
    }
    public void outTakeTriggerControl(){
        if (gamepad1.b) {
            isOuttakeLocked = true;
            lockedOuttakePower = gamepad1.right_trigger;
        }


        if (gamepad1.x) {
            isOuttakeLocked = false;
        }

        double outtakePower;

        if (isOuttakeLocked) {
            outtakePower = lockedOuttakePower;
        } else {
            outtakePower = gamepad1.right_trigger;
        }

        outTake.setPower(outtakePower);
    }
    public void coreHexBumperControl(){
        if (gamepad1.right_bumper){ coreHexPower = 0.9;} else {coreHexPower = 0;}
        if (gamepad1.left_bumper){ coreHex2Power = 0.9;} else {coreHex2Power = 0;}


    }


}

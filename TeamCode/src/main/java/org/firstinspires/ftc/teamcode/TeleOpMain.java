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
    int direction;
    int coreHexDirection;

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

        int direction = 0;
        coreHexDirection = 1;
    }

    @Override
    public void loop(){
        if (gamepad1.dpad_right) {
            direction = 1;
        }
        else if (gamepad1.dpad_left) {
            direction = -1;
        }
        drive.arcadeDrive(gamepad1.left_stick_y * direction * 0.6, gamepad1.left_stick_x*0.6);
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
        telemetry.addData("Direction", direction == 1 ? "Intake Frente" : "Outtake Frente");

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
        telemetry.addData("Ângulo de ajuste", cam.getAimAngle());

        //In-take
        telemetry.addData("Orientação do coreHex", coreHexDirection == 1 ? "Engolir" : "Repelir");

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
            flywheelVelocity = Math.pow(distance, 1.16) + distance + 780 + 45*Math.sin(((double) 1 /13)*distance + 8);
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


        // Ideia: Nao usamos potencia muito baixa =>
        // Criar um range de uso da potencia
        // Ex: Potencia minima: A potencia para realizar um gol da menor distancia possivel
        // Ex: Potencia maxima: A potencia necessária para realizar um gol da maior distancia necessário
        // Criar uma validação de potencia critica, para proteção do motor



        double trigger = gamepad1.right_trigger;
        if (gamepad1.b) {
            isOuttakeLocked = true;

            if (trigger > 0.05) {
                lockedOuttakePower = 0.4 + (trigger * 0.6);
            } else {
                lockedOuttakePower = 0;
            }
        }
        if (gamepad1.x) {
            isOuttakeLocked = false;
        }


        if (isOuttakeLocked) {
            flywheelPower = lockedOuttakePower;

        } else {
            if (trigger > 0.05) {
                flywheelPower = 0.4 + (trigger * 0.6);
            } else {
                flywheelPower = 0;
            }
        }
        double roundedPower = Math.round(flywheelPower*1000.0)/1000.0;

        outTake.setPower(roundedPower);

    }
    public void coreHexBumperControl(){
        if (gamepad1.y) {coreHexDirection = -1;} else {coreHexDirection = 1;}
        if (gamepad1.right_bumper){ coreHexPower = 0.9 * coreHexDirection;} else {coreHexPower = 0;}
        if (gamepad1.left_bumper){ coreHex2Power = 0.9 * coreHexDirection;} else {coreHex2Power = 0;}


    }


}

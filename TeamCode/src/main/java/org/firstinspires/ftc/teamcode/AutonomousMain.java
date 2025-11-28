package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.mechanisms.InTake;
import org.firstinspires.ftc.teamcode.mechanisms.OutTake;
import org.firstinspires.ftc.teamcode.mechanisms.TwoMotorsDrive;
import org.firstinspires.ftc.teamcode.mechanisms.WebCam;


@Autonomous
public class AutonomousMain extends LinearOpMode {

    TwoMotorsDrive drive;
    InTake inTake;

    OutTake outTake;

    WebCam cam;

    enum State{
        IDLE
    }
    State state = State.IDLE;
    long runningTimer = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("Inicializando...");
        telemetry.update();

        // init
        drive = new TwoMotorsDrive();
        drive.init(hardwareMap);

        inTake = new InTake();
        inTake.init(hardwareMap);

        outTake = new OutTake();
        outTake.init(hardwareMap);

        cam = new WebCam();
        cam.init(hardwareMap);


        telemetry.addLine("Pronto! Aperte PLAY.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;
        telemetry.addLine("Rodando...");
        telemetry.update();

        //Início do processo

        rotate(360);
        moveForward(50);



        while (opModeIsActive()) {

            telemetry.addData("Status", "Rodando");
            telemetry.update();


            //Ações em loop


        }
    }
    public void moveForward(double distanceCm) {

        // -------------------------
        // CONSTANTES DO MOTOR/RODA
        // -------------------------

        double wheelDiameter = 9;
        double cpr = 560;

        double wheelCircumference = Math.PI * wheelDiameter;
        int targetPulses = (int)((distanceCm / wheelCircumference) * cpr);

        // -------------------------
        // PREPARAR OS ENCODERS
        // -------------------------
        drive.setMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER, DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        drive.setTargetPositions(targetPulses, targetPulses);

        drive.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION, DcMotor.RunMode.RUN_TO_POSITION);

        // Velocidade de movimento (0 a 1)
        drive.setPowers(0.5, 0.5);

        // -------------------------
        // LOOP DE MOVIMENTO (bloqueante)
        // -------------------------
        while (opModeIsActive() &&
                (drive.leftMotorIsBusy() || drive.rightMotorIsBusy())) {

            telemetry.addData("Posições(E / D)", drive.getPositions().toString());
            telemetry.update();
        }

        // -------------------------
        // PARAR OS MOTORES
        // -------------------------
        drive.stopMotors();

        drive.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER, DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void rotate(double angleDeg) {

        // -------------------------------------
        // CONSTANTES DO ROBÔ
        // -------------------------------------
        int targetPulses = getTargetPulses(angleDeg);

        // -------------------------------------
        // PREPARAR OS ENCODERS
        // -------------------------------------
        drive.setMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER,
                DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Para girar, uma roda vai pra frente e outra pra trás
        drive.setTargetPositions(targetPulses, -targetPulses);

        drive.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION,
                DcMotor.RunMode.RUN_TO_POSITION);

        // Velocidade do giro
        drive.setPowers(0.5, 0.5);

        // -------------------------------------
        // LOOP BLOQUEANTE
        // -------------------------------------
        while (opModeIsActive() &&
                (drive.leftMotorIsBusy() || drive.rightMotorIsBusy())) {

            telemetry.addData("Alvos(E / D)", "L: " + targetPulses + " | R: " + -targetPulses);
            telemetry.addData("Posições(E / D)", drive.getPositions().toString());
            telemetry.update();
        }

        // -------------------------------------
        // PARAR MOTORES
        // -------------------------------------
        drive.stopMotors();

        drive.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER,
                DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private static int getTargetPulses(double angleDeg) {
        double wheelDiameter = 9.0;
        double cpr = 560;
        double trackWidth = 37.0;

        double wheelCircumference = Math.PI * wheelDiameter;

        // Circunferência do círculo descrito pelo giro do robô
        double turnCircumference = Math.PI * trackWidth;

        // Distância linear que cada roda precisa percorrer
        double distancePerWheel = (angleDeg / 360.0) * turnCircumference;

        // Converter distância → pulsos
        return (int)((distancePerWheel / wheelCircumference) * cpr);
    }


}

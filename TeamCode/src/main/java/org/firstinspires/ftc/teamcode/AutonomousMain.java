package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.mechanisms.InTake;
import org.firstinspires.ftc.teamcode.mechanisms.OutTake;
import org.firstinspires.ftc.teamcode.mechanisms.TwoMotorsDrive;
import org.firstinspires.ftc.teamcode.mechanisms.WebCam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Autonomous
public class AutonomousMain extends LinearOpMode {

    private static final Logger log = LoggerFactory.getLogger(AutonomousMain.class);
    TwoMotorsDrive drive;
    InTake inTake;

    OutTake outTake;

    WebCam cam;
    enum State{
        WALKING,
        SHOOTING,
        TURNING,
        GET_ARTIFACTS,
        WALKING_BACK_TO_GOAL,
        TURN_TO_GOAL,
        SHOOTING_ARTIFACTS,
        FINISHED

    }
    State state = State.WALKING;
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





        while (opModeIsActive()) {

            telemetry.addData("Status", "Rodando");
            telemetry.addData("Estado", state.toString());

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
            telemetry.update();


            //Ações em loop
            switch (state){
                case WALKING:
                    moveForward(-145);
                    //Thread.sleep(1000);
                    state = State.SHOOTING;
                    break;
                case SHOOTING:
                    outTake.setVelocity(1360);
                    Thread.sleep(1500);
                    inTake.setCoreHexPowers(0.6, -0.6);
                    Thread.sleep(4000);

                    outTake.turnOff();
                    inTake.setCoreHexPowers(0,0);
                    Thread.sleep(1500);
                    state = State.TURNING;
                    break;
                case TURNING:
                    rotate(-135);
                    //Ideia: girar pro outro lado e parar de andar pra trás e ao invés disso andar pra frente
                    Thread.sleep(500);
                    state = State.GET_ARTIFACTS;
                    break;
                case GET_ARTIFACTS:
                    inTake.setCoreHexPowers(0.6, 0); //Aqui ele gira só o coreHex de baixo (por isso que o outro é zero)
                    //Nota: Como o coreHex de cima tá do lado oposto ao de baixo, o poder dele precisa ser sempre negativo
                    moveForward(-98); //Move 50cm pra trás (frente está invertida) (estipulação)
                    Thread.sleep(1000);
                    inTake.setCoreHexPowers(0, 0); //Aqui ele gira só o coreHex de baixo (por isso que o outro é zero)

                    state = State.WALKING_BACK_TO_GOAL;
                    break;
                case WALKING_BACK_TO_GOAL:
                    moveForward(98); //Anda de volta
                    state = State.TURN_TO_GOAL;
                    break;
                case TURN_TO_GOAL:

                    //Sugestão: (com ajuste com ângulo da câmera)
                    //double anguloAjuste = cam.getYaw();
                    //while (anguloAjuste != 0){
                    //    rotate(anguloAjuste);
                    //    anguloAjuste = cam.getYaw();
                    //}

                    rotate(135); //Gira de volta pra onde tava
                    Thread.sleep(500);
                    state = State.SHOOTING_ARTIFACTS;
                    break;
                case SHOOTING_ARTIFACTS:

                    //Mesma coisa que o de cima, mas no final ele muda pra FINISHED
                    //Sugestão:
                    //Ao invés de outTake.setVelocity(), usar autoShoot()  (isso vai usar minha função de lançamento)
                    outTake.setVelocity(1360);
                    Thread.sleep(3000);
                    inTake.setCoreHexPowers(0.65, -0.65);
                    Thread.sleep(3000);
                    inTake.setCoreHexPowers(0, 0);
                    outTake.turnOff();
                    Thread.sleep(1000);
                    state = State.FINISHED;
                    break;
                case FINISHED:
                    requestOpModeStop();
                    return;
                    //Aqui acabou, não sei exatamente como terminar o loop
            }



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

        }
        return;
    }

}

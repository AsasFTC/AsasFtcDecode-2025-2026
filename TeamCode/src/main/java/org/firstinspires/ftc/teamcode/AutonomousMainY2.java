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
public class AutonomousMainY2 extends LinearOpMode {

    private static final Logger log = LoggerFactory.getLogger(AutonomousMainY2.class);


    TwoMotorsDrive drive;
    InTake inTake;
    OutTake outTake;
    WebCam cam;

    enum State {
        WALKING,
        SHOOTING,
        TURNING,
        GET_ARTIFACTS,
        WALKING_BACK_TO_GOAL,
        TURN_TO_GOAL,
        SHOOTING_ARTIFACTS,
        FINISHED
    }

    State state = State.SHOOTING;
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
            telemetry.addLine("-----------------------Out-take---------------------");
            telemetry.addData("Potência", outTake.getPower());
            telemetry.addData("Velocidade", outTake.getVelocity());
            telemetry.addData("Corrente", outTake.getCurrent());
            telemetry.addLine("------------------------WebCam----------------------");
            telemetry.addData("Tags detectadas", cam.getDetectionsNumber());
            telemetry.addData("ID da Tag", cam.getTagId());
            telemetry.addData("ID da Tag", cam.getTagId());
            telemetry.addData("Distância", cam.getTagDistanceCentimeters());
            telemetry.addData("Ângulo de ajuste", cam.getAimAngle());

            telemetry.update();

            //Ações em loop
            switch (state) {
                case SHOOTING:
                    outTake.setVelocity(1720);
                    Thread.sleep(5000);
                    inTake.setCoreHexPowers(0.9, -0.9);
                    Thread.sleep(5500);
                    outTake.turnOff();
                    inTake.setCoreHexPowers(0, 0);
                    Thread.sleep(3000);
                    state = State.WALKING;
                    break;

                case WALKING:
                    moveForward(54);
                    state = State.TURNING;
                    break;

                case TURNING:
                    rotate(113);
                    Thread.sleep(500);
                    state = State.GET_ARTIFACTS;
                    break;

                case GET_ARTIFACTS:
                    inTake.setCoreHexPowers(0.6, 0);
                    moveForward(-80);
                    Thread.sleep(1000);
                    inTake.setCoreHexPowers(0, 0);
                    state = State.WALKING_BACK_TO_GOAL;
                    break;
                case WALKING_BACK_TO_GOAL:
                    moveForward(74);
                    state = State.TURN_TO_GOAL;
                    break;
                case TURN_TO_GOAL:
                    rotate(-110);
                    Thread.sleep(500);
                    moveForward(-43);
                    telemetry.addData("Ângulo de ajuste pro gol (webcam)", cam.getYaw());
                    telemetry.update();
                    state = State.SHOOTING_ARTIFACTS;
                    break;
                case SHOOTING_ARTIFACTS:
                    outTake.setVelocity(1700);
                    Thread.sleep(4000);
                    inTake.setCoreHexPowers(0.9, -0.9);
                    Thread.sleep(3000);
                    inTake.setCoreHexPowers(0, 0);
                    outTake.turnOff();
                    Thread.sleep(1000);
                    state = State.FINISHED;
                    break;
                case FINISHED:
                    requestOpModeStop();
                    return;
            }
        }
    }

    // INÍCIO DOS MÉTODOS AUXILIARES
    public void moveForward(double distanceCm) {
        double wheelDiameter = 9;
        double cpr = 560;
        double wheelCircumference = Math.PI * wheelDiameter;
        int targetPulses = (int) ((distanceCm / wheelCircumference) * cpr);

        drive.setMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setTargetPositions(targetPulses, targetPulses);
        drive.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION, DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPowers(0.5, 0.5);

        while (opModeIsActive() && (drive.leftMotorIsBusy() || drive.rightMotorIsBusy())) {
            telemetry.addData("Posições(E / D)", drive.getPositions().toString());
            telemetry.update();
        }

        drive.stopMotors();
        drive.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER, DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void rotate(double angleDeg) {
        int targetPulses = getTargetPulses(angleDeg);

        drive.setMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setTargetPositions(targetPulses, -targetPulses);
        drive.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION, DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPowers(0.8, 0.8);

        while (opModeIsActive() && (drive.leftMotorIsBusy() || drive.rightMotorIsBusy())) {
            telemetry.addData("Alvos(E / D)", "L: " + targetPulses + " | R: " + -targetPulses);
            telemetry.addData("Posições(E / D)", drive.getPositions().toString());
            telemetry.update();
        }

        drive.stopMotors();
        drive.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER, DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private int getTargetPulses(double angleDeg) {
        double wheelDiameter = 9.0;
        double cpr = 560;
        double trackWidth = 37.0;
        double wheelCircumference = Math.PI * wheelDiameter;
        double turnCircumference = Math.PI * trackWidth;
        double distancePerWheel = (angleDeg / 360.0) * turnCircumference;
        return (int) ((distancePerWheel / wheelCircumference) * cpr);
    }

    public void autoShoot() {
        double flywheelVelocity = 0;
        double distance = cam.getTagDistanceCentimeters();

        while (cam.getTagId() == 20) {
            telemetry.addLine("Calculando velocidade...");
            flywheelVelocity = Math.pow(distance, 1.16) + distance + 1080 + 45 * Math.sin(((double) 1 / 13) * distance + 8);
            telemetry.addData("Velocidade Calculada", flywheelVelocity);
            if (flywheelVelocity <= 2340) {
                outTake.setVelocity((int) flywheelVelocity);
            }
            telemetry.addData("Velocidade da flywheel", outTake.getVelocity());
        }
    }
}

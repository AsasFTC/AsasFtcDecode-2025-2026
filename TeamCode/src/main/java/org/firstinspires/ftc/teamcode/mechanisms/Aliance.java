package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Aliance {

    private Definicoes definicao;

    private WebCam cam;

    public Definicoes getDefinicao() {
        return definicao;
    }

    public void setDefinicao(Definicoes definicao) {
        this.definicao = definicao;
    }

    public void initCam(HardwareMap hwmap){
        cam.init(hwmap);
    }
}

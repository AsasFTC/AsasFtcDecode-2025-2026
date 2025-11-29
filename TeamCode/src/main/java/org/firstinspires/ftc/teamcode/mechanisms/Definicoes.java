package org.firstinspires.ftc.teamcode.mechanisms;

public enum Definicoes {

    AZUL(1,"Azul"),VERMELHO(2,"Vermelho");
    private Integer codigo;
    private String cor;

    Definicoes(Integer codigo, String cor) {
        this.codigo = codigo;
        this.cor = cor;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }


}

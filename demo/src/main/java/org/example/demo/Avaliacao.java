package org.example.demo;

public class Avaliacao {
    private String origem;
    private String destino;
    private double seguranca;
    private double iluminacao;
    private double trafego;
    private String comentario;

    public String getRota() {
        return origem + "→" + destino;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public double getSeguranca() {
        return seguranca;
    }

    public double getIluminacao() {
        return iluminacao;
    }

    public double getTrafego() {
        return trafego;
    }

    public String getComentario() {
        return comentario;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setSeguranca(double seguranca) {
        this.seguranca = seguranca;
    }

    public void setIluminacao(double iluminacao) {
        this.iluminacao = iluminacao;
    }

    public void setTrafego(double trafego) {
        this.trafego = trafego;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

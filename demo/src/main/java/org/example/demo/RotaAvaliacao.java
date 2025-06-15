package org.example.demo;

import java.util.List;

public class RotaAvaliacao implements Comparable<RotaAvaliacao> {
    private final String nome;
    private final List<Avaliacao> avaliacoes;

    public RotaAvaliacao(String nome, List<Avaliacao> avaliacoes) {
        this.nome = nome;
        this.avaliacoes = avaliacoes;
    }

    public double calcularMediaSeguranca() {
        return avaliacoes.stream().mapToDouble(Avaliacao::getSeguranca).average().orElse(0);
    }

    public String getNome() {
        return nome;
    }

    @Override
    public int compareTo(RotaAvaliacao outra) {
        return Double.compare(outra.calcularMediaSeguranca(), this.calcularMediaSeguranca());
    }

    @Override
    public String toString() {
        return nome + " → Média de Segurança: " + String.format("%.2f", calcularMediaSeguranca());
    }
}
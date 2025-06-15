package org.example.demo;

import java.util.*;
import java.util.regex.*;

public class MapaService {

    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public void processarAvaliacao(String jsonDados) {
        Avaliacao av = parseJsonManual(jsonDados);
        if (av == null || !avaliacaoValida(av)) {
            System.out.println("Dados inválidos: " + jsonDados);
            return;
        }
        avaliacoes.add(av);
    }

    public String obterAvaliacoesJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < avaliacoes.size(); i++) {
            Avaliacao a = avaliacoes.get(i);
            sb.append("{");
            sb.append("\"origem\":\"").append(escape(a.getOrigem())).append("\",");
            sb.append("\"destino\":\"").append(escape(a.getDestino())).append("\",");
            sb.append("\"seguranca\":").append(a.getSeguranca()).append(",");
            sb.append("\"iluminacao\":").append(a.getIluminacao()).append(",");
            sb.append("\"trafego\":").append(a.getTrafego()).append(",");
            sb.append("\"comentario\":\"").append(escape(a.getComentario())).append("\"");
            sb.append("}");
            if (i < avaliacoes.size() - 1) sb.append(",");
        }

        sb.append("]");
        return sb.toString();
    }

    public String compararRotasMaisSeguras() {
        if (avaliacoes.isEmpty()) return "Nenhuma";

        Map<String, List<Avaliacao>> agrupadas = new HashMap<>();
        for (Avaliacao a : avaliacoes) {
            agrupadas.computeIfAbsent(a.getRota(), k -> new ArrayList<>()).add(a);
        }

        String melhorRota = null;
        double melhorNota = -1.0;

        for (Map.Entry<String, List<Avaliacao>> entry : agrupadas.entrySet()) {
            double media = entry.getValue().stream()
                    .mapToDouble(a -> a.getSeguranca() + a.getIluminacao() - a.getTrafego())
                    .average().orElse(0);

            if (media > melhorNota) {
                melhorNota = media;
                melhorRota = entry.getKey();
            }
        }

        return melhorRota;
    }

    private boolean avaliacaoValida(Avaliacao av) {
        return av.getSeguranca() >= 0 && av.getSeguranca() <= 5 &&
                av.getIluminacao() >= 0 && av.getIluminacao() <= 5 &&
                av.getTrafego() >= 0 && av.getTrafego() <= 5;
    }

    private Avaliacao parseJsonManual(String json) {
        try {
            Avaliacao a = new Avaliacao();
            a.setOrigem(extrair(json, "origem"));
            a.setDestino(extrair(json, "destino"));
            a.setSeguranca(Double.parseDouble(extrair(json, "seguranca")));
            a.setIluminacao(Double.parseDouble(extrair(json, "iluminacao")));
            a.setTrafego(Double.parseDouble(extrair(json, "trafego")));
            a.setComentario(extrair(json, "comentario"));
            return a;
        } catch (Exception e) {
            System.err.println("Erro ao parsear JSON manual: " + e.getMessage());
            return null;
        }
    }

    private String extrair(String json, String campo) {
        Pattern p = Pattern.compile("\"" + campo + "\"\\s*:\\s*\"?(.*?)\"?(,|})");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1).replace("\\\"", "\"");
        }
        return "";
    }

    private String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}

package org.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URL;

public class MapaController extends Application {

    private final MapaService mapaService = new MapaService();
    private WebEngine webEngine;

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        webEngine = webView.getEngine();

        webEngine.setOnError(event -> System.err.println(" Erro JavaScript: " + event.getMessage()));
        webEngine.setOnAlert(event -> System.out.println("JS Alert: " + event.getData()));

        URL mapaURL = getClass().getResource("/MapaView.html");
        if (mapaURL == null) {
            System.err.println(" ERRO: MapaView.html não encontrado em /resources");
            return;
        }

        webEngine.load(mapaURL.toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                try {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    JavaBridge bridge = new JavaBridge(this, mapaService);
                    window.setMember("javaConnector", bridge);
                    Platform.runLater(() -> {
                        webEngine.executeScript("onJavaConnectorReady();");
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(webView, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Rotas Seguras - Maringá");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class JavaBridge {
        private final MapaController controller;
        private final MapaService mapaService;

        public JavaBridge(MapaController controller, MapaService mapaService) {
            this.controller = controller;
            this.mapaService = mapaService;
        }

        public void receberAvaliacao(String json) {
            mapaService.processarAvaliacao(json);
            String avaliacoesJson = mapaService.obterAvaliacoesJson();
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.call("updateFeed", avaliacoesJson);
        }

        public void mostrarRelatorioEmJanela() {
            String avaliacoesJson = mapaService.obterAvaliacoesJson();
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.call("updateFeed", avaliacoesJson);
        }

        public void compararRotasMaisSeguras() {
            String melhorRota = mapaService.compararRotasMaisSeguras();
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.call("mostrarRotaMaisSegura", melhorRota);
        }
    }
}

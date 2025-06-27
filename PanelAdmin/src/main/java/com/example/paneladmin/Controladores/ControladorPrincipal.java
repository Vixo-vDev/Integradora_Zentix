package com.example.paneladmin.Controladores;

import com.example.paneladmin.Vistas.VistaCarga;
import com.example.paneladmin.Vistas.VistaPrincipal;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControladorPrincipal {
    private Stage primaryStage;
    private VistaPrincipal vistaPrincipal;

    public ControladorPrincipal(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mostrarPantallaCarga();
    }

    private void mostrarPantallaCarga() {
        VistaCarga vistaCarga = new VistaCarga();
        Scene scene = new Scene(vistaCarga.getVista(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // Simular carga inicial
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        javafx.application.Platform.runLater(() -> {
                            vistaPrincipal = new VistaPrincipal("Admin", "Administrador");
                            new ControladorMain(ControladorPrincipal.this, vistaPrincipal);

                            Scene mainScene = new Scene(vistaPrincipal.getVista(), 1200, 800);
                            mainScene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
                            primaryStage.setScene(mainScene);
                        });
                    }
                },
                1500
        );
    }

    public void cambiarContenido(Region contenido) {
        vistaPrincipal.setContenido(contenido);
    }
}
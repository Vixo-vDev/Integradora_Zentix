package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorPrincipal;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VistaPrincipal {
    private BorderPane raiz;
    private ControladorPrincipal controlador;

    public VistaPrincipal() {
        raiz = new BorderPane();
        raiz.setStyle("-fx-background-color: #f5f7fa;");
        this.controlador = new ControladorPrincipal(this);
    }

    public void mostrar(Stage escenario) {
        Scene escena = new Scene(raiz);
        escenario.setTitle("Panel de Administración");
        escenario.setScene(escena);
        escenario.setMaximized(true); // Esta línea hace que ocupe toda la pantalla
        raiz.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        controlador.mostrarDashboardInicio();
        escenario.show();
    }

    public BorderPane getRaiz() {
        return raiz;
    }
}
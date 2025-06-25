package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorPrincipal;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VistaPrincipal {
    private ControladorPrincipal controlador;
    private BorderPane raiz;

    public VistaPrincipal() {
        controlador = new ControladorPrincipal(this);
        raiz = new BorderPane();
        controlador.inicializarUI();
    }

    public void mostrar(Stage escenario) {
        Scene escena = new Scene(raiz, 1200, 750);
        escenario.setTitle("Panel de Administracion");
        escenario.setScene(escena);
        escenario.show();
    }

    public BorderPane getRaiz() {
        return raiz;
    }
}
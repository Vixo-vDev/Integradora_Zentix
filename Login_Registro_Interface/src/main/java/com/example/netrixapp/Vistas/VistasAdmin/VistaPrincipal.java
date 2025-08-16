package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorPrincipal;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VistaPrincipal {
    private final BorderPane raiz;
    private final ControladorPrincipal controlador;

    public VistaPrincipal() {
        this.raiz = new BorderPane();
        this.controlador = new ControladorPrincipal(this);
    }

    public void mostrar(Stage escenario) {
        Scene escena = new Scene(raiz);
        escenario.setTitle("Netrix | Panel de Administración");
        escenario.setScene(escena);
        escenario.setMaximized(true);
        escenario.show();
    }

    public BorderPane getRaiz() {
        return raiz;
    }
}
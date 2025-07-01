package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorEstadisticas;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VistaEstadisticas {
    private ControladorEstadisticas controlador;
    private VBox vista;

    public VistaEstadisticas() {
        vista = new VBox(20);
        vista.setPadding(new Insets(20));
        this.controlador = new ControladorEstadisticas(this);
        controlador.inicializarUI();
    }

    public VBox getVista() {
        return vista;
    }
}
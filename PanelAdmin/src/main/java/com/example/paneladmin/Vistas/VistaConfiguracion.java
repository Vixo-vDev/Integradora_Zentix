package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorConfiguracion;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class VistaConfiguracion {
    private ControladorConfiguracion controlador;
    private VBox vista;

    public VistaConfiguracion() {
        vista = new VBox(20);
        vista.setPadding(new Insets(20));
        this.controlador = new ControladorConfiguracion(this);
        controlador.inicializarUI();
    }

    public VBox getVista() {
        return vista;
    }
}
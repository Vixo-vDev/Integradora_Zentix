package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorSolicitudes;
import com.example.paneladmin.Modelos.Solicitud;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VistaSolicitudes {
    private ControladorSolicitudes controlador;
    private VBox vista;
    private TableView<Solicitud> tabla;

    public VistaSolicitudes() {
        vista = new VBox(20);
        vista.setPadding(new Insets(20));
        vista.setStyle("-fx-background-color: #f8f9fa;");
        controlador = new ControladorSolicitudes(this);
        controlador.inicializarUI();
    }

    public VBox getVista() {
        return vista;
    }

    public TableView<Solicitud> getTabla() {
        return tabla;
    }

    public void setTabla(TableView<Solicitud> tabla) {
        this.tabla = tabla;
        if (tabla != null) {
            tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tabla.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");
            VBox.setVgrow(tabla, Priority.ALWAYS);
        }
    }
}

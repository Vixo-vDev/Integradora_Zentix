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
        this.controlador = new ControladorSolicitudes(this); // Inicialización correcta del controlador
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
        // Configuración adicional de la tabla
        if (tabla != null) {
            tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tabla.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");
            // Hacer que la tabla ocupe todo el espacio disponible
            VBox.setVgrow(tabla, Priority.ALWAYS);
        }
    }
}
package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorFormularios;
import com.example.paneladmin.Modelos.Formulario;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VistaFormularios {
    private ControladorFormularios controlador;
    private VBox vista;
    private TableView<Formulario> tabla;

    public VistaFormularios() {
        vista = new VBox(20);
        vista.setPadding(new Insets(20));
        this.controlador = new ControladorFormularios(this); // Inicialización correcta del controlador
        controlador.inicializarUI();
    }

    public VBox getVista() {
        return vista;
    }

    public TableView<Formulario> getTabla() {
        return tabla;
    }

    public void setTabla(TableView<Formulario> tabla) {
        this.tabla = tabla;
        if (tabla != null) {
            tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tabla.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");
            VBox.setVgrow(tabla, Priority.ALWAYS);
        }
    }
}
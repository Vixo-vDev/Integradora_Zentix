package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorInventario;
import com.example.paneladmin.Modelos.Producto;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VistaInventario {
    private ControladorInventario controlador;
    private VBox vista;
    private TableView<Producto> tabla;

    public VistaInventario() {
        vista = new VBox(20);
        vista.setPadding(new Insets(20));
        this.controlador = new ControladorInventario(this); // Inicialización correcta del controlador
        controlador.inicializarUI();
    }

    public VBox getVista() {
        return vista;
    }

    public TableView<Producto> getTabla() {
        return tabla;
    }

    public void setTabla(TableView<Producto> tabla) {
        this.tabla = tabla;
        if (tabla != null) {
            tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tabla.setStyle("-fx-background-color: #cbcbcb; -fx-border-color: #bdc3c7;");
            VBox.setVgrow(tabla, Priority.ALWAYS);
        }
    }
}
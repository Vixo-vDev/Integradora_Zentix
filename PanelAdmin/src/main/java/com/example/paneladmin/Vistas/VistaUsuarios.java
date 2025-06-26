package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorUsuarios;
import com.example.paneladmin.Modelos.Usuario;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class VistaUsuarios {
    private ControladorUsuarios controlador;
    private VBox vista;
    private TableView<Usuario> tabla;

    public VistaUsuarios() {
        vista = new VBox(20);
        vista.setPadding(new Insets(20));
        this.controlador = new ControladorUsuarios(this); // Inicializar el controlador
        controlador.inicializarUI();
    }

    public VBox getVista() {
        return vista;
    }

    public TableView<Usuario> getTabla() {
        return tabla;
    }

    public void setTabla(TableView<Usuario> tabla) {
        this.tabla = tabla;
        if (tabla != null) {
            tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tabla.setStyle("-fx-background-color: white;");
        }
    }
}
package com.example.paneladmin.Controladores;


import com.example.paneladmin.Vistas.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ControladorPrincipal {
    private VistaPrincipal vista;
    private VBox barraLateral;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        barraLateral = crearBarraLateral();
        vista.getRaiz().setLeft(barraLateral);
        mostrarContenidoBienvenida();
    }


}
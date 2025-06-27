package com.example.paneladmin.Vistas;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;

public class VistaUsuarios {
    private VBox vista;

    public VistaUsuarios() {
        vista = new VBox(20);
        vista.setStyle("-fx-background-color: transparent; -fx-padding: 20;");
        vista.setAlignment(Pos.CENTER);

        // Icono de usuarios
        Label icono = new Label("👥");
        icono.setStyle("-fx-font-size: 60; -fx-text-fill: #bdc3c7;");

        // Mensaje principal
        Label titulo = new Label("No hay usuarios registrados");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#7f8c8d"));

        // Mensaje secundario
        Label subtitulo = new Label("El sistema no tiene usuarios dados de alta actualmente");
        subtitulo.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitulo.setTextFill(Color.web("#95a5a6"));

        VBox contenedorMensaje = new VBox(10, icono, titulo, subtitulo);
        contenedorMensaje.setAlignment(Pos.CENTER);

        vista.getChildren().addAll(contenedorMensaje);
    }

    public VBox getVista() {
        return vista;
    }
}
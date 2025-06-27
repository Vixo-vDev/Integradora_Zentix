package com.example.paneladmin.Vistas;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;

public class VistaSolicitudes {
    private VBox vista;

    public VistaSolicitudes() {
        vista = new VBox(20);
        vista.setStyle("-fx-background-color: transparent; -fx-padding: 20;");
        vista.setAlignment(Pos.CENTER);

        // Icono de solicitudes vacías
        Label icono = new Label("✉️");
        icono.setStyle("-fx-font-size: 60; -fx-text-fill: #bdc3c7;");

        // Mensaje principal
        Label titulo = new Label("No hay solicitudes registradas");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#7f8c8d"));

        // Mensaje secundario
        Label subtitulo = new Label("No se encontraron solicitudes pendientes o procesadas");
        subtitulo.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitulo.setTextFill(Color.web("#95a5a6"));

        // Botón opcional
        Button btnInfo = new Button("¿Problemas con las solicitudes?");
        btnInfo.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: #3498db; " +
                "-fx-font-size: 14; " +
                "-fx-underline: true; " +
                "-fx-border-width: 0;");

        // Efecto hover para el botón
        btnInfo.setOnMouseEntered(e -> {
            btnInfo.setStyle("-fx-background-color: transparent; " +
                    "-fx-text-fill: #2980b9; " +
                    "-fx-font-size: 14; " +
                    "-fx-underline: true;");
            btnInfo.setCursor(javafx.scene.Cursor.HAND);
        });

        btnInfo.setOnMouseExited(e -> {
            btnInfo.setStyle("-fx-background-color: transparent; " +
                    "-fx-text-fill: #3498db; " +
                    "-fx-font-size: 14; " +
                    "-fx-underline: true;");
        });

        VBox contenedorMensaje = new VBox(10, icono, titulo, subtitulo);
        contenedorMensaje.setAlignment(Pos.CENTER);

        vista.getChildren().addAll(contenedorMensaje, btnInfo);
    }

    public VBox getVista() {
        return vista;
    }
}
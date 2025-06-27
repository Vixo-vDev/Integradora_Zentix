package com.example.paneladmin.Vistas;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;

public class VistaFormularios {
    private VBox vista;

    public VistaFormularios() {
        vista = new VBox(20);
        vista.setStyle("-fx-background-color: transparent; -fx-padding: 20;");
        vista.setAlignment(Pos.CENTER);

        // Icono de formulario vacío
        Label icono = new Label("📋");
        icono.setStyle("-fx-font-size: 60; -fx-text-fill: #bdc3c7;");

        // Mensaje principal
        Label titulo = new Label("No hay formularios registrados");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#7f8c8d"));

        // Mensaje secundario
        Label subtitulo = new Label("No se encontraron formularios en el sistema");
        subtitulo.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitulo.setTextFill(Color.web("#95a5a6"));

        // Botón para refrescar formulario
        Button btnNuevo = new Button("REFRESCAR FORMULARIO");
        btnNuevo.setStyle("-fx-background-color: #009475; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Efecto hover
        btnNuevo.setOnMouseEntered(e -> {
            btnNuevo.setStyle("-fx-background-color: #27ae60; " +
                    "-fx-text-fill: white; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 3);");
            btnNuevo.setCursor(javafx.scene.Cursor.HAND);
        });

        btnNuevo.setOnMouseExited(e -> {
            btnNuevo.setStyle("-fx-background-color: #009475; " +
                    "-fx-text-fill: white; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        });

        VBox contenedorMensaje = new VBox(10, icono, titulo, subtitulo);
        contenedorMensaje.setAlignment(Pos.CENTER);

        vista.getChildren().addAll(contenedorMensaje, btnNuevo);
    }

    public VBox getVista() {
        return vista;
    }
}
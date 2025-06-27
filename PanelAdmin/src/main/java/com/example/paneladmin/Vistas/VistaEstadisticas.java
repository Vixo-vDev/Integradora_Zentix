package com.example.paneladmin.Vistas;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;

public class VistaEstadisticas {
    private StackPane vista;

    public VistaEstadisticas() {
        vista = new StackPane();
        vista.setStyle("-fx-background-color: #f5f7fa;"); // Fondo suave

        // Mensaje central
        VBox contenedorMensaje = new VBox(10);
        contenedorMensaje.setAlignment(Pos.CENTER);

        // Icono
        Label icono = new Label("📊");
        icono.setStyle("-fx-font-size: 60; -fx-text-fill: #bdc3c7;");

        // Mensaje principal
        Label mensajePrincipal = new Label("No hay datos disponibles");
        mensajePrincipal.setStyle("-fx-font-size: 24; -fx-text-fill: #7f8c8d; -fx-font-weight: bold;");

        // Mensaje secundario
        Label mensajeSecundario = new Label("No se encontraron registros para mostrar estadísticas");
        mensajeSecundario.setStyle("-fx-font-size: 14; -fx-text-fill: #95a5a6;");

        contenedorMensaje.getChildren().addAll(icono, mensajePrincipal, mensajeSecundario);
        vista.getChildren().add(contenedorMensaje);
    }

    public StackPane getVista() {
        return vista;
    }
}
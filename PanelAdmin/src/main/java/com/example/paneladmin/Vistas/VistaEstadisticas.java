package com.example.paneladmin.Vistas;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class VistaEstadisticas {
    private BorderPane vista;

    public VistaEstadisticas(Runnable onBackAction) {
        vista = new BorderPane();
        vista.setStyle("-fx-background-color: #f5f7fa;");

        // Crear botón de flecha para regresar
        Button btnRegresar = crearBotonRegreso(onBackAction);

        // Contenedor superior con el botón de regreso
        HBox topBar = new HBox(btnRegresar);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        vista.setTop(topBar);

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
        vista.setCenter(contenedorMensaje);
    }

    private Button crearBotonRegreso(Runnable onBackAction) {
        Button btnRegresar = new Button();
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-padding: 5;");

        // Crear icono de flecha usando SVG
        SVGPath flecha = new SVGPath();
        flecha.setContent("M10 0 L0 10 L10 20");
        flecha.setStroke(Color.web("#3498db"));
        flecha.setStrokeWidth(2);
        flecha.setFill(null);

        btnRegresar.setGraphic(flecha);
        btnRegresar.setOnAction(e -> onBackAction.run());

        // Efecto hover
        btnRegresar.setOnMouseEntered(e -> {
            flecha.setStroke(Color.web("#2980b9"));
            btnRegresar.setCursor(javafx.scene.Cursor.HAND);
        });
        btnRegresar.setOnMouseExited(e -> {
            flecha.setStroke(Color.web("#3498db"));
        });

        return btnRegresar;
    }

    public BorderPane getVista() {
        return vista;
    }
}
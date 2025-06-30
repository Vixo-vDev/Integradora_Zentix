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

public class VistaSolicitudes {
    private StackPane vista;

    public VistaSolicitudes(Runnable onBackAction) {
        // Contenedor principal que cubre toda la pantalla
        StackPane contenedorPrincipal = new StackPane();
        contenedorPrincipal.setStyle("-fx-background-color: white;");

        // Botón de flecha SVG para regresar
        Button btnRegresar = crearBotonRegreso(onBackAction);
        StackPane.setAlignment(btnRegresar, Pos.TOP_LEFT);
        StackPane.setMargin(btnRegresar, new Insets(20, 0, 0, 20));

        // Contenido central
        VBox contenedorMensaje = new VBox(20);
        contenedorMensaje.setAlignment(Pos.CENTER);
        contenedorMensaje.setMaxWidth(600);

        // Icono de solicitudes
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
        subtitulo.setWrapText(true);
        subtitulo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Botón de ayuda
        Button btnInfo = crearBotonAyuda();

        contenedorMensaje.getChildren().addAll(icono, titulo, subtitulo, btnInfo);
        contenedorPrincipal.getChildren().addAll(btnRegresar, contenedorMensaje);

        vista = new StackPane(contenedorPrincipal);
    }

    private Button crearBotonRegreso(Runnable onBackAction) {
        Button btnRegresar = new Button();
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-padding: 5;");

        // Icono SVG de flecha
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

    private Button crearBotonAyuda() {
        Button btnInfo = new Button("¿Problemas con las solicitudes?");
        btnInfo.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: #3498db; " +
                "-fx-font-size: 14; " +
                "-fx-underline: true; " +
                "-fx-border-width: 0;");

        // Efecto hover
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

        return btnInfo;
    }

    public StackPane getVista() {
        return vista;
    }
}
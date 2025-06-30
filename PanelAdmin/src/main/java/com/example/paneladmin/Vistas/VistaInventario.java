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

public class VistaInventario {
    private StackPane vista;

    public VistaInventario(Runnable onBackAction) {
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

        // Icono de inventario
        Label icono = new Label("📦");
        icono.setStyle("-fx-font-size: 60; -fx-text-fill: #bdc3c7;");

        // Mensaje principal
        Label titulo = new Label("Inventario Vacío");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#7f8c8d"));

        // Mensaje secundario
        Label subtitulo = new Label("No hay productos registrados en el inventario");
        subtitulo.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitulo.setTextFill(Color.web("#95a5a6"));
        subtitulo.setWrapText(true);
        subtitulo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Botón para agregar producto
        Button btnAgregar = crearBotonAgregar();

        contenedorMensaje.getChildren().addAll(icono, titulo, subtitulo, btnAgregar);
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

    private Button crearBotonAgregar() {
        Button btnAgregar = new Button("AGREGAR PRODUCTO");
        btnAgregar.setStyle("-fx-background-color: #009475; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Efecto hover
        btnAgregar.setOnMouseEntered(e -> {
            btnAgregar.setStyle("-fx-background-color: #27ae60; " +
                    "-fx-text-fill: white; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 3);");
            btnAgregar.setCursor(javafx.scene.Cursor.HAND);
        });

        btnAgregar.setOnMouseExited(e -> {
            btnAgregar.setStyle("-fx-background-color: #009475; " +
                    "-fx-text-fill: white; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        });

        return btnAgregar;
    }

    public StackPane getVista() {
        return vista;
    }
}
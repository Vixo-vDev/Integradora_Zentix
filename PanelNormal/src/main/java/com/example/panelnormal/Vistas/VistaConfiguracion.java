package com.example.panelnormal.Vistas;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.SVGPath;

public class VistaConfiguracion {
    private VBox vista;

    public VistaConfiguracion(Runnable onBackAction) {
        // Contenedor principal
        vista = new VBox();
        vista.setAlignment(Pos.TOP_CENTER);
        vista.setPadding(new Insets(20));
        vista.setStyle("-fx-background-color: transparent;");

        // Botón de regreso (flecha)
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

        // Efecto hover para el botón
        btnRegresar.setOnMouseEntered(e -> {
            flecha.setStroke(Color.web("#2980b9"));
        });
        btnRegresar.setOnMouseExited(e -> {
            flecha.setStroke(Color.web("#3498db"));
        });

        // Contenedor para el botón de regreso (alineado a la izquierda)
        HBox header = new HBox(btnRegresar);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        // Mensaje de no configuración disponible
        Label mensaje = new Label("No hay configuración disponible");
        mensaje.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        mensaje.setTextFill(Color.web("#7f8c8d"));
        mensaje.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 1, 1);");

        vista.getChildren().addAll(header, mensaje);
    }

    public VBox getVista() {
        return vista;
    }
}
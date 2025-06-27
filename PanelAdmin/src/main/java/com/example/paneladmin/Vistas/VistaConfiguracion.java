package com.example.paneladmin.Vistas;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class VistaConfiguracion {
    private VBox vista;

    public VistaConfiguracion() {
        // Contenedor principal
        vista = new VBox(20);
        vista.setAlignment(Pos.TOP_CENTER);
        vista.setPadding(new Insets(20));
        vista.setStyle("-fx-background-color: transparent;");

        // Título
        Label titulo = new Label("CONFIGURACIÓN DEL SISTEMA");
        titulo.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 26));
        titulo.setStyle("-fx-text-fill: linear-gradient(to right, #2c3e50, #4a148c); " +
                "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 1, 1);");
        titulo.setPadding(new Insets(0, 0, 20, 0));

        // Sección Seguridad
        VBox seccionSeguridad = crearSeccionConfiguracion(
                "SEGURIDAD",
                "shield-icon.png",
                new Control[] {
                        crearCampoPassword("Nueva contraseña", ""),
                        crearCampoPassword("Confirmar contraseña", "")
                }
        );

        // Sección Preferencias
        VBox seccionPref = crearSeccionConfiguracion(
                "PREFERENCIAS",
                "settings-icon.png",
                new Control[] {
                        crearCheckBox("Modo oscuro", false),
                        crearComboBox("Idioma", new String[]{"Español", "Inglés", "Francés"})
                }
        );

        // Botones de acción
        HBox botones = new HBox(20);
        botones.setAlignment(Pos.CENTER);
        Button btnGuardar = crearBotonAccion("GUARDAR CAMBIOS", "#27ae60", "#2ecc71");
        Button btnCancelar = crearBotonAccion("CANCELAR", "#e74c3c", "#c0392b");

        botones.getChildren().addAll(btnCancelar, btnGuardar);

        vista.getChildren().addAll(
                titulo,
                seccionSeguridad,
                seccionPref,
                botones
        );
    }

    private VBox crearSeccionConfiguracion(String tituloSeccion, String icono, Control[] controles) {
        VBox seccion = new VBox(15);
        seccion.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 1);");
        seccion.setAlignment(Pos.TOP_LEFT);
        seccion.setMaxWidth(800);

        // Encabezado de sección
        Label lblTitulo = new Label(tituloSeccion);
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblTitulo.setTextFill(Color.web("#2c3e50"));
        lblTitulo.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.05), 1, 0, 1, 1);");

        // Controles de la sección
        VBox contenedorControles = new VBox(10);
        for (Control control : controles) {
            contenedorControles.getChildren().add(control);
        }

        seccion.getChildren().addAll(lblTitulo, contenedorControles);
        return seccion;
    }

    private PasswordField crearCampoPassword(String prompt, String placeholder) {
        PasswordField campo = new PasswordField();
        campo.setPromptText(placeholder);
        campo.setStyle("-fx-background-color: #f8f9fa; " +
                "-fx-border-color: #dfe6e9; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10; " +
                "-fx-font-size: 14;");
        campo.setMinHeight(40);

        // Efecto hover
        campo.setOnMouseEntered(e -> campo.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #b2bec3;"));
        campo.setOnMouseExited(e -> campo.setStyle("-fx-background-color: #f8f9fa; " +
                "-fx-border-color: #dfe6e9;"));
        return campo;
    }

    private CheckBox crearCheckBox(String texto, boolean seleccionado) {
        CheckBox checkBox = new CheckBox(texto);
        checkBox.setSelected(seleccionado);
        checkBox.setStyle("-fx-font-size: 14; " +
                "-fx-text-fill: #2d3436; " +
                "-fx-font-weight: bold;");
        return checkBox;
    }

    private ComboBox<String> crearComboBox(String prompt, String[] opciones) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(opciones);
        comboBox.setPromptText(prompt);
        comboBox.setStyle("-fx-background-color: #f8f9fa; " +
                "-fx-border-color: #dfe6e9; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 5; " +
                "-fx-font-size: 14;");
        comboBox.setMinHeight(40);

        // Efecto hover
        comboBox.setOnMouseEntered(e -> comboBox.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #b2bec3;"));
        comboBox.setOnMouseExited(e -> comboBox.setStyle("-fx-background-color: #f8f9fa; " +
                "-fx-border-color: #dfe6e9;"));
        return comboBox;
    }

    private Button crearBotonAccion(String texto, String colorBase, String colorHover) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + colorBase + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Efecto hover
        boton.setOnMouseEntered(e -> {
            boton.setStyle("-fx-background-color: " + colorHover + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 3);");
            boton.setCursor(javafx.scene.Cursor.HAND);
        });

        boton.setOnMouseExited(e -> {
            boton.setStyle("-fx-background-color: " + colorBase + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        });

        // Efecto al presionar
        boton.setOnMousePressed(e -> {
            boton.setStyle("-fx-background-color: derive(" + colorHover + ", -20%); " +
                    "-fx-text-fill: white;");
        });

        boton.setOnMouseReleased(e -> {
            boton.setStyle("-fx-background-color: " + colorHover + "; " +
                    "-fx-text-fill: white;");
        });

        return boton;
    }

    public VBox getVista() {
        return vista;
    }
}
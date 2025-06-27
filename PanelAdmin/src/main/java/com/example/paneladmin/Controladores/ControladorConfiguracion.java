package com.example.paneladmin.Controladores;

import com.example.paneladmin.Vistas.VistaConfiguracion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ControladorConfiguracion {
    private VistaConfiguracion vista;

    // Componentes de formulario
    private TextField campoNombreCuenta;
    private PasswordField campoContrasenaActual;
    private PasswordField campoNuevaContrasena;
    private CheckBox checkNotificacionesApp;
    private ComboBox<String> comboEstilos;

    public ControladorConfiguracion(VistaConfiguracion vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        // Contenedor principal con scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: #bdc3c7;");

        VBox contenidoPrincipal = new VBox(20);
        contenidoPrincipal.setPadding(new Insets(20));
        contenidoPrincipal.setStyle("-fx-background-color: white;");

        // Título
        Label titulo = new Label("Configuración del Sistema");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Sección Información de Cuenta
        VBox seccionCuenta = crearSeccionCuenta();

        // Sección Seguridad
        VBox seccionSeguridad = crearSeccionSeguridad();

        // Sección Apariencia
        VBox seccionApariencia = crearSeccionApariencia();

        // Sección Notificaciones
        VBox seccionNotificaciones = crearSeccionNotificaciones();

        contenidoPrincipal.getChildren().addAll(
                titulo,
                seccionCuenta,
                new Separator(),
                seccionSeguridad,
                new Separator(),
                seccionApariencia,
                new Separator(),
                seccionNotificaciones
        );

        scrollPane.setContent(contenidoPrincipal);
        vista.getVista().getChildren().add(scrollPane);
    }

    private VBox crearSeccionCuenta() {
        VBox seccion = new VBox(15);
        seccion.setPadding(new Insets(15));
        seccion.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        Label titulo = new Label("Información de la Cuenta");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        campoNombreCuenta = new TextField();
        campoNombreCuenta.setPromptText("Nombre de la cuenta");
        campoNombreCuenta.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-padding: 8;");

        // Botón de confirmar para esta sección
        Button btnConfirmarCuenta = new Button("Guardar Cambios de Cuenta");
        btnConfirmarCuenta.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirmarCuenta.setOnAction(e -> {
            if (campoNombreCuenta.getText().isEmpty()) {
                mostrarAlerta("Error", "El nombre de cuenta no puede estar vacío");
            } else {
                mostrarAlerta("Éxito", "Información de cuenta actualizada correctamente");
            }
        });

        HBox contenedorBotones = new HBox(btnConfirmarCuenta);
        contenedorBotones.setAlignment(Pos.CENTER_RIGHT);

        seccion.getChildren().addAll(titulo, campoNombreCuenta, contenedorBotones);
        return seccion;
    }

    private VBox crearSeccionSeguridad() {
        VBox seccion = new VBox(15);
        seccion.setPadding(new Insets(15));
        seccion.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        Label titulo = new Label("Seguridad");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        campoContrasenaActual = new PasswordField();
        campoContrasenaActual.setPromptText("Contraseña actual");
        campoContrasenaActual.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-padding: 8;");

        campoNuevaContrasena = new PasswordField();
        campoNuevaContrasena.setPromptText("Nueva contraseña");
        campoNuevaContrasena.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-padding: 8;");

        // Botón de confirmar para esta sección
        Button btnConfirmarSeguridad = new Button("Cambiar Contraseña");
        btnConfirmarSeguridad.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirmarSeguridad.setOnAction(e -> {
            if (campoContrasenaActual.getText().isEmpty() || campoNuevaContrasena.getText().isEmpty()) {
                mostrarAlerta("Error", "Ambos campos de contraseña son requeridos");
            } else if (campoNuevaContrasena.getText().length() < 8) {
                mostrarAlerta("Error", "La nueva contraseña debe tener al menos 8 caracteres");
            } else {
                mostrarAlerta("Éxito", "Contraseña cambiada correctamente");
                campoContrasenaActual.clear();
                campoNuevaContrasena.clear();
            }
        });

        HBox contenedorBotones = new HBox(btnConfirmarSeguridad);
        contenedorBotones.setAlignment(Pos.CENTER_RIGHT);

        seccion.getChildren().addAll(titulo, campoContrasenaActual, campoNuevaContrasena, contenedorBotones);
        return seccion;
    }

    private VBox crearSeccionApariencia() {
        VBox seccion = new VBox(15);
        seccion.setPadding(new Insets(15));
        seccion.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        Label titulo = new Label("Apariencia");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        comboEstilos = new ComboBox<>();
        comboEstilos.getItems().addAll("Claro", "Oscuro", "Sistema");
        comboEstilos.setPromptText("Seleccione un tema");
        comboEstilos.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");

        // Botón de confirmar para esta sección
        Button btnConfirmarApariencia = new Button("Aplicar Tema");
        btnConfirmarApariencia.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirmarApariencia.setOnAction(e -> {
            if (comboEstilos.getSelectionModel().isEmpty()) {
                mostrarAlerta("Error", "Por favor seleccione un tema");
            } else {
                mostrarAlerta("Éxito", "Tema aplicado: " + comboEstilos.getValue());
            }
        });

        HBox contenedorBotones = new HBox(btnConfirmarApariencia);
        contenedorBotones.setAlignment(Pos.CENTER_RIGHT);

        seccion.getChildren().addAll(titulo, comboEstilos, contenedorBotones);
        return seccion;
    }

    private VBox crearSeccionNotificaciones() {
        VBox seccion = new VBox(15);
        seccion.setPadding(new Insets(15));
        seccion.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        Label titulo = new Label("Notificaciones");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        checkNotificacionesApp = new CheckBox("Recibir notificaciones en la aplicación");
        checkNotificacionesApp.setStyle("-fx-font-weight: bold;");

        // Botón de confirmar para esta sección
        Button btnConfirmarNotificaciones = new Button("Guardar Preferencias");
        btnConfirmarNotificaciones.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirmarNotificaciones.setOnAction(e -> {
            String estado = checkNotificacionesApp.isSelected() ? "activadas" : "desactivadas";
            mostrarAlerta("Éxito", "Notificaciones " + estado + " correctamente");
        });

        HBox contenedorBotones = new HBox(btnConfirmarNotificaciones);
        contenedorBotones.setAlignment(Pos.CENTER_RIGHT);

        seccion.getChildren().addAll(titulo, checkNotificacionesApp, contenedorBotones);
        return seccion;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
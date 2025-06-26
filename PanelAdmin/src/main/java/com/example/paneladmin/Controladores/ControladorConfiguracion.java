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

        // Botones de acción
        HBox contenedorBotones = crearContenedorBotones();

        contenidoPrincipal.getChildren().addAll(
                titulo,
                seccionCuenta,
                new Separator(),
                seccionSeguridad,
                new Separator(),
                seccionApariencia,
                new Separator(),
                seccionNotificaciones,
                contenedorBotones
        );

        scrollPane.setContent(contenidoPrincipal);
        vista.getVista().getChildren().add(scrollPane);
    }

    private VBox crearSeccionCuenta() {
        VBox seccion = new VBox(10);
        seccion.setPadding(new Insets(15));
        seccion.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        Label titulo = new Label("Información de la Cuenta");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        campoNombreCuenta = new TextField();
        campoNombreCuenta.setPromptText("Nombre de la cuenta");
        campoNombreCuenta.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-padding: 8;");

        seccion.getChildren().addAll(titulo, campoNombreCuenta);
        return seccion;
    }

    private VBox crearSeccionSeguridad() {
        VBox seccion = new VBox(10);
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

        seccion.getChildren().addAll(titulo, campoContrasenaActual, campoNuevaContrasena);
        return seccion;
    }

    private VBox crearSeccionApariencia() {
        VBox seccion = new VBox(10);
        seccion.setPadding(new Insets(15));
        seccion.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        Label titulo = new Label("Apariencia");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        comboEstilos = new ComboBox<>();
        comboEstilos.getItems().addAll("Claro", "Oscuro", "Sistema");
        comboEstilos.setPromptText("Seleccione un tema");
        comboEstilos.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");

        seccion.getChildren().addAll(titulo, comboEstilos);
        return seccion;
    }

    private VBox crearSeccionNotificaciones() {
        VBox seccion = new VBox(10);
        seccion.setPadding(new Insets(15));
        seccion.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        Label titulo = new Label("Notificaciones");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        checkNotificacionesApp = new CheckBox("Recibir notificaciones en la aplicación");
        checkNotificacionesApp.setStyle("-fx-font-weight: bold;");

        seccion.getChildren().addAll(titulo, checkNotificacionesApp);
        return seccion;
    }

    private HBox crearContenedorBotones() {
        HBox contenedor = new HBox(15);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setStyle("-fx-padding: 20 0 10 0;");

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");
        btnGuardar.setOnAction(e -> guardarConfiguracion());

        Button btnRestaurar = new Button("Restaurar Valores");
        btnRestaurar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");
        btnRestaurar.setOnAction(e -> restaurarConfiguracion());

        contenedor.getChildren().addAll(btnGuardar, btnRestaurar);
        return contenedor;
    }

    private void guardarConfiguracion() {
        // Validación de contraseña
        if (campoNuevaContrasena.getText().length() > 0 && campoNuevaContrasena.getText().length() < 8) {
            mostrarAlerta("Error", "La contraseña debe tener al menos 8 caracteres");
            return;
        }

        // Lógica para guardar configuración
        mostrarAlerta("Éxito", "Configuración guardada correctamente");
    }

    private void restaurarConfiguracion() {
        // Lógica para restaurar valores por defecto
        campoNombreCuenta.clear();
        campoContrasenaActual.clear();
        campoNuevaContrasena.clear();
        comboEstilos.getSelectionModel().clearSelection();
        checkNotificacionesApp.setSelected(false);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
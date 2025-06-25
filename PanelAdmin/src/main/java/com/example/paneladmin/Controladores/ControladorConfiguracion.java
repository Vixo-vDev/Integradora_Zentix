package com.example.paneladmin.Controladores;

import com.example.paneladmin.Vistas.VistaConfiguracion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ControladorConfiguracion {
    private VistaConfiguracion vista;

    // Componentes de formulario
    private TextField campoNombreCuenta;
    private PasswordField campoContrasenaActual;
    private PasswordField campoNuevaContrasena;
    private CheckBox checkDosFactores;
    private CheckBox checkCorreo;
    private CheckBox checkApp;

    public ControladorConfiguracion(VistaConfiguracion vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        // Título
        Label etiquetaTitulo = new Label("Configuración del Sistema");
        etiquetaTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Panel de pestañas
        TabPane panelPestanas = new TabPane();
        panelPestanas.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");

        // Pestañas
        Tab pestanaGeneral = crearPestanaGeneral();
        Tab pestanaSeguridad = crearPestanaSeguridad();
        Tab pestanaNotificaciones = crearPestanaNotificaciones();

        panelPestanas.getTabs().addAll(pestanaGeneral, pestanaSeguridad, pestanaNotificaciones);

        // Botones de acción
        HBox contenedorBotones = crearContenedorBotones();

        // Configurar la vista completa
        VBox.setVgrow(panelPestanas, Priority.ALWAYS);
        vista.getVista().getChildren().addAll(etiquetaTitulo, panelPestanas, contenedorBotones);
    }

    private Tab crearPestanaGeneral() {
        Tab pestana = new Tab("General");
        pestana.setClosable(false);

        VBox contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f9f9f9;");

        // Sección Información de la cuenta
        Label etiquetaCuenta = new Label("Información de la Cuenta");
        etiquetaCuenta.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        campoNombreCuenta = crearCampoTexto("Nombre de la Cuenta");

        // Sección Apariencia
        Label etiquetaApariencia = new Label("Apariencia");
        etiquetaApariencia.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ComboBox<String> comboEstilos = new ComboBox<>();
        comboEstilos.getItems().addAll("Claro", "Oscuro", "Sistema");
        comboEstilos.setPromptText("Seleccione un tema");

        contenido.getChildren().addAll(
                etiquetaCuenta, campoNombreCuenta,
                new Separator(),
                etiquetaApariencia, comboEstilos
        );

        pestana.setContent(contenido);
        return pestana;
    }

    private Tab crearPestanaSeguridad() {
        Tab pestana = new Tab("Seguridad");
        pestana.setClosable(false);

        VBox contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f9f9f9;");

        Label etiquetaSeguridad = new Label("Configuración de Seguridad");
        etiquetaSeguridad.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        campoContrasenaActual = crearCampoPassword("Contraseña actual");
        campoNuevaContrasena = crearCampoPassword("Nueva contraseña");
        checkDosFactores = new CheckBox("Autenticación de dos factores");
        checkDosFactores.setStyle("-fx-font-weight: bold;");

        contenido.getChildren().addAll(
                etiquetaSeguridad, campoContrasenaActual, campoNuevaContrasena, checkDosFactores
        );

        pestana.setContent(contenido);
        return pestana;
    }

    private Tab crearPestanaNotificaciones() {
        Tab pestana = new Tab("Notificaciones");
        pestana.setClosable(false);

        VBox contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f9f9f9;");

        Label etiquetaNotificaciones = new Label("Configuración de Notificaciones");
        etiquetaNotificaciones.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        checkCorreo = new CheckBox("Notificaciones por correo");
        checkApp = new CheckBox("Notificaciones en la aplicación");

        contenido.getChildren().addAll(
                etiquetaNotificaciones, checkCorreo, checkApp
        );

        pestana.setContent(contenido);
        return pestana;
    }

    private TextField crearCampoTexto(String prompt) {
        TextField campo = new TextField();
        campo.setPromptText(prompt);
        campo.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-padding: 8;");
        return campo;
    }

    private PasswordField crearCampoPassword(String prompt) {
        PasswordField campo = new PasswordField();
        campo.setPromptText(prompt);
        campo.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-padding: 8;");
        return campo;
    }

    private HBox crearContenedorBotones() {
        HBox contenedor = new HBox(15);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setStyle("-fx-padding: 15 0;");

        Button btnGuardar = crearBotonAccion("Guardar Cambios", "#2ecc71");
        Button btnRestaurar = crearBotonAccion("Restaurar Valores", "#3498db");
        Button btnCancelar = crearBotonAccion("Cancelar", "#e74c3c");

        // Configurar eventos
        btnGuardar.setOnAction(e -> guardarConfiguracion());
        btnRestaurar.setOnAction(e -> restaurarConfiguracion());
        btnCancelar.setOnAction(e -> cancelarCambios());

        contenedor.getChildren().addAll(btnGuardar, btnRestaurar, btnCancelar);
        return contenedor;
    }

    private Button crearBotonAccion(String texto, String color) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 5;");

        boton.setOnMouseEntered(e -> boton.setStyle(
                "-fx-background-color: derive(" + color + ", -20%); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 5;"
        ));

        boton.setOnMouseExited(e -> boton.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 5;"
        ));

        return boton;
    }

    // Métodos de acción
    private void guardarConfiguracion() {
        System.out.println("Guardando configuración...");
        // Validar campos
        if (campoNuevaContrasena.getText().length() > 0 && campoNuevaContrasena.getText().length() < 8) {
            mostrarAlerta("Error", "La contraseña debe tener al menos 8 caracteres");
            return;
        }

        // Aquí iría la lógica para guardar la configuración
        mostrarAlerta("Éxito", "Configuración guardada correctamente");
    }

    private void restaurarConfiguracion() {
        System.out.println("Restaurando valores por defecto...");
        // Aquí iría la lógica para restaurar valores por defecto
        mostrarAlerta("Información", "Valores por defecto restaurados");
    }

    private void cancelarCambios() {
        System.out.println("Cancelando cambios...");
        // Aquí iría la lógica para cancelar cambios
        mostrarAlerta("Información", "Cambios descartados");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
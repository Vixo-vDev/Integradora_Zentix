package com.example.netrixapp.Vistas;

import config.ConnectionBD;
import com.example.netrixapp.Controladores.ControladorLogin;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.SQLException;

public class VistaLogin extends Application {

    private TextField campoUsuario;
    private PasswordField campoPassword;
    private Button btnConfirmar;

    public String getCampoUsuario() {
        return campoUsuario.getText().trim();
    }

    public String getCampoPassword() {
        return campoPassword.getText().trim();
    }

    public Button getBtnConfirmar() {
        return btnConfirmar;
    }

    @Override
    public void start(Stage ventanaPrincipal) {
        // Configuración básica de la ventana
        ventanaPrincipal.setTitle("Inicio de Sesión - NetrixApp");

        // Obtener dimensiones de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Configurar para ocupar toda la pantalla pero manteniendo controles de ventana
        ventanaPrincipal.setX(screenBounds.getMinX());
        ventanaPrincipal.setY(screenBounds.getMinY());
        ventanaPrincipal.setWidth(screenBounds.getWidth());
        ventanaPrincipal.setHeight(screenBounds.getHeight());

        // Permitir redimensionamiento y maximización
        ventanaPrincipal.setResizable(true);
        ventanaPrincipal.setMaximized(true);  // Iniciar maximizada pero permitir restaurar

        // Contenedor principal con estilo moderno
        HBox contenedorPrincipal = new HBox();
        contenedorPrincipal.setStyle("-fx-background-color: #f5f7fa;");

        // Panel izquierdo con tamaño relativo
        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.prefWidthProperty().bind(ventanaPrincipal.widthProperty().multiply(0.5));

        // Gradiente moderno para el fondo
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#D9D9D9")),
                new Stop(1, Color.web("#CCCCCC")));
        panelIzquierdo.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Logo que se ajusta al tamaño
        ImageView logoUtezV = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/logo_utez.png")));
        logoUtezV.setPreserveRatio(true);
        logoUtezV.fitWidthProperty().bind(panelIzquierdo.widthProperty().multiply(0.4));
        logoUtezV.setEffect(new DropShadow(10, Color.BLACK));

        VBox contenedorLogo = new VBox(logoUtezV);
        contenedorLogo.setAlignment(Pos.CENTER);
        contenedorLogo.setPadding(new Insets(40));

        // Texto de bienvenida
        Text textoBienvenida = new Text("Bienvenido a NetrixApp");
        textoBienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        textoBienvenida.setFill(Color.WHITE);

        VBox contenedorVertical = new VBox(20, contenedorLogo, textoBienvenida);
        contenedorVertical.setAlignment(Pos.CENTER);

        // Panel derecho con formulario
        VBox panelDerecho = new VBox(30);
        panelDerecho.setPadding(new Insets(60, 80, 40, 80));  // Más padding en pantallas grandes
        panelDerecho.setAlignment(Pos.TOP_CENTER);
        panelDerecho.prefWidthProperty().bind(ventanaPrincipal.widthProperty().multiply(0.5));
        panelDerecho.setStyle("-fx-background-color: white;");
        panelDerecho.setMinWidth(500);  // Ancho mínimo para buena visualización

        // Título con estilo moderno
        Label titulo = new Label("Iniciar Sesión");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.web("#333333"));

        // Subtítulo
        Label subtitulo = new Label("Ingresa tus credenciales para continuar");
        subtitulo.setFont(Font.font("Segoe UI", 14));
        subtitulo.setTextFill(Color.web("#666666"));

        // Formulario con estilo moderno
        GridPane gridCampos = new GridPane();
        gridCampos.setAlignment(Pos.CENTER);
        gridCampos.setHgap(15);
        gridCampos.setVgap(20);
        gridCampos.setPadding(new Insets(20, 0, 20, 0));

        // Campo de usuario
        Label etiquetaUsuario = new Label("Usuario:");
        etiquetaUsuario.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        etiquetaUsuario.setTextFill(Color.web("#444444"));

        campoUsuario = new TextField();
        campoUsuario.setPrefWidth(300);
        campoUsuario.setPrefHeight(40);
        campoUsuario.setStyle("-fx-font-size: 14; -fx-padding: 0 10; -fx-background-radius: 4;");
        campoUsuario.setPromptText("Ingresa tu usuario");

        // Campo de contraseña
        Label etiquetaContrasena = new Label("Contraseña:");
        etiquetaContrasena.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        etiquetaContrasena.setTextFill(Color.web("#444444"));

        campoPassword = new PasswordField();
        campoPassword.setPrefWidth(300);
        campoPassword.setPrefHeight(40);
        campoPassword.setStyle("-fx-font-size: 14; -fx-padding: 0 10; -fx-background-radius: 4;");
        campoPassword.setPromptText("Ingresa tu contraseña");

        // Enlace para recuperar contraseña
        Hyperlink enlaceContrasenaOlvidada = new Hyperlink("¿Olvidaste tu contraseña?");
        enlaceContrasenaOlvidada.setFont(Font.font("Segoe UI", 12));
        enlaceContrasenaOlvidada.setTextFill(Color.web("#009475"));
        enlaceContrasenaOlvidada.setBorder(Border.EMPTY);
        enlaceContrasenaOlvidada.setPadding(new Insets(0));

        // Botón de confirmar con efecto hover
        btnConfirmar = new Button("Continuar");
        btnConfirmar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        btnConfirmar.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;");
        btnConfirmar.setPrefWidth(300);
        btnConfirmar.setPrefHeight(45);

        // Efecto hover para el botón
        btnConfirmar.setOnMouseEntered(e -> btnConfirmar.setStyle("-fx-background-color: #007a63; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnConfirmar.setOnMouseExited(e -> btnConfirmar.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;"));

        // Inicializar controlador
        new ControladorLogin(this);

        // Panel inferior con opción de registro
        HBox panelInferior = new HBox(5);
        panelInferior.setAlignment(Pos.CENTER);

        Label etiquetaSinCuenta = new Label("¿No tienes una cuenta?");
        etiquetaSinCuenta.setFont(Font.font("Segoe UI", 12));
        etiquetaSinCuenta.setTextFill(Color.web("#666666"));

        Hyperlink enlaceCrearCuenta = new Hyperlink("Regístrate ahora");
        enlaceCrearCuenta.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        enlaceCrearCuenta.setTextFill(Color.web("#009475"));
        enlaceCrearCuenta.setBorder(Border.EMPTY);
        enlaceCrearCuenta.setPadding(new Insets(0));

        panelInferior.getChildren().addAll(etiquetaSinCuenta, enlaceCrearCuenta);

        enlaceCrearCuenta.setOnAction(e -> {
            VistaRegistro registro = new VistaRegistro();
            registro.start(ventanaPrincipal);
        });

        // Añadir elementos al grid
        gridCampos.add(etiquetaUsuario, 0, 0);
        gridCampos.add(campoUsuario, 0, 1);
        gridCampos.add(etiquetaContrasena, 0, 2);
        gridCampos.add(campoPassword, 0, 3);
        gridCampos.add(enlaceContrasenaOlvidada, 0, 4);

        // Construir panel derecho
        panelDerecho.getChildren().addAll(titulo, subtitulo, gridCampos, btnConfirmar, panelInferior);

        // Construir panel izquierdo
        panelIzquierdo.getChildren().add(contenedorVertical);

        // Añadir paneles al contenedor principal
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        HBox.setHgrow(panelIzquierdo, Priority.ALWAYS);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        panelIzquierdo.setMaxWidth(Double.MAX_VALUE);
        panelDerecho.setMaxWidth(Double.MAX_VALUE);

        ventanaPrincipal.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue();
            // Ajustar tamaños relativos
            if (newWidth < 1200) {
                panelDerecho.setPadding(new Insets(40, 40, 40, 40));
            } else {
                panelDerecho.setPadding(new Insets(60, 80, 40, 80));
            }
        });

        // Configurar escena
        Scene escena = new Scene(contenedorPrincipal);
        ventanaPrincipal.setScene(escena);

        // Centrar contenido cuando se restaura
        ventanaPrincipal.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (!isNowMaximized) {
                ventanaPrincipal.centerOnScreen();
            }
        });
        ventanaPrincipal.show();
    }

    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        // Estilo moderno para la alerta
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14;");
        alert.showAndWait();
    }

    public static void main(String[] args) throws SQLException {
        ConnectionBD con = new ConnectionBD();
        con.getConnection();
        launch(args);
    }
}

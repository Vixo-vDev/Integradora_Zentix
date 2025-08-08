package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorLogin;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        ventanaPrincipal.setTitle("Inicio de Sesión");

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        ventanaPrincipal.setX(screenBounds.getMinX());
        ventanaPrincipal.setY(screenBounds.getMinY());
        ventanaPrincipal.setWidth(screenBounds.getWidth());
        ventanaPrincipal.setHeight(screenBounds.getHeight());

        ventanaPrincipal.setResizable(true);
        ventanaPrincipal.setMaximized(true);

        StackPane contenedorPrincipal = new StackPane();
        contenedorPrincipal.setStyle("-fx-background-color: #f0f0f0;");

        Pane fondoAnimado = crearFondoAnimadoMejorado(screenBounds);
        contenedorPrincipal.getChildren().add(fondoAnimado);

        VBox contenedorFormulario = new VBox(20);
        contenedorFormulario.setAlignment(Pos.TOP_CENTER);
        contenedorFormulario.setPadding(new Insets(40, 60, 40, 60));
        contenedorFormulario.setMaxWidth(450);
        contenedorFormulario.setMaxHeight(600);
        contenedorFormulario.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10;");
        contenedorFormulario.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.3)));

        StackPane.setAlignment(contenedorFormulario, Pos.CENTER);
        StackPane.setMargin(contenedorFormulario, new Insets(20));


        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/logo_utez.png")));
        logo.setPreserveRatio(true);
        logo.setFitWidth(150);
        logo.setEffect(new DropShadow(5, Color.BLACK));

        Label titulo = new Label("Iniciar sesión");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#333333"));

        GridPane gridCampos = crearFormulario(ventanaPrincipal);


        contenedorFormulario.getChildren().addAll(logo, titulo, gridCampos);


        StackPane.setAlignment(contenedorFormulario, Pos.CENTER);
        contenedorPrincipal.getChildren().add(contenedorFormulario);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();

        // Aquí sí: instanciar el controlador después de que todo esté creado
        new ControladorLogin(this);
    }

    private GridPane crearFormulario(Stage ventanaPrincipal) {
        GridPane gridCampos = new GridPane();
        gridCampos.setAlignment(Pos.CENTER);
        gridCampos.setHgap(15);
        gridCampos.setVgap(20);
        gridCampos.setPadding(new Insets(20, 0, 20, 0));
        gridCampos.setMaxHeight(Region.USE_PREF_SIZE);

        // Campo de usuario
        Label etiquetaUsuario = new Label("Usuario:");
        etiquetaUsuario.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        etiquetaUsuario.setTextFill(Color.web("#444444"));

        campoUsuario = new TextField();
        campoUsuario.setPrefWidth(300);
        campoUsuario.setPrefHeight(40);
        campoUsuario.setStyle("-fx-font-size: 14; -fx-padding: 0 10; -fx-background-radius: 4; -fx-border-color: #ccc; -fx-border-radius: 4;");
        campoUsuario.setPromptText("Ingrese su correo");

        // Campo de contraseña
        Label etiquetaContrasena = new Label("Contraseña:");
        etiquetaContrasena.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        etiquetaContrasena.setTextFill(Color.web("#444444"));

        campoPassword = new PasswordField();
        campoPassword.setPrefWidth(300);
        campoPassword.setPrefHeight(40);
        campoPassword.setStyle("-fx-font-size: 14; -fx-padding: 0 10; -fx-background-radius: 4; -fx-border-color: #ccc; -fx-border-radius: 4;");
        campoPassword.setPromptText("Ingrese su contraseña");

        // Botón de confirmar
        btnConfirmar = new Button("Iniciar");
        btnConfirmar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btnConfirmar.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;");
        btnConfirmar.setPrefWidth(300);
        btnConfirmar.setPrefHeight(45);

        // Efecto hover para el botón
        btnConfirmar.setOnMouseEntered(e -> btnConfirmar.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnConfirmar.setOnMouseExited(e -> btnConfirmar.setStyle("-fx-background-color: #008B6A; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;"));

        // Etiqueta y enlace para registro
        Label etiquetaSinCuenta = new Label("¿No tienes una cuenta?");
        etiquetaSinCuenta.setFont(Font.font("Segoe UI", 12));
        etiquetaSinCuenta.setTextFill(Color.web("#666666"));

        Hyperlink enlaceCrearCuenta = new Hyperlink("Regístrate ahora");
        enlaceCrearCuenta.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        enlaceCrearCuenta.setTextFill(Color.web("#009475"));
        enlaceCrearCuenta.setBorder(Border.EMPTY);
        enlaceCrearCuenta.setPadding(new Insets(0));

        // Acción para abrir la ventana de registro
        enlaceCrearCuenta.setOnAction(e -> {
            VistaRegistro registro = new VistaRegistro();
            try {
                registro.start(ventanaPrincipal);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Contenedor horizontal para registro
        HBox registroBox = new HBox(5, etiquetaSinCuenta, enlaceCrearCuenta);
        registroBox.setAlignment(Pos.CENTER);

        // Contenedor vertical para botón + texto registro
        VBox contenedorBotonYRegistro = new VBox(20, btnConfirmar, registroBox);
        contenedorBotonYRegistro.setAlignment(Pos.CENTER);

        // Inicializar controlador
        new ControladorLogin(this);

        // Añadimos todo al grid
        gridCampos.add(etiquetaUsuario, 0, 0);
        gridCampos.add(campoUsuario, 0, 1);
        gridCampos.add(etiquetaContrasena, 0, 2);
        gridCampos.add(campoPassword, 0, 3);
        gridCampos.add(contenedorBotonYRegistro, 0, 4);

        return gridCampos;
    }



    private Pane crearFondoAnimadoMejorado(Rectangle2D screenBounds) {
        Pane pane = new Pane();
        pane.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());

        Color[] colores = {
                Color.web("#D9D9D9", 0.3),
                Color.web("#E0E0E0", 0.3),
                Color.web("#D0D0D0", 0.3),
                Color.web("#D9E0E0", 0.3),
                Color.web("#E0D9D9", 0.3)
        };

        for (int i = 0; i < 20; i++) {
            Rectangle rect = new Rectangle(
                    Math.random() * screenBounds.getWidth(),
                    Math.random() * screenBounds.getHeight(),
                    50 + Math.random() * 100,
                    50 + Math.random() * 100
            );

            Color colorBase = colores[(int)(Math.random() * colores.length)];

            double red = clamp(colorBase.getRed() + (Math.random() * 0.2 - 0.1));
            double green = clamp(colorBase.getGreen() + (Math.random() * 0.2 - 0.1));
            double blue = clamp(colorBase.getBlue() + (Math.random() * 0.2 - 0.1));
            double opacity = 0.3 + Math.random() * 0.3;

            Color colorFinal = new Color(red, green, blue, opacity);

            rect.setFill(colorFinal);
            rect.setRotate(Math.random() * 360);
            rect.setStroke(Color.web("#D9D9D9", 0.2));
            rect.setStrokeWidth(1);
            pane.getChildren().add(rect);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(rect.translateXProperty(), Math.random() * 100 - 50),
                            new KeyValue(rect.translateYProperty(), Math.random() * 100 - 50),
                            new KeyValue(rect.rotateProperty(), Math.random() * 360)
                    ),
                    new KeyFrame(Duration.seconds(8 + Math.random() * 15),
                            new KeyValue(rect.translateXProperty(), Math.random() * 300 - 150),
                            new KeyValue(rect.translateYProperty(), Math.random() * 300 - 150),
                            new KeyValue(rect.rotateProperty(), rect.getRotate() + (Math.random() * 180 - 90))
                    )
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.setAutoReverse(true);
            timeline.play();
        }

        return pane;
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }

    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14;");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

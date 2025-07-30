package com.example.netrixapp.Vistas;


import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Controladores.ControladorLogin;
import com.example.netrixapp.Controladores.ControladorRegistro;
import com.example.netrixapp.Controladores.ControladorSolicitudes;
import com.example.netrixapp.HelloApplication;
import impl.UsuarioDaoImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;

public class VistaLogin extends Application {

    private TextField campoUsuario;
    private PasswordField campoPassword;
    private Button btnConfirmar;

    public String getCampoUsuario() {
        return campoUsuario.getText().trim();
    }

    public
    String getCampoPassword() {
        return campoPassword.getText().trim();
    }

    public Button  getBtnConfirmar() {
        return btnConfirmar;
    }

    @Override
    public void start(Stage ventanaPrincipal) {
        ventanaPrincipal.setWidth(1000);
        ventanaPrincipal.setHeight(600);

        HBox contenedorPrincipal = new HBox();

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.setPrefWidth(500);

        Image logoUtez = new Image(getClass().getResourceAsStream("/imagenes/logo_utez.png"));
        ImageView logoUtezV = new ImageView(logoUtez);
        logoUtezV.setFitHeight(100);
        logoUtezV.setFitWidth(200);
        logoUtezV.setPreserveRatio(true);

        VBox contenedorVertical = new VBox(logoUtezV);
        contenedorVertical.setAlignment(Pos.TOP_CENTER);  // imagen arriba y centrada
        contenedorVertical.setPadding(new Insets(20));    // espacio superior


        Rectangle rectanguloFondo = new Rectangle();
        rectanguloFondo.setFill(Color.LIGHTGRAY);
        rectanguloFondo.widthProperty().bind(panelIzquierdo.widthProperty());
        rectanguloFondo.heightProperty().bind(panelIzquierdo.heightProperty());

        panelIzquierdo.getChildren().addAll(rectanguloFondo);

        VBox panelDerecho = new VBox(20);
        panelDerecho.setPadding(new Insets(40));
        panelDerecho.setAlignment(Pos.CENTER);
        panelDerecho.setPrefWidth(500);

        Label titulo = new Label("Inicio Sesión");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        GridPane gridCampos = new GridPane();
        gridCampos.setAlignment(Pos.CENTER);
        gridCampos.setHgap(15);
        gridCampos.setVgap(15);

        Label etiquetaUsuario = new Label("Usuario:");
        etiquetaUsuario.setFont(Font.font("Arial", 16));
        campoUsuario = new TextField();
        campoUsuario.setPrefWidth(250);
        campoUsuario.setPrefHeight(35);

        Label etiquetaContrasena = new Label("Contraseña:");
        etiquetaContrasena.setFont(Font.font("Arial", 16));
        campoPassword = new PasswordField();
        campoPassword.setPrefWidth(250);
        campoPassword.setPrefHeight(35);

        Hyperlink enlaceContrasenaOlvidada = new Hyperlink("Se te olvido la contrasena?");
        enlaceContrasenaOlvidada.setFont(Font.font("Arial", 14));
        enlaceContrasenaOlvidada.setTextFill(Color.BLUE);

        btnConfirmar = new Button("Continuar");
        btnConfirmar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btnConfirmar.setStyle("-fx-background-color: #009475;; -fx-text-fill: white;");
        btnConfirmar.setPrefWidth(250);
        btnConfirmar.setPrefHeight(40);
        new ControladorLogin(this);

        HBox panelInferior = new HBox(5);
        panelInferior.setAlignment(Pos.CENTER);
        Label etiquetaSinCuenta = new Label("No tienes una cuenta?");
        Hyperlink enlaceCrearCuenta = new Hyperlink("Crea una cuenta");
        enlaceCrearCuenta.setFont(Font.font("Arial", 14));
        enlaceCrearCuenta.setTextFill(Color.BLUE);
        panelInferior.getChildren().addAll(etiquetaSinCuenta, enlaceCrearCuenta);

        enlaceCrearCuenta.setOnAction(e -> {
            VistaRegistro registro = new VistaRegistro();
            registro.start(ventanaPrincipal);
        });

        gridCampos.add(etiquetaUsuario, 0, 0);
        gridCampos.add(campoUsuario, 0, 1);
        gridCampos.add(etiquetaContrasena, 0, 2);
        gridCampos.add(campoPassword, 0, 3);
        gridCampos.add(enlaceContrasenaOlvidada, 0, 4);

        panelIzquierdo.getChildren().addAll(contenedorVertical);
        panelDerecho.getChildren().addAll(titulo, gridCampos, btnConfirmar, panelInferior);
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.setTitle("Inicio de Sesion");
        ventanaPrincipal.show();
    }

    public void showAlert(String title, String msg){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    
    public static void main(String[] args) throws SQLException {
        ConnectionBD con = new ConnectionBD();
        con.getConnection();
        launch(args);
    }
}
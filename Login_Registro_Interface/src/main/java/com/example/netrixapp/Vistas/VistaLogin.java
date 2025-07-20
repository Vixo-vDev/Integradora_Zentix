package com.example.netrixapp.Vistas;


import com.example.netrixapp.Controladores.ConnectionBD;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        TextField campoUsuario = new TextField();
        campoUsuario.setPrefWidth(250);
        campoUsuario.setPrefHeight(35);

        Label etiquetaContrasena = new Label("Contraseña:");
        etiquetaContrasena.setFont(Font.font("Arial", 16));
        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPrefWidth(250);
        campoContrasena.setPrefHeight(35);

        Hyperlink enlaceContrasenaOlvidada = new Hyperlink("Se te olvido la contrasena?");
        enlaceContrasenaOlvidada.setFont(Font.font("Arial", 14));
        enlaceContrasenaOlvidada.setTextFill(Color.BLUE);

        Button botonContinuar = new Button("Continuar");
        botonContinuar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonContinuar.setStyle("-fx-background-color: #009475;; -fx-text-fill: white;");
        botonContinuar.setPrefWidth(250);
        botonContinuar.setPrefHeight(40);

        botonContinuar.setOnAction(e -> {
            // Crear una nueva raíz vacía para la nueva escena

            Main nuevaVentana = new Main();
            nuevaVentana.mostrar(ventanaPrincipal);

            // Reutilizamos el mismo Stage
        });


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
        gridCampos.add(campoContrasena, 0, 3);
        gridCampos.add(enlaceContrasenaOlvidada, 0, 4);

        panelIzquierdo.getChildren().addAll(contenedorVertical);
        panelDerecho.getChildren().addAll(titulo, gridCampos, botonContinuar, panelInferior);
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.setTitle("Inicio de Sesion");
        ventanaPrincipal.show();
    }

    public static void main(String[] args) throws SQLException {
        ConnectionBD con = new ConnectionBD();
        con.getConnection();
        launch(args);
    }
}
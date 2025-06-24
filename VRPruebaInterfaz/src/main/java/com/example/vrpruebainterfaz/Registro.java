package com.example.vrpruebainterfaz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Registro {

    public void start(Stage ventanaRegistro) {

        //Asignando dimensiones de la vetana registro
        ventanaRegistro.setWidth(1000);
        ventanaRegistro.setHeight(700);

        HBox contenedorPrincipal = new HBox();

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.setPrefWidth(500);

        Rectangle rectanguloFondo = new Rectangle();
        rectanguloFondo.setFill(Color.LIGHTGRAY);
        rectanguloFondo.widthProperty().bind(panelIzquierdo.widthProperty());
        rectanguloFondo.heightProperty().bind(panelIzquierdo.heightProperty());

        panelIzquierdo.getChildren().addAll(rectanguloFondo);

        VBox panelDerecho = new VBox(20);
        panelDerecho.setPadding(new Insets(40));
        panelDerecho.setAlignment(Pos.CENTER);
        panelDerecho.setPrefWidth(500);

        Label titulo = new Label("Registrarse");
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

        Label etiquetaContrasena = new Label("Contrasena:");
        etiquetaContrasena.setFont(Font.font("Arial", 16));
        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPrefWidth(250);
        campoContrasena.setPrefHeight(35);

        Button botonContinuar = new Button("Continuar");
        botonContinuar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonContinuar.setStyle("-fx-background-color: #0078d7; -fx-text-fill: white;");
        botonContinuar.setPrefWidth(250);
        botonContinuar.setPrefHeight(40);

        /*botonContinuar.setOnAction(e -> {
            Crear una nueva raíz vacía para la nueva escena

            Main nuevaVentana = new Main();
            nuevaVentana.mostrar(ventanaPrincipal);

            Reutilizamos el mismo Stage
        });*/


        HBox panelInferior = new HBox(5);
        panelInferior.setAlignment(Pos.CENTER);
        Label etiquetaSinCuenta = new Label("No tienes una cuenta?");
        Hyperlink enlaceCrearCuenta = new Hyperlink("Crea una cuenta");
        enlaceCrearCuenta.setFont(Font.font("Arial", 14));
        enlaceCrearCuenta.setTextFill(Color.BLUE);
        panelInferior.getChildren().addAll(etiquetaSinCuenta, enlaceCrearCuenta);

        gridCampos.add(etiquetaUsuario, 0, 0);
        gridCampos.add(campoUsuario, 0, 1);
        gridCampos.add(etiquetaContrasena, 0, 2);
        gridCampos.add(campoContrasena, 0, 3);

        panelDerecho.getChildren().addAll(titulo, gridCampos, botonContinuar, panelInferior);
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaRegistro.setScene(escena);
        ventanaRegistro.setTitle("Registrarse");
        ventanaRegistro.show();
    }

}

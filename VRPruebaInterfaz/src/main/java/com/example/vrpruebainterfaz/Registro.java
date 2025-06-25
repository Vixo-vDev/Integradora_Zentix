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
        ventanaRegistro.setHeight(600);

        HBox contenedorPrincipal = new HBox();

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.setPrefWidth(300);

        Rectangle rectanguloFondo = new Rectangle();
        rectanguloFondo.setFill(Color.LIGHTGRAY);
        rectanguloFondo.widthProperty().bind(panelIzquierdo.widthProperty());
        rectanguloFondo.heightProperty().bind(panelIzquierdo.heightProperty());

        panelIzquierdo.getChildren().addAll(rectanguloFondo);

        VBox panelDerecho = new VBox(20);
        panelDerecho.setPadding(new Insets(40));
        panelDerecho.setAlignment(Pos.TOP_LEFT);
        panelDerecho.setPrefWidth(700);

        Label titulo = new Label("Registrarse");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        GridPane gridCampos = new GridPane();
        gridCampos.setAlignment(Pos.TOP_LEFT);
        gridCampos.setHgap(15);
        gridCampos.setVgap(12);


        Label section1 = new Label("Información Personal");
        section1.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        //Personalización de campo nombre
        Label etiquetaName = new Label("Nombre");
        etiquetaName.setFont(Font.font("Arial", 15));

        TextField campoName = new TextField();
        campoName.setPrefWidth(200);
        campoName.setPrefHeight(25);

        //Personalización de campo apellido
        Label etiquetaLastName = new Label("Apellidos");
        etiquetaLastName.setFont(Font.font("Arial", 15));

        TextField campoLastName = new TextField();
        campoLastName.setPrefWidth(200);
        campoLastName.setPrefHeight(25);

        //Personalización de campo correo institucional
        Label etiquetaEmail = new Label("Correo Institucional");
        etiquetaEmail.setFont(Font.font("Arial", 15));

        TextField campoEmail = new TextField();
        campoEmail.setPrefWidth(200);
        campoEmail.setPrefHeight(25);

        //Personalización de campo calle
        Label etiquetaCalle = new Label("Calle");
        etiquetaCalle.setFont(Font.font("Arial", 15));

        TextField campoCalle = new TextField();
        campoCalle.setPrefWidth(200);
        campoCalle.setPrefHeight(25);

        //Personalización de campo LADA
        Label etiquetaLada = new Label("Lada ");
        etiquetaLada.setFont(Font.font("Arial", 15));

        TextField campoLada = new TextField();
        campoLada.setPrefWidth(100);
        campoLada.setPrefHeight(25);

        //Personalización de campo Teléfono
        Label etiquetaTelefono = new Label("Lada ");
        etiquetaTelefono.setFont(Font.font("Arial", 15));

        TextField campoTelefono = new TextField();
        campoTelefono.setPrefWidth(150);
        campoTelefono.setPrefHeight(25);

        //Personalizacion de campo date
        Label eitquetaDate = new Label("Fecha");
        DatePicker campofecha = new DatePicker();

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button botonContinuar = new Button("Continuar");
        botonContinuar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonContinuar.setStyle("-fx-background-color: #009475; -fx-text-fill: white;");
        botonContinuar.setPrefWidth(200);
        botonContinuar.setPrefHeight(30);

        Button botonCancelar = new Button("Regresar");
        botonCancelar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonCancelar.setStyle("-fx-background-color: #005994; -fx-text-fill: white;");
        botonCancelar.setPrefWidth(200);
        botonCancelar.setPrefHeight(30);

        botonCancelar.setOnAction(e -> {
           HelloApplication regresar = new HelloApplication();
           regresar.start(ventanaRegistro);
        });

        buttons.getChildren().addAll(botonContinuar, botonCancelar);

        //Etiquetas Columna 0
        gridCampos.add(etiquetaName, 0, 0);
        gridCampos.add(etiquetaLastName, 1, 0);
        gridCampos.add(etiquetaEmail, 2, 0);

        //Etiquetas Columna 1
        gridCampos.add(etiquetaCalle, 0,2);
        girdCampo

        //TextField Columna 0
        gridCampos.add(campoName, 0, 1);
        gridCampos.add(campoLastName, 1, 1);
        gridCampos.add(campoEmail, 2, 1);

        //TextField Columna 1
        gridCampos.add(campoCalle, 0,3);


        panelDerecho.getChildren().addAll(titulo, section1, gridCampos, buttons);
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaRegistro.setScene(escena);
        ventanaRegistro.setTitle("Registrarse");
        ventanaRegistro.show();
    }

}

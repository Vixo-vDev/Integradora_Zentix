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

        //Personalización de campo nombre
        Label etiquetaName = new Label("Nombre");
        etiquetaName.setFont(Font.font("Arial", 15));

        TextField campoName = new TextField();
        campoName.setPrefWidth(200);
        campoName.setPrefHeight(20);

        //Personalización de campo apellido
        Label etiquetaLastName = new Label("Apellido");
        etiquetaLastName.setFont(Font.font("Arial", 15));

        TextField campoLastName = new TextField();
        campoLastName.setPrefWidth(200);
        campoLastName.setPrefHeight(20);

        Button botonContinuar = new Button("Continuar");
        botonContinuar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonContinuar.setStyle("-fx-background-color: #0078d7; -fx-text-fill: white;");
        botonContinuar.setPrefWidth(250);
        botonContinuar.setPrefHeight(40);


        //Etiquetas fila 0
        gridCampos.add(etiquetaName, 0, 0);
        gridCampos.add(etiquetaLastName, 1, 0);

        //TextField fila 0
        gridCampos.add(campoName, 0, 1);
        gridCampos.add(campoLastName, 1, 1);

        panelDerecho.getChildren().addAll(titulo, gridCampos, botonContinuar);
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaRegistro.setScene(escena);
        ventanaRegistro.setTitle("Registrarse");
        ventanaRegistro.show();
    }

}

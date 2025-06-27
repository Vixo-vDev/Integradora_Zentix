package com.example.paneladmin.Vistas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class VistaMain {
    private GridPane vista;

    public VistaMain() {
        vista = new GridPane();
        vista.getStyleClass().add("main-grid");
        vista.setHgap(24);
        vista.setVgap(24);
        vista.setPadding(new Insets(24));

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(33.33);
        vista.getColumnConstraints().addAll(col, col, col);

        agregarCarta("Inventario", "/iconos/box.png");
        agregarCarta("Usuarios", "/iconos/users.png");
        agregarCarta("Estadísticas", "/iconos/graph.png");
        agregarCarta("Formularios", "/iconos/form.png");
    }

    private void agregarCarta(String titulo, String iconoPath) {
        VBox carta = new VBox(16);
        carta.getStyleClass().add("card");
        carta.setAlignment(Pos.CENTER);
        carta.setPadding(new Insets(24));

        ImageView icono = new ImageView(new Image(iconoPath));
        icono.setFitWidth(48);
        icono.setFitHeight(48);
        icono.getStyleClass().add("card-icon");

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("card-title");

        Button btn = new Button("Abrir");
        btn.getStyleClass().addAll("button", "primary-button");

        carta.getChildren().addAll(icono, lblTitulo, btn);
        vista.add(carta, vista.getChildren().size() % 3, vista.getChildren().size() / 3);
    }

    public GridPane getVista() {
        return vista;
    }
}
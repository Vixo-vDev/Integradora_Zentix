package com.example.paneladmin.Vistas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VistaMain {
    private GridPane vista;
    private VistaPrincipal vistaPrincipal;

    public VistaMain(VistaPrincipal vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
        vista = new GridPane();
        configurarDiseno();
        agregarCartas();
    }

    private void configurarDiseno() {
        vista.setAlignment(Pos.CENTER);
        vista.setHgap(20);
        vista.setVgap(20);
        vista.setPadding(new Insets(20));
        vista.setStyle("-fx-background-color: #f5f5f5;");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(34);
        vista.getColumnConstraints().addAll(col1, col2, col3);
    }

    private void agregarCartas() {
        agregarCarta("Inventario", "/iconos/inventario.png", 0, 0);
        agregarCarta("Usuarios", "/iconos/usuarios.png", 1, 0);
        agregarCarta("Estadísticas", "/iconos/estadisticas.png", 2, 0);
        agregarCarta("Formularios", "/iconos/formularios.png", 0, 1);
        agregarCarta("Solicitudes", "/iconos/solicitudes.png", 1, 1);
    }

    private void agregarCarta(String titulo, String iconoPath, int col, int fila) {
        VBox carta = new VBox(15);
        carta.setAlignment(Pos.CENTER);
        carta.setPadding(new Insets(20));
        carta.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        // Icono
        ImageView icono = cargarIcono(iconoPath);

        // Título
        Label labelTitulo = new Label(titulo);
        labelTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelTitulo.setTextFill(Color.web("#333333"));

        // Botón
        Button boton = crearBotonAcceso(titulo);

        carta.getChildren().addAll(icono, labelTitulo, boton);
        vista.add(carta, col, fila);
    }

    private Button crearBotonAcceso(String modulo) {
        Button boton = new Button("Acceder");
        boton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;"));

        boton.setOnAction(e -> {
            switch(modulo) {
                case "Inventario":
                    vistaPrincipal.cambiarContenido(new VistaInventario().getVista());
                    break;
                case "Usuarios":
                    vistaPrincipal.cambiarContenido(new VistaUsuarios().getVista());
                    break;
                // ... otros casos
            }
        });

        return boton;
    }

    private ImageView cargarIcono(String path) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView icono = new ImageView(img);
            icono.setFitWidth(80);
            icono.setFitHeight(80);
            return icono;
        } catch (Exception e) {
            System.err.println("Error al cargar icono: " + path);
            return crearIconoPorDefecto();
        }
    }

    private ImageView crearIconoPorDefecto() {
        ImageView icono = new ImageView();
        icono.setFitWidth(80);
        icono.setFitHeight(80);
        icono.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 40;");
        return icono;
    }

    public GridPane getVista() {
        return vista;
    }
}
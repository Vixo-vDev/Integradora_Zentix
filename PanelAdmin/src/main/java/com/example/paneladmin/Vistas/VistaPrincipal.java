package com.example.paneladmin.Vistas;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class VistaPrincipal {
    private BorderPane raiz;
    private VBox barraLateral;
    private StackPane contenido;

    public VistaPrincipal(String nombreUsuario, String rolUsuario) {
        raiz = new BorderPane();
        raiz.getStyleClass().add("main-container");

        crearBarraLateral(nombreUsuario, rolUsuario);

        contenido = new StackPane();
        contenido.getStyleClass().add("content-container");
        raiz.setCenter(contenido);
    }

    private void crearBarraLateral(String nombreUsuario, String rolUsuario) {
        barraLateral = new VBox(20);
        barraLateral.getStyleClass().add("sidebar");
        barraLateral.setMinWidth(240);

        // Perfil de usuario
        ImageView avatar = new ImageView(new Image("/imagenes/avatar.png"));
        avatar.setFitWidth(64);
        avatar.setFitHeight(64);
        avatar.getStyleClass().add("avatar");

        Label lblNombre = new Label(nombreUsuario);
        lblNombre.getStyleClass().add("user-name");

        Label lblRol = new Label(rolUsuario);
        lblRol.getStyleClass().add("user-role");

        VBox perfil = new VBox(8, avatar, lblNombre, lblRol);
        perfil.getStyleClass().add("profile-container");

        // Menú principal
        VBox menu = new VBox(8);
        menu.getStyleClass().add("menu-container");

        String[] opciones = {"Inventario", "Usuarios", "Estadísticas", "Formularios"};
        for (String opcion : opciones) {
            Button btn = new Button(opcion);
            btn.getStyleClass().add("menu-button");
            btn.setMaxWidth(Double.MAX_VALUE);
            menu.getChildren().add(btn);
        }

        // Pie de página
        Button btnConfig = new Button("Configuración");
        btnConfig.getStyleClass().add("menu-button");

        Button btnSalir = new Button("Cerrar sesión");
        btnSalir.getStyleClass().addAll("menu-button", "danger-button");

        VBox pie = new VBox(8, new Separator(), btnConfig, btnSalir);
        pie.getStyleClass().add("footer-container");

        barraLateral.getChildren().addAll(perfil, new Separator(), menu, pie);
        raiz.setLeft(barraLateral);
    }

    public void setContenido(Region contenido) {
        contenido.getStyleClass().add("content-pane");
        this.contenido.getChildren().setAll(contenido);
    }

    public BorderPane getVista() {
        return raiz;
    }

    public Button getBoton(String texto) {
        return (Button) barraLateral.lookup(".menu-button:text='" + texto + "'");
    }
}
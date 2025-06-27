package com.example.paneladmin.Controladores;

import com.example.paneladmin.Vistas.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ControladorPrincipal {
    private VistaPrincipal vista;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        inicializarUI();
    }

    private void inicializarUI() {
        VBox barraLateral = crearBarraLateral();
        vista.getRaiz().setLeft(barraLateral);
        mostrarDashboardInicio();
    }

    private VBox crearBarraLateral() {
        VBox barra = new VBox();
        barra.setStyle("-fx-background-color: #D9D9D9; " +
                "-fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 5);");
        barra.setAlignment(Pos.TOP_CENTER);
        barra.setSpacing(20);
        barra.setMinWidth(200);
        barra.setPrefWidth(200);

        ImageView iconoPerfil = new ImageView();
        iconoPerfil.setFitWidth(80);
        iconoPerfil.setFitHeight(80);
        iconoPerfil.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255,255,255,0.3), 10, 0, 0, 0);");

        // Información del usuario con imagen y emoji
        VBox infoUsuario = new VBox(5);
        infoUsuario.setAlignment(Pos.CENTER);


        ImageView imagenPerfil = new ImageView(new Image("file:src/main/resources/imagenes/Utez-Logo.png"));
        imagenPerfil.setFitWidth(180);
        imagenPerfil.setFitHeight(180);
        imagenPerfil.setPreserveRatio(true);
        imagenPerfil.setSmooth(true);
        imagenPerfil.setCache(true);
        imagenPerfil.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 2);");

        // Emoji de perfil
        Label iconoUsuario = new Label("👤");
        iconoUsuario.setStyle("-fx-font-size: 36;");

        Label lblNombre = new Label("Nombre");
        lblNombre.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 18; -fx-font-weight: bold;");

        Label lblRol = new Label("Administrador");
        lblRol.setStyle("-fx-text-fill: #555555; -fx-font-size: 14;");

        infoUsuario.getChildren().addAll(imagenPerfil, iconoUsuario, lblNombre, lblRol);

        Button btnInicio = crearBotonLateral("Inicio", "#009475");
        btnInicio.setOnAction(e -> mostrarDashboardInicio());

        Pane espaciador = new Pane();
        VBox.setVgrow(espaciador, Priority.ALWAYS);

        Button btnCerrarSesion = crearBotonLateral("Cerrar Sesión", "#e74c3c");
        btnCerrarSesion.setOnAction(e -> mostrarConfirmacionCerrarSesion());

        barra.getChildren().addAll(iconoPerfil, infoUsuario, btnInicio, espaciador, btnCerrarSesion);
        VBox.setMargin(btnCerrarSesion, new Insets(20, 0, 0, 0));

        return barra;
    }

    private Button crearBotonLateral(String texto, String color) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-min-width: 160; " +
                "-fx-min-height: 40; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);");

        btn.setOnMouseEntered(e -> {
            btn.setStyle(btn.getStyle() +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 2); " +
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(btn.getStyle() +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1); " +
                    "-fx-scale-x: 1.0; -fx-scale-y: 1.0;");
        });

        return btn;
    }

    public void mostrarDashboardInicio() {
        GridPane dashboard = new GridPane();
        dashboard.setStyle("-fx-background-color: #ffff; -fx-padding: 40;");
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setHgap(30);
        dashboard.setVgap(30);

        Button cardUsuarios = crearCard("Gestión de Usuarios", "👥", "Administra los usuarios del sistema");
        Button cardInventario = crearCard("Control de Inventario", "📦", "Gestiona productos y existencias");
        Button cardEstadisticas = crearCard("Estadísticas", "📊", "Reportes y análisis de datos");
        Button cardFormularios = crearCard("Documentación", "📝", "Formularios y documentos");
        Button cardSolicitudes = crearCard("Solicitudes", "🔄", "Gestión de pedidos y solicitudes");
        Button cardConfiguracion = crearCard("Configuración", "⚙️", "Ajustes del sistema");

        cardUsuarios.setOnAction(e -> vista.getRaiz().setCenter(new VistaUsuarios().getVista()));
        cardInventario.setOnAction(e -> vista.getRaiz().setCenter(new VistaInventario().getVista()));
        cardEstadisticas.setOnAction(e -> vista.getRaiz().setCenter(new VistaEstadisticas().getVista()));
        cardFormularios.setOnAction(e -> vista.getRaiz().setCenter(new VistaFormularios().getVista()));
        cardSolicitudes.setOnAction(e -> vista.getRaiz().setCenter(new VistaSolicitudes().getVista()));
        cardConfiguracion.setOnAction(e -> vista.getRaiz().setCenter(new VistaConfiguracion().getVista()));

        dashboard.addRow(0, cardUsuarios, cardInventario, cardEstadisticas);
        dashboard.addRow(1, cardFormularios, cardSolicitudes, cardConfiguracion);

        vista.getRaiz().setCenter(dashboard);
    }

    private Button crearCard(String titulo, String emoji, String descripcion) {
        Button card = new Button();
        card.setStyle("-fx-background-color: #D9D9D9; " +
                "-fx-text-fill: #2c3e50; " +
                "-fx-font-size: 16; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5);");
        card.setMinSize(320, 240);
        card.setMaxSize(320, 240);

        VBox contenido = new VBox(10);
        contenido.setAlignment(Pos.CENTER);

        Label lblEmoji = new Label(emoji);
        lblEmoji.setStyle("-fx-font-size: 48; -fx-text-fill: #2c3e50;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 18;");
        lblTitulo.setWrapText(true);
        lblTitulo.setAlignment(Pos.CENTER);

        Label lblDesc = new Label(descripcion);
        lblDesc.setStyle("-fx-text-fill: #333333; -fx-font-size: 14;");
        lblDesc.setWrapText(true);
        lblDesc.setAlignment(Pos.CENTER);
        lblDesc.setMaxWidth(250);

        Button botonAccion = new Button("Acceder");
        botonAccion.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 10; -fx-padding: 8 16 8 16;");
        botonAccion.setOnAction(card.getOnAction());

        contenido.getChildren().addAll(lblEmoji, lblTitulo, lblDesc, botonAccion);
        card.setGraphic(contenido);

        return card;
    }

    private void mostrarConfirmacionCerrarSesion() {
        System.out.println("Cerrando sesión");
    }
}

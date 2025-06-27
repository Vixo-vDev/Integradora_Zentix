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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

public class ControladorPrincipal {
    private VistaPrincipal vista;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        inicializarUI();
    }

    private void inicializarUI() {
        // Barra lateral izquierda con gradiente
        VBox barraLateral = crearBarraLateral();
        vista.getRaiz().setLeft(barraLateral);
        mostrarDashboardInicio();
    }

    private VBox crearBarraLateral() {
        VBox barra = new VBox();
        // Gradiente azul oscuro a morado
        barra.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #4a148c); " +
                "-fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 5);");
        barra.setAlignment(Pos.TOP_CENTER);
        barra.setSpacing(20);
        barra.setMinWidth(200);
        barra.setPrefWidth(200);

        // Icono de perfil
        ImageView iconoPerfil = new ImageView();
        iconoPerfil.setFitWidth(80);
        iconoPerfil.setFitHeight(80);
        iconoPerfil.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255,255,255,0.3), 10, 0, 0, 0);");
        // Cargar imagen: iconoPerfil.setImage(new Image("ruta/a/tu/perfil.png"));

        // Información del usuario
        VBox infoUsuario = new VBox(5);
        infoUsuario.setAlignment(Pos.CENTER);

        Label lblNombre = new Label("Nombre");
        lblNombre.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");

        Label lblRol = new Label("Administrador");
        lblRol.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 14;");

        infoUsuario.getChildren().addAll(lblNombre, lblRol);

        Button btnInicio = crearBotonLateral("Inicio", "#3498db");
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

        // Efecto hover
        btn.setOnMouseEntered(e -> {
            btn.setStyle(btn.getStyle() +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 2); " +
                    "-fx-scale-x: 1.05; " +
                    "-fx-scale-y: 1.05;");
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(btn.getStyle() +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1); " +
                    "-fx-scale-x: 1.0; " +
                    "-fx-scale-y: 1.0;");
        });

        return btn;
    }

    public void mostrarDashboardInicio() {
        GridPane dashboard = new GridPane();
        // Fondo con gradiente
        dashboard.setStyle("-fx-background-color: linear-gradient(to bottom right, #f5f7fa, #e4e8eb); " +
                "-fx-padding: 40;");
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setHgap(30);
        dashboard.setVgap(30);

        // Cards para cada funcion
        Button cardUsuarios = crearCard("Gestión de Usuarios", "#3498db", "👥", "Administra los usuarios del sistema");
        Button cardInventario = crearCard("Control de Inventario", "#2ecc71", "📦", "Gestiona productos y existencias");
        Button cardEstadisticas = crearCard("Estadísticas", "#9b59b6", "📊", "Reportes y análisis de datos");
        Button cardFormularios = crearCard("Documentación", "#f39c12", "📝", "Formularios y documentos");
        Button cardSolicitudes = crearCard("Solicitudes", "#1abc9c", "🔄", "Gestión de pedidos y solicitudes");
        Button cardConfiguracion = crearCard("Configuración", "#34495e", "⚙️", "Ajustes del sistema");

        // Configuraciones acciones
        cardUsuarios.setOnAction(e -> vista.getRaiz().setCenter(new VistaUsuarios().getVista()));
        cardInventario.setOnAction(e -> vista.getRaiz().setCenter(new VistaInventario().getVista()));
        cardEstadisticas.setOnAction(e -> vista.getRaiz().setCenter(new VistaEstadisticas().getVista()));
        cardFormularios.setOnAction(e -> vista.getRaiz().setCenter(new VistaFormularios().getVista()));
        cardSolicitudes.setOnAction(e -> vista.getRaiz().setCenter(new VistaSolicitudes().getVista()));
        cardConfiguracion.setOnAction(e -> vista.getRaiz().setCenter(new VistaConfiguracion().getVista()));

        // Organizar en grid 2x3
        dashboard.addRow(0, cardUsuarios, cardInventario, cardEstadisticas);
        dashboard.addRow(1, cardFormularios, cardSolicitudes, cardConfiguracion);

        vista.getRaiz().setCenter(dashboard);
    }

    private Button crearCard(String titulo, String color, String emoji, String descripcion) {
        Button card = new Button();
        // Gradiente para las cards
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", derive(" + color + ", -20%)); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5);");
        card.setMinSize(320, 220);
        card.setMaxSize(320, 220);

        VBox contenido = new VBox(10);
        contenido.setAlignment(Pos.CENTER);

        Label lblEmoji = new Label(emoji);
        lblEmoji.setStyle("-fx-font-size: 48; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1);");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 0, 1);");
        lblTitulo.setWrapText(true);
        lblTitulo.setAlignment(Pos.CENTER);

        Label lblDesc = new Label(descripcion);
        lblDesc.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 14;");
        lblDesc.setWrapText(true);
        lblDesc.setAlignment(Pos.CENTER);
        lblDesc.setMaxWidth(250);

        contenido.getChildren().addAll(lblEmoji, lblTitulo, lblDesc);
        card.setGraphic(contenido);

        // Efecto hover mejorado
        card.setOnMouseEntered(e -> {
            card.setStyle(card.getStyle() +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 20, 0, 0, 8); " +
                    "-fx-scale-x: 1.05; " +
                    "-fx-scale-y: 1.05;");
            card.setCursor(javafx.scene.Cursor.HAND);
        });

        card.setOnMouseExited(e -> {
            card.setStyle(card.getStyle() +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5); " +
                    "-fx-scale-x: 1.0; " +
                    "-fx-scale-y: 1.0;");
        });

        return card;
    }

    private void mostrarConfirmacionCerrarSesion() {
        System.out.println("Cerrando sesion");
    }
}
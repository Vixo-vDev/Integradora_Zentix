package com.example.netrixapp.Controladores.ControladorAdmin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ControladorBarraNavegacion {
    // Colores
    private static final String COLOR_BARRA_SUPERIOR = "#009475";
    private static final String COLOR_BARRA_LATERAL = "#005994";
    private static final String COLOR_TEXTO_CLARO = "#FFFFFF";
    private static final String COLOR_BOTON_HOVER = "#007A5E";
    private static final String COLOR_SALIR = "#D32F2F";

    // Componentes UI
    private final VBox barraLateral;
    private final HBox barraSuperior;

    // Botones de navegación (actualizados para administrador)
    private Button btnDashboard;
    private Button btnInventario;
    private Button btnEstadisticas;
    private Button btnUsuarios;
    private Button btnSolicitudes;
    private Button btnNotificaciones;
    private Button btnSalir;

    public ControladorBarraNavegacion() {
        this.barraSuperior = crearBarraSuperior();
        this.barraLateral = crearBarraLateral();
    }

    private HBox crearBarraSuperior() {
        HBox barra = new HBox();
        barra.setStyle("-fx-background-color: " + COLOR_BARRA_SUPERIOR + ";");
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(12, 30, 12, 30));
        barra.setPrefHeight(60);

        Label lblTitulo = new Label("Netrix | SGI");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_CLARO + ";");

        Pane espaciador = new Pane();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Circle circuloPerfil = new Circle(20, Color.WHITE);
        Label lblIniciales = new Label("AD");
        lblIniciales.setStyle("-fx-text-fill: " + COLOR_BARRA_SUPERIOR + "; -fx-font-weight: bold;");
        StackPane iconoUsuario = new StackPane(circuloPerfil, lblIniciales);
        iconoUsuario.setAlignment(Pos.CENTER);

        barra.getChildren().addAll(lblTitulo, espaciador, iconoUsuario);
        return barra;
    }

    private VBox crearBarraLateral() {
        VBox barra = new VBox(10);
        barra.setStyle("-fx-background-color: " + COLOR_BARRA_LATERAL + "; -fx-padding: 25px 15px;");
        barra.setMinWidth(220);
        barra.setPrefWidth(220);
        barra.setAlignment(Pos.TOP_CENTER);

        // Título del menú
        Label lblMenu = new Label("Menú");
        lblMenu.setStyle("-fx-text-fill: " + COLOR_TEXTO_CLARO + "; -fx-font-weight: bold; -fx-font-size: 18px;");

        // Botones de navegación
        btnDashboard = crearBotonLateral("Dashboard", "📊");
        btnInventario = crearBotonLateral("Inventario", "📦");
        btnEstadisticas = crearBotonLateral("Estadísticas", "📈");
        btnUsuarios = crearBotonLateral("Usuarios", "👥");
        btnSolicitudes = crearBotonLateral("Solicitudes", "📋");

        Pane espaciador = new Pane();
        VBox.setVgrow(espaciador, Priority.ALWAYS);

        btnSalir = crearBotonLateral("Salir", "🚪");
        btnSalir.setStyle("-fx-background-color: " + COLOR_SALIR + "; -fx-text-fill: " + COLOR_TEXTO_CLARO + "; " +
                "-fx-font-weight: bold; -fx-background-radius: 8px;");

        barra.getChildren().addAll(lblMenu, btnDashboard, btnInventario, btnEstadisticas,
                btnUsuarios, btnSolicitudes, espaciador, btnSalir);
        return barra;
    }

    private Button crearBotonLateral(String texto, String emoji) {
        Button btn = new Button(texto + "  " + emoji);

        String estiloBase = "-fx-background-color: transparent; " +
                "-fx-text-fill: " + COLOR_TEXTO_CLARO + "; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: 500; " +
                "-fx-padding: 12px 20px; " +
                "-fx-alignment: center-left; " +
                "-fx-min-width: 180px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;";

        btn.setStyle(estiloBase);

        btn.setOnMouseEntered(e -> btn.setStyle(estiloBase +
                "-fx-background-color: " + COLOR_BOTON_HOVER + "; " +
                "-fx-text-fill: white;"));

        btn.setOnMouseExited(e -> btn.setStyle(estiloBase));

        return btn;
    }

    // Getters para los botones
    public Button getBtnDashboard() { return btnDashboard; }
    public Button getBtnInventario() { return btnInventario; }
    public Button getBtnEstadisticas() { return btnEstadisticas; }
    public Button getBtnUsuarios() { return btnUsuarios; }
    public Button getBtnSolicitudes() { return btnSolicitudes; }
    public Button getBtnNotificaciones() { return btnNotificaciones; }
    public Button getBtnSalir() { return btnSalir; }

    // Getters para las barras
    public VBox getBarraLateral() { return barraLateral; }
    public HBox getBarraSuperior() { return barraSuperior; }
}
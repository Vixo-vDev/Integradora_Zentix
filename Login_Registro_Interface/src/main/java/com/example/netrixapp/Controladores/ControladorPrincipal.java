package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.*;
import impl.EquipoDaoImpl;
import impl.SolicitudDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;

public class ControladorPrincipal {

    private final Usuario usuario = SesionUsuario.getUsuarioActual();
    private final int id_usuario = usuario.getId_usuario();
    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    private final SolicitudDaoImpl solicitudDao = new SolicitudDaoImpl();

    private final VistaPrincipal vista;
    private final ControladorBarraNavegacion controladorBarra;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.controladorBarra = new ControladorBarraNavegacion();
        configurarEventos();
        inicializarUI();
        mostrarDashboard(); // Mostrar dashboard por defecto
    }

    private void configurarEventos() {
        controladorBarra.getBtnDashboard().setOnAction(e -> mostrarDashboard());
        controladorBarra.getBtnInventario().setOnAction(e -> mostrarInventario());
        controladorBarra.getBtnHistorial().setOnAction(e -> mostrarHistorial());
        controladorBarra.getBtnSolicitudes().setOnAction(e -> mostrarSolicitudes());
        controladorBarra.getBtnPerfil().setOnAction(e -> mostrarPerfil());
        controladorBarra.getBtnSalir().setOnAction(e -> confirmarCierreSesion());
    }

    private void inicializarUI() {
        // Configuración del fondo blanco
        vista.getRaiz().setStyle("-fx-background-color: #FFFFFF;");

        // Asegurar que las barras siempre estén presentes
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
    }

    public void mostrarDashboard() {
        // Contenedor principal con scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: white;");

        VBox contenedorPrincipal = new VBox(25); // Más espacio entre elementos
        contenedorPrincipal.setStyle("-fx-background-color: white; -fx-padding: 30;");
        contenedorPrincipal.setAlignment(Pos.TOP_LEFT);

        // Sección de métricas
        Label tituloMetricas = new Label("Resumen General");
        tituloMetricas.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Grid para las cards de métricas
        GridPane gridMetricas = new GridPane();
        gridMetricas.setHgap(20);
        gridMetricas.setVgap(20);
        gridMetricas.setPadding(new Insets(15, 0, 25, 0));

        // Cards de métricas con diseño moderno
        VBox cardArticulos = null;
        try {
            cardArticulos = crearCardMetrica("Artículos", String.valueOf(equipoDao.totalEquipos()), "#4F46E5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        VBox cardSolicitudes = null;
        try {
            cardSolicitudes = crearCardMetrica("Solicitudes", String.valueOf(solicitudDao.totalSolicitudes(id_usuario)), "#10B981");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        VBox cardPendientes = null;
        try {
            cardPendientes = crearCardMetrica("Pendientes", String.valueOf(solicitudDao.total_pendientes(id_usuario)), "#F59E0B");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        VBox cardRechazados = null;
        try {
            cardRechazados = crearCardMetrica("Rechazados", String.valueOf(solicitudDao.totalRechazados(id_usuario)), "#EF4444");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        gridMetricas.add(cardArticulos, 0, 0);
        gridMetricas.add(cardSolicitudes, 1, 0);
        gridMetricas.add(cardPendientes, 2, 0);
        gridMetricas.add(cardRechazados, 3, 0);

        // Hacer que las cards se expandan
        for (Node card : gridMetricas.getChildren()) {
            GridPane.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxWidth(Double.MAX_VALUE);
        }

        // Sección de últimos pedidos
        VBox cardPedidos = crearCardPedidos("Tus últimos pedidos");

        // Ensamblar la interfaz
        contenedorPrincipal.getChildren().addAll(
                tituloMetricas,
                gridMetricas,
                cardPedidos
        );

        scrollPane.setContent(contenedorPrincipal);

        // Asegurar que las barras estén presentes
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(scrollPane);
    }

    private VBox crearCardMetrica(String titulo, String valor, String color) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; " +
                "-fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 10;");
        card.setAlignment(Pos.TOP_LEFT);

        // Línea superior de color
        Pane colorLine = new Pane();
        colorLine.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 4 4 0 0;");
        colorLine.setPrefHeight(4);
        colorLine.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(colorLine, new Insets(-20, -20, 15, -20));

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: #6B7280;");

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        card.getChildren().addAll(colorLine, lblTitulo, lblValor);
        return card;
    }

    private VBox crearCardPedidos(String titulo) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 25; " +
                "-fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 10;");
        card.setAlignment(Pos.TOP_LEFT);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        // Tabla de pedidos
        GridPane tabla = new GridPane();
        tabla.setHgap(25);
        tabla.setVgap(15);
        tabla.setPadding(new Insets(15, 0, 5, 0));

        // Encabezados
        String[] encabezados = {"Artículo", "Fecha", "Estado"};
        for (int i = 0; i < encabezados.length; i++) {
            Label lbl = new Label(encabezados[i]);
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #4B5563; -fx-font-size: 14;");
            tabla.add(lbl, i, 0);
        }

        // Línea divisoria
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E5E7EB;");
        GridPane.setColumnSpan(separator, encabezados.length);
        tabla.add(separator, 0, 1);

        try {
            List<Solicitud> solicitudes = solicitudDao.findAll(id_usuario);
            int filas = Math.min(solicitudes.size(), 5);

            for (int i = 0; i < filas; i++) {
                Solicitud s = solicitudes.get(i);

                Label lblArticulo = new Label(s.getArticulo());
                lblArticulo.setStyle("-fx-text-fill: #111827;");

                Label lblFecha = new Label(s.getFecha_solicitud().toString());
                lblFecha.setStyle("-fx-text-fill: #6B7280;");

                Label lblEstado = new Label(s.getEstado());
                lblEstado.setStyle(getStyleForStatus(s.getEstado()));

                tabla.add(lblArticulo, 0, i + 2);
                tabla.add(lblFecha, 1, i + 2);
                tabla.add(lblEstado, 2, i + 2);
            }

            if (solicitudes.size() > 5) {
                Button btnVerMas = new Button("Ver más solicitudes →");
                btnVerMas.setStyle("-fx-text-fill: #4F46E5; -fx-font-weight: bold; -fx-background-color: transparent; -fx-padding: 10 0 0 0; -fx-cursor: hand;");
                btnVerMas.setOnAction(e -> mostrarHistorial());
                btnVerMas.setOnMouseEntered(e -> btnVerMas.setStyle("-fx-text-fill: #4338CA; -fx-font-weight: bold; -fx-background-color: transparent; -fx-padding: 10 0 0 0; -fx-cursor: hand;"));
                btnVerMas.setOnMouseExited(e -> btnVerMas.setStyle("-fx-text-fill: #4F46E5; -fx-font-weight: bold; -fx-background-color: transparent; -fx-padding: 10 0 0 0; -fx-cursor: hand;"));
                GridPane.setColumnSpan(btnVerMas, encabezados.length);
                tabla.add(btnVerMas, 0, filas + 2);
            }
        } catch (Exception e) {
            Label error = new Label("No se pudieron cargar las solicitudes.");
            error.setStyle("-fx-text-fill: #EF4444;");
            tabla.add(error, 0, 2, encabezados.length, 1);
        }

        card.getChildren().addAll(lblTitulo, tabla);
        return card;
    }

    private String getStyleForStatus(String status) {
        return switch (status.toUpperCase()) {
            case "APROBADO" -> "-fx-text-fill: #10B981; -fx-font-weight: bold;";
            case "PENDIENTE" -> "-fx-text-fill: #F59E0B; -fx-font-weight: bold;";
            case "RECHAZADO" -> "-fx-text-fill: #EF4444; -fx-font-weight: bold;";
            default -> "-fx-text-fill: #6B7280;";
        };
    }

    // Métodos para mostrar otras vistas (asegurando que las barras permanezcan)
    public void mostrarInventario() {
        VistaInventario vistaInventario = new VistaInventario(controladorBarra);
        new ControladorInventario(vistaInventario);
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(vistaInventario.getVista());
    }

    public void mostrarHistorial() {
        VistaHistorial vistaHistorial = new VistaHistorial(controladorBarra);
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(vistaHistorial.getVista());
    }

    public void mostrarSolicitudes() {
        VistaSolicitudes vistaSolicitudes = new VistaSolicitudes(controladorBarra);
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(vistaSolicitudes.getVista());
    }

    public void mostrarPerfil() {
        VistaPerfil vistaPerfil = new VistaPerfil(controladorBarra);
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(vistaPerfil.getVista());
    }

    private void confirmarCierreSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText("¿Está seguro de que desea salir?");
        alert.initOwner(vista.getRaiz().getScene().getWindow());

        // Estilo moderno para la alerta
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14; -fx-background-color: white;");

        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType btnConfirmar = new ButtonType("Cerrar sesión", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(btnCancelar, btnConfirmar);

        if (alert.showAndWait().orElse(btnCancelar) == btnConfirmar) {
            SesionUsuario.cerrarSesion();
            Stage stage = (Stage) vista.getRaiz().getScene().getWindow();
            VistaLogin login = new VistaLogin();
            login.start(stage);
        }
    }
}
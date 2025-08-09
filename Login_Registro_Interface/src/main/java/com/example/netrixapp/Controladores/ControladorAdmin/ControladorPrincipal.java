package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Controladores.SesionUsuario;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistaLogin;
import com.example.netrixapp.Vistas.VistasAdmin.*;
import impl.EquipoDaoImpl;
import impl.SolicitudDaoImpl;
import impl.UsuarioDaoImpl;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControladorPrincipal {
    private final VistaPrincipal vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final SolicitudDaoImpl solicitudDao = new SolicitudDaoImpl();
    private final UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.controladorBarra = new ControladorBarraNavegacion();
        configurarEventos();
        inicializarUI();
        mostrarDashboard(); // Mostrar dashboard por defecto
    }

    private void configurarEventos() {
        controladorBarra.getBtnDashboard().setOnAction(e ->
                mostrarConLoading(controladorBarra.getBtnDashboard(), this::mostrarDashboard));
        controladorBarra.getBtnInventario().setOnAction(e ->
                mostrarConLoading(controladorBarra.getBtnInventario(), this::mostrarInventario));
        controladorBarra.getBtnEstadisticas().setOnAction(e ->
                mostrarConLoading(controladorBarra.getBtnEstadisticas(), this::mostrarEstadisticas));
        controladorBarra.getBtnUsuarios().setOnAction(e ->
                mostrarConLoading(controladorBarra.getBtnUsuarios(), this::mostrarUsuarios));
        controladorBarra.getBtnSolicitudes().setOnAction(e ->
                mostrarConLoading(controladorBarra.getBtnSolicitudes(), this::mostrarSolicitudes));
        controladorBarra.getBtnPrestamos().setOnAction(e ->
                mostrarConLoading(controladorBarra.getBtnPrestamos(), this::mostrarPrestamos));
        controladorBarra.getBtnSalir().setOnAction(e -> confirmarCierreSesion());

    }

    private void mostrarConLoading(Button boton, Runnable accion) {
        String textoOriginal = boton.getText();
        boton.setText("Cargando...");
        boton.setDisable(true);

        executor.execute(() -> {
            try {
                // Simular carga (opcional, puedes quitarlo)
                Thread.sleep(300);

                Platform.runLater(accion);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> {
                    boton.setText(textoOriginal);
                    boton.setDisable(false);
                });
            }
        });
    }

    private void inicializarUI() {
        vista.getRaiz().setStyle("-fx-background-color: #F9FAFB;");
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
    }

    public void mostrarDashboard() {
        // Asegurar que las barras estén presentes
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #F5F7FA; -fx-border-color: #F5F7FA;");

        VBox contenedorPrincipal = new VBox(20);
        contenedorPrincipal.setStyle("-fx-background-color: #F5F7FA; -fx-padding: 20;");
        contenedorPrincipal.setAlignment(Pos.TOP_LEFT);

        // 1. Fila horizontal con las 4 cards
        HBox filaCards = new HBox(15);
        filaCards.setAlignment(Pos.TOP_LEFT);

        try {
            // Card de Usuarios Registrados
            VBox cardUsuarios = crearCardMetrica("Usuarios", String.valueOf(usuarioDao.totalUsuarios()), "👥");

            // Card de Activos en Sistema
            VBox cardActivos = crearCardMetrica("Activos", String.valueOf(equipoDao.totalEquipos()), "💻");

            // Card de Solicitudes Pendientes
            VBox cardPendientes = crearCardMetrica("Pendientes", String.valueOf(solicitudDao.total_pendientesAdmin()), "⏳");

            // Card de Solicitudes Recientes
            VBox cardRecientes = crearCardMetrica("Recientes", String.valueOf(solicitudDao.solicitudesRecientes().size()), "🆕");



            filaCards.getChildren().addAll(cardUsuarios, cardActivos, cardPendientes, cardRecientes);

            // Ajustar el crecimiento horizontal de las cards
            for (Node card : filaCards.getChildren()) {
                HBox.setHgrow(card, Priority.ALWAYS);
                ((VBox) card).setMaxWidth(Double.MAX_VALUE);
            }

            // 2. Tabla de Últimas Actividades (ahora va directamente después de las cards)
            VBox cardActividades = crearCardActividades("Últimas actividades del sistema");

            contenedorPrincipal.getChildren().addAll(filaCards, cardActividades);
        } catch (Exception e) {
            Label error = new Label("Error al cargar los datos del dashboard");
            error.setStyle("-fx-text-fill: #EF4444;");
            contenedorPrincipal.getChildren().add(error);
        }

        scrollPane.setContent(contenedorPrincipal);
        vista.getRaiz().setCenter(scrollPane);
    }

    private VBox crearCardMetrica(String titulo, String valor, String emoji) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16; " +
                "-fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 8;");
        card.setAlignment(Pos.TOP_LEFT);
        card.setMinWidth(200);

        HBox tituloBox = new HBox(5);
        tituloBox.setAlignment(Pos.CENTER_LEFT);

        Label lblEmoji = new Label(emoji);
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        tituloBox.getChildren().addAll(lblEmoji, lblTitulo);

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #005994;");

        card.getChildren().addAll(tituloBox, lblValor);
        return card;
    }

    private VBox crearCardGrafico(String titulo, String emoji) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16; " +
                "-fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 8;");
        card.setAlignment(Pos.TOP_LEFT);
        card.setMinHeight(200);

        HBox tituloBox = new HBox(5);
        tituloBox.setAlignment(Pos.CENTER_LEFT);

        Label lblEmoji = new Label(emoji);
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        tituloBox.getChildren().addAll(lblEmoji, lblTitulo);

        // Espacio para el gráfico
        Pane graficoPlaceholder = new Pane();
        graficoPlaceholder.setStyle("-fx-background-color: #F5F7FA; -fx-min-height: 150px;");
        graficoPlaceholder.setPrefHeight(150);

        card.getChildren().addAll(tituloBox, graficoPlaceholder);
        return card;
    }

    private VBox crearCardActividades(String titulo) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16; " +
                "-fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 8;");
        card.setAlignment(Pos.TOP_LEFT);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        GridPane tabla = new GridPane();
        tabla.setHgap(30);
        tabla.setVgap(10);
        tabla.setPadding(new Insets(8, 0, 0, 0));

        // Encabezados que solo muestran datos propios de la solicitud
        String[] encabezados = {"Artículo", "Fecha de solicitud", "Estado"};
        for (int i = 0; i < encabezados.length; i++) {
            Label lbl = new Label(encabezados[i]);
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C3E50;");
            tabla.add(lbl, i, 0);
        }

        try {
            List<Solicitud> solicitudes = solicitudDao.solicitudesRecientes();
            for (int i = 0; i < solicitudes.size(); i++) {
                Solicitud s = solicitudes.get(i);

                Label lblArticulo = new Label(s.getArticulo());
                Label lblFecha = new Label(s.getFecha_solicitud().toString());
                Label lblEstado = new Label(s.getEstado());
                lblEstado.setStyle(getStyleForStatus(s.getEstado()));

                tabla.add(lblArticulo, 0, i + 1);
                tabla.add(lblFecha, 1, i + 1);
                tabla.add(lblEstado, 2, i + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label error = new Label("No se pudieron cargar las solicitudes recientes.");
            error.setStyle("-fx-text-fill: #EF4444;");
            tabla.add(error, 0, 1, encabezados.length, 1);
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

    public void mostrarInventario() {
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(new VistaInventario(controladorBarra).getVista());
    }

    public void mostrarEstadisticas() {
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(new VistaEstadisticas(controladorBarra).getVista());
    }

    public void mostrarUsuarios() {
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(new VistaUsuarios(controladorBarra).getVista());
    }

    public void mostrarSolicitudes() {
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(new VistaSolicitudes(controladorBarra).getVista());
    }

    public void mostrarPrestamos() {
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        vista.getRaiz().setCenter(new VistaPrestamos(controladorBarra).getVista());
    }

    private void confirmarCierreSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText("¿Está seguro de que desea salir?");
        alert.initOwner(vista.getRaiz().getScene().getWindow());

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
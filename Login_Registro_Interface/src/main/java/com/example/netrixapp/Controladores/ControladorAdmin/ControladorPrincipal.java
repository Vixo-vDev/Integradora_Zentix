package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Controladores.SesionUsuario;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistaLogin;
import com.example.netrixapp.Vistas.VistasAdmin.*;
import impl.EquipoDaoImpl;
import impl.SolicitudDaoImpl;
import impl.UsuarioDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ControladorPrincipal {
    private final VistaPrincipal vista;
    private final ControladorBarraNavegacion controladorBarra;
    SolicitudDaoImpl solicitudDao = new SolicitudDaoImpl();
    UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
    EquipoDaoImpl equipoDao = new EquipoDaoImpl();

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.controladorBarra = new ControladorBarraNavegacion();
        configurarEventos();
        inicializarUI();
    }

    private void configurarEventos() {
        controladorBarra.getBtnDashboard().setOnAction(e -> mostrarDashboard());
        controladorBarra.getBtnInventario().setOnAction(e -> mostrarInventario());
        controladorBarra.getBtnEstadisticas().setOnAction(e -> mostrarEstadisticas());
        controladorBarra.getBtnUsuarios().setOnAction(e -> mostrarUsuarios());
        controladorBarra.getBtnSolicitudes().setOnAction(e -> mostrarSolicitudes());
        controladorBarra.getBtnSalir().setOnAction(e -> confirmarCierreSesion());
    }

    private void inicializarUI() {
        vista.getRaiz().setStyle("-fx-background-color: #F9FAFB;");
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        mostrarDashboard();
    }

    public void mostrarDashboard() {
        VBox contenedorPrincipal = new VBox(20);
        contenedorPrincipal.setStyle("-fx-background-color: #F5F7FA; -fx-padding: 20;");
        contenedorPrincipal.setAlignment(Pos.TOP_LEFT);

        // 1. Fila horizontal con las 4 cards
        HBox filaCards = new HBox(15);
        filaCards.setAlignment(Pos.TOP_LEFT);

        // Card de Usuarios Registrados
        VBox cardUsuarios = null;
        try {
            cardUsuarios = crearCardMetrica("Usuarios", String.valueOf(usuarioDao.totalUsuarios()), "👥");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        filaCards.getChildren().add(cardUsuarios);

        // Card de Activos en Sistema
        VBox cardActivos = null;
        try {
            cardActivos = crearCardMetrica("Activos", String.valueOf(equipoDao.totalEquipos()), "💻");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        filaCards.getChildren().add(cardActivos);

        // Card de Solicitudes Pendientes
        VBox cardPendientes = null;
        try {
            cardPendientes = crearCardMetrica("Pendientes", String.valueOf(solicitudDao.total_pendientesAdmin()), "⏳");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        filaCards.getChildren().add(cardPendientes);

        // Ajustar el crecimiento horizontal de las cards
        for (Node card : filaCards.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxWidth(Double.MAX_VALUE);
        }

        contenedorPrincipal.getChildren().add(filaCards);

        // 2. Gráficos y estadísticas rápidas
        HBox filaGraficos = new HBox(15);
        filaGraficos.setAlignment(Pos.TOP_LEFT);

        // Ajustar crecimiento
        for (Node card : filaGraficos.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxWidth(Double.MAX_VALUE);
        }

        contenedorPrincipal.getChildren().add(filaGraficos);

        // 3. Tabla de Últimas Actividades
        VBox cardActividades = crearCardActividades("Últimas actividades del sistema");
        contenedorPrincipal.getChildren().add(cardActividades);

        if (vista.getRaiz().getTop() == null) {
            vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        }
        if (vista.getRaiz().getLeft() == null) {
            vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        }

        vista.getRaiz().setCenter(contenedorPrincipal);
    }

    private VBox crearCardMetrica(String titulo, String valor, String emoji) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");
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
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");
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
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");
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

                tabla.add(lblArticulo, 0, i + 1);
                tabla.add(lblFecha, 1, i + 1);
                tabla.add(lblEstado, 2, i + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label error = new Label("No se pudieron cargar las solicitudes recientes.");
            error.setStyle("-fx-text-fill: red;");
            tabla.add(error, 0, 1);
        }

        card.getChildren().addAll(lblTitulo, tabla);
        return card;
    }

    private void agregarFilaActividad(GridPane tabla, int fila, String usuario, String accion, String fecha, String detalles) {
        tabla.add(new Label(usuario), 0, fila);
        tabla.add(new Label(accion), 1, fila);
        tabla.add(new Label(fecha), 2, fila);

        Label lblDetalles = new Label(detalles);
        lblDetalles.setStyle("-fx-text-fill: #7F8C8D;");
        tabla.add(lblDetalles, 3, fila);
    }

    public void mostrarInventario() {
        vista.getRaiz().setCenter(new VistaInventario(controladorBarra).getVista());
    }

    public void mostrarEstadisticas() {
        vista.getRaiz().setCenter(new VistaEstadisticas(controladorBarra).getVista());
    }

    public void mostrarUsuarios() {
        vista.getRaiz().setCenter(new VistaUsuarios(controladorBarra).getVista());
    }

    public void mostrarSolicitudes() {
        vista.getRaiz().setCenter(new VistaSolicitudes(controladorBarra).getVista());
    }

    public void mostrarNotificaciones() {
        vista.getRaiz().setCenter(new VistaNotificaciones(controladorBarra).getVista());
    }

    private void confirmarCierreSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText("¿Está seguro de que desea salir?");

        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType btnConfirmar = new ButtonType("Cerrar sesión", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(btnCancelar, btnConfirmar);

        if (alert.showAndWait().orElse(btnCancelar) == btnConfirmar) {
            if (alert.showAndWait().orElse(btnCancelar) == btnConfirmar) {

                SesionUsuario.cerrarSesion();
                Stage stage = (Stage) vista.getRaiz().getScene().getWindow();
                VistaLogin login = new VistaLogin();
                login.start(stage);
            }
        }
    }
}
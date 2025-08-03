package com.example.netrixapp.Controladores;

import com.example.netrixapp.HelloApplication;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.*;
import impl.EquipoDaoImpl;
import impl.SolicitudDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ControladorPrincipal {

    Usuario usuario = SesionUsuario.getUsuarioActual();
    int id_usuario = usuario.getId_usuario();
    EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    SolicitudDaoImpl solicitudDao = new SolicitudDaoImpl();

    private final VistaPrincipal vista;
    private final ControladorBarraNavegacion controladorBarra;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.controladorBarra = new ControladorBarraNavegacion();
        configurarEventos();
        inicializarUI();
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

        VBox cardArticulos = crearCardMetrica("Artículos", String.valueOf(equipoDao.totalEquipos()));
        VBox cardSolicitudes = crearCardMetrica("Solicitudes", String.valueOf(solicitudDao.totalSolicitudes(id_usuario)));
        VBox cardPendientes = crearCardMetrica("Pendientes", String.valueOf(solicitudDao.total_pendientes(id_usuario)));
        VBox cardRechazados = crearCardMetrica("Rechazados", String.valueOf(solicitudDao.totalRechazados(id_usuario)));

        filaCards.getChildren().addAll(cardArticulos, cardSolicitudes, cardPendientes, cardRechazados);

        for (Node card : filaCards.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxWidth(Double.MAX_VALUE);
        }

        contenedorPrincipal.getChildren().add(filaCards);

        // 2. Últimos pedidos
        VBox cardPedidos = crearCardPedidos("Tus últimos pedidos");
        contenedorPrincipal.getChildren().add(cardPedidos);

        if (vista.getRaiz().getTop() == null) {
            vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        }
        if (vista.getRaiz().getLeft() == null) {
            vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        }

        vista.getRaiz().setCenter(contenedorPrincipal);
    }

    private VBox crearCardMetrica(String titulo, String valor) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");
        card.setAlignment(Pos.TOP_LEFT);
        card.setMinWidth(200);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #005994;");

        card.getChildren().addAll(lblTitulo, lblValor);
        return card;
    }

    private VBox crearCardPedidos(String titulo) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");
        card.setAlignment(Pos.TOP_LEFT);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        GridPane tabla = new GridPane();
        tabla.setHgap(30);
        tabla.setVgap(10);
        tabla.setPadding(new Insets(8, 0, 0, 0));

        String[] encabezados = {"Artículo", "Fecha de solicitud", "Estado"};
        for (int i = 0; i < encabezados.length; i++) {
            Label lbl = new Label(encabezados[i]);
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C3E50;");
            tabla.add(lbl, i, 0);
        }

        try {
            List<Solicitud> solicitudes = solicitudDao.findAll(id_usuario);
            int filas = Math.min(solicitudes.size(), 5);
            for (int i = 0; i < filas; i++) {
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
            Label error = new Label("No se pudieron cargar las solicitudes.");
            error.setStyle("-fx-text-fill: red;");
            tabla.add(error, 0, 1);
        }

        card.getChildren().addAll(lblTitulo, tabla);
        return card;
    }

    public void mostrarInventario() {
        VistaInventario vistaInventario = new VistaInventario(controladorBarra);
        new ControladorInventario(vistaInventario);
        vista.getRaiz().setCenter(vistaInventario.getVista());
    }

    public void mostrarHistorial() {
        vista.getRaiz().setCenter(new VistaHistorial(controladorBarra).getVista());
    }

    public void mostrarSolicitudes() {
        vista.getRaiz().setCenter(new VistaSolicitudes(controladorBarra).getVista());
    }

    public void mostrarPerfil() {
        vista.getRaiz().setCenter(new VistaPerfil(controladorBarra).getVista());
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
            SesionUsuario.cerrarSesion();
            Stage stage = (Stage) vista.getRaiz().getScene().getWindow();
            VistaLogin login = new VistaLogin();
            login.start(stage);
        }
    }
}

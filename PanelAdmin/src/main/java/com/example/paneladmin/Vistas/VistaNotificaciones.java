package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;

import com.example.paneladmin.DAO.NotificacionDAO;
import com.example.paneladmin.Modelo.Notificacion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VistaNotificaciones {
    @FXML private BorderPane vista;
    @FXML private VBox contenidoPrincipal;
    @FXML private VBox listaNotificaciones;
    @FXML private ToggleGroup grupoFiltros;
    @FXML private RadioButton rbTodas;
    @FXML private RadioButton rbNoLeidas;
    @FXML private RadioButton rbImportantes;
    @FXML private Button btnEliminarTodo;
    @FXML private Button btnMarcarLeido;

    private final NotificacionDAO notificacionDAO;
    private final ControladorBarraNavegacion controladorBarra;

    public VistaNotificaciones(ControladorBarraNavegacion controladorBarra, NotificacionDAO notificacionDAO) {
        this.controladorBarra = controladorBarra;
        this.notificacionDAO = notificacionDAO;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DiseñoNotificaciones.fxml"));
            loader.setController(this);
            vista = loader.load();

            if (grupoFiltros == null) {
                grupoFiltros = new ToggleGroup();
                rbTodas.setToggleGroup(grupoFiltros);
                rbNoLeidas.setToggleGroup(grupoFiltros);
                rbImportantes.setToggleGroup(grupoFiltros);
                rbTodas.setSelected(true);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar VistaNotificaciones.fxml", e);
        }
    }

    @FXML
    public void initialize() {
        configurarUI();
        cargarNotificaciones();
        configurarEventos();
    }

    private void configurarUI() {
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.getStyleClass().add("vista-notificaciones");
    }

    private void configurarEventos() {
        // Filtros
        rbTodas.setOnAction(e -> cargarNotificaciones());
        rbNoLeidas.setOnAction(e -> cargarNotificaciones());
        rbImportantes.setOnAction(e -> cargarNotificaciones());

        // Acciones
        btnEliminarTodo.setOnAction(e -> confirmarEliminarTodas());
        btnMarcarLeido.setOnAction(e -> marcarTodasComoLeidas());
    }

    private void cargarNotificaciones() {
        listaNotificaciones.getChildren().clear();

        List<Notificacion> notificaciones;

        if (rbNoLeidas.isSelected()) {
            notificaciones = notificacionDAO.obtenerNoLeidas();
        } else if (rbImportantes.isSelected()) {
            notificaciones = notificacionDAO.obtenerImportantes();
        } else {
            notificaciones = notificacionDAO.obtenerTodas();
        }

        if (notificaciones.isEmpty()) {
            Label lblSinNotificaciones = new Label("No hay notificaciones");
            lblSinNotificaciones.getStyleClass().add("texto-sin-notificaciones");
            listaNotificaciones.getChildren().add(lblSinNotificaciones);
            return;
        }

        for (Notificacion notificacion : notificaciones) {
            agregarNotificacionUI(notificacion);
        }
    }

    private void agregarNotificacionUI(Notificacion notificacion) {
        HBox notificacionBox = new HBox(15);
        notificacionBox.getStyleClass().add("notificacion");
        if (notificacion.isImportante()) {
            notificacionBox.getStyleClass().add("importante");
        }
        if (!notificacion.isLeida()) {
            notificacionBox.getStyleClass().add("no-leida");
        }

        // Icono
        Label lblIcono = new Label(notificacion.isImportante() ? "❗" : "🔔");
        lblIcono.getStyleClass().add("icono-notificacion");

        // Contenido
        VBox contenido = new VBox(5);
        Label lblTitulo = new Label(notificacion.getTitulo());
        lblTitulo.getStyleClass().add("titulo-notificacion");

        Label lblMensaje = new Label(notificacion.getMensaje());
        lblMensaje.getStyleClass().add("mensaje-notificacion");

        Label lblFecha = new Label(notificacion.getFecha()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        lblFecha.getStyleClass().add("fecha-notificacion");

        contenido.getChildren().addAll(lblTitulo, lblMensaje, lblFecha);

        // Botones de acción
        HBox botones = new HBox(5);
        botones.setAlignment(Pos.CENTER_RIGHT);

        Button btnLeer = new Button(notificacion.isLeida() ? "✔ Leída" : "Marcar como leída");
        btnLeer.getStyleClass().add(notificacion.isLeida() ? "boton-leida" : "boton-no-leida");
        btnLeer.setOnAction(e -> {
            notificacionDAO.marcarComoLeida(notificacion.getId());
            cargarNotificaciones();
        });

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.getStyleClass().add("boton-eliminar");
        btnEliminar.setOnAction(e -> {
            notificacionDAO.eliminarNotificacion(notificacion.getId());
            cargarNotificaciones();
        });

        botones.getChildren().addAll(btnLeer, btnEliminar);

        notificacionBox.getChildren().addAll(lblIcono, contenido, botones);
        listaNotificaciones.getChildren().add(notificacionBox);
    }

    private void marcarTodasComoLeidas() {
        notificacionDAO.marcarTodasComoLeidas();
        cargarNotificaciones();
    }

    private void confirmarEliminarTodas() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar todas las notificaciones?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                notificacionDAO.eliminarTodas();
                cargarNotificaciones();
            }
        });
    }

    public BorderPane getVista() {
        return vista;
    }
}
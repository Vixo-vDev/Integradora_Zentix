package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class VistaNotificaciones {

    private final BorderPane vista;  // Cambiado a BorderPane para mejor organización
    private final ControladorBarraNavegacion controladorBarra;

    // Colores específicos para esta vista
    private final String COLOR_EXITO = "#2ECC71";
    private final String COLOR_ADVERTENCIA = "#F39C12";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_FONDO = "#ECF0F1";
    private final String COLOR_TEXTO = "#2C3E50";

    public VistaNotificaciones(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Panel de contenido principal
        VBox panelContenido = new VBox();
        panelContenido.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        panelContenido.setPadding(new Insets(20));
        VBox.setVgrow(panelContenido, Priority.ALWAYS);

        // Panel superior (filtros y acciones)
        HBox panelSuperior = new HBox();
        panelSuperior.setAlignment(Pos.CENTER_LEFT);

        // Panel de filtros
        HBox panelFiltros = crearPanelFiltros();

        // Panel de acciones
        HBox panelAcciones = crearPanelAcciones();

        // Espaciador para separar los dos grupos
        Pane espaciador = new Pane();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        panelSuperior.getChildren().addAll(panelFiltros, espaciador, panelAcciones);

        // Lista de notificaciones con scroll
        ScrollPane scrollNotificaciones = new ScrollPane(crearListaNotificaciones());
        scrollNotificaciones.setFitToWidth(true);
        scrollNotificaciones.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollNotificaciones.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollNotificaciones, Priority.ALWAYS);

        panelContenido.getChildren().addAll(panelSuperior, scrollNotificaciones);

        // Construir vista completa
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setCenter(panelContenido);

        // Configurar márgenes
        BorderPane.setMargin(panelContenido, new Insets(20));
        BorderPane.setMargin(controladorBarra.getBarraLateral(), new Insets(0, 20, 0, 0));
    }

    private HBox crearPanelFiltros() {
        HBox panelFiltros = new HBox(10);
        panelFiltros.setAlignment(Pos.CENTER_LEFT);

        Label lblFiltro = new Label("Filter:");
        lblFiltro.setStyle("-fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO + ";");

        ToggleGroup grupoFiltros = new ToggleGroup();
        RadioButton rbTodas = new RadioButton("Todas");
        RadioButton rbNoLeidas = new RadioButton("No leídas");
        RadioButton rbImportantes = new RadioButton("Importantes");

        // Estilo para los radio buttons
        String estiloRadio = "-fx-text-fill: " + COLOR_TEXTO + ";";
        rbTodas.setStyle(estiloRadio);
        rbNoLeidas.setStyle(estiloRadio);
        rbImportantes.setStyle(estiloRadio);

        rbTodas.setToggleGroup(grupoFiltros);
        rbNoLeidas.setToggleGroup(grupoFiltros);
        rbImportantes.setToggleGroup(grupoFiltros);
        rbTodas.setSelected(true);

        panelFiltros.getChildren().addAll(lblFiltro, rbTodas, rbNoLeidas, rbImportantes);
        return panelFiltros;
    }

    private HBox crearPanelAcciones() {
        HBox panelAcciones = new HBox(15);
        panelAcciones.setAlignment(Pos.CENTER_RIGHT);

        Button btnEliminarTodo = new Button("Eliminar todo");
        btnEliminarTodo.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + COLOR_PELIGRO + "; " +
                "-fx-border-color: " + COLOR_PELIGRO + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 3; " +
                "-fx-padding: 5 10;");

        Button btnMarcarLeido = new Button("Marcar todo como leído");
        btnMarcarLeido.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-border-color: " + COLOR_TEXTO + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 3; " +
                "-fx-padding: 5 10;");

        panelAcciones.getChildren().addAll(btnEliminarTodo, btnMarcarLeido);
        return panelAcciones;
    }

    private VBox crearListaNotificaciones() {
        VBox listaNotificaciones = new VBox();
        listaNotificaciones.setPadding(new Insets(10, 0, 0, 0));

        // Notificaciones de ejemplo
        agregarNotificacion(listaNotificaciones, "Nuevo artículo disponible", "2023-05-15", false);
        agregarNotificacion(listaNotificaciones, "Solicitud aprobada", "2023-05-14", true);
        agregarNotificacion(listaNotificaciones, "Recordatorio: inventario", "2023-05-12", false);
        agregarNotificacion(listaNotificaciones, "Stock crítico: Teclados", "2023-05-10", true);

        return listaNotificaciones;
    }

    private void agregarNotificacion(VBox contenedor, String mensaje, String fecha, boolean importante) {
        HBox notificacion = new HBox(15);
        notificacion.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 15; " +
                "-fx-border-color: #BDC3C7; -fx-border-width: 0 0 1 0;");
        notificacion.setAlignment(Pos.CENTER_LEFT);

        // Icono
        Label lblIcono = new Label(importante ? "❗" : "🔔");
        lblIcono.setStyle("-fx-font-size: 20;");

        // Contenido
        VBox contenido = new VBox(5);
        Label lblMensaje = new Label(mensaje);
        lblMensaje.setStyle("-fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO + ";" +
                (importante ? "-fx-text-fill: " + COLOR_PELIGRO + ";" : ""));

        Label lblFecha = new Label(fecha);
        lblFecha.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12;");

        contenido.getChildren().addAll(lblMensaje, lblFecha);
        notificacion.getChildren().addAll(lblIcono, contenido);
        contenedor.getChildren().add(notificacion);
    }

    public BorderPane getVista() {
        return vista;
    }
}
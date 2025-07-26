package com.example.paneladmin.Controladores;

import com.example.paneladmin.DAO.ActividadDAO;
import com.example.paneladmin.DAO.Impl.ActividadDAOImpl;
import com.example.paneladmin.DAO.Impl.UsuarioDAOImpl;
import com.example.paneladmin.DAO.UsuarioDAO;
import com.example.paneladmin.Modelo.Actividad;
import com.example.paneladmin.Vistas.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControladorPrincipal {
    private final VistaPrincipal vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final ActividadDAO actividadDAO;
    private final UsuarioDAO usuarioDAO;
    private String usuarioActual = "admin";

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.controladorBarra = new ControladorBarraNavegacion();
        this.actividadDAO = new ActividadDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();

        // Aplicar CSS principal
        String cssFile = getClass().getResource("/css/EstiloPrincipal.css").toExternalForm();
        vista.getRaiz().getStylesheets().add(cssFile);

        configurarEventos();
        inicializarUI();
        registrarActividad("Sistema iniciado", "Aplicación cargada correctamente");
    }

    private void configurarEventos() {
        controladorBarra.getBtnDashboard().setOnAction(e -> mostrarDashboard());
        controladorBarra.getBtnInventario().setOnAction(e -> mostrarInventario());
        controladorBarra.getBtnEstadisticas().setOnAction(e -> mostrarEstadisticas());
        controladorBarra.getBtnUsuarios().setOnAction(e -> mostrarUsuarios());
        controladorBarra.getBtnSolicitudes().setOnAction(e -> mostrarSolicitudes());
        controladorBarra.getBtnNotificaciones().setOnAction(e -> mostrarNotificaciones());
        controladorBarra.getBtnSalir().setOnAction(e -> confirmarCierreSesion());
    }

    private void inicializarUI() {
        vista.getRaiz().getStyleClass().add("raiz-principal");
        vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        mostrarDashboard();
    }

    private void registrarActividad(String accion, String detalles) {
        Actividad actividad = new Actividad(usuarioActual, accion, LocalDateTime.now(), detalles);
        actividadDAO.crearActividad(actividad);
    }

    private void actualizarVistaPrincipal(Region contenidoCentral) {
        // Asegurar que las barras de navegación estén presentes
        if (vista.getRaiz().getTop() == null) {
            vista.getRaiz().setTop(controladorBarra.getBarraSuperior());
        }
        if (vista.getRaiz().getLeft() == null) {
            vista.getRaiz().setLeft(controladorBarra.getBarraLateral());
        }

        // Actualizar el contenido central
        vista.getRaiz().setCenter(contenidoCentral);
    }

    public void mostrarDashboard() {
        registrarActividad("Navegación", "Accedió al Dashboard");

        VBox contenedorPrincipal = new VBox();
        contenedorPrincipal.getStyleClass().add("contenedor-dashboard");

        // 1. Fila horizontal con las 4 cards
        HBox filaCards = new HBox();
        filaCards.getStyleClass().add("fila-cards");

        // Cards con datos dinámicos
        VBox cardUsuarios = crearCardMetrica("Usuarios", String.valueOf(usuarioDAO.obtenerTodos().size()), "👥");
        VBox cardActivos = crearCardMetrica("Activos", "0", "💻");
        VBox cardPendientes = crearCardMetrica("Pendientes", "0", "⏳");
        VBox cardAlertas = crearCardMetrica("Alertas", "0", "⚠️");

        filaCards.getChildren().addAll(cardUsuarios, cardActivos, cardPendientes, cardAlertas);
        contenedorPrincipal.getChildren().add(filaCards);

        // 2. Gráficos y estadísticas rápidas
        HBox filaGraficos = new HBox();
        filaGraficos.getStyleClass().add("fila-cards");

        VBox cardActividad = crearCardGrafico("Actividad Reciente", "📊");
        VBox cardRecursos = crearCardGrafico("Distribución Recursos", "📦");
        filaGraficos.getChildren().addAll(cardActividad, cardRecursos);
        contenedorPrincipal.getChildren().add(filaGraficos);

        // 3. Tabla de Últimas Actividades
        VBox cardActividades = crearCardActividades("Últimas actividades del sistema");
        contenedorPrincipal.getChildren().add(cardActividades);

        actualizarVistaPrincipal(contenedorPrincipal);
    }

    private VBox crearCardMetrica(String titulo, String valor, String emoji) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("card", "card-metrica");

        HBox tituloBox = new HBox(5);
        tituloBox.setAlignment(Pos.CENTER_LEFT);

        Label lblEmoji = new Label(emoji);
        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("titulo-card");
        tituloBox.getChildren().addAll(lblEmoji, lblTitulo);

        Label lblValor = new Label(valor);
        lblValor.getStyleClass().add("valor-metrica");

        card.getChildren().addAll(tituloBox, lblValor);
        return card;
    }

    private VBox crearCardGrafico(String titulo, String emoji) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("card", "card-grafico");

        HBox tituloBox = new HBox(5);
        tituloBox.setAlignment(Pos.CENTER_LEFT);

        Label lblEmoji = new Label(emoji);
        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("titulo-card");
        tituloBox.getChildren().addAll(lblEmoji, lblTitulo);

        Pane graficoPlaceholder = new Pane();
        graficoPlaceholder.getStyleClass().add("placeholder-grafico");

        card.getChildren().addAll(tituloBox, graficoPlaceholder);
        return card;
    }

    private VBox crearCardActividades(String titulo) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("titulo-seccion");

        GridPane tabla = new GridPane();
        tabla.getStyleClass().add("tabla-actividades");

        // Encabezados para actividades
        String[] encabezados = {"Usuario", "Acción", "Fecha/Hora", "Detalles"};
        for (int i = 0; i < encabezados.length; i++) {
            Label lbl = new Label(encabezados[i]);
            lbl.getStyleClass().add("encabezado-tabla");
            tabla.add(lbl, i, 0);
        }

        List<Actividad> actividades = actividadDAO.obtenerRecientes(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < actividades.size(); i++) {
            Actividad actividad = actividades.get(i);
            agregarFilaActividad(tabla, i+1,
                    actividad.getUsuario(),
                    actividad.getAccion(),
                    actividad.getFechaHora().format(formatter),
                    actividad.getDetalles());
        }

        Button btnVerMas = new Button("Ver más actividades");
        btnVerMas.setOnAction(e -> mostrarTodasActividades());
        btnVerMas.getStyleClass().add("boton-primario");

        card.getChildren().addAll(lblTitulo, tabla, btnVerMas);
        return card;
    }

    private void agregarFilaActividad(GridPane tabla, int fila, String usuario, String accion, String fecha, String detalles) {
        tabla.add(new Label(usuario), 0, fila);
        tabla.add(new Label(accion), 1, fila);
        tabla.add(new Label(fecha), 2, fila);

        Label lblDetalles = new Label(detalles);
        lblDetalles.getStyleClass().add("detalle-actividad");
        tabla.add(lblDetalles, 3, fila);
    }

    private void mostrarTodasActividades() {
        registrarActividad("Navegación", "Accedió a todas las actividades");

        ScrollPane scrollPane = new ScrollPane();
        VBox contenedor = new VBox(10);
        contenedor.getStyleClass().add("scroll-contenedor");
        contenedor.setPadding(new Insets(20));

        Label titulo = new Label("Todas las actividades");
        titulo.getStyleClass().add("titulo-grande");
        contenedor.getChildren().add(titulo);

        GridPane tabla = new GridPane();
        tabla.getStyleClass().add("tabla-completa");

        String[] encabezados = {"ID", "Usuario", "Acción", "Fecha/Hora", "Detalles"};
        for (int i = 0; i < encabezados.length; i++) {
            Label lbl = new Label(encabezados[i]);
            lbl.getStyleClass().add("encabezado-tabla");
            tabla.add(lbl, i, 0);
        }

        List<Actividad> actividades = actividadDAO.obtenerTodas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < actividades.size(); i++) {
            Actividad actividad = actividades.get(i);
            tabla.add(new Label(String.valueOf(actividad.getId())), 0, i+1);
            tabla.add(new Label(actividad.getUsuario()), 1, i+1);
            tabla.add(new Label(actividad.getAccion()), 2, i+1);
            tabla.add(new Label(actividad.getFechaHora().format(formatter)), 3, i+1);

            Label detalles = new Label(actividad.getDetalles());
            detalles.getStyleClass().add("detalle-actividad");
            tabla.add(detalles, 4, i+1);
        }

        contenedor.getChildren().add(tabla);
        scrollPane.setContent(contenedor);
        scrollPane.setFitToWidth(true);

        actualizarVistaPrincipal(scrollPane);
    }

    public void mostrarInventario() {
        registrarActividad("Navegación", "Accedió al Inventario");
        actualizarVistaPrincipal(new VistaInventario(controladorBarra).getVista());
    }

    public void mostrarEstadisticas() {
        registrarActividad("Navegación", "Accedió a Estadísticas");
        actualizarVistaPrincipal(new VistaEstadisticas(controladorBarra).getVista());
    }

    public void mostrarUsuarios() {
        registrarActividad("Navegación", "Accedió a Gestión de Usuarios");
        actualizarVistaPrincipal(new VistaUsuarios(controladorBarra).getVista());
    }

    public void mostrarSolicitudes() {
        registrarActividad("Navegación", "Accedió a Solicitudes");
        actualizarVistaPrincipal(new VistaSolicitudes(controladorBarra).getVista());
    }

    public void mostrarNotificaciones() {
        registrarActividad("Navegación", "Accedió a Notificaciones");
        actualizarVistaPrincipal(new VistaNotificaciones(controladorBarra).getVista());
    }

    private void confirmarCierreSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText("¿Está seguro de que desea salir?");
        alert.setContentText("Se registrará el cierre de sesión en el sistema");

        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType btnConfirmar = new ButtonType("Cerrar sesión", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(btnCancelar, btnConfirmar);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnConfirmar) {
                registrarActividad("Cierre de sesión", "Usuario cerró sesión correctamente");
                System.exit(0);
            }
        });
    }
}
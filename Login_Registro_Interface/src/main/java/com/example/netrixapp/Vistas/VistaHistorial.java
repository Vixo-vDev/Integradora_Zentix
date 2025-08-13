package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorHistorial;
import com.example.netrixapp.Modelos.Solicitud;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class VistaHistorial {

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    // Componentes de filtrado
    private DatePicker datePickerInicio;
    private DatePicker datePickerFin;
    private ComboBox<String> comboEstado;
    private ComboBox<String> comboOrdenamiento;
    private TextField txtBusqueda;
    private Button btnAplicar;
    private Button btnLimpiar;
    private Button btnVistaTabla;
    private Button btnVistaTarjetas;

    // Componentes de visualización
    private TableView<Solicitud> tablaHistorial;
    private ScrollPane scrollPaneTarjetas;
    private VBox contenedorTarjetas;
    private Pagination paginador;
    private List<Solicitud> todasLasSolicitudes;
    private static final int FILAS_POR_PAGINA = 12;
    
    // Estado de vista
    private boolean esVistaTabla = true;

    // Paleta de colores moderna
    private final String COLOR_PRIMARIO = "#4F46E5";
    private final String COLOR_SECUNDARIO = "#10B981";
    private final String COLOR_ADVERTENCIA = "#F59E0B";
    private final String COLOR_PELIGRO = "#EF4444";
    private final String COLOR_TEXTO_OSCURO = "#1F2937";
    private final String COLOR_TEXTO_NORMAL = "#6B7280";
    private final String COLOR_BORDE = "#E5E7EB";
    private final String COLOR_FONDO = "#FFFFFF";
    private final String COLOR_FONDO_SECUNDARIO = "#F8FAFC";
    private final String COLOR_SOMBRA = "rgba(0, 0, 0, 0.1)";
    private final String COLOR_FONDO_CARD = "#F3F4F6";

    public VistaHistorial(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        new ControladorHistorial(this);
    }

    private void inicializarUI() {
        // Configuración del layout principal
        vista.setStyle("-fx-background-color: " + COLOR_FONDO_SECUNDARIO + ";");
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenedor principal con scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: " + COLOR_FONDO_SECUNDARIO + ";");

        VBox contenido = new VBox(25);
        contenido.setPadding(new Insets(30));
        contenido.setStyle("-fx-background-color: " + COLOR_FONDO_SECUNDARIO + ";");
        contenido.setAlignment(Pos.TOP_CENTER);

        // Título y estadísticas
        VBox headerSection = crearHeaderSection();
        contenido.getChildren().add(headerSection);

        // Panel de filtros mejorado
        VBox panelFiltros = crearPanelFiltrosMejorado();
        contenido.getChildren().add(panelFiltros);

        // Controles de vista
        HBox controlesVista = crearControlesVista();
        contenido.getChildren().add(controlesVista);

        // Configuración de la tabla
        tablaHistorial = new TableView<>();
        tablaHistorial.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        configurarColumnasTabla();

        // Configuración de tarjetas
        contenedorTarjetas = new VBox(15);
        contenedorTarjetas.setPadding(new Insets(20));
        contenedorTarjetas.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        
        scrollPaneTarjetas = new ScrollPane(contenedorTarjetas);
        scrollPaneTarjetas.setFitToWidth(true);
        scrollPaneTarjetas.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneTarjetas.setStyle("-fx-background: " + COLOR_FONDO + ";");

        // Paginación
        paginador = new Pagination();
        paginador.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        paginador.setPageFactory(this::crearPagina);

        // Contenedor de vista dinámica
        StackPane contenedorVista = new StackPane();
        contenedorVista.getChildren().addAll(tablaHistorial, scrollPaneTarjetas);
        
        // Mostrar tabla por defecto
        tablaHistorial.setVisible(true);
        scrollPaneTarjetas.setVisible(false);

        contenido.getChildren().addAll(contenedorVista, paginador);
        scrollPane.setContent(contenido);
        vista.setCenter(scrollPane);
    }

    private VBox crearHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label lblTitulo = new Label("Historial de Solicitudes");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 28));
        lblTitulo.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        Label lblSubtitulo = new Label("Gestiona y revisa todas tus solicitudes de equipos");
        lblSubtitulo.setFont(Font.font("System", 14));
        lblSubtitulo.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");

        header.getChildren().addAll(lblTitulo, lblSubtitulo);
        return header;
    }

    private VBox crearPanelFiltrosMejorado() {
        VBox panelFiltros = new VBox(20);
        panelFiltros.setPadding(new Insets(25));
        panelFiltros.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, " + COLOR_SOMBRA + ", 8, 0, 0, 2);");

        // Título de filtros
        Label lblTituloFiltros = new Label("🔍 Filtros de Búsqueda");
        lblTituloFiltros.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblTituloFiltros.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        // Primera fila de filtros
        HBox filtrosFila1 = new HBox(20);
        filtrosFila1.setAlignment(Pos.CENTER_LEFT);

        // Búsqueda por texto
        VBox filtroBusqueda = new VBox(5);
        Label lblBusqueda = new Label("Buscar:");
        lblBusqueda.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");
        txtBusqueda = new TextField();
        txtBusqueda.setPromptText("Buscar por artículo, razón...");
        txtBusqueda.setStyle("-fx-font-size: 13px; -fx-pref-width: 200px; -fx-background-radius: 6;");
        filtroBusqueda.getChildren().addAll(lblBusqueda, txtBusqueda);

        // Estado
        VBox filtroEstado = new VBox(5);
        Label lblEstado = new Label("Estado:");
        lblEstado.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");
        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Todos", "Aprobado", "Pendiente", "Rechazado", "En Uso", "Completado");
        comboEstado.setValue("Todos");
        comboEstado.setStyle("-fx-font-size: 13px; -fx-pref-width: 150px; -fx-background-radius: 6;");
        filtroEstado.getChildren().addAll(lblEstado, comboEstado);

        // Ordenamiento
        VBox filtroOrden = new VBox(5);
        Label lblOrden = new Label("Ordenar por:");
        lblOrden.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");
        comboOrdenamiento = new ComboBox<>();
        comboOrdenamiento.getItems().addAll("Fecha más reciente", "Fecha más antigua", "Estado", "Artículo A-Z");
        comboOrdenamiento.setValue("Fecha más reciente");
        comboOrdenamiento.setStyle("-fx-font-size: 13px; -fx-pref-width: 180px; -fx-background-radius: 6;");
        filtroOrden.getChildren().addAll(lblOrden, comboOrdenamiento);

        filtrosFila1.getChildren().addAll(filtroBusqueda, filtroEstado, filtroOrden);

        // Segunda fila de filtros
        HBox filtrosFila2 = new HBox(20);
        filtrosFila2.setAlignment(Pos.CENTER_LEFT);

        // Fecha inicio
        VBox filtroFechaInicio = new VBox(5);
        Label lblInicio = new Label("Desde:");
        lblInicio.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");
        datePickerInicio = new DatePicker();
        datePickerInicio.setStyle("-fx-font-size: 13px; -fx-pref-width: 150px; -fx-background-radius: 6;");
        filtroFechaInicio.getChildren().addAll(lblInicio, datePickerInicio);

        // Fecha fin
        VBox filtroFechaFin = new VBox(5);
        Label lblFin = new Label("Hasta:");
        lblFin.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");
        datePickerFin = new DatePicker();
        datePickerFin.setStyle("-fx-font-size: 13px; -fx-pref-width: 150px; -fx-background-radius: 6;");
        filtroFechaFin.getChildren().addAll(lblFin, datePickerFin);

        filtrosFila2.getChildren().addAll(filtroFechaInicio, filtroFechaFin);

        // Botones de acción
        HBox botones = new HBox(15);
        botones.setAlignment(Pos.CENTER_RIGHT);

        btnAplicar = crearBotonEstilizado("Aplicar Filtros", COLOR_SECUNDARIO);
        btnLimpiar = crearBotonEstilizado("Limpiar Filtros", COLOR_BORDE, COLOR_TEXTO_OSCURO);

        botones.getChildren().addAll(btnLimpiar, btnAplicar);

        panelFiltros.getChildren().addAll(lblTituloFiltros, filtrosFila1, filtrosFila2, botones);
        return panelFiltros;
    }

    private HBox crearControlesVista() {
        HBox controles = new HBox(15);
        controles.setAlignment(Pos.CENTER_RIGHT);
        controles.setPadding(new Insets(0, 20, 0, 0));

        Label lblVista = new Label("Vista:");
        lblVista.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");

        btnVistaTabla = crearBotonVista("📊 Tabla", true);
        btnVistaTarjetas = crearBotonVista("🃏 Tarjetas", false);

        controles.getChildren().addAll(lblVista, btnVistaTabla, btnVistaTarjetas);

        // Configurar eventos
        btnVistaTabla.setOnAction(e -> cambiarVista(true));
        btnVistaTarjetas.setOnAction(e -> cambiarVista(false));

        return controles;
    }

    private Button crearBotonVista(String texto, boolean activo) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-font-size: 12px; -fx-padding: 8 16; -fx-background-radius: 6; -fx-font-weight: bold;");
        
        if (activo) {
            boton.setStyle(boton.getStyle() + "-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white;");
        } else {
            boton.setStyle(boton.getStyle() + "-fx-background-color: transparent; -fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-border-color: " + COLOR_BORDE + "; -fx-border-width: 1;");
        }
        
        return boton;
    }

    private void cambiarVista(boolean esTabla) {
        esVistaTabla = esTabla;
        
        if (esTabla) {
            tablaHistorial.setVisible(true);
            scrollPaneTarjetas.setVisible(false);
            btnVistaTabla.setStyle(btnVistaTabla.getStyle().replace("transparent", COLOR_PRIMARIO).replace(COLOR_TEXTO_NORMAL, "white"));
            btnVistaTarjetas.setStyle(btnVistaTarjetas.getStyle().replace(COLOR_PRIMARIO, "transparent").replace("white", COLOR_TEXTO_NORMAL));
        } else {
            tablaHistorial.setVisible(false);
            scrollPaneTarjetas.setVisible(true);
            btnVistaTarjetas.setStyle(btnVistaTarjetas.getStyle().replace("transparent", COLOR_PRIMARIO).replace(COLOR_TEXTO_NORMAL, "white"));
            btnVistaTabla.setStyle(btnVistaTabla.getStyle().replace(COLOR_PRIMARIO, "transparent").replace("white", COLOR_TEXTO_NORMAL));
        }
        
        // Actualizar la vista actual
        if (todasLasSolicitudes != null) {
            mostrarHistorial(todasLasSolicitudes);
        }
    }

    private Button crearBotonEstilizado(String texto, String colorFondo) {
        return crearBotonEstilizado(texto, colorFondo, "white");
    }

    private Button crearBotonEstilizado(String texto, String colorFondo, String colorTexto) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + colorFondo + "; " +
                "-fx-text-fill: " + colorTexto + "; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10 20; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-font-size: 13px;");
        
        boton.setOnMouseEntered(e -> boton.setStyle(boton.getStyle().replace(colorFondo, 
                colorFondo.equals(COLOR_SECUNDARIO) ? "#059669" : "#D1D5DB")));
        boton.setOnMouseExited(e -> boton.setStyle(boton.getStyle().replace(
                colorFondo.equals(COLOR_SECUNDARIO) ? "#059669" : "#D1D5DB", colorFondo)));
        
        return boton;
    }

    public void limpiarFiltros() {
        comboEstado.setValue("Todos");
        comboOrdenamiento.setValue("Fecha más reciente");
        txtBusqueda.clear();
        if (datePickerInicio != null) datePickerInicio.setValue(null);
        if (datePickerFin != null) datePickerFin.setValue(null);
    }

    private void configurarColumnasTabla() {
        tablaHistorial.getStylesheets().add(
                getClass().getResource("/css/tabla.css").toExternalForm()
        );

        TableColumn<Solicitud, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_solicitud"));
        colId.setPrefWidth(80);

        TableColumn<Solicitud, String> colArticulo = new TableColumn<>("Artículo");
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));

        TableColumn<Solicitud, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<Solicitud, String> colFecha = new TableColumn<>("Fecha Solicitud");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_solicitud"));

        TableColumn<Solicitud, String> colFechaRecibo = new TableColumn<>("Fecha Recibo");
        colFechaRecibo.setCellValueFactory(new PropertyValueFactory<>("fecha_recibo"));

        TableColumn<Solicitud, String> colTiempo = new TableColumn<>("Tiempo Uso");
        colFechaRecibo.setCellValueFactory(new PropertyValueFactory<>("tiempo_uso"));

        TableColumn<Solicitud, String> colRazon = new TableColumn<>("Razón");
        colRazon.setCellValueFactory(new PropertyValueFactory<>("razon"));

        TableColumn<Solicitud, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: " + getColorPorEstado(item) + "; -fx-font-weight: bold;");
                }
            }
        });

        tablaHistorial.getColumns().setAll(colId, colArticulo, colCantidad, colFecha, colFechaRecibo, colTiempo, colRazon, colEstado);
        tablaHistorial.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private String getColorPorEstado(String estado) {
        return switch (estado.toUpperCase()) {
            case "APROBADO" -> COLOR_SECUNDARIO;
            case "PENDIENTE" -> COLOR_ADVERTENCIA;
            case "RECHAZADO" -> COLOR_PELIGRO;
            case "EN USO" -> COLOR_PRIMARIO;
            case "COMPLETADO" -> COLOR_SECUNDARIO;
            default -> COLOR_TEXTO_OSCURO;
        };
    }

    public VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todasLasSolicitudes.size());

        List<Solicitud> solicitudesPagina = todasLasSolicitudes.subList(fromIndex, toIndex);

        if (esVistaTabla) {
            tablaHistorial.getItems().setAll(solicitudesPagina);
            return new VBox(tablaHistorial);
        } else {
            contenedorTarjetas.getChildren().clear();
            for (Solicitud solicitud : solicitudesPagina) {
                contenedorTarjetas.getChildren().add(crearTarjetaSolicitud(solicitud));
            }
            return new VBox(scrollPaneTarjetas);
        }
    }

    private VBox crearTarjetaSolicitud(Solicitud solicitud) {
        VBox tarjeta = new VBox(15);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, " + COLOR_SOMBRA + ", 4, 0, 0, 1);");

        // Header de la tarjeta
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        // ID y estado
        VBox infoPrincipal = new VBox(5);
        Label lblId = new Label("Solicitud #" + solicitud.getId_solicitud());
        lblId.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblId.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        Label lblEstado = new Label(solicitud.getEstado());
        lblEstado.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblEstado.setStyle("-fx-text-fill: " + getColorPorEstado(solicitud.getEstado()) + ";");
        lblEstado.setPadding(new Insets(4, 12, 4, 12));
        lblEstado.setStyle(lblEstado.getStyle() + "-fx-background-color: " + getColorPorEstado(solicitud.getEstado()) + "20; -fx-background-radius: 12;");

        infoPrincipal.getChildren().addAll(lblId, lblEstado);

        // Fecha
        VBox infoFecha = new VBox(5);
        Label lblFechaSolicitud = new Label("Solicitado: " + formatearFecha(solicitud.getFecha_solicitud()));
        lblFechaSolicitud.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px;");
        
        if (solicitud.getFecha_recibo() != null) {
            Label lblFechaRecibo = new Label("Recibo: " + formatearFecha(solicitud.getFecha_recibo()));
            lblFechaRecibo.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px;");
            infoFecha.getChildren().add(lblFechaRecibo);
        }
        infoFecha.getChildren().add(lblFechaSolicitud);

        header.getChildren().addAll(infoPrincipal, infoFecha);

        // Contenido de la tarjeta
        VBox contenido = new VBox(10);

        // Artículo y cantidad
        HBox infoArticulo = new HBox(10);
        infoArticulo.setAlignment(Pos.CENTER_LEFT);
        
        Label lblArticulo = new Label("📦 " + solicitud.getArticulo());
        lblArticulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblArticulo.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        
        Label lblCantidad = new Label("x" + solicitud.getCantidad());
        lblCantidad.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px; -fx-background-color: " + COLOR_FONDO_SECUNDARIO + "; -fx-padding: 4 8; -fx-background-radius: 8;");
        
        infoArticulo.getChildren().addAll(lblArticulo, lblCantidad);

        // Tiempo de uso
        if (solicitud.getTiempo_uso() != null && !solicitud.getTiempo_uso().isEmpty()) {
            Label lblTiempo = new Label("⏱️ Tiempo: " + solicitud.getTiempo_uso() + " horas");
            lblTiempo.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px;");
            contenido.getChildren().add(lblTiempo);
        }

        // Razón
        if (solicitud.getRazon() != null && !solicitud.getRazon().isEmpty()) {
            Label lblRazon = new Label("📝 " + solicitud.getRazon());
            lblRazon.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px;");
            lblRazon.setWrapText(true);
            contenido.getChildren().add(lblRazon);
        }

        contenido.getChildren().add(infoArticulo);

        // Footer con acciones
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_RIGHT);

        // Botón de detalles con funcionalidad completa
        Button btnDetalles = new Button("🔍 Ver Detalles");
        btnDetalles.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; " +
                "-fx-text-fill: white; " +
                "-fx-border-color: " + COLOR_PRIMARIO + "; " +
                "-fx-border-width: 1; " +
                "-fx-background-radius: 6; " +
                "-fx-padding: 8 16; " +
                "-fx-font-size: 12px; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 2, 0, 0, 1);");
        
        // Efectos hover para el botón
        btnDetalles.setOnMouseEntered(e -> btnDetalles.setStyle(btnDetalles.getStyle().replace(COLOR_PRIMARIO, "#4338CA")));
        btnDetalles.setOnMouseExited(e -> btnDetalles.setStyle(btnDetalles.getStyle().replace("#4338CA", COLOR_PRIMARIO)));
        
        // Agregar funcionalidad al botón
        btnDetalles.setOnAction(e -> mostrarDetallesCompletos(solicitud));

        footer.getChildren().add(btnDetalles);

        tarjeta.getChildren().addAll(header, contenido, footer);
        return tarjeta;
    }

    private String formatearFecha(LocalDate fecha) {
        if (fecha == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }

    /**
     * Muestra un diálogo modal con todos los detalles de la solicitud
     */
    private void mostrarDetallesCompletos(Solicitud solicitud) {
        // Crear el diálogo principal
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(vista.getScene().getWindow());
        dialogStage.setTitle("📋 Detalles Completos de la Solicitud #" + solicitud.getId_solicitud());
        dialogStage.setResizable(false);

        // Contenedor principal del diálogo
        VBox dialogContent = new VBox(25);
        dialogContent.setPadding(new Insets(30));
        dialogContent.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5);");

        // Header del diálogo
        VBox headerDialog = new VBox(10);
        headerDialog.setAlignment(Pos.CENTER);
        
        Label lblTituloDialog = new Label("📋 SOLICITUD #" + solicitud.getId_solicitud());
        lblTituloDialog.setFont(Font.font("System", FontWeight.BOLD, 24));
        lblTituloDialog.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        
        // Badge de estado prominente
        Label lblEstadoDialog = new Label(solicitud.getEstado());
        lblEstadoDialog.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblEstadoDialog.setStyle("-fx-text-fill: " + getColorPorEstado(solicitud.getEstado()) + "; " +
                "-fx-background-color: " + getColorPorEstado(solicitud.getEstado()) + "20; " +
                "-fx-background-radius: 20; " +
                "-fx-padding: 8 20; " +
                "-fx-border-color: " + getColorPorEstado(solicitud.getEstado()) + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 20;");
        
        headerDialog.getChildren().addAll(lblTituloDialog, lblEstadoDialog);

        // Información principal organizada en secciones
        VBox infoPrincipal = new VBox(20);
        
        // Sección 1: Información de la Solicitud
        VBox seccionSolicitud = crearSeccionDetalle("📋 INFORMACIÓN DE LA SOLICITUD", 
            "📦 Artículo solicitado: " + solicitud.getArticulo(),
            "🔢 Cantidad: " + solicitud.getCantidad() + " unidades",
            "⏱️ Tiempo de uso: " + (solicitud.getTiempo_uso() != null && !solicitud.getTiempo_uso().isEmpty() ? solicitud.getTiempo_uso() + " horas" : "No especificado"),
            "📝 Razón: " + (solicitud.getRazon() != null && !solicitud.getRazon().isEmpty() ? solicitud.getRazon() : "No especificada")
        );
        
        // Sección 2: Fechas
        VBox seccionFechas = crearSeccionDetalle("📅 FECHAS",
            "📝 Fecha de solicitud: " + formatearFecha(solicitud.getFecha_solicitud()),
            "📦 Fecha de recibo: " + formatearFecha(solicitud.getFecha_recibo())
        );
        
        // Sección 3: Estado
        VBox seccionEstado = crearSeccionDetalle("🏷️ ESTADO",
            "📊 Estado actual: " + solicitud.getEstado()
        );
        
        infoPrincipal.getChildren().addAll(seccionSolicitud, seccionFechas, seccionEstado);

        // Botón de cerrar
        Button btnCerrar = new Button("✅ Cerrar");
        btnCerrar.setStyle("-fx-background-color: " + COLOR_SECUNDARIO + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 12 30; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        
        btnCerrar.setOnMouseEntered(e -> btnCerrar.setStyle(btnCerrar.getStyle().replace(COLOR_SECUNDARIO, "#059669")));
        btnCerrar.setOnMouseExited(e -> btnCerrar.setStyle(btnCerrar.getStyle().replace("#059669", COLOR_SECUNDARIO)));
        btnCerrar.setOnAction(e -> dialogStage.close());

        // Agregar todo al contenido del diálogo
        dialogContent.getChildren().addAll(headerDialog, infoPrincipal, btnCerrar);
        dialogContent.setAlignment(Pos.CENTER);

        // Crear la escena y mostrar el diálogo
        Scene dialogScene = new Scene(dialogContent);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    /**
     * Crea una sección de detalle con título y campos
     */
    private VBox crearSeccionDetalle(String titulo, String... campos) {
        VBox seccion = new VBox(12);
        seccion.setPadding(new Insets(20));
        seccion.setStyle("-fx-background-color: " + COLOR_FONDO_CARD + "; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");

        // Título de la sección
        Label lblTituloSeccion = new Label(titulo);
        lblTituloSeccion.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblTituloSeccion.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-padding: 0 0 10 0;");
        
        seccion.getChildren().add(lblTituloSeccion);

        // Campos de la sección
        for (String campo : campos) {
            Label lblCampo = new Label(campo);
            lblCampo.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 5 0;");
            lblCampo.setWrapText(true);
            seccion.getChildren().add(lblCampo);
        }

        return seccion;
    }

    public void mostrarHistorial(List<Solicitud> solicitudes) {
        this.todasLasSolicitudes = solicitudes;
        int totalPaginas = (int) Math.ceil((double) solicitudes.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(totalPaginas);
        paginador.setCurrentPageIndex(0);
        paginador.setPageFactory(this::crearPagina);
    }

    // Getters para los componentes
    public Button getBtnLimpiar() { return btnLimpiar; }
    public Button getBtnAplicar() { return btnAplicar; }
    public String getdateInicio() { return datePickerInicio.getValue() != null ? datePickerInicio.getValue().toString() : null; }
    public String getdateFin() { return datePickerFin.getValue() != null ? datePickerFin.getValue().toString() : null; }
    public String getComboEstado() { return comboEstado.getValue(); }
    public String getComboOrdenamiento() { return comboOrdenamiento.getValue(); }
    public String getTxtBusqueda() { return txtBusqueda.getText().trim(); }
    public BorderPane getVista() { return vista; }
}
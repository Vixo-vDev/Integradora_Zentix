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
import java.util.List;

public class VistaHistorial {

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    // Componentes de filtrado
    private DatePicker datePickerInicio;
    private DatePicker datePickerFin;
    private ComboBox<String> comboEstado;
    private Button btnAplicar;
    private Button btnLimpiar;

    // Componentes de visualización
    private TableView<Solicitud> tablaHistorial;
    private Pagination paginador;
    private List<Solicitud> todasLasSolicitudes;
    private static final int FILAS_POR_PAGINA = 15;

    // Paleta de colores moderna
    private final String COLOR_PRIMARIO = "#4F46E5";
    private final String COLOR_SECUNDARIO = "#10B981";
    private final String COLOR_ADVERTENCIA = "#F59E0B";
    private final String COLOR_PELIGRO = "#EF4444";
    private final String COLOR_TEXTO_OSCURO = "#1F2937";
    private final String COLOR_TEXTO_NORMAL = "#6B7280";
    private final String COLOR_BORDE = "#E5E7EB";
    private final String COLOR_FONDO = "#FFFFFF";

    public VistaHistorial(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        new ControladorHistorial(this);
    }

    private void inicializarUI() {
        // Configuración del layout principal
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenedor principal con scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: " + COLOR_FONDO + ";");

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(30));
        contenido.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        contenido.setAlignment(Pos.TOP_CENTER);

        // Panel de filtros
        GridPane panelFiltros = crearPanelFiltros();
        contenido.getChildren().add(panelFiltros);

        // Configuración de la tabla
        tablaHistorial = new TableView<>();
        tablaHistorial.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        configurarColumnasTabla();

        // Paginación
        paginador = new Pagination();
        paginador.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        paginador.setPageFactory(this::crearPagina);

        contenido.getChildren().addAll(tablaHistorial, paginador);
        scrollPane.setContent(contenido);
        vista.setCenter(scrollPane);
    }

    private GridPane crearPanelFiltros() {
        GridPane panelFiltros = new GridPane();
        panelFiltros.setHgap(15);
        panelFiltros.setVgap(15);
        panelFiltros.setPadding(new Insets(10));
        panelFiltros.setAlignment(Pos.CENTER_LEFT);
        panelFiltros.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Estado
        Label lblEstado = new Label("Estado:");
        lblEstado.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Todos", "Aprobado", "Pendiente", "Rechazado");
        comboEstado.setValue("Todos");
        comboEstado.setStyle("-fx-font-size: 14px; -fx-pref-width: 180px;");

        // Fecha inicio
        Label lblInicio = new Label("Desde:");
        lblInicio.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        datePickerInicio = new DatePicker();
        datePickerInicio.setStyle("-fx-font-size: 14px; -fx-pref-width: 150px;");

        // Fecha fin
        Label lblFin = new Label("Hasta:");
        lblFin.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        datePickerFin = new DatePicker();
        datePickerFin.setStyle("-fx-font-size: 14px; -fx-pref-width: 150px;");

        // Botones
        btnAplicar = new Button("Aplicar Filtros");
        btnAplicar.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");
        btnAplicar.setOnMouseEntered(e -> btnAplicar.setStyle("-fx-background-color: #007A62; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;"));
        btnAplicar.setOnMouseExited(e -> btnAplicar.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;"));

        btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: " + COLOR_BORDE + "; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");
        btnLimpiar.setOnMouseEntered(e -> btnLimpiar.setStyle("-fx-background-color: #D1D5DB; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";   -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;"));
        btnLimpiar.setOnMouseExited(e -> btnLimpiar.setStyle("-fx-background-color: " + COLOR_BORDE + "; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";  -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;"));

        HBox boxBotones = new HBox(10, btnAplicar, btnLimpiar);
        boxBotones.setAlignment(Pos.CENTER_LEFT);

        // Distribución en el grid
        panelFiltros.add(lblEstado, 0, 0);
        panelFiltros.add(comboEstado, 1, 0);
        panelFiltros.add(lblInicio, 0, 1);
        panelFiltros.add(datePickerInicio, 1, 1);
        panelFiltros.add(lblFin, 2, 1);
        panelFiltros.add(datePickerFin, 3, 1);
        panelFiltros.add(boxBotones, 0, 2, 4, 1); // botones ocupan 4 columnas

        return panelFiltros;
    }


    public void limpiarFiltros() {
        comboEstado.setValue("Todos");
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

        TableColumn<Solicitud, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_solicitud"));

        TableColumn<Solicitud, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);;
                } else {
                    setText(item);
                    setStyle(" -fx-text-fill: " + getColorPorEstado(item) + "; -fx-font-weight: bold;");
                }
            }
        });

        tablaHistorial.getColumns().setAll(colId, colArticulo, colCantidad, colFecha, colEstado);
        tablaHistorial.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private String getColorPorEstado(String estado) {
        return switch (estado.toUpperCase()) {
            case "APROBADO" -> COLOR_SECUNDARIO;
            case "PENDIENTE" -> COLOR_ADVERTENCIA;
            case "RECHAZADO" -> COLOR_PELIGRO;
            default -> COLOR_TEXTO_OSCURO;
        };
    }

    public VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todasLasSolicitudes.size());

        tablaHistorial.getItems().setAll(todasLasSolicitudes.subList(fromIndex, toIndex));

        return new VBox(tablaHistorial);
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
    public BorderPane getVista() { return vista; }
}
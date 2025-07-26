package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorHistorial;
import com.example.netrixapp.Modelos.Solicitud;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class VistaHistorial {

    private Button btnAplicar;
    private Button btnLimpiar;
    private DatePicker datePickerInicio;
    private DatePicker datePickerFin;
    private ComboBox<String> comboEstado;
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    private TableView<Solicitud> tablaHistorial;
    private Pagination paginador;
    private List<Solicitud> todosLasSolicitudes;
    private static final int FILAS_POR_PAGINA = 50;

    private VBox contenedorPrincipal;

    // Colores
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";
    private final String COLOR_BOTON_NORMAL = "#E5E7EB";
    private final String COLOR_BOTON_ACCION = "#3B82F6";

    public Button getBtnLimpiar() {
        return btnLimpiar;
    }

    public Button getBtnAplicar() {
        return btnAplicar;
    }

    public String getdateInicio(){
        return datePickerInicio.getValue() != null ? datePickerInicio.getValue().toString() : null;
    }

    public String getdateFin(){
        return datePickerFin.getValue() != null ? datePickerFin.getValue().toString() : null;
    }

    public String getComboEstado(){
        return comboEstado.getValue();
    }

    public VistaHistorial(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();

        // 👇 Aquí se llama al controlador para que cargue los datos
        new ControladorHistorial(this);
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: transparent;");
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        contenedorPrincipal = new VBox(20);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setStyle("-fx-background-color: transparent;");

        // Panel de filtros
        GridPane panelFiltros = new GridPane();
        panelFiltros.setHgap(15);
        panelFiltros.setVgap(10);
        panelFiltros.setPadding(new Insets(0, 0, 20, 0));

        Label lblEstado = new Label("Estado");
        lblEstado.setStyle("-fx-font-size: 14px;");
        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Todos", "Aprobado", "Pendiente", "Rechazado");
        comboEstado.setValue("Todos");
        comboEstado.setStyle("-fx-min-width: 150px;");

        Label lblRangoFecha = new Label("Rango de fecha");
        lblRangoFecha.setStyle("-fx-font-size: 14px;");
        datePickerInicio = new DatePicker();
        datePickerInicio.setStyle("-fx-min-width: 120px;");
        datePickerFin = new DatePicker();
        datePickerFin.setStyle("-fx-min-width: 120px;");

        btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: " + COLOR_BOTON_NORMAL + "; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-padding: 5 15;");

        btnAplicar = new Button("Aplicar");
        btnAplicar.setStyle("-fx-background-color: " + COLOR_BOTON_ACCION + "; -fx-text-fill: white; -fx-padding: 5 15;");

        panelFiltros.add(lblEstado, 0, 0);
        panelFiltros.add(comboEstado, 1, 0);
        panelFiltros.add(lblRangoFecha, 0, 1);
        panelFiltros.add(datePickerInicio, 1, 1);
        panelFiltros.add(new Label("a"), 2, 1);
        panelFiltros.add(datePickerFin, 3, 1);
        panelFiltros.add(btnLimpiar, 4, 0);
        panelFiltros.add(btnAplicar, 4, 1);

        // Tabla
        tablaHistorial = new TableView<>();
        tablaHistorial.setStyle("-fx-background-color: transparent;");
        tablaHistorial.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Solicitud, Integer> columnaID = new TableColumn<>("ID");
        columnaID.setCellValueFactory(new PropertyValueFactory<>("id_solicitud"));
        columnaID.setPrefWidth(80);

        TableColumn<Solicitud, String> columnaArticulo = new TableColumn<>("Artículo");
        columnaArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        columnaArticulo.setPrefWidth(400);

        TableColumn<Solicitud, Integer> columnaCantidad = new TableColumn<>("Cantidad");
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaCantidad.setPrefWidth(100);

        TableColumn<Solicitud, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_solicitud"));
        columnaFecha.setPrefWidth(120);

        TableColumn<Solicitud, String> columnaEstado = new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaEstado.setPrefWidth(120);

        tablaHistorial.getColumns().addAll(columnaID, columnaArticulo, columnaCantidad, columnaFecha, columnaEstado);

        contenedorPrincipal.getChildren().addAll(panelFiltros, tablaHistorial);
        vista.setCenter(contenedorPrincipal);
    }

    public VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todosLasSolicitudes.size());

        tablaHistorial.getItems().setAll(todosLasSolicitudes.subList(fromIndex, toIndex));

        return new VBox(); // No se necesita retornar tabla aquí
    }

    public void mostrarHistorial(List<Solicitud> solicitudes) {
        this.todosLasSolicitudes = solicitudes;

        if (paginador != null) {
            contenedorPrincipal.getChildren().remove(paginador);
        }

        paginador = new Pagination((int) Math.ceil((double) todosLasSolicitudes.size() / FILAS_POR_PAGINA), 0);
        paginador.setPageFactory(this::crearPagina);

        contenedorPrincipal.getChildren().add(paginador);
    }

    public BorderPane getVista() {
        return vista;
    }
}

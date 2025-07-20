package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class VistaHistorial {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    // Colores
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";
    private final String COLOR_BOTON_NORMAL = "#E5E7EB";
    private final String COLOR_BOTON_ACCION = "#3B82F6";

    public VistaHistorial(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: transparent;");

        // Barra superior
        vista.setTop(controladorBarra.getBarraSuperior());

        // Barra lateral
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenido principal
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: transparent;");

        // 1. Panel de filtros
        GridPane panelFiltros = new GridPane();
        panelFiltros.setHgap(15);
        panelFiltros.setVgap(10);
        panelFiltros.setPadding(new Insets(0, 0, 20, 0));

        // Filtro por estado
        Label lblEstado = new Label("Estado");
        lblEstado.setStyle("-fx-font-size: 14px;");
        ComboBox<String> comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Todos", "Aprobado", "Pendiente", "Rechazado");
        comboEstado.setValue("Todos");
        comboEstado.setStyle("-fx-min-width: 150px;");

        // Filtro por rango de fechas
        Label lblRangoFecha = new Label("Rango de fecha");
        lblRangoFecha.setStyle("-fx-font-size: 14px;");
        DatePicker datePickerInicio = new DatePicker();
        datePickerInicio.setStyle("-fx-min-width: 120px;");
        DatePicker datePickerFin = new DatePicker();
        datePickerFin.setStyle("-fx-min-width: 120px;");

        // Botones de acción
        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: " + COLOR_BOTON_NORMAL + "; " +
                "-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-padding: 5 15;");

        Button btnAplicar = new Button("Aplicar");
        btnAplicar.setStyle("-fx-background-color: " + COLOR_BOTON_ACCION + "; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 5 15;");

        // Organizar elementos en el grid
        panelFiltros.add(lblEstado, 0, 0);
        panelFiltros.add(comboEstado, 1, 0);
        panelFiltros.add(lblRangoFecha, 0, 1);
        panelFiltros.add(datePickerInicio, 1, 1);
        panelFiltros.add(new Label("a"), 2, 1);
        panelFiltros.add(datePickerFin, 3, 1);
        panelFiltros.add(btnLimpiar, 4, 0);
        panelFiltros.add(btnAplicar, 4, 1);

        // 2. Tabla de historial
        TableView<Solicitud> tablaHistorial = new TableView<>();
        tablaHistorial.setStyle("-fx-background-color: transparent;");

        // Columnas de la tabla
        TableColumn<Solicitud, String> columnaID = new TableColumn<>("ID");
        columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaID.setPrefWidth(80);

        TableColumn<Solicitud, String> columnaArticulo = new TableColumn<>("Artículo");
        columnaArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        columnaArticulo.setPrefWidth(200);

        TableColumn<Solicitud, Integer> columnaCantidad = new TableColumn<>("Cantidad");
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaCantidad.setPrefWidth(100);

        TableColumn<Solicitud, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaFecha.setPrefWidth(120);

        TableColumn<Solicitud, String> columnaEstado = new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaEstado.setPrefWidth(120);

        // Añadir columnas a la tabla
        tablaHistorial.getColumns().addAll(columnaID, columnaArticulo,
                columnaCantidad, columnaFecha, columnaEstado);

        // Datos de ejemplo
        tablaHistorial.getItems().addAll(
                new Solicitud("001", "Laptop HP", 2, "2023-05-15", "Aprobado"),
                new Solicitud("002", "Monitor 24\"", 1, "2023-05-10", "Pendiente"),
                new Solicitud("003", "Teclado inalámbrico", 5, "2023-05-05", "Rechazado")
        );

        // Construir vista completa
        contenido.getChildren().addAll(panelFiltros, tablaHistorial);
        vista.setCenter(contenido);
    }

    public BorderPane getVista() {
        return vista;
    }

    // Clase interna para representar las solicitudes
    public static class Solicitud {
        private final String id;
        private final String articulo;
        private final int cantidad;
        private final String fecha;
        private final String estado;

        public Solicitud(String id, String articulo, int cantidad, String fecha, String estado) {
            this.id = id;
            this.articulo = articulo;
            this.cantidad = cantidad;
            this.fecha = fecha;
            this.estado = estado;
        }

        // Getters necesarios para PropertyValueFactory
        public String getId() { return id; }
        public String getArticulo() { return articulo; }
        public int getCantidad() { return cantidad; }
        public String getFecha() { return fecha; }
        public String getEstado() { return estado; }
    }
}
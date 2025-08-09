package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorEstadisticas;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

import static javafx.application.Application.launch;

public class VistaEstadisticas {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final VBox contenedorMasSolicitados;
    private final VBox contenedorGrafico;
    private final TableView<Object[]> tablaMasSolicitados;
    private final TableView<Object[]> tablaMenosSolicitados;

    private final ControladorEstadisticas controlador;

    public VistaEstadisticas(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        this.tablaMasSolicitados = new TableView<>();
        this.tablaMenosSolicitados = new TableView<>();
        this.contenedorMasSolicitados = new VBox(10);
        this.contenedorGrafico = new VBox(10);
        this.controlador = new ControladorEstadisticas(this); // Instancia del controlador
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setStyle("-fx-background-color: #ECF0F1;");
        contenedorGrafico.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 15;");

        // Cargar gráfico inicial
        List<ControladorEstadisticas.EquipoChartData> datos = null;
        try {
            datos = controlador.getDatosParaGrafico(10);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mostrarGraficoBarras(datos);

        VBox contenidoPrincipal = new VBox(20);
        contenidoPrincipal.setPadding(new Insets(20));

        // Título principal visible y con texto oscuro
        Label lblTitulo = new Label("Estadísticas de Solicitudes");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // Filtro de periodo con botón aplicar y botón quitar filtros
        HBox filtroBox = new HBox(10);
        Label lblFiltro = new Label("Periodo:");
        lblFiltro.setStyle("-fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        ComboBox<String> comboPeriodo = new ComboBox<>();
        comboPeriodo.getItems().addAll("Semana", "Mes");
        comboPeriodo.setValue("Semana");

        Button btnAplicar = new Button("Aplicar filtros");
        btnAplicar.setOnAction(e -> {
            String seleccion = comboPeriodo.getValue().toLowerCase();
            controlador.cargarDatosEstadisticos(seleccion);
        });

        Button btnQuitarFiltros = new Button("Quitar filtros");
        btnQuitarFiltros.setOnAction(e -> {
            controlador.cargarMasSolicitadosSinFiltro();
        });

        filtroBox.getChildren().addAll(lblFiltro, comboPeriodo, btnAplicar, btnQuitarFiltros);
        filtroBox.setPadding(new Insets(10, 0, 10, 0));

        // Configurar tablas
        configurarTabla(tablaMasSolicitados, new String[]{"Equipo", "Cantidad"});
        configurarTabla(tablaMenosSolicitados, new String[]{"Equipo", "Cantidad"});

        // Etiqueta para tabla más solicitados con texto oscuro
        Label lblMas = new Label("Equipos Más Solicitados");
        lblMas.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        contenedorMasSolicitados.getChildren().addAll(lblMas, tablaMasSolicitados);
        contenedorMasSolicitados.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #BDC3C7; -fx-border-radius: 8;");
        contenedorMasSolicitados.setPadding(new Insets(15));

        // Etiqueta para tabla menos solicitados con texto oscuro
        Label tituloGrafico = new Label("Gráfico de Barras");
        tituloGrafico.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        BarChart<String, Number> grafico = null;
        try {
            grafico = crearGraficoBarras(controlador.getDatosParaGrafico(10));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        contenedorGrafico.getChildren().clear();
        contenedorGrafico.getChildren().addAll(tituloGrafico, grafico);
        // Separador para separar visualmente las tablas
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        contenidoPrincipal.getChildren().addAll(lblTitulo, filtroBox, contenedorMasSolicitados, separator, contenedorGrafico);

        ScrollPane scrollPane = new ScrollPane(contenidoPrincipal);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        vista.setCenter(scrollPane);
    }

    private void configurarTabla(TableView<Object[]> tabla, String[] columnas) {
        tabla.getStylesheets().add(
                getClass().getResource("/css/tabla.css").toExternalForm()
        );

        tabla.getColumns().clear();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setStyle("-fx-font-size: 14px;");

        for (int i = 0; i < columnas.length; i++) {
            final int colIndex = i;
            TableColumn<Object[], String> columna = new TableColumn<>(columnas[i]);
            columna.setCellValueFactory(data -> {
                Object val = data.getValue()[colIndex];
                return new javafx.beans.property.SimpleStringProperty(val == null ? "" : val.toString());
            });
            tabla.getColumns().add(columna);
        }

        tabla.setRowFactory(tv -> new TableRow<Object[]>() {
            @Override
            protected void updateItem(Object[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: " + (getIndex() % 2 == 0 ? "#F8F9F9" : "white") + ";");
                }
            }
        });

        tabla.setPlaceholder(new Label("No hay datos disponibles."));
    }

    private BarChart<String, Number> crearGraficoBarras(List<ControladorEstadisticas.EquipoChartData> datos) {
        // 1. Configurar ejes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Equipos");
        yAxis.setLabel("Cantidad de Solicitudes");

        // 2. Crear gráfico
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Distribución de Solicitudes por Equipo");
        barChart.setLegendVisible(false);
        barChart.setStyle("-fx-font-size: 14px; -fx-title-fill: #2C3E50;");

        // 3. Paleta de colores para las barras
        String[] colores = {"#009475", "#4F46E5", "#10B981", "#F59E0B", "#EC4899"};

        // 4. Preparar datos
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < datos.size(); i++) {
            ControladorEstadisticas.EquipoChartData item = datos.get(i);
            XYChart.Data<String, Number> data = new XYChart.Data<>(item.getNombreEquipo(), item.getCantidad());
            series.getData().add(data);

            int finalI = i;
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    // Color diferente para cada barra (cíclico)
                    String color = colores[finalI % colores.length];
                    newNode.setStyle("-fx-bar-fill: " + color + ";");

                    // Etiqueta interna
                    StackPane stackPane = (StackPane) newNode;
                    Label label = new Label(item.getNombreEquipo() + ": " + item.getCantidad());
                    label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
                    stackPane.getChildren().add(label);
                }
            });
        }

        barChart.getData().add(series);

        // 6. Ajustes finales
        xAxis.setTickLabelRotation(-45); // Rotar nombres largos
        barChart.setCategoryGap(20);     // Espacio entre barras

        return barChart;
    }

    public void mostrarGraficoBarras(List<ControladorEstadisticas.EquipoChartData> datos) {
        BarChart<String, Number> grafico = crearGraficoBarras(datos);
        contenedorGrafico.getChildren().clear();
        contenedorGrafico.getChildren().addAll(
                new Label("Distribución de Solicitudes"), // Título del gráfico
                grafico
        );
    }

    public void mostrarEquiposMasSolicitados(List<Object[]> datos) {
        tablaMasSolicitados.getItems().clear();
        tablaMasSolicitados.getItems().addAll(datos);
    }


    public BorderPane getVista() {
        return vista;
    }

}

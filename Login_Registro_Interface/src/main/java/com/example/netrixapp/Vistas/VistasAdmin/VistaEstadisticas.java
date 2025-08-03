package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorEstadisticas;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

import static javafx.application.Application.launch;

public class VistaEstadisticas {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final VBox contenedorMasSolicitados;
    private final VBox contenedorMenosSolicitados;
    private final TableView<Object[]> tablaMasSolicitados;
    private final TableView<Object[]> tablaMenosSolicitados;

    private final ControladorEstadisticas controlador;

    public VistaEstadisticas(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        this.tablaMasSolicitados = new TableView<>();
        this.tablaMenosSolicitados = new TableView<>();
        this.contenedorMasSolicitados = new VBox(10);
        this.contenedorMenosSolicitados = new VBox(10);
        this.controlador = new ControladorEstadisticas(this); // Instancia del controlador
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setStyle("-fx-background-color: #ECF0F1;");

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
        Label lblMenos = new Label("Equipos Menos Solicitados");
        lblMenos.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        contenedorMenosSolicitados.getChildren().addAll(lblMenos, tablaMenosSolicitados);
        contenedorMenosSolicitados.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #BDC3C7; -fx-border-radius: 8;");
        contenedorMenosSolicitados.setPadding(new Insets(15));

        // Separador para separar visualmente las tablas
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        contenidoPrincipal.getChildren().addAll(lblTitulo, filtroBox, contenedorMasSolicitados, separator, contenedorMenosSolicitados);

        ScrollPane scrollPane = new ScrollPane(contenidoPrincipal);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        vista.setCenter(scrollPane);
    }

    private void configurarTabla(TableView<Object[]> tabla, String[] columnas) {
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

    public void mostrarEquiposMasSolicitados(List<Object[]> datos) {
        tablaMasSolicitados.getItems().clear();
        tablaMasSolicitados.getItems().addAll(datos);
    }

    public void mostrarEquiposMenosSolicitados(List<Object[]> datos) {
        tablaMenosSolicitados.getItems().clear();
        tablaMenosSolicitados.getItems().addAll(datos);
    }

    public BorderPane getVista() {
        return vista;
    }

}

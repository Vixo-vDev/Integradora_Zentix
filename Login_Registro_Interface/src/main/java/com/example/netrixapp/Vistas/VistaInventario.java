package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Modelos.Equipo;
import impl.EquipoDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.List;

public class VistaInventario {
    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private TableView<Equipo> tablaEquipos;
    private Pagination paginador;
    private List<Equipo> todosLosEquipos;
    private static final int FILAS_POR_PAGINA = 50;

    // Paleta de colores moderna
    private final String COLOR_PRIMARIO = "#4F46E5";
    private final String COLOR_SECUNDARIO = "#10B981";
    private final String COLOR_ADVERTENCIA = "#F59E0B";
    private final String COLOR_PELIGRO = "#EF4444";
    private final String COLOR_TEXTO_OSCURO = "#1F2937";
    private final String COLOR_TEXTO_NORMAL = "#6B7280";
    private final String COLOR_BORDE = "#E5E7EB";
    private final String COLOR_FONDO = "#FFFFFF";

    public VistaInventario(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
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
        scrollPane.setStyle("-fx-background: " + COLOR_FONDO + "; -fx-border-color: " + COLOR_FONDO + ";");

        VBox contenido = new VBox(25);
        contenido.setPadding(new Insets(30));
        contenido.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        contenido.setAlignment(Pos.TOP_CENTER);

        // Título de la página
        Label tituloPagina = new Label("Gestión de Inventario");
        tituloPagina.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        contenido.getChildren().add(tituloPagina);

        // Panel de métricas
        HBox panelMetricas = crearPanelMetricas();
        contenido.getChildren().add(panelMetricas);

        // Separador
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + COLOR_BORDE + ";");
        separator.setPadding(new Insets(20, 0, 20, 0));
        contenido.getChildren().add(separator);

        // Panel de búsqueda y acciones
        HBox panelAcciones = crearPanelAcciones();
        contenido.getChildren().add(panelAcciones);

        // Configuración de la tabla
        tablaEquipos = new TableView<>();
        tablaEquipos.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        configurarColumnasTabla();

        // Paginación
        paginador = new Pagination();
        paginador.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        paginador.setPageFactory(this::crearPagina);

        VBox contenedorTabla = new VBox(15, tablaEquipos, paginador);
        contenedorTabla.setPadding(new Insets(10, 0, 0, 0));
        contenido.getChildren().add(contenedorTabla);

        scrollPane.setContent(contenido);
        vista.setCenter(scrollPane);
    }

    private HBox crearPanelMetricas() {
        HBox panelMetricas = new HBox(20);
        panelMetricas.setAlignment(Pos.TOP_CENTER);

        VBox cardTotal = null;
        try {
            cardTotal = crearCardMetrica("Total de Artículos",
                    String.valueOf(equipoDao.totalEquipos()),
                    COLOR_PRIMARIO, "📦");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VBox cardStockBajo = null;
        try {
            cardStockBajo = crearCardMetrica("Stock Bajo",
                    String.valueOf(equipoDao.totalStockBajo()),
                    COLOR_ADVERTENCIA, "⚠️");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VBox cardDisponibles = null;
        try {
            cardDisponibles = crearCardMetrica("Disponibles",
                    String.valueOf(equipoDao.equiposDisponibles()),
                    COLOR_SECUNDARIO, "✔️");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        panelMetricas.getChildren().addAll(cardTotal, cardStockBajo, cardDisponibles);

        // Hacer que las cards se expandan
        for (Node card : panelMetricas.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxWidth(Double.MAX_VALUE);
        }

        return panelMetricas;
    }

    private VBox crearCardMetrica(String titulo, String valor, String color, String emoji) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 10; " +
                "-fx-padding: 20;");
        card.setAlignment(Pos.CENTER);

        // Encabezado con emoji
        HBox encabezado = new HBox(8);
        encabezado.setAlignment(Pos.CENTER);

        Label lblEmoji = new Label(emoji);
        lblEmoji.setStyle("-fx-font-size: 24px;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");

        encabezado.getChildren().addAll(lblEmoji, lblTitulo);

        // Valor
        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(encabezado, lblValor);
        return card;
    }

    private HBox crearPanelAcciones() {
        HBox panelAcciones = new HBox(15);
        panelAcciones.setAlignment(Pos.CENTER_LEFT);

        // Campo de búsqueda
        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Buscar equipo...");
        campoBusqueda.setStyle("-fx-font-size: 14px; -fx-padding: 8 12; -fx-background-radius: 6;");
        campoBusqueda.setPrefWidth(300);

        // Botón de búsqueda
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 6;");
        btnBuscar.setOnMouseEntered(e -> btnBuscar.setStyle("-fx-background-color: #4338CA; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        btnBuscar.setOnMouseExited(e -> btnBuscar.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));

        // Botón de agregar
        Button btnAgregar = new Button("Agregar Equipo");
        btnAgregar.setStyle("-fx-background-color: " + COLOR_SECUNDARIO + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 6;");
        btnAgregar.setOnMouseEntered(e -> btnAgregar.setStyle("-fx-background-color: #0D9488; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        btnAgregar.setOnMouseExited(e -> btnAgregar.setStyle("-fx-background-color: " + COLOR_SECUNDARIO + "; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));

        panelAcciones.getChildren().addAll(campoBusqueda, btnBuscar, btnAgregar);
        return panelAcciones;
    }

    private void configurarColumnasTabla() {
        // Estilo base para las columnas
        String estiloColumna = "-fx-alignment: CENTER-LEFT; -fx-font-size: 14px; -fx-padding: 8 12;";
        String estiloHeader = "-fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-size: 14px;";

        TableColumn<Equipo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_equipo"));
        colId.setStyle(estiloColumna);
        colId.setPrefWidth(80);

        TableColumn<Equipo, String> colCodigo = new TableColumn<>("Código Bien");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo_bien"));
        colCodigo.setStyle(estiloColumna);

        TableColumn<Equipo, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDescripcion.setStyle(estiloColumna);

        TableColumn<Equipo, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colMarca.setStyle(estiloColumna);

        TableColumn<Equipo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colModelo.setStyle(estiloColumna);

        TableColumn<Equipo, String> colSerie = new TableColumn<>("N° Serie");
        colSerie.setCellValueFactory(new PropertyValueFactory<>("numero_serie"));
        colSerie.setStyle(estiloColumna);

        TableColumn<Equipo, Integer> colDisponible = new TableColumn<>("Disponible");
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        colDisponible.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(estiloColumna);
                } else {
                    setText(item.toString());
                    setStyle(estiloColumna + " -fx-text-fill: " + (item > 0 ? COLOR_SECUNDARIO : COLOR_PELIGRO) + ";");
                }
            }
        });

        TableColumn<Equipo, Integer> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo_equipo"));
        colTipo.setStyle(estiloColumna);

        // Aplicar estilo a los headers
        for (TableColumn<Equipo, ?> col : List.of(colId, colCodigo, colDescripcion, colMarca, colModelo, colSerie, colDisponible, colTipo)) {
            col.setStyle("-fx-alignment: CENTER-LEFT;");
            col.setReorderable(false);
            col.setSortable(true);
            col.getStyleClass().add("table-column");
        }

        tablaEquipos.getColumns().setAll(colId, colCodigo, colDescripcion, colMarca, colModelo, colSerie, colDisponible, colTipo);
        tablaEquipos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todosLosEquipos.size());

        tablaEquipos.getItems().setAll(todosLosEquipos.subList(fromIndex, toIndex));

        VBox pagina = new VBox(tablaEquipos);
        pagina.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        return pagina;
    }

    public void mostrarEquipos(List<Equipo> equipos) {
        this.todosLosEquipos = equipos;
        int totalPaginas = (int) Math.ceil((double) equipos.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(totalPaginas);
        paginador.setCurrentPageIndex(0);
        paginador.setPageFactory(this::crearPagina);
    }

    public BorderPane getVista() {
        return vista;
    }
}
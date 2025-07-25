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

import java.util.List;

public class VistaInventario {
    private EquipoDaoImpl equipoDao = new EquipoDaoImpl();

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private TableView<Equipo> tablaEquipos;
    private Pagination paginador;
    private List<Equipo> todosLosEquipos;

    private static final int FILAS_POR_PAGINA = 50;

    private final String COLOR_TEXTO_OSCURO = "#2C3E50";
    private final String COLOR_TEXTO_NORMAL = "#4B5563";
    private final String COLOR_ADVERTENCIA = "#F59E0B";

    public VistaInventario(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: transparent;");
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: transparent;");
        contenido.setAlignment(Pos.TOP_CENTER);

        HBox filaMetricas = new HBox(15);
        filaMetricas.setAlignment(Pos.TOP_CENTER);

        VBox cardArticulos = crearCardMetrica("Artículos", String.valueOf(equipoDao.totalEquipos()), COLOR_TEXTO_OSCURO);
        VBox cardStockBajo = crearCardMetrica("Stock bajo", String.valueOf(equipoDao.totalStockBajo()), COLOR_ADVERTENCIA);
        VBox cardDisponibles = crearCardMetrica("Disponibles", String.valueOf(equipoDao.equiposDisponibles()), COLOR_TEXTO_OSCURO);

        filaMetricas.getChildren().addAll(cardArticulos, cardStockBajo, cardDisponibles);
        for (Node card : filaMetricas.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxWidth(Double.MAX_VALUE);
        }

        Separator separator = new Separator();
        separator.setPadding(new Insets(15, 0, 15, 0));

        Label lblTituloTabla = new Label("Inventario de Equipos");
        lblTituloTabla.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tablaEquipos = new TableView<>();
        tablaEquipos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        configurarColumnasTabla();

        paginador = new Pagination();
        paginador.setPageFactory(this::crearPagina);

        contenido.getChildren().addAll(filaMetricas, separator, lblTituloTabla, paginador);
        vista.setCenter(contenido);
    }

    private VBox crearCardMetrica(String titulo, String valor, String colorTexto) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: transparent; -fx-padding: 20;");
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(200);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + colorTexto + ";");

        card.getChildren().addAll(lblTitulo, lblValor);
        return card;
    }

    private void configurarColumnasTabla() {
        TableColumn<Equipo, Integer> colIdEquipo = new TableColumn<>("ID Equipo");
        colIdEquipo.setCellValueFactory(new PropertyValueFactory<>("id_equipo"));

        TableColumn<Equipo, String> colCodigo = new TableColumn<>("Código Bien");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo_bien"));

        TableColumn<Equipo, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Equipo, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        TableColumn<Equipo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));

        TableColumn<Equipo, String> colNumeroSerie = new TableColumn<>("Número Serie");
        colNumeroSerie.setCellValueFactory(new PropertyValueFactory<>("numero_serie"));

        TableColumn<Equipo, Integer> colDisponible = new TableColumn<>("Disponible");
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        TableColumn<Equipo, Integer> colTipoEquipo = new TableColumn<>("Tipo de Equipo");
        colTipoEquipo.setCellValueFactory(new PropertyValueFactory<>("tipo_equipo"));

        tablaEquipos.getColumns().addAll(colIdEquipo, colCodigo, colDescripcion, colMarca, colModelo, colNumeroSerie, colDisponible, colTipoEquipo);
    }

    public VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todosLosEquipos.size());

        tablaEquipos.getItems().setAll(todosLosEquipos.subList(fromIndex, toIndex));

        VBox pagina = new VBox(tablaEquipos);
        return pagina;
    }

    // Se llama desde el controlador para actualizar la vista
    public void mostrarEquipos(List<Equipo> equipos) {
        this.todosLosEquipos = equipos;

        int totalPaginas = (int) Math.ceil((double) equipos.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(totalPaginas);
        paginador.setCurrentPageIndex(0);
        paginador.setPageFactory(this::crearPagina); // asegura refresco
    }

    public BorderPane getVista() {
        return vista;
    }
}

package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Modelos.Equipo;
import impl.EquipoDaoImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VistaInventario {
    private static final Logger logger = Logger.getLogger(VistaInventario.class.getName());
    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private TableView<Equipo> tablaEquipos;
    private Pagination paginador;
    private List<Equipo> todosLosEquipos;
    private static final int FILAS_POR_PAGINA = 50;



    // Colores
    private final String COLOR_PRIMARIO = "#4F46E5";
    private final String COLOR_SECUNDARIO = "#10B981";
    private final String COLOR_ADVERTENCIA = "#F59E0B";
    private final String COLOR_TEXTO_OSCURO = "#1F2937";
    private final String COLOR_TEXTO_NORMAL = "#6B7280";
    private final String COLOR_FONDO = "#FFFFFF";

    public VistaInventario(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        try {
            inicializarUI();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar la interfaz de inventario", e);
            mostrarError("Error al cargar el inventario");
        }
    }

    private void inicializarUI() throws Exception {

        try {
            vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
            vista.setTop(controladorBarra.getBarraSuperior());
            vista.setLeft(controladorBarra.getBarraLateral());

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setStyle("-fx-background: " + COLOR_FONDO + ";");

            VBox contenido = new VBox(20);
            contenido.setPadding(new Insets(30));
            contenido.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
            contenido.setAlignment(Pos.TOP_CENTER);

            // Panel de métricas
            HBox panelMetricas = crearPanelMetricas();
            contenido.getChildren().add(panelMetricas);

            // Tabla
            tablaEquipos = new TableView<>();
            tablaEquipos.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
            configurarColumnasTabla();

            // Paginación
            paginador = new Pagination();
            paginador.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
            paginador.setPageFactory(this::crearPagina);

            contenido.getChildren().addAll(tablaEquipos, paginador);
            scrollPane.setContent(contenido);
            vista.setCenter(scrollPane);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en inicializarUI", e);
            throw e;
        }
    }

    private HBox crearPanelMetricas() {
        HBox panelMetricas = new HBox(20);
        panelMetricas.setAlignment(Pos.TOP_CENTER);

        try {
            VBox cardTotal = crearCardMetrica("Total Equipos",
                    String.valueOf(equipoDao.totalEquipos()),
                    COLOR_PRIMARIO);

            VBox cardStockBajo = crearCardMetrica("Stock Bajo",
                    String.valueOf(equipoDao.totalStockBajo()),
                    COLOR_ADVERTENCIA);

            VBox cardDisponibles = crearCardMetrica("Disponibles",
                    String.valueOf(equipoDao.equiposDisponibles()),
                    COLOR_SECUNDARIO);

            panelMetricas.getChildren().addAll(cardTotal, cardStockBajo, cardDisponibles);

            for (Node card : panelMetricas.getChildren()) {
                HBox.setHgrow(card, Priority.ALWAYS);
                ((VBox) card).setMaxWidth(Double.MAX_VALUE);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al crear métricas", e);
            mostrarError("Error al cargar métricas");
        }

        return panelMetricas;
    }

    private VBox crearCardMetrica(String titulo, String valor, String color) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-border-color: #E5E7EB; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 0;"); // quitamos padding para manejarlo internamente
        card.setAlignment(Pos.CENTER);

        // Franja superior de color
        Region barraSuperior = new Region();
        barraSuperior.setPrefHeight(6); // Grosor del borde superior
        barraSuperior.setStyle("-fx-background-color: " + color + "; " +
                "-fx-background-radius: 8 8 0 0;"); // esquinas superiores redondeadas

        VBox contenidoCard = new VBox(8);
        contenidoCard.setAlignment(Pos.CENTER);
        contenidoCard.setPadding(new Insets(16));

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        contenidoCard.getChildren().addAll(lblTitulo, lblValor);

        card.getChildren().addAll(barraSuperior, contenidoCard);
        return card;
    }

    private void configurarColumnasTabla() {
        try {
            tablaEquipos.getStylesheets().add(
                    getClass().getResource("/css/tabla.css").toExternalForm()
            );
            TableColumn<Equipo, String> colDescripcion = new TableColumn<>("Descripción");
            colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

            TableColumn<Equipo, String> colMarca = new TableColumn<>("Marca");
            colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

            TableColumn<Equipo, String> colModelo = new TableColumn<>("Modelo");
            colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));


            TableColumn<Equipo, Integer> colEstado = new TableColumn<>("Estado");
            colEstado.setCellValueFactory(new PropertyValueFactory<>("en_uso")); // Asume getEnUso() retorna int

            colEstado.setCellFactory(column -> new TableCell<Equipo, Integer>() {
                private final StackPane pill = new StackPane();
                private final Label lblEstado = new Label();

                {
                    // Estilo base para la píldora
                    pill.getChildren().add(lblEstado);
                    pill.setStyle("-fx-padding: 3px 12px; -fx-background-radius: 15; -fx-border-radius: 15;");
                    lblEstado.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
                }

                @Override
                protected void updateItem(Integer estado, boolean empty) {
                    super.updateItem(estado, empty);

                    if (empty || estado == null) {
                        setGraphic(null);
                    } else {
                        switch (estado) {
                            case 1 -> { // En Uso
                                lblEstado.setText("EN USO");
                                pill.setStyle(pill.getStyle() +
                                        "-fx-background-color: #FFEBEE; " +
                                        "-fx-text-fill: #C62828; " +
                                        "-fx-border-color: #EF9A9A; " +
                                        "-fx-border-width: 1;");
                                lblEstado.setTextFill(Color.valueOf("#C62828"));
                            }
                            case 0 -> { // Disponible
                                lblEstado.setText("DISPONIBLE");
                                pill.setStyle(pill.getStyle() +
                                        "-fx-background-color: #E8F5E9; " +
                                        "-fx-text-fill: #2E7D32; " +
                                        "-fx-border-color: #A5D6A7; " +
                                        "-fx-border-width: 1;");
                                lblEstado.setTextFill(Color.valueOf("#2E7D32"));
                            }
                            default -> { // Otros valores (por si acaso)
                                lblEstado.setText("DESCONOCIDO");
                                pill.setStyle(pill.getStyle() +
                                        "-fx-background-color: #E3F2FD; " +
                                        "-fx-text-fill: #1565C0; " +
                                        "-fx-border-color: #90CAF9; " +
                                        "-fx-border-width: 1;");
                                lblEstado.setTextFill(Color.valueOf("#1565C0"));
                            }
                        }

                        setGraphic(pill);
                        setAlignment(Pos.CENTER);
                    }
                }
            });

            tablaEquipos.getColumns().setAll(colDescripcion, colMarca, colModelo, colEstado);
            tablaEquipos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al configurar columnas", e);
            mostrarError("Error al configurar la tabla");
        }
    }


    public VBox crearPagina(int pageIndex) {
        try {
            if (todosLosEquipos == null || todosLosEquipos.isEmpty()) {
                return new VBox(new Label("No hay datos disponibles"));
            }

            int fromIndex = pageIndex * FILAS_POR_PAGINA;
            int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todosLosEquipos.size());

            tablaEquipos.getItems().setAll(todosLosEquipos.subList(fromIndex, toIndex));

            VBox pagina = new VBox(tablaEquipos);
            pagina.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
            return pagina;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al crear página", e);
            return new VBox(new Label("Error al cargar los datos"));
        }
    }

    public void refrescarTabla() {
        if (tablaEquipos != null) {
            tablaEquipos.setItems(null);
            tablaEquipos.layout();
            tablaEquipos.setItems(FXCollections.observableList(todosLosEquipos));

            Platform.runLater(() -> {
                for (TableColumn<Equipo, ?> col : tablaEquipos.getColumns()) {
                    col.setVisible(false);
                    col.setVisible(true);
                }
                tablaEquipos.refresh();
            });
        }
    }

    public void mostrarEquipos(List<Equipo> equipos) {
        try {
            this.todosLosEquipos = equipos;
            int totalPaginas = (int) Math.ceil((double) equipos.size() / FILAS_POR_PAGINA);
            paginador.setPageCount(totalPaginas);
            paginador.setCurrentPageIndex(0);
            paginador.setPageFactory(this::crearPagina);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al mostrar equipos", e);
            mostrarError("Error al cargar el inventario");
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public BorderPane getVista() {
        return vista;
    }
}
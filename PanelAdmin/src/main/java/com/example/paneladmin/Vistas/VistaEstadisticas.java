package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;
import com.example.paneladmin.DAO.EstadisticaDAO;
import com.example.paneladmin.Modelo.Estadistica;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class VistaEstadisticas {
    @FXML private BorderPane vista;
    @FXML private VBox contenidoPrincipal;

    private final EstadisticaDAO estadisticaDAO;
    private final ControladorBarraNavegacion controladorBarra;

    public VistaEstadisticas(ControladorBarraNavegacion controladorBarra, EstadisticaDAO estadisticaDAO) {
        this.controladorBarra = controladorBarra;
        this.estadisticaDAO = estadisticaDAO;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DiseñoEstadisticas.fxml"));
            loader.setController(this);
            vista = loader.load();

            // Cargar CSS específico
            String cssFile = getClass().getResource("/css/EstiloEstadistica.css").toExternalForm();
            vista.getStylesheets().add(cssFile);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar VistaEstadisticas.fxml", e);
        }
    }

    @FXML
    public void initialize() {
        configurarUI();
        cargarEstadisticas();
    }

    private void configurarUI() {
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.getStyleClass().add("raiz-estadisticas");
    }

    private void cargarEstadisticas() {
        contenidoPrincipal.getChildren().clear();

        // Título principal
        Label lblTitulo = new Label("Estadísticas del Sistema");
        lblTitulo.getStyleClass().add("titulo-principal");
        contenidoPrincipal.getChildren().add(lblTitulo);

        // Cargar estadísticas por categoría
        cargarEstadisticasPorCategoria("Usuarios");
        cargarEstadisticasPorCategoria("Solicitudes");
        cargarEstadisticasPorCategoria("Inventario");
    }

    private void cargarEstadisticasPorCategoria(String categoria) {
        List<Estadistica> stats = estadisticaDAO.obtenerPorCategoria(categoria);

        VBox contenedorTabla = new VBox(10);
        contenedorTabla.getStyleClass().add("contenedor-tabla");

        Label lblTitulo = new Label("Estadísticas de " + categoria);
        lblTitulo.getStyleClass().add("titulo-seccion");

        TableView<Estadistica> tabla = new TableView<>();
        tabla.getStyleClass().add("tabla-estadisticas");

        // Configurar columnas
        TableColumn<Estadistica, String> colMetrica = new TableColumn<>("Métrica");
        colMetrica.setCellValueFactory(new PropertyValueFactory<>("metrica"));
        colMetrica.setPrefWidth(200);

        TableColumn<Estadistica, String> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colValor.setPrefWidth(150);

        TableColumn<Estadistica, String> colTendencia = new TableColumn<>("Tendencia");
        colTendencia.setCellValueFactory(new PropertyValueFactory<>("tendencia"));
        colTendencia.setPrefWidth(150);

        // Aplicar estilo a la columna de tendencia
        colTendencia.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Estadistica, String> call(TableColumn<Estadistica, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            if (item.startsWith("↑")) {
                                getStyleClass().add("tendencia-positiva");
                            } else if (item.startsWith("↓")) {
                                getStyleClass().add("tendencia-negativa");
                            } else {
                                getStyleClass().add("tendencia-neutral");
                            }
                        }
                    }
                };
            }
        });

        tabla.getColumns().addAll(colMetrica, colValor, colTendencia);

        // Agregar datos
        ObservableList<Estadistica> datos = FXCollections.observableArrayList(stats);
        tabla.setItems(datos);

        // Botones CRUD
        HBox contenedorBotones = new HBox(10);
        contenedorBotones.getStyleClass().add("contenedor-botones");

        Button btnAgregar = new Button("Agregar");
        btnAgregar.getStyleClass().addAll("boton-operacion", "boton-agregar");
        btnAgregar.setOnAction(e -> mostrarFormularioEdicion(null, categoria));

        Button btnEditar = new Button("Editar");
        btnEditar.getStyleClass().addAll("boton-operacion", "boton-editar");
        btnEditar.setOnAction(e -> {
            Estadistica seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                mostrarFormularioEdicion(seleccionada, categoria);
            } else {
                mostrarAlerta("Selección requerida", "Por favor seleccione una estadística para editar.");
            }
        });

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.getStyleClass().addAll("boton-operacion", "boton-eliminar");
        btnEliminar.setOnAction(e -> {
            Estadistica seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                confirmarEliminacion(seleccionada);
            } else {
                mostrarAlerta("Selección requerida", "Por favor seleccione una estadística para eliminar.");
            }
        });

        contenedorBotones.getChildren().addAll(btnAgregar, btnEditar, btnEliminar);
        contenedorTabla.getChildren().addAll(lblTitulo, tabla, contenedorBotones);
        contenidoPrincipal.getChildren().add(contenedorTabla);
    }

    private void mostrarFormularioEdicion(Estadistica estadistica, String categoria) {
        Dialog<Estadistica> dialog = new Dialog<>();
        dialog.setTitle(estadistica == null ? "Agregar Estadística" : "Editar Estadística");
        dialog.getDialogPane().getStyleClass().add("dialogo-formulario");

        // Configurar botones
        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Crear formulario
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField txtMetrica = new TextField();
        TextField txtValor = new TextField();
        TextField txtTendencia = new TextField();

        if (estadistica != null) {
            txtMetrica.setText(estadistica.getMetrica());
            txtValor.setText(estadistica.getValor());
            txtTendencia.setText(estadistica.getTendencia());
        }

        grid.add(new Label("Métrica:"), 0, 0);
        grid.add(txtMetrica, 1, 0);
        grid.add(new Label("Valor:"), 0, 1);
        grid.add(txtValor, 1, 1);
        grid.add(new Label("Tendencia:"), 0, 2);
        grid.add(txtTendencia, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convertir resultado
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnGuardar) {
                if (txtMetrica.getText().isEmpty() || txtValor.getText().isEmpty()) {
                    mostrarAlerta("Campos requeridos", "Métrica y Valor son campos obligatorios.");
                    return null;
                }

                Estadistica resultado = estadistica != null ? estadistica : new Estadistica();
                resultado.setCategoria(categoria);
                resultado.setMetrica(txtMetrica.getText());
                resultado.setValor(txtValor.getText());
                resultado.setTendencia(txtTendencia.getText());
                return resultado;
            }
            return null;
        });

        // Procesar resultado
        dialog.showAndWait().ifPresent(result -> {
            if (estadistica == null) {
                estadisticaDAO.crearEstadistica(result);
            } else {
                estadisticaDAO.actualizarEstadistica(result);
            }
            cargarEstadisticas();
        });
    }

    private void confirmarEliminacion(Estadistica estadistica) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar esta estadística?");
        alert.setContentText(estadistica.getMetrica() + ": " + estadistica.getValor());
        alert.getDialogPane().getStyleClass().add("dialogo-formulario");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                estadisticaDAO.eliminarEstadistica(estadistica.getId());
                cargarEstadisticas();
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.getDialogPane().getStyleClass().add("dialogo-formulario");
        alert.showAndWait();
    }

    public BorderPane getVista() {
        return vista;
    }
}
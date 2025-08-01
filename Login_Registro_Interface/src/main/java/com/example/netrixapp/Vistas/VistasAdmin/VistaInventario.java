package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import com.example.netrixapp.Modelos.Equipo;
import impl.EquipoDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class VistaInventario {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private TableView<Equipo> tablaEquipos;
    private Pagination paginador;
    private List<Equipo> todosLosEquipos;

    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    private static final int FILAS_POR_PAGINA = 10;

    // Colores
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";
    private final String COLOR_TEXTO_NORMAL = "#4B5563";
    private final String COLOR_PRIMARIO = "#005994";
    private final String COLOR_ADVERTENCIA = "#F59E0B";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_EXITO = "#2ECC71";

    public VistaInventario(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: #F5F7FA;");
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));

        // Encabezado
        HBox encabezado = new HBox();
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setSpacing(20);

        Label lblTitulo = new Label("Gestión de Inventario");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        Button btnAgregar = new Button("+ Agregar Producto");
        btnAgregar.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 4px;");
        btnAgregar.setOnAction(e -> mostrarFormularioAgregar());

        encabezado.getChildren().addAll(lblTitulo, btnAgregar);
        contenido.getChildren().add(encabezado);

        // Métricas
        HBox filaMetricas = new HBox(15);
        filaMetricas.setAlignment(Pos.TOP_CENTER);

        VBox cardTotal = crearCardMetrica("Total Equipos", String.valueOf(equipoDao.totalEquipos()), COLOR_PRIMARIO);
        VBox cardStockBajo = crearCardMetrica("Stock Bajo", String.valueOf(equipoDao.totalStockBajo()), COLOR_ADVERTENCIA);
        VBox cardDisponibles = crearCardMetrica("Disponibles", String.valueOf(equipoDao.equiposDisponibles()), COLOR_EXITO);

        filaMetricas.getChildren().addAll(cardTotal, cardStockBajo, cardDisponibles);
        contenido.getChildren().add(filaMetricas);

        // Sección tabla
        VBox seccionTabla = new VBox(10);
        seccionTabla.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");

        Label lblTablaTitulo = new Label("Lista de Equipos");
        lblTablaTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        tablaEquipos = new TableView<>();
        tablaEquipos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        configurarColumnasTabla();

        paginador = new Pagination();

        try {
            todosLosEquipos = equipoDao.findAll(); // Datos reales de BD
        } catch (Exception e) {
            e.printStackTrace();
            todosLosEquipos = new ArrayList<>();
        }

        int totalPaginas = (int) Math.ceil((double) todosLosEquipos.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(totalPaginas);
        paginador.setPageFactory(this::crearPagina);

        seccionTabla.getChildren().addAll(lblTablaTitulo, paginador);
        contenido.getChildren().add(seccionTabla);

        vista.setCenter(contenido);
    }

    private void configurarColumnasTabla() {
        TableColumn<Equipo, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId_equipo()));

        TableColumn<Equipo, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo_bien()));

        TableColumn<Equipo, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));

        TableColumn<Equipo, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMarca()));

        TableColumn<Equipo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModelo()));

        TableColumn<Equipo, String> colSerie = new TableColumn<>("Numero Serie");
        colSerie.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNumero_serie()));

        TableColumn<Equipo, Number> colDisponible = new TableColumn<>("Disponible");
        colDisponible.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getDisponible()));

        TableColumn<Equipo, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox cajaBotones = new HBox(5, btnEditar, btnEliminar);

            {
                btnEditar.setStyle("-fx-background-color: " + COLOR_ADVERTENCIA + "; -fx-text-fill: white; -fx-padding: 4 8;");
                btnEliminar.setStyle("-fx-background-color: " + COLOR_PELIGRO + "; -fx-text-fill: white; -fx-padding: 4 8;");

                btnEditar.setOnAction(e -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    editarEquipo(equipo);
                });

                btnEliminar.setOnAction(e -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    eliminarEquipo(equipo);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cajaBotones);
            }
        });

        tablaEquipos.getColumns().addAll(colId, colCodigo, colDescripcion, colMarca, colModelo, colSerie, colDisponible, colAcciones);
    }

    private VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todosLosEquipos.size());
        tablaEquipos.getItems().setAll(todosLosEquipos.subList(fromIndex, toIndex));
        return new VBox(tablaEquipos);
    }

    private VBox crearCardMetrica(String titulo, String valor, String colorTexto) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(180);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + colorTexto + ";");

        card.getChildren().addAll(lblTitulo, lblValor);
        return card;
    }
    private void mostrarFormularioAgregar() {
        Dialog<Equipo> dialog = new Dialog<>();
        dialog.setTitle("Registrar un equipo");
        dialog.setHeaderText("Registrar un nuevo equipo en el sistema");

        // Configurar botones
        ButtonType btnCrear = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCrear, ButtonType.CANCEL);

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField tfCodigoBien = new TextField();
        tfCodigoBien.setPromptText("Código_Bien");

        TextField tfDescripcion = new TextField();
        tfDescripcion.setPromptText("Descripción");

        TextField tfMarca = new TextField();
        tfMarca.setPromptText("Marca");

        TextField tfModelo = new TextField();
        tfModelo.setPromptText("Modelo");

        TextField tfNumeroSerie = new TextField();
        tfNumeroSerie.setPromptText("Numero serie");

        Spinner<Integer> spinnerTipoEquipo = new Spinner<>(1, 10, 1);
        spinnerTipoEquipo.setEditable(false);
        grid.add(new Label("Codigo_Bien:"), 0, 0);
        grid.add(tfCodigoBien, 1, 0);
        grid.add(new Label("Descripcion:"), 0, 1);
        grid.add(tfDescripcion, 1, 1);
        grid.add(new Label("Marca:"), 0, 2);
        grid.add(tfMarca, 1, 2);
        grid.add(new Label("Modelo:"), 0, 3);
        grid.add(tfModelo, 1, 3);
        grid.add(new Label("Numero serie:"), 0, 4);
        grid.add(tfNumeroSerie, 1, 4);
        grid.add(new Label("Tipo de Equipo:"), 0, 5);
        grid.add(spinnerTipoEquipo, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Procesar resultado
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnCrear) {
                String codigo_bien = tfCodigoBien.getText().trim();
                String descripcion = tfDescripcion.getText().trim();
                String marca = tfMarca.getText().trim();
                String modelo = tfModelo.getText().trim();
                String numero_serie = tfNumeroSerie.getText().trim();
                Integer tipo_equipo = spinnerTipoEquipo.getValue();

                if (codigo_bien.isEmpty() || descripcion.isEmpty() || marca.isEmpty() ||
                        modelo.isEmpty() || numero_serie.isEmpty() || tipo_equipo == null) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Campos incompletos");
                    alerta.setHeaderText("Por favor completa todos los campos.");
                    alerta.showAndWait();
                    return null;
                } else {
                    return new Equipo(
                            codigo_bien,
                            descripcion,
                            marca,
                            modelo,
                            numero_serie,
                            tipo_equipo
                    );
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(equipo -> {
            try {
                equipoDao.create(equipo);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Operación exitosa");
                alert.setHeaderText("Equipo registrado");
                alert.setContentText("Equipo registrado exitosamente.");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tablaEquipos.refresh();
        });
    }


    private void editarEquipo(Equipo equipo) {
        int id = equipo.getId_equipo();  // <--- AQUÍ OBTIENES EL ID
        System.out.println("ID del equipo seleccionado: " + id);

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Editar equipo");
        alerta.setHeaderText("Modifica los campos necesarios");

        // Crear formulario con GridPane
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        TextField txtCodigo = new TextField(equipo.getCodigo_bien());
        TextField txtDescripcion = new TextField(equipo.getDescripcion());
        TextField txtMarca = new TextField(equipo.getMarca());
        TextField txtModelo = new TextField(equipo.getModelo());
        TextField txtSerie = new TextField(equipo.getNumero_serie());
        Spinner<Integer> spinnerDisponible = new Spinner<>(0, 1, equipo.getDisponible());
        Spinner<Integer> spinnerTipoEquipo = new Spinner<>(0, 20, equipo.getTipo_equipo());


        formulario.addRow(0, new Label("Código:"), txtCodigo);
        formulario.addRow(1, new Label("Descripción:"), txtDescripcion);
        formulario.addRow(2, new Label("Marca:"), txtMarca);
        formulario.addRow(3, new Label("Modelo:"), txtModelo);
        formulario.addRow(4, new Label("Serie:"), txtSerie);
        formulario.addRow(5, new Label("Disponible:"), spinnerDisponible);
        formulario.addRow(6, new Label("ID tipo equipo: ", spinnerTipoEquipo));

        // Añadir el formulario al diálogo
        alerta.getDialogPane().setContent(formulario);

        // Cambiar texto de botones
        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(btnGuardar, btnCancelar);

        // Mostrar y procesar resultado
        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnGuardar) {
                equipo.setCodigo_bien(txtCodigo.getText());
                equipo.setDescripcion(txtDescripcion.getText());
                equipo.setMarca(txtMarca.getText());
                equipo.setModelo(txtModelo.getText());
                equipo.setNumero_serie(txtSerie.getText());
                equipo.setDisponible(spinnerDisponible.getValue());
                equipo.setTipo_equipo(spinnerTipoEquipo.getValue());

                try {
                    equipoDao.update(equipo, id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                tablaEquipos.refresh();
            }
        });
    }


    private void eliminarEquipo(Equipo equipo) {
        int id  = equipo.getId_equipo();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Eliminar equipo: " + equipo.getDescripcion() + "?");
        alert.setContentText("Esta acción no se puede deshacer.");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                equipoDao.delete(equipo, id);
                System.out.println("Equipo eliminado: " + equipo.getDescripcion());

                todosLosEquipos = equipoDao.findAll();
                int totalPaginas = (int) Math.ceil((double) todosLosEquipos.size() / FILAS_POR_PAGINA);
                paginador.setPageCount(totalPaginas);

                int paginaActual = paginador.getCurrentPageIndex();
                if (paginaActual >= totalPaginas) {
                    paginaActual = totalPaginas - 1;
                    if (paginaActual < 0) paginaActual = 0;
                }
                paginador.setCurrentPageIndex(paginaActual);
                paginador.setPageFactory(this::crearPagina);

            } catch(Exception e) {
                e.printStackTrace();
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText("No se pudo eliminar el equipo");
                alertError.setContentText(e.getMessage());
                alertError.showAndWait();
            }
        }
    }

    public BorderPane getVista() {
        return vista;
    }
}

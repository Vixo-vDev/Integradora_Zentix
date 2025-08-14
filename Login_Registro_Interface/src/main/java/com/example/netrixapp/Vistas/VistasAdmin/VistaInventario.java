package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.*;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorSolicitudes;
import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.Usuario;
import impl.EquipoDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class VistaInventario {
    private ControladorInventario controladorInventario;
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private TableView<Equipo> tablaEquipos;
    private Pagination paginador;
    private List<Equipo> todosLosEquipos;
    private TextField tfCodigoBien = new TextField();
    private TextField tfDescripcion = new TextField();
    private TextField tfMarca = new TextField();
    private TextField tfModelo = new TextField();
    private TextField tfNumeroSerie = new TextField();
    private Spinner<Integer> spinnerTipoEquipo = new Spinner<>(1, 10, 1);
    private TextField tfBusqueda = new TextField();
    private Label lblResultados = new Label();

    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    private static final int FILAS_POR_PAGINA = 10;

    // Colores
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";
    private final String COLOR_TEXTO_NORMAL = "#4B5563";
    private final String COLOR_PRIMARIO = "#005994";
    private final String COLOR_ADVERTENCIA = "#F59E0B";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_EXITO = "#2ECC71";

    public String getCodigoBien() {
        return tfCodigoBien.getText().trim();
    }

    public String getDescripcion() {
        return tfDescripcion.getText().trim();
    }

    public String getMarca() {
        return tfMarca.getText().trim();
    }

    public String getModelo() {
        return tfModelo.getText().trim();
    }

    public String getNumeroSerie() {
        return tfNumeroSerie.getText().trim();
    }

    public int getTipoEquipo() {
        return spinnerTipoEquipo.getValue();
    }


    public VistaInventario(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        this.controladorInventario = new ControladorInventario(this);
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
        btnAgregar.setOnAction(e -> controladorInventario.agregarEquipo());

        encabezado.getChildren().addAll(lblTitulo, btnAgregar);
        contenido.getChildren().add(encabezado);

        // Métricas
        HBox filaMetricas = new HBox(15);
        filaMetricas.setAlignment(Pos.TOP_CENTER);

        VBox cardTotal = null;
        try {
            cardTotal = crearCardMetrica("Total Equipos", String.valueOf(equipoDao.totalEquipos()), COLOR_PRIMARIO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        VBox cardStockBajo = null;
        try {
            cardStockBajo = crearCardMetrica("Stock Bajo", String.valueOf(equipoDao.totalStockBajo()), COLOR_ADVERTENCIA);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        VBox cardDisponibles = null;
        try {
            cardDisponibles = crearCardMetrica("Disponibles", String.valueOf(equipoDao.equiposDisponibles()), COLOR_EXITO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filaMetricas.getChildren().addAll(cardTotal, cardStockBajo, cardDisponibles);
        contenido.getChildren().add(filaMetricas);

        // Sección tabla
        VBox seccionTabla = new VBox(10);
        seccionTabla.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");

        Label lblTablaTitulo = new Label("Lista de Equipos");
        lblTablaTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        
        // Configurar label para mostrar cantidad de resultados
        lblResultados.setStyle("-fx-font-size: 12px; -fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");
        
        // Barra de búsqueda
        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        
        Label lblBusqueda = new Label("🔍 Buscar:");
        lblBusqueda.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        
        tfBusqueda.setPromptText("Buscar por código, descripción, marca, modelo...");
        tfBusqueda.setStyle("-fx-font-size: 14px; -fx-pref-width: 300px; -fx-background-radius: 6; -fx-padding: 8 12;");
        tfBusqueda.setPrefHeight(35);
        
        // Búsqueda en tiempo real
        tfBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                filtrarEquipos();
            } else if (newValue != null && newValue.trim().isEmpty()) {
                limpiarBusqueda();
            }
        });
        
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");
        btnBuscar.setOnAction(e -> filtrarEquipos());
        
        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: " + COLOR_TEXTO_NORMAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");
        btnLimpiar.setOnAction(e -> limpiarBusqueda());
        
        barraBusqueda.getChildren().addAll(lblBusqueda, tfBusqueda, btnBuscar, btnLimpiar);
        
        seccionTabla.getChildren().addAll(lblTablaTitulo, barraBusqueda, lblResultados);

        tablaEquipos = new TableView<>();
        tablaEquipos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        configurarColumnasTabla();

        paginador = new Pagination();
        paginador.setPageFactory(this::crearPagina);
        
        seccionTabla.getChildren().addAll(tablaEquipos, paginador);
        contenido.getChildren().add(seccionTabla);

        vista.setCenter(contenido);
    }


    private void configurarColumnasTabla() {
        tablaEquipos.getStylesheets().add(
                getClass().getResource("/css/tabla.css").toExternalForm()
        );
        TableColumn<Equipo, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_equipo"));

        TableColumn<Equipo, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo_bien"));

        TableColumn<Equipo, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Equipo, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        TableColumn<Equipo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));

        TableColumn<Equipo, String> colSerie = new TableColumn<>("Numero Serie");
        colSerie.setCellValueFactory(new PropertyValueFactory<>("numero_serie"));

        TableColumn<Equipo, Number> colDisponible = new TableColumn<>("Disponible");
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        TableColumn<Equipo, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();

            {

                ImageView iconoEditView = new ImageView(new Image(getClass().getResourceAsStream("/Imagenes/edit.png")));
                iconoEditView.setFitWidth(16);
                iconoEditView.setFitHeight(16);
                btnEditar.setGraphic(iconoEditView);
                btnEditar.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                ImageView iconoDeleteView = new ImageView(new Image(getClass().getResourceAsStream("/Imagenes/delete.png")));
                iconoDeleteView.setFitWidth(16);
                iconoDeleteView.setFitHeight(16);
                btnEliminar.setGraphic(iconoDeleteView);
                btnEliminar.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                btnEditar.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-padding: 4 8;");
                btnEliminar.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-padding: 4 8;");

                btnEditar.setStyle("-fx-background-color: " + COLOR_ADVERTENCIA + "; -fx-text-fill: white; -fx-padding: 4 8;");
                btnEliminar.setStyle("-fx-background-color: " + COLOR_PELIGRO + "; -fx-text-fill: white; -fx-padding: 4 8;");

                btnEditar.setOnAction(e -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    editarEquipo(equipo);
                });

                btnEliminar.setOnAction(e -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    controladorInventario.eliminarEquipo(equipo);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, btnEditar, btnEliminar));
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

    public void mostrarEquipos(List<Equipo> equipos) {
        this.todosLosEquipos = equipos;
        
        // Actualizar contador de resultados
        if (tfBusqueda.getText().trim().isEmpty()) {
            // Mostrar total de equipos
            try {
                int totalEquipos = equipoDao.totalEquipos();
                lblResultados.setText("Mostrando " + equipos.size() + " de " + totalEquipos + " equipos");
            } catch (Exception e) {
                lblResultados.setText("Mostrando " + equipos.size() + " equipos");
            }
        }
        
        int totalPaginas = (int) Math.ceil((double) todosLosEquipos.size() / FILAS_POR_PAGINA);
        if (totalPaginas == 0) totalPaginas = 1;
        paginador.setPageCount(totalPaginas);
        paginador.setCurrentPageIndex(0);
        paginador.setPageFactory(this::crearPagina);
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
    public Alert mostrarFormularioAgregar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Agregar un nuevo Equipo");
        alerta.setHeaderText("Ingresa los campos que se te solicitan");
        alerta.setContentText("Aquí puedes implementar el formulario para crear un nuevo equipo");

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        tfCodigoBien = new TextField();
        tfCodigoBien.setPromptText("Código_Bien");

        tfDescripcion = new TextField();
        tfDescripcion.setPromptText("Descripción");

        tfMarca = new TextField();
        tfMarca.setPromptText("Marca");

        tfModelo = new TextField();
        tfModelo.setPromptText("Modelo");

        tfNumeroSerie = new TextField();
        tfNumeroSerie.setPromptText("Numero serie");

        spinnerTipoEquipo = new Spinner<>(1, 10, 1);
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

        alerta.getDialogPane().setContent(grid);
            return alerta;
        }

    private void editarEquipo(Equipo equipo) {
        int id = equipo.getId_equipo();
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


    public Alert confirmareliminarEquipo(Equipo equipo) {
        int id = equipo.getId_equipo();
        System.out.println(id);
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Equipo");
        confirmacion.setHeaderText("¿Eliminar equipo: " + equipo.getDescripcion() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        return confirmacion;
    }

    public BorderPane getVista() {
        return vista;
    }
    
    private void filtrarEquipos() {
        String textoBusqueda = tfBusqueda.getText().trim().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            // Si no hay texto de búsqueda, mostrar todos los equipos
            if (todosLosEquipos != null) {
                mostrarEquipos(todosLosEquipos);
            }
            return;
        }
        
        // Filtrar equipos que coincidan con la búsqueda
        List<Equipo> equiposFiltrados = todosLosEquipos.stream()
            .filter(equipo -> 
                (equipo.getCodigo_bien() != null && equipo.getCodigo_bien().toLowerCase().contains(textoBusqueda)) ||
                (equipo.getDescripcion() != null && equipo.getDescripcion().toLowerCase().contains(textoBusqueda)) ||
                (equipo.getMarca() != null && equipo.getMarca().toLowerCase().contains(textoBusqueda)) ||
                (equipo.getModelo() != null && equipo.getModelo().toLowerCase().contains(textoBusqueda)) ||
                (equipo.getNumero_serie() != null && equipo.getNumero_serie().toLowerCase().contains(textoBusqueda))
            )
            .toList();
        
        if (equiposFiltrados.isEmpty()) {
            // Mostrar mensaje de "no se encontraron resultados"
            mostrarEquipos(new ArrayList<>());
            lblResultados.setText("No se encontraron equipos para: '" + textoBusqueda + "'");
        } else {
            mostrarEquipos(equiposFiltrados);
            lblResultados.setText("Se encontraron " + equiposFiltrados.size() + " equipos para: '" + textoBusqueda + "'");
        }
    }
    
    private void limpiarBusqueda() {
        tfBusqueda.clear();
        if (todosLosEquipos != null) {
            mostrarEquipos(todosLosEquipos);
            try {
                int totalEquipos = equipoDao.totalEquipos();
                lblResultados.setText("Mostrando " + todosLosEquipos.size() + " de " + totalEquipos + " equipos");
            } catch (Exception e) {
                lblResultados.setText("Mostrando " + todosLosEquipos.size() + " equipos");
            }
        }
    }
    
    public String getTextoBusqueda() {
        return tfBusqueda.getText().trim();
    }
}

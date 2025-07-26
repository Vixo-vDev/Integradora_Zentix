package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class VistaInventario {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

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

        // Barras de navegación
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenido principal
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));

        // 1. Encabezado con título y botón de agregar
        HBox encabezado = new HBox();
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setSpacing(20);

        Label lblTitulo = new Label("Gestión de Inventario");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        Button btnAgregar = new Button("+ Agregar Producto");
        btnAgregar.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 8 16; -fx-background-radius: 4px;");
        btnAgregar.setOnAction(e -> mostrarFormularioAgregar());

        encabezado.getChildren().addAll(lblTitulo, btnAgregar);
        contenido.getChildren().add(encabezado);

        // 2. Fila de métricas rápidas
        HBox filaMetricas = new HBox(15);
        filaMetricas.setAlignment(Pos.TOP_CENTER);

        VBox cardTotal = crearCardMetrica("Total Productos", "245", COLOR_PRIMARIO);
        VBox cardStockBajo = crearCardMetrica("Stock Bajo", "18", COLOR_ADVERTENCIA);
        VBox cardAgotados = crearCardMetrica("Agotados", "7", COLOR_PELIGRO);
        VBox cardNuevos = crearCardMetrica("Nuevos (30d)", "32", COLOR_EXITO);

        filaMetricas.getChildren().addAll(cardTotal, cardStockBajo, cardAgotados, cardNuevos);
        contenido.getChildren().add(filaMetricas);

        // 3. Tabla de productos con controles de administración
        VBox seccionTabla = new VBox(10);
        seccionTabla.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16;");

        Label lblTablaTitulo = new Label("Lista de Productos");
        lblTablaTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        // Tabla de productos
        TableView<Producto> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columnas de la tabla
        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());

        TableColumn<Producto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());

        TableColumn<Producto, Number> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty());

        TableColumn<Producto, String> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<Producto, String>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox cajaBotones = new HBox(5, btnEditar, btnEliminar);

            {
                btnEditar.setStyle("-fx-background-color: " + COLOR_ADVERTENCIA + "; -fx-text-fill: white; -fx-padding: 4 8;");
                btnEliminar.setStyle("-fx-background-color: " + COLOR_PELIGRO + "; -fx-text-fill: white; -fx-padding: 4 8;");

                btnEditar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    editarProducto(producto);
                });

                btnEliminar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    eliminarProducto(producto);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cajaBotones);
            }
        });

        tabla.getColumns().addAll(colNombre, colCodigo, colCategoria, colStock, colAcciones);

        // Datos de ejemplo
        tabla.getItems().addAll(
                new Producto("Laptop HP EliteBook", "LP-HP-001", "Computación", 15),
                new Producto("Monitor Dell 24\"", "MN-DL-024", "Monitores", 8),
                new Producto("Teclado Mecánico", "TC-MC-002", "Periféricos", 22),
                new Producto("Mouse Inalámbrico", "MS-IN-005", "Periféricos", 34),
                new Producto("Disco SSD 500GB", "DD-SS-500", "Almacenamiento", 5)
        );

        seccionTabla.getChildren().addAll(lblTablaTitulo, tabla);
        contenido.getChildren().add(seccionTabla);

        vista.setCenter(contenido);
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
        // Implementar lógica para mostrar formulario de agregar producto
        System.out.println("Mostrar formulario para agregar nuevo producto");

        // Ejemplo básico de diálogo
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Producto");
        dialog.setHeaderText("Ingrese los datos del nuevo producto");
        dialog.setContentText("Nombre del producto:");

        dialog.showAndWait().ifPresent(nombre -> {
            System.out.println("Nuevo producto: " + nombre);
            // Aquí iría la lógica para guardar el producto
        });
    }

    private void editarProducto(Producto producto) {
        // Implementar lógica para editar producto
        System.out.println("Editando producto: " + producto.getNombre());
    }

    private void eliminarProducto(Producto producto) {
        // Implementar lógica para eliminar producto
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Eliminar producto " + producto.getNombre() + "?");
        alert.setContentText("Esta acción no se puede deshacer.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Producto eliminado: " + producto.getNombre());
        }
    }

    public BorderPane getVista() {
        return vista;
    }

    // Clase modelo para productos
    public static class Producto {
        private final StringProperty nombre;
        private final StringProperty codigo;
        private final StringProperty categoria;
        private final IntegerProperty stock;

        public Producto(String nombre, String codigo, String categoria, int stock) {
            this.nombre = new SimpleStringProperty(nombre);
            this.codigo = new SimpleStringProperty(codigo);
            this.categoria = new SimpleStringProperty(categoria);
            this.stock = new SimpleIntegerProperty(stock);
        }

        // Getters y propiedades para TableView
        public String getNombre() { return nombre.get(); }
        public StringProperty nombreProperty() { return nombre; }

        public String getCodigo() { return codigo.get(); }
        public StringProperty codigoProperty() { return codigo; }

        public String getCategoria() { return categoria.get(); }
        public StringProperty categoriaProperty() { return categoria; }

        public int getStock() { return stock.get(); }
        public IntegerProperty stockProperty() { return stock; }
    }
}
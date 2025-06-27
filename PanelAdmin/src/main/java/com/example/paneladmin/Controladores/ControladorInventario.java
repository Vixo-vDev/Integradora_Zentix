package com.example.paneladmin.Controladores;

import com.example.paneladmin.Modelos.Producto;
import com.example.paneladmin.Vistas.VistaInventario;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ControladorInventario {
    private VistaInventario vista;

    public ControladorInventario(VistaInventario vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        Label etiquetaTitulo = new Label("Gestión de Inventario");
        etiquetaTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Configurar tabla
        TableView<Producto> tabla = new TableView<>();
        configurarTabla(tabla);

        // Configurar barra de búsqueda
        HBox barraBusqueda = crearBarraBusqueda();

        // Configurar botones de acción
        HBox contenedorBotones = crearContenedorBotones();

        // Agregar componentes a la vista
        vista.getVista().getChildren().addAll(
                etiquetaTitulo,
                barraBusqueda,
                contenedorBotones,
                tabla
        );
        vista.setTabla(tabla);
    }

    private void configurarTabla(TableView<Producto> tabla) {
        // Columnas
        TableColumn<Producto, String> columnaNombre = new TableColumn<>("Producto");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaNombre.setPrefWidth(200);

        TableColumn<Producto, Integer> columnaCantidad = new TableColumn<>("Cantidad");
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaCantidad.setPrefWidth(100);

        // Configuración adicional
        tabla.setPlaceholder(new Label("No hay productos en el inventario"));
    }

    private HBox crearBarraBusqueda() {
        HBox barra = new HBox(10);
        barra.setStyle("-fx-padding: 0 0 10 0;");

        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Buscar producto...");
        HBox.setHgrow(campoBusqueda, Priority.ALWAYS);

        Button btnBuscar = crearBotonAccion("Buscar", "#BCBCBC");
        btnBuscar.setPrefWidth(100);

        barra.getChildren().addAll(campoBusqueda, btnBuscar);
        return barra;
    }

    private HBox crearContenedorBotones() {
        HBox contenedor = new HBox(15);
        contenedor.setStyle("-fx-padding: 10 0;");
        contenedor.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Button btnAgregar = crearBotonAccion("Agregar", "#345179");
        Button btnEditar = crearBotonAccion("Editar", "#777779");
        Button btnEliminar = crearBotonAccion("Eliminar", "#e74c3c");
        Button btnActualizar = crearBotonAccion("Actualizar", "#1F997A");

        // Configurar eventos
        btnAgregar.setOnAction(e -> agregarProducto());
        btnEditar.setOnAction(e -> editarProducto());
        btnEliminar.setOnAction(e -> eliminarProducto());
        btnActualizar.setOnAction(e -> actualizarInventario());

        contenedor.getChildren().addAll(btnAgregar, btnEditar, btnEliminar, btnActualizar);
        return contenedor;
    }

    private Button crearBotonAccion(String texto, String color) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 5;");

        boton.setOnMouseEntered(e -> boton.setStyle(
                "-fx-background-color: derive(" + color + ", -20%); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 5;"
        ));

        boton.setOnMouseExited(e -> boton.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 5;"
        ));

        return boton;
    }

    // Métodos de acción
    private void agregarProducto() {
        System.out.println("Agregar nuevo producto - Implementar lógica aquí");
        // Aquí iría la lógica para mostrar un formulario de agregar producto
    }

    private void editarProducto() {
        Producto seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Editar producto: " + seleccionado.getNombre());
            // Aquí iría la lógica para editar
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un producto para editar");
        }
    }

    private void eliminarProducto() {
        Producto seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Eliminar producto: " + seleccionado.getNombre());
            // Aquí iría la lógica para eliminar
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un producto para eliminar");
        }
    }

    private void actualizarInventario() {
        System.out.println("Actualizando inventario...");
        // Aquí iría la lógica para refrescar los datos
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
package com.example.paneladmin.Controladores;

import com.example.paneladmin.Modelos.Formulario;
import com.example.paneladmin.Vistas.VistaFormularios;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ControladorFormularios {
    private VistaFormularios vista;

    public ControladorFormularios(VistaFormularios vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        Label etiquetaTitulo = new Label("Gestión de Formularios");
        etiquetaTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Configurar tabla
        TableView<Formulario> tabla = new TableView<>();
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

    private void configurarTabla(TableView<Formulario> tabla) {
        // Columnas
        TableColumn<Formulario, String> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaId.setPrefWidth(80);

        TableColumn<Formulario, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaNombre.setPrefWidth(200);

        TableColumn<Formulario, String> columnaEstado = new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaEstado.setPrefWidth(120);
        columnaEstado.setCellFactory(tc -> new TableCell<Formulario, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estado);
                    switch(estado.toLowerCase()) {
                        case "activo":
                            setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                            break;
                        case "inactivo":
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                            break;
                        case "pendiente":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        // Configuración adicional
        tabla.setPlaceholder(new Label("No hay formularios registrados"));
        tabla.getColumns().addAll(columnaId, columnaNombre, columnaEstado);
    }

    private HBox crearBarraBusqueda() {
        HBox barra = new HBox(10);
        barra.setStyle("-fx-padding: 0 0 10 0;");

        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Buscar formularios...");
        HBox.setHgrow(campoBusqueda, Priority.ALWAYS);

        Button btnBuscar = crearBotonAccion("Buscar", "#3498db");
        btnBuscar.setPrefWidth(100);

        barra.getChildren().addAll(campoBusqueda, btnBuscar);
        return barra;
    }

    private HBox crearContenedorBotones() {
        HBox contenedor = new HBox(15);
        contenedor.setStyle("-fx-padding: 10 0;");
        contenedor.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Button btnNuevo = crearBotonAccion("Nuevo", "#2ecc71");
        Button btnEditar = crearBotonAccion("Editar", "#3498db");
        Button btnEliminar = crearBotonAccion("Eliminar", "#e74c3c");
        Button btnPrevisualizar = crearBotonAccion("Previsualizar", "#f39c12");
        Button btnCambiarEstado = crearBotonAccion("Cambiar Estado", "#9b59b6");

        // Configurar eventos
        btnNuevo.setOnAction(e -> nuevoFormulario());
        btnEditar.setOnAction(e -> editarFormulario());
        btnEliminar.setOnAction(e -> eliminarFormulario());
        btnPrevisualizar.setOnAction(e -> previsualizarFormulario());
        btnCambiarEstado.setOnAction(e -> cambiarEstadoFormulario());

        contenedor.getChildren().addAll(btnNuevo, btnEditar, btnEliminar, btnPrevisualizar, btnCambiarEstado);
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
    private void nuevoFormulario() {
        System.out.println("Crear nuevo formulario - Implementar lógica aquí");
        // Aquí iría la lógica para mostrar un formulario de creación
    }

    private void editarFormulario() {
        Formulario seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Editar formulario: " + seleccionado.getNombre());
            // Aquí iría la lógica para editar
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un formulario para editar");
        }
    }

    private void eliminarFormulario() {
        Formulario seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Eliminar formulario: " + seleccionado.getNombre());
            // Aquí iría la lógica para eliminar
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un formulario para eliminar");
        }
    }

    private void previsualizarFormulario() {
        Formulario seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Previsualizar formulario: " + seleccionado.getNombre());
            // Aquí iría la lógica para previsualizar
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un formulario para previsualizar");
        }
    }

    private void cambiarEstadoFormulario() {
        Formulario seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Cambiar estado de formulario: " + seleccionado.getNombre());
            // Aquí iría la lógica para cambiar estado
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un formulario para cambiar estado");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
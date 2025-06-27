package com.example.paneladmin.Controladores;

import com.example.paneladmin.Modelos.Usuario;
import com.example.paneladmin.Vistas.VistaUsuarios;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ControladorUsuarios {
    private VistaUsuarios vista;

    public ControladorUsuarios(VistaUsuarios vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        Label etiquetaTitulo = new Label("Gestión de Usuarios");
        etiquetaTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Configurar tabla
        TableView<Usuario> tabla = new TableView<>();
        configurarTabla(tabla);

        // Configurar botones de acción
        HBox contenedorBotones = crearContenedorBotones();

        // Agregar elementos a la vista
        vista.getVista().getChildren().addAll(etiquetaTitulo, contenedorBotones, tabla);
        vista.setTabla(tabla);

        VBox.setVgrow(tabla, Priority.ALWAYS);
    }

    private void configurarTabla(TableView<Usuario> tabla) {
        // Columnas
        TableColumn<Usuario, String> columnaUsuario = new TableColumn<>("Usuario");
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        columnaUsuario.setPrefWidth(150);

        TableColumn<Usuario, String> columnaRol = new TableColumn<>("Rol");
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        columnaRol.setPrefWidth(120);

        TableColumn<Usuario, String> columnaCorreo = new TableColumn<>("Correo");
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        columnaCorreo.setPrefWidth(200);

        // Configuración adicional de la tabla
        tabla.setPlaceholder(new Label("No hay usuarios registrados"));
        tabla.getColumns().addAll(columnaUsuario, columnaRol, columnaCorreo);
        tabla.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7;");
    }

    private HBox crearContenedorBotones() {
        HBox contenedorBotones = new HBox(15);
        contenedorBotones.setStyle("-fx-padding: 10 0;");
        contenedorBotones.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Button btnAgregar = crearBotonAccion("Agregar", "#2ecc71");
        Button btnEditar = crearBotonAccion("Editar", "#3498db");
        Button btnEliminar = crearBotonAccion("Eliminar", "#e74c3c");
        Button btnPermisos = crearBotonAccion("Permisos", "#9b59b6");

        // Configurar eventos (aquí puedes agregar la funcionalidad)
        btnAgregar.setOnAction(e -> agregarUsuario());
        btnEditar.setOnAction(e -> editarUsuario());
        btnEliminar.setOnAction(e -> eliminarUsuario());
        btnPermisos.setOnAction(e -> gestionarPermisos());

        contenedorBotones.getChildren().addAll(btnAgregar, btnEditar, btnEliminar, btnPermisos);
        return contenedorBotones;
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

    // Métodos de acción (implementación básica)
    private void agregarUsuario() {
        System.out.println("Agregar usuario - Implementar lógica aquí");
        // Aquí iría la lógica para mostrar un formulario de agregar usuario
    }

    private void editarUsuario() {
        Usuario seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Editar usuario: " + seleccionado.getNombreUsuario());
            // Aquí iría la lógica para editar
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un usuario para editar");
        }
    }

    private void eliminarUsuario() {
        Usuario seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Eliminar usuario: " + seleccionado.getNombreUsuario());
            // Aquí iría la lógica para eliminar
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un usuario para eliminar");
        }
    }

    private void gestionarPermisos() {
        Usuario seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Gestionar permisos para: " + seleccionado.getNombreUsuario());
            // Aquí iría la lógica para permisos
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione un usuario para gestionar permisos");
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
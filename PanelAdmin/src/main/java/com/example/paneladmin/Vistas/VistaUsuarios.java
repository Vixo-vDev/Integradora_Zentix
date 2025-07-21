package com.example.paneladmin.Vistas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

public class VistaUsuarios {
    private final VBox vista;
    private final Consumer<Void> volverCallback;

    public VistaUsuarios(Consumer<Void> volverCallback) {
        this.volverCallback = volverCallback;
        this.vista = crearVista();
    }

    private VBox crearVista() {
        VBox contenedorPrincipal = new VBox(20);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setStyle("-fx-background-color: #ECF0F1;");

        // Barra superior con título y botón de volver
        HBox barraSuperior = new HBox();
        barraSuperior.setAlignment(Pos.CENTER_LEFT);
        barraSuperior.setSpacing(15);

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold;");
        btnVolver.setOnAction(e -> volverCallback.accept(null));

        Label lblTitulo = new Label("Gestión de Usuarios");
        lblTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        barraSuperior.getChildren().addAll(btnVolver, lblTitulo);

        // Contenedor de formulario y tabla
        HBox contenedorContenido = new HBox(20);
        contenedorContenido.setAlignment(Pos.TOP_CENTER);

        // Formulario para agregar/editar usuarios
        VBox formulario = crearFormularioUsuario();
        formulario.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        // Tabla de usuarios
        VBox tablaUsuarios = crearTablaUsuarios();
        tablaUsuarios.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        contenedorContenido.getChildren().addAll(formulario, tablaUsuarios);

        contenedorPrincipal.getChildren().addAll(barraSuperior, contenedorContenido);
        return contenedorPrincipal;
    }

    private VBox crearFormularioUsuario() {
        VBox formulario = new VBox(15);
        formulario.setPrefWidth(300);

        Label lblTituloForm = new Label("Editar Usuario");
        lblTituloForm.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // Campos del formulario
        TextField txtId = new TextField();
        txtId.setPromptText("ID (automático)");
        txtId.setDisable(true);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo electrónico");

        ComboBox<String> cbDivision = new ComboBox<>();
        cbDivision.getItems().addAll(
                "TIC",
                "Administración",
                "Contaduría",
                "Industrial",
                "Mecánica",
                "Eléctrica"
        );
        cbDivision.setPromptText("Seleccione división");

        // Botones de acción
        HBox botonesAccion = new HBox(10);
        botonesAccion.setAlignment(Pos.CENTER);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #F39C12; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLimpiar.setOnAction(e -> {
            txtId.clear();
            txtNombre.clear();
            txtCorreo.clear();
            cbDivision.getSelectionModel().clearSelection();
        });

        botonesAccion.getChildren().addAll(btnGuardar, btnLimpiar);

        // Estilo para los campos
        String estiloCampo = "-fx-padding: 8; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #BDC3C7;";
        txtNombre.setStyle(estiloCampo);
        txtCorreo.setStyle(estiloCampo);
        cbDivision.setStyle(estiloCampo);

        formulario.getChildren().addAll(lblTituloForm, txtId, txtNombre, txtCorreo, cbDivision, botonesAccion);
        return formulario;
    }

    private VBox crearTablaUsuarios() {
        VBox contenedorTabla = new VBox(15);

        Label lblTituloTabla = new Label("Lista de Usuarios Registrados");
        lblTituloTabla.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // Crear tabla
        TableView<Usuario> tabla = new TableView<>();

        // Columnas de la tabla
        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(200);

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCorreo.setPrefWidth(200);

        TableColumn<Usuario, String> colDivision = new TableColumn<>("División");
        colDivision.setCellValueFactory(new PropertyValueFactory<>("division"));

        // Columna de acciones
        TableColumn<Usuario, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setPrefWidth(150);
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");

            {
                // Estilo para los botones
                btnEditar.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-size: 12;");
                btnEliminar.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-size: 12;");

                // Acciones de los botones
                btnEditar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    // Lógica para editar usuario
                    System.out.println("Editando usuario: " + usuario.getNombre());
                });

                btnEliminar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    // Lógica para eliminar usuario
                    System.out.println("Eliminando usuario: " + usuario.getNombre());

                    // Mostrar confirmación
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmar eliminación");
                    alert.setHeaderText("¿Está seguro de eliminar este usuario?");
                    alert.setContentText("Usuario: " + usuario.getNombre());

                    if (alert.showAndWait().get() == ButtonType.OK) {
                        getTableView().getItems().remove(usuario);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox botones = new HBox(5, btnEditar, btnEliminar);
                    setGraphic(botones);
                }
            }
        });

        tabla.getColumns().addAll(colId, colNombre, colCorreo, colDivision, colAcciones);

        // Agregar datos de ejemplo
        tabla.getItems().addAll(
                new Usuario(1, "Juan Pérez", "juan.perez@utez.edu.mx", "TIC"),
                new Usuario(2, "María García", "maria.garcia@utez.edu.mx", "Administración"),
                new Usuario(3, "Carlos López", "carlos.lopez@utez.edu.mx", "Industrial")
        );

        // Barra de búsqueda
        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);

        TextField txtBusqueda = new TextField();
        txtBusqueda.setPromptText("Buscar usuario...");
        txtBusqueda.setStyle("-fx-padding: 8; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #BDC3C7;");
        txtBusqueda.setPrefWidth(300);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold;");

        barraBusqueda.getChildren().addAll(txtBusqueda, btnBuscar);

        contenedorTabla.getChildren().addAll(lblTituloTabla, barraBusqueda, tabla);
        return contenedorTabla;
    }

    public VBox getVista() {
        return vista;
    }

    // Clase modelo para los usuarios
    public static class Usuario {
        private final int id;
        private final String nombre;
        private final String correo;
        private final String division;

        public Usuario(int id, String nombre, String correo, String division) {
            this.id = id;
            this.nombre = nombre;
            this.correo = correo;
            this.division = division;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getCorreo() {
            return correo;
        }

        public String getDivision() {
            return division;
        }
    }
}
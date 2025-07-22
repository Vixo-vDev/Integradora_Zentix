package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class VistaUsuarios {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    // Colores
    private final String COLOR_EXITO = "#2ECC71";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_PRIMARIO = "#3498DB";
    private final String COLOR_INACTIVO = "#95A5A6";
    private final String COLOR_FONDO = "#F5F7FA";
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";

    public VistaUsuarios(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Barras de navegación
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenido principal
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));

        // Encabezado con título y botón de agregar
        HBox encabezado = new HBox();
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setSpacing(20);

        Label lblTitulo = new Label("Gestión de Usuarios Registrados");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        Button btnAgregar = new Button("+ Nuevo Usuario");
        btnAgregar.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 8 16; -fx-background-radius: 4px;");
        btnAgregar.setOnAction(e -> mostrarFormularioNuevoUsuario());

        encabezado.getChildren().addAll(lblTitulo, btnAgregar);
        contenido.getChildren().add(encabezado);

        // Filtros y búsqueda
        HBox controlesSuperiores = new HBox(15);
        controlesSuperiores.setAlignment(Pos.CENTER_LEFT);

        TextField tfBusqueda = new TextField();
        tfBusqueda.setPromptText("Buscar usuario...");
        tfBusqueda.setPrefWidth(250);

        ComboBox<String> cbFiltroRol = new ComboBox<>();
        cbFiltroRol.getItems().addAll("Todos", "Administrador", "Usuario");
        cbFiltroRol.setValue("Todos");

        ComboBox<String> cbFiltroEstado = new ComboBox<>();
        cbFiltroEstado.getItems().addAll("Todos", "Activos", "Inactivos", "Bloqueados");
        cbFiltroEstado.setValue("Todos");

        controlesSuperiores.getChildren().addAll(
                new Label("Filtrar:"),
                tfBusqueda,
                new Label("Rol:"), cbFiltroRol,
                new Label("Estado:"), cbFiltroEstado
        );
        contenido.getChildren().add(controlesSuperiores);

        // Tabla de usuarios
        TableView<Usuario> tablaUsuarios = new TableView<>();
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaUsuarios.setStyle("-fx-font-size: 14px;");

        // Columnas
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));

        TableColumn<Usuario, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));

        TableColumn<Usuario, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        TableColumn<Usuario, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(column -> new TableCell<Usuario, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Activo":
                            setStyle("-fx-text-fill: " + COLOR_EXITO + "; -fx-font-weight: bold;");
                            break;
                        case "Bloqueado":
                            setStyle("-fx-text-fill: " + COLOR_PELIGRO + "; -fx-font-weight: bold;");
                            break;
                        case "Inactivo":
                            setStyle("-fx-text-fill: " + COLOR_INACTIVO + "; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        TableColumn<Usuario, String> colUltimoAcceso = new TableColumn<>("Último Acceso");
        colUltimoAcceso.setCellValueFactory(new PropertyValueFactory<>("ultimoAcceso"));

        TableColumn<Usuario, String> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<Usuario, String>() {
            private final Button btnActivar = new Button("Activar");
            private final Button btnDesactivar = new Button("Desactivar");
            private final Button btnResetPass = new Button("Reset Pass");
            private final Button btnCambiarRol = new Button("Cambiar Rol");
            private final HBox cajaBotones = new HBox(5, btnActivar, btnDesactivar, btnResetPass, btnCambiarRol);

            {
                btnActivar.setStyle("-fx-background-color: " + COLOR_EXITO + "; -fx-text-fill: white; -fx-min-width: 80;");
                btnDesactivar.setStyle("-fx-background-color: " + COLOR_PELIGRO + "; -fx-text-fill: white; -fx-min-width: 80;");
                btnResetPass.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white; -fx-min-width: 80;");
                btnCambiarRol.setStyle("-fx-background-color: #9B59B6; -fx-text-fill: white; -fx-min-width: 80;");

                btnActivar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    cambiarEstadoUsuario(usuario, "Activo");
                });

                btnDesactivar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    cambiarEstadoUsuario(usuario, "Inactivo");
                });

                btnResetPass.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    resetearPassword(usuario);
                });

                btnCambiarRol.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    cambiarRolUsuario(usuario);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    // Mostrar botones según estado
                    btnActivar.setDisable(usuario.getEstado().equals("Activo"));
                    btnDesactivar.setDisable(usuario.getEstado().equals("Inactivo"));
                    setGraphic(cajaBotones);
                }
            }
        });

        tablaUsuarios.getColumns().addAll(colNombre, colUsuario, colEmail, colRol, colEstado, colUltimoAcceso, colAcciones);

        // Datos de ejemplo (solo usuarios registrados)
        tablaUsuarios.getItems().addAll(
                new Usuario("Juan Pérez", "jperez", "jperez@empresa.com", "Usuario", "Activo", "2023-11-15 09:30"),
                new Usuario("María García", "mgarcia", "mgarcia@empresa.com", "Administrador", "Activo", "2023-11-15 10:15"),
                new Usuario("Luis Rodríguez", "lrodriguez", "lrodriguez@empresa.com", "Usuario", "Inactivo", "2023-10-28 14:20"),
                new Usuario("Ana López", "alopez", "alopez@empresa.com", "Usuario", "Bloqueado", "2023-11-10 16:45")
        );

        contenido.getChildren().add(tablaUsuarios);
        vista.setCenter(contenido);
    }

    private void mostrarFormularioNuevoUsuario() {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Usuario Registrado");
        dialog.setHeaderText("Registrar nuevo usuario en el sistema");

        // Configurar botones
        ButtonType btnCrear = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCrear, ButtonType.CANCEL);

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField tfNombre = new TextField();
        tfNombre.setPromptText("Nombre completo");
        TextField tfUsuario = new TextField();
        tfUsuario.setPromptText("Nombre de usuario");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Email");
        PasswordField pfPassword = new PasswordField();
        pfPassword.setPromptText("Contraseña");
        ComboBox<String> cbRol = new ComboBox<>();
        cbRol.getItems().addAll("Administrador", "Usuario");
        cbRol.setValue("Usuario");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(tfNombre, 1, 0);
        grid.add(new Label("Usuario:"), 0, 1);
        grid.add(tfUsuario, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(tfEmail, 1, 2);
        grid.add(new Label("Contraseña:"), 0, 3);
        grid.add(pfPassword, 1, 3);
        grid.add(new Label("Rol:"), 0, 4);
        grid.add(cbRol, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnCrear) {
                return new Usuario(
                        tfNombre.getText(),
                        tfUsuario.getText(),
                        tfEmail.getText(),
                        cbRol.getValue(),
                        "Activo",
                        "Nunca"
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(usuario -> {
            System.out.println("Nuevo usuario registrado creado: " + usuario.getNombreUsuario());
            // Aquí iría la lógica para guardar el nuevo usuario
        });
    }

    private void cambiarEstadoUsuario(Usuario usuario, String nuevoEstado) {
        System.out.println("Cambiando estado de " + usuario.getNombreUsuario() + " a " + nuevoEstado);
        usuario.setEstado(nuevoEstado);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Estado actualizado");
        alert.setHeaderText("El estado del usuario ha sido cambiado");
        alert.setContentText("Nuevo estado: " + nuevoEstado);
        alert.show();
    }

    private void resetearPassword(Usuario usuario) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Resetear Contraseña");
        confirmacion.setHeaderText("¿Resetear contraseña para " + usuario.getNombreUsuario() + "?");
        confirmacion.setContentText("Se enviará una contraseña temporal al email del usuario.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Operación exitosa");
            info.setHeaderText("Contraseña reseteada");
            info.setContentText("Se ha enviado una contraseña temporal a " + usuario.getEmail());
            info.show();
        }
    }

    private void cambiarRolUsuario(Usuario usuario) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                usuario.getRol(),
                "Administrador",
                "Usuario"
        );
        dialog.setTitle("Cambiar Rol");
        dialog.setHeaderText("Cambiar rol de " + usuario.getNombreUsuario());
        dialog.setContentText("Seleccione el nuevo rol:");

        dialog.showAndWait().ifPresent(nuevoRol -> {
            usuario.setRol(nuevoRol);
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Rol actualizado");
            info.setHeaderText("El rol ha sido cambiado");
            info.setContentText("Nuevo rol: " + nuevoRol);
            info.show();
        });
    }

    public BorderPane getVista() {
        return vista;
    }

    // Clase modelo para usuarios registrados
    public static class Usuario {
        private final String nombreCompleto;
        private final String nombreUsuario;
        private final String email;
        private String rol;
        private String estado;
        private final String ultimoAcceso;

        public Usuario(String nombreCompleto, String nombreUsuario, String email, String rol, String estado, String ultimoAcceso) {
            this.nombreCompleto = nombreCompleto;
            this.nombreUsuario = nombreUsuario;
            this.email = email;
            this.rol = rol;
            this.estado = estado;
            this.ultimoAcceso = ultimoAcceso;
        }

        // Getters
        public String getNombreCompleto() { return nombreCompleto; }
        public String getNombreUsuario() { return nombreUsuario; }
        public String getEmail() { return email; }
        public String getRol() { return rol; }
        public String getEstado() { return estado; }
        public String getUltimoAcceso() { return ultimoAcceso; }

        // Setters
        public void setEstado(String estado) { this.estado = estado; }
        public void setRol(String rol) { this.rol = rol; }
    }
}
package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;
import com.example.paneladmin.DAO.UsuarioDAO;
import com.example.paneladmin.Modelo.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class VistaUsuarios {
    @FXML private BorderPane vista;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TextField tfBusqueda;
    @FXML private ComboBox<String> cbFiltroRol;
    @FXML private ComboBox<String> cbFiltroEstado;

    private final UsuarioDAO usuarioDAO;
    private final ControladorBarraNavegacion controladorBarra;

    public VistaUsuarios(ControladorBarraNavegacion controladorBarra, UsuarioDAO usuarioDAO) {
        this.controladorBarra = controladorBarra;
        this.usuarioDAO = usuarioDAO;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DiseñoUsuario.fxml"));
            loader.setController(this);
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar VistaUsuarios.fxml", e);
        }
    }

    @FXML
    public void initialize() {
        configurarUI();
        cargarUsuarios();
        configurarEventos();
    }

    private void configurarUI() {
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Configurar comboboxes
        cbFiltroRol.getItems().addAll("Todos", "Administrador", "Usuario");
        cbFiltroRol.setValue("Todos");

        cbFiltroEstado.getItems().addAll("Todos", "Activo", "Inactivo", "Bloqueado");
        cbFiltroEstado.setValue("Todos");

        // Configurar tabla
        configurarColumnasTabla();
    }

    private void configurarColumnasTabla() {
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
        colEstado.setCellFactory(column -> new TableCell<>() {
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
                            setStyle("-fx-text-fill: #2ECC71; -fx-font-weight: bold;");
                            break;
                        case "Bloqueado":
                            setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
                            break;
                        case "Inactivo":
                            setStyle("-fx-text-fill: #95A5A6; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        TableColumn<Usuario, String> colUltimoAcceso = new TableColumn<>("Último Acceso");
        colUltimoAcceso.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return cellData.getValue().getUltimoAcceso() != null ?
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUltimoAcceso().format(formatter)) :
                    new javafx.beans.property.SimpleStringProperty("Nunca");
        });

        TableColumn<Usuario, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnActivar = new Button("Activar");
            private final Button btnDesactivar = new Button("Desactivar");
            private final Button btnResetPass = new Button("Reset Pass");
            private final Button btnEditar = new Button("Editar");
            private final HBox cajaBotones = new HBox(5, btnActivar, btnDesactivar, btnResetPass, btnEditar);

            {
                btnActivar.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-min-width: 80;");
                btnDesactivar.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-min-width: 80;");
                btnResetPass.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-min-width: 80;");
                btnEditar.setStyle("-fx-background-color: #9B59B6; -fx-text-fill: white; -fx-min-width: 80;");

                btnActivar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    activarUsuario(usuario.getId());
                });

                btnDesactivar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    desactivarUsuario(usuario.getId());
                });

                btnResetPass.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    resetearPassword(usuario);
                });

                btnEditar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    mostrarFormularioEdicion(usuario);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    btnActivar.setDisable(usuario.getEstado().equals("Activo"));
                    btnDesactivar.setDisable(usuario.getEstado().equals("Inactivo") || usuario.getEstado().equals("Bloqueado"));
                    setGraphic(cajaBotones);
                }
            }
        });

        tablaUsuarios.getColumns().addAll(colNombre, colUsuario, colEmail, colRol, colEstado, colUltimoAcceso, colAcciones);
    }

    private void configurarEventos() {
        tfBusqueda.textProperty().addListener((obs, oldVal, newVal) -> filtrarUsuarios());
        cbFiltroRol.valueProperty().addListener((obs, oldVal, newVal) -> filtrarUsuarios());
        cbFiltroEstado.valueProperty().addListener((obs, oldVal, newVal) -> filtrarUsuarios());
    }

    private void cargarUsuarios() {
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList(usuarioDAO.obtenerTodos());
        tablaUsuarios.setItems(usuarios);
    }

    private void filtrarUsuarios() {
        String textoBusqueda = tfBusqueda.getText().toLowerCase();
        String rolSeleccionado = cbFiltroRol.getValue();
        String estadoSeleccionado = cbFiltroEstado.getValue();

        List<Usuario> usuariosFiltrados = usuarioDAO.obtenerTodos().stream()
                .filter(u -> textoBusqueda.isEmpty() ||
                        u.getNombreCompleto().toLowerCase().contains(textoBusqueda) ||
                        u.getNombreUsuario().toLowerCase().contains(textoBusqueda) ||
                        u.getEmail().toLowerCase().contains(textoBusqueda))
                .filter(u -> rolSeleccionado.equals("Todos") || u.getRol().equals(rolSeleccionado))
                .filter(u -> estadoSeleccionado.equals("Todos") || u.getEstado().equals(estadoSeleccionado))
                .collect(Collectors.toList());

        tablaUsuarios.setItems(FXCollections.observableArrayList(usuariosFiltrados));
    }

    @FXML
    private void mostrarFormularioNuevoUsuario() {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Usuario");
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
        PasswordField pfConfirmPassword = new PasswordField();
        pfConfirmPassword.setPromptText("Confirmar contraseña");
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
        grid.add(new Label("Confirmar:"), 0, 4);
        grid.add(pfConfirmPassword, 1, 4);
        grid.add(new Label("Rol:"), 0, 5);
        grid.add(cbRol, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnCrear) {
                if (!pfPassword.getText().equals(pfConfirmPassword.getText())) {
                    mostrarAlerta("Error", "Las contraseñas no coinciden");
                    return null;
                }
                if (tfNombre.getText().isEmpty() || tfUsuario.getText().isEmpty() ||
                        tfEmail.getText().isEmpty() || pfPassword.getText().isEmpty()) {
                    mostrarAlerta("Error", "Todos los campos son obligatorios");
                    return null;
                }

                return new Usuario(
                        tfNombre.getText(),
                        tfUsuario.getText(),
                        tfEmail.getText(),
                        pfPassword.getText(),
                        cbRol.getValue()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(usuario -> {
            usuarioDAO.crearUsuario(usuario);
            cargarUsuarios();
            mostrarAlerta("Éxito", "Usuario creado correctamente");
        });
    }

    private void mostrarFormularioEdicion(Usuario usuario) {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Editar Usuario");
        dialog.setHeaderText("Editar información del usuario");

        // Configurar botones
        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField tfNombre = new TextField(usuario.getNombreCompleto());
        TextField tfUsuario = new TextField(usuario.getNombreUsuario());
        TextField tfEmail = new TextField(usuario.getEmail());
        ComboBox<String> cbRol = new ComboBox<>();
        cbRol.getItems().addAll("Administrador", "Usuario");
        cbRol.setValue(usuario.getRol());
        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("Activo", "Inactivo", "Bloqueado");
        cbEstado.setValue(usuario.getEstado());

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(tfNombre, 1, 0);
        grid.add(new Label("Usuario:"), 0, 1);
        grid.add(tfUsuario, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(tfEmail, 1, 2);
        grid.add(new Label("Rol:"), 0, 3);
        grid.add(cbRol, 1, 3);
        grid.add(new Label("Estado:"), 0, 4);
        grid.add(cbEstado, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnGuardar) {
                usuario.setNombreCompleto(tfNombre.getText());
                usuario.setNombreUsuario(tfUsuario.getText());
                usuario.setEmail(tfEmail.getText());
                usuario.setRol(cbRol.getValue());
                usuario.setEstado(cbEstado.getValue());
                return usuario;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(usuarioEditado -> {
            usuarioDAO.actualizarUsuario(usuarioEditado);
            cargarUsuarios();
            mostrarAlerta("Éxito", "Usuario actualizado correctamente");
        });
    }

    private void activarUsuario(int id) {
        usuarioDAO.activarUsuario(id);
        cargarUsuarios();
        mostrarAlerta("Éxito", "Usuario activado correctamente");
    }

    private void desactivarUsuario(int id) {
        usuarioDAO.desactivarUsuario(id);
        cargarUsuarios();
        mostrarAlerta("Éxito", "Usuario desactivado correctamente");
    }

    private void resetearPassword(Usuario usuario) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Resetear Contraseña");
        dialog.setHeaderText("Resetear contraseña para " + usuario.getNombreUsuario());

        // Configurar botones
        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmar, ButtonType.CANCEL);

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        PasswordField pfPassword = new PasswordField();
        pfPassword.setPromptText("Nueva contraseña");
        PasswordField pfConfirmPassword = new PasswordField();
        pfConfirmPassword.setPromptText("Confirmar contraseña");

        grid.add(new Label("Nueva contraseña:"), 0, 0);
        grid.add(pfPassword, 1, 0);
        grid.add(new Label("Confirmar:"), 0, 1);
        grid.add(pfConfirmPassword, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnConfirmar) {
                if (!pfPassword.getText().equals(pfConfirmPassword.getText())) {
                    mostrarAlerta("Error", "Las contraseñas no coinciden");
                    return null;
                }
                return pfPassword.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(nuevaPassword -> {
            usuarioDAO.cambiarPassword(usuario.getId(), nuevaPassword);
            mostrarAlerta("Éxito", "Contraseña actualizada correctamente");
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public BorderPane getVista() {
        return vista;
    }
}
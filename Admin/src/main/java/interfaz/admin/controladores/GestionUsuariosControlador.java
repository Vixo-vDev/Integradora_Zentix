package interfaz.admin.controladores;

import interfaz.admin.dao.UsuarioDAO;
import interfaz.admin.dao.impl.UsuarioDAOImpl;
import interfaz.admin.modelos.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionUsuariosControlador implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(GestionUsuariosControlador.class.getName());

    @FXML private TextField campoNombre;
    @FXML private TextField campoApellidos;
    @FXML private TextField campoCorreo;
    @FXML private TextField campoDomicilio;
    @FXML private TextField campoLada;
    @FXML private TextField campoTelefono;
    @FXML private DatePicker campoFechaNacimiento;
    @FXML private TextField campoEdad; // Representa la edad como String, se convierte a Integer
    @FXML private ComboBox<String> campoRol;
    @FXML private TextField campoMatricula; // Puede ser String o null
    @FXML private PasswordField campoPassword;
    @FXML private PasswordField campoConfirmarPassword;
    @FXML private Label etiquetaMensaje;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colIdUsuario;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellidos;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colDomicilio;
    @FXML private TableColumn<Usuario, String> colLada;
    @FXML private TableColumn<Usuario, String> colTelefono;
    @FXML private TableColumn<Usuario, Date> colFechaNacimiento;
    @FXML private TableColumn<Usuario, Integer> colEdad;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colMatricula;

    private UsuarioDAO usuarioDAO;
    private ObservableList<Usuario> listaUsuarios;
    private Usuario usuarioSeleccionado; // Para almacenar el usuario seleccionado en la tabla

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO = new UsuarioDAOImpl();
        listaUsuarios = FXCollections.observableArrayList();

        // Inicializar ComboBox de Roles
        campoRol.setItems(FXCollections.observableArrayList("Administrador", "Profesor", "Estudiante"));

        // Configurar columnas de la tabla
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correoInstitucional"));
        colDomicilio.setCellValueFactory(new PropertyValueFactory<>("domicilio"));
        colLada.setCellValueFactory(new PropertyValueFactory<>("lada"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula")); // Propiedad para String

        // Cargar usuarios al iniciar la vista
        cargarUsuarios();

        // Listener para la selección de filas en la tabla
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usuarioSeleccionado = newSelection;
                llenarCamposConUsuario(newSelection);
            } else {
                usuarioSeleccionado = null;
                limpiarCampos();
            }
        });
    }

    private void cargarUsuarios() {
        try {
            listaUsuarios.setAll(usuarioDAO.obtenerTodosLosUsuarios());
            tablaUsuarios.setItems(listaUsuarios);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar usuarios desde la base de datos.", e);
            mostrarMensaje("Error al cargar usuarios: " + e.getMessage(), "error");
        }
    }

    private void llenarCamposConUsuario(Usuario usuario) {
        campoNombre.setText(usuario.getNombre());
        campoApellidos.setText(usuario.getApellidos());
        campoCorreo.setText(usuario.getCorreoInstitucional());
        campoDomicilio.setText(usuario.getDomicilio());
        campoLada.setText(usuario.getLada());
        campoTelefono.setText(usuario.getTelefono());
        campoFechaNacimiento.setValue(usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toLocalDate() : null);
        campoEdad.setText(usuario.getEdad() != null ? String.valueOf(usuario.getEdad()) : "");
        campoRol.setValue(usuario.getRol());
        campoMatricula.setText(usuario.getMatricula());
        campoPassword.setText(""); // No cargar contraseña existente por seguridad
        campoConfirmarPassword.setText(""); // No cargar contraseña existente por seguridad
    }

    @FXML
    private void crearUsuario(ActionEvent event) {
        if (!validarCampos()) return;
        if (!campoPassword.getText().equals(campoConfirmarPassword.getText())) {
            mostrarMensaje("Las contraseñas no coinciden.", "error");
            return;
        }

        Usuario nuevoUsuario = construirUsuarioDesdeCampos();
        // El ID_USUARIO es autoincremental en la BD, no lo establecemos aquí.

        try {
            if (usuarioDAO.insertarUsuario(nuevoUsuario)) {
                mostrarMensaje("Usuario creado exitosamente.", "exito");
                limpiarCampos();
                cargarUsuarios(); // Recargar la tabla
            } else {
                mostrarMensaje("No se pudo crear el usuario.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear usuario.", e);
            mostrarMensaje("Error de base de datos al crear usuario: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void actualizarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Seleccione un usuario de la tabla para actualizar.", "error");
            return;
        }
        if (!validarCampos()) return;

        Usuario usuarioActualizado = construirUsuarioDesdeCampos();
        usuarioActualizado.setIdUsuario(usuarioSeleccionado.getIdUsuario()); // Mantener el ID del usuario seleccionado

        // Validar y aplicar contraseña solo si se ha ingresado algo
        String nuevaPassword = campoPassword.getText();
        String confirmarPassword = campoConfirmarPassword.getText();
        if (!nuevaPassword.isEmpty()) {
            if (!nuevaPassword.equals(confirmarPassword)) {
                mostrarMensaje("Las contraseñas no coinciden.", "error");
                return;
            }
            usuarioActualizado.setPassword(nuevaPassword);
        } else {
            // Si no se ingresó nueva contraseña, mantener la existente (requiere obtenerla de la BD)
            try {
                Usuario existingUser = usuarioDAO.obtenerUsuarioPorId(usuarioSeleccionado.getIdUsuario());
                if (existingUser != null) {
                    usuarioActualizado.setPassword(existingUser.getPassword());
                } else {
                    mostrarMensaje("Error: Usuario no encontrado para obtener contraseña existente.", "error");
                    return;
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al obtener contraseña existente para actualizar.", e);
                mostrarMensaje("Error al obtener contraseña existente.", "error");
                return;
            }
        }


        try {
            if (usuarioDAO.actualizarUsuario(usuarioActualizado)) {
                mostrarMensaje("Usuario actualizado exitosamente.", "exito");
                limpiarCampos();
                cargarUsuarios(); // Recargar la tabla
            } else {
                mostrarMensaje("No se pudo actualizar el usuario.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar usuario.", e);
            mostrarMensaje("Error de base de datos al actualizar usuario: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void eliminarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Seleccione un usuario de la tabla para eliminar.", "error");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea eliminar a " + usuarioSeleccionado.getNombre() + " " + usuarioSeleccionado.getApellidos() + "?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirmar eliminación");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                if (usuarioDAO.eliminarUsuario(usuarioSeleccionado.getIdUsuario())) {
                    mostrarMensaje("Usuario eliminado exitosamente.", "exito");
                    limpiarCampos();
                    cargarUsuarios(); // Recargar la tabla
                } else {
                    mostrarMensaje("No se pudo eliminar el usuario.", "error");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al eliminar usuario.", e);
                mostrarMensaje("Error de base de datos al eliminar usuario: " + e.getMessage(), "error");
            }
        }
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        limpiarCampos();
    }

    private void limpiarCampos() {
        campoNombre.clear();
        campoApellidos.clear();
        campoCorreo.clear();
        campoDomicilio.clear();
        campoLada.clear();
        campoTelefono.clear();
        campoFechaNacimiento.setValue(null);
        campoEdad.clear();
        campoRol.getSelectionModel().clearSelection(); // Limpiar selección del ComboBox
        campoMatricula.clear();
        campoPassword.clear();
        campoConfirmarPassword.clear();
        etiquetaMensaje.setText("");
        tablaUsuarios.getSelectionModel().clearSelection(); // Deseleccionar de la tabla
        usuarioSeleccionado = null;
    }

    private Usuario construirUsuarioDesdeCampos() {
        Usuario usuario = new Usuario();
        usuario.setNombre(campoNombre.getText().trim());
        usuario.setApellidos(campoApellidos.getText().trim());
        usuario.setCorreoInstitucional(campoCorreo.getText().trim());
        usuario.setDomicilio(campoDomicilio.getText().trim());
        usuario.setLada(campoLada.getText().trim());
        usuario.setTelefono(campoTelefono.getText().trim());
        usuario.setFechaNacimiento(campoFechaNacimiento.getValue() != null ? Date.valueOf(campoFechaNacimiento.getValue()) : null);

        // Manejar Edad como Integer (puede ser nulo)
        String edadText = campoEdad.getText().trim();
        if (!edadText.isEmpty()) {
            try {
                usuario.setEdad(Integer.parseInt(edadText));
            } catch (NumberFormatException e) {
                // Esto debería ser capturado por validarCampos, pero como fallback
                LOGGER.log(Level.WARNING, "Edad no es un número válido: " + edadText);
                usuario.setEdad(null);
            }
        } else {
            usuario.setEdad(null);
        }

        usuario.setRol(campoRol.getValue());
        usuario.setMatricula(campoMatricula.getText().trim().isEmpty() ? null : campoMatricula.getText().trim());
        usuario.setPassword(campoPassword.getText().trim()); // Contraseña para crear/actualizar
        return usuario;
    }

    private boolean validarCampos() {
        if (campoNombre.getText().trim().isEmpty() ||
                campoApellidos.getText().trim().isEmpty() ||
                campoCorreo.getText().trim().isEmpty() ||
                campoDomicilio.getText().trim().isEmpty() ||
                campoLada.getText().trim().isEmpty() ||
                campoTelefono.getText().trim().isEmpty() ||
                campoFechaNacimiento.getValue() == null ||
                campoRol.getValue() == null ||
                campoPassword.getText().trim().isEmpty()) {
            mostrarMensaje("Todos los campos obligatorios deben ser llenados (excepto Edad y Matrícula).", "error");
            return false;
        }

        // Validar formato de correo básico
        if (!campoCorreo.getText().trim().matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            mostrarMensaje("Formato de correo institucional inválido.", "error");
            return false;
        }

        // Validar que la edad sea un número si está presente
        String edadText = campoEdad.getText().trim();
        if (!edadText.isEmpty()) {
            if (!edadText.matches("\\d+")) { // Solo dígitos
                mostrarMensaje("La edad debe ser un número entero.", "error");
                return false;
            }
            try {
                int edad = Integer.parseInt(edadText);
                if (edad < 0 || edad > 120) { // Rango razonable
                    mostrarMensaje("La edad debe estar entre 0 y 120 años.", "error");
                    return false;
                }
            } catch (NumberFormatException e) {
                // Ya cubierto por matches, pero como precaución
                mostrarMensaje("La edad debe ser un número válido.", "error");
                return false;
            }
        }

        return true;
    }

    private void mostrarMensaje(String mensaje, String tipo) {
        etiquetaMensaje.setText(mensaje);
        if ("exito".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #27ae60;"); // Verde
        } else if ("error".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #e74c3c;"); // Rojo
        } else {
            etiquetaMensaje.setStyle("-fx-text-fill: #2c3e50;"); // Azul oscuro (por defecto)
        }
    }
}
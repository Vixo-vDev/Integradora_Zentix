package interfaz.admin.controladores;

import interfaz.admin.dao.UsuarioDAO;
import interfaz.admin.dao.impl.UsuarioDAOImpl;
import interfaz.admin.modelos.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginControlador {

    @FXML
    private TextField campoCorreo;
    @FXML
    private PasswordField campoPassword;
    @FXML
    private Label etiquetaMensaje;

    private UsuarioDAO usuarioDAO;

    public LoginControlador() {
        this.usuarioDAO = new UsuarioDAOImpl(); // Instanciamos la implementación del DAO
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String correo = campoCorreo.getText();
        String password = campoPassword.getText();

        if (correo.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, ingrese su correo y contraseña.", "error");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.obtenerUsuarioPorCorreo(correo);

            // IMPORTANTE: En una aplicación real, aquí deberías HASHEAR y SALTAR la contraseña
            // y luego comparar el hash almacenado con el hash de la contraseña ingresada.
            // Por simplicidad, se usa comparación directa en este ejemplo.
            if (usuario != null && usuario.getPassword().equals(password)) {
                mostrarMensaje("¡Inicio de sesión exitoso! Redirigiendo...", "exito");

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(e -> {
                    try {
                        cargarVistaPrincipal(usuario); // <-- Pasa el objeto usuario aquí
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        mostrarMensaje("Error al cargar la siguiente pantalla.", "error");
                    }
                });
                pause.play();

            } else {
                mostrarMensaje("Credenciales inválidas. Verifique su correo o contraseña.", "error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error de base de datos al iniciar sesión.", "error");
        }
    }

    /**
     * Muestra un mensaje en la etiqueta de mensaje y aplica un estilo CSS.
     * @param mensaje El texto del mensaje.
     * @param tipo "exito" para verde, "error" para rojo.
     */
    private void mostrarMensaje(String mensaje, String tipo) {
        etiquetaMensaje.setText(mensaje);
        if ("exito".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #27ae60;"); // Verde
        } else if ("error".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #e74c3c;"); // Rojo
        }
    }

    /**
     * Carga la vista principal de la aplicación después de un inicio de sesión exitoso.
     * Dependiendo del rol del usuario, podría cargar un dashboard diferente o habilitar/deshabilitar funcionalidades.
     * Por ahora, cargaremos una vista genérica de "Dashboard".
     * @param usuario El usuario que ha iniciado sesión.
     * @throws IOException Si la vista FXML no se puede cargar.
     */
    private void cargarVistaPrincipal(Usuario usuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/DashboardVista.fxml")));
        Parent root = loader.load();

        // Obtener el controlador del Dashboard y pasar el objeto Usuario
        DashboardControlador dashboardControlador = loader.getController();
        dashboardControlador.setUsuarioActual(usuario); // <-- Llama al método para pasar el usuario

        Stage stage = (Stage) campoCorreo.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard - Gestión de Equipos");
        stage.show();
    }
}
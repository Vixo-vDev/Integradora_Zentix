package interfaz.admin.controladores;

import interfaz.admin.modelos.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Objects;

public class DashboardControlador {

    @FXML
    private Label labelNombreUsuario;
    @FXML
    private Label labelRolUsuario;
    @FXML
    private VBox menuAdmin;
    @FXML
    private VBox menuProfesor;
    @FXML
    private VBox menuEstudiante;
    @FXML
    private StackPane contentArea; // Contenedor para cargar las diferentes vistas

    private Usuario usuarioActual;

    /**
     * Método para inicializar el controlador con el usuario que ha iniciado sesión.
     * Este método debe ser llamado desde el LoginControlador después de una autenticación exitosa.
     *
     * @param usuario El objeto Usuario que ha iniciado sesión.
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        labelNombreUsuario.setText(usuario.getNombre() + " " + usuario.getApellidos());
        labelRolUsuario.setText(usuario.getRol());
        mostrarMenuSegunRol(usuario.getRol());

        // Cargar una vista de "bienvenida" o "inicio" por defecto en el contentArea
        // Ajusta esta ruta si tienes una vista de inicio específica para el Dashboard
        cargarContenido("/fxml/InicioDashboard.fxml", "Inicio"); // Nuevo FXML para el dashboard de inicio
    }

    /**
     * Muestra el menú de navegación correspondiente al rol del usuario.
     */
    private void mostrarMenuSegunRol(String rol) {
        // Ocultar todos los menús primero
        menuAdmin.setVisible(false);
        menuAdmin.setManaged(false);
        menuProfesor.setVisible(false);
        menuProfesor.setManaged(false);
        menuEstudiante.setVisible(false);
        menuEstudiante.setManaged(false);

        // Mostrar solo el menú correspondiente al rol
        switch (rol) {
            case "Administrador":
                menuAdmin.setVisible(true);
                menuAdmin.setManaged(true);
                break;
            case "Profesor":
                menuProfesor.setVisible(true);
                menuProfesor.setManaged(true);
                break;
            case "Estudiante":
                menuEstudiante.setVisible(true);
                menuEstudiante.setManaged(true);
                break;
            default:
                System.err.println("Rol de usuario desconocido: " + rol);
                // Quizás mostrar un menú por defecto o un mensaje de error
                break;
        }
    }

    // --- Métodos para cargar las diferentes vistas en el contentArea ---

    // ===================================================================
    // Métodos para el menú de ADMINISTRADOR
    // ===================================================================

    @FXML
    private void mostrarDashboard(ActionEvent event) {
        // Esta sería la vista de resumen principal para el administrador
        cargarContenido("/fxml/DashboardInicio.fxml", "Dashboard Principal");
    }

    @FXML
    private void mostrarGestionUsuarios(ActionEvent event) {
        cargarContenido("/fxml/GestionUsuariosVista.fxml", "Gestión de Usuarios");
    }

    @FXML
    private void mostrarGestionInventario(ActionEvent event) {
        // Podría ser GestionEquiposVista o un nuevo InventarioVista
        cargarContenido("/fxml/GestionEquiposVista.fxml", "Gestión de Inventario");
    }

    @FXML
    private void mostrarEstadisticas(ActionEvent event) {
        cargarContenido("/fxml/EstadisticasVista.fxml", "Estadísticas del Sistema");
    }

    @FXML
    private void mostrarSolicitudesAdmin(ActionEvent event) {
        // Usamos la vista que acabamos de trabajar
        cargarContenido("/fxml/AdminSolicitudes.fxml", "Gestión de Solicitudes");
    }

    @FXML
    private void mostrarNotificaciones(ActionEvent event) {
        cargarContenido("/fxml/NotificacionesVista.fxml", "Notificaciones");
    }

    // ===================================================================
    // Métodos para el menú de PROFESOR (Ajusta los nombres FXML si son diferentes)
    // ===================================================================

    @FXML
    private void mostrarSolicitudesProfesor(ActionEvent event) {
        // La vista del profesor solo mostrará sus solicitudes a validar o sus propias
        cargarContenido("/fxml/profesor/ProfesorSolicitudes.fxml", "Mis Solicitudes (Profesor)");
    }

    // ===================================================================
    // Métodos para el menú de ESTUDIANTE (Ajusta los nombres FXML si son diferentes)
    // ===================================================================

    @FXML
    private void mostrarMisSolicitudesEstudiante(ActionEvent event) {
        // La vista del estudiante para ver sus propias solicitudes
        cargarContenido("/fxml/estudiante/EstudianteMisSolicitudes.fxml", "Mis Solicitudes");
    }

    @FXML
    private void mostrarCrearSolicitud(ActionEvent event) {
        // La vista para que el estudiante cree una nueva solicitud
        cargarContenido("/fxml/estudiante/EstudianteCrearSolicitud.fxml", "Crear Nueva Solicitud");
    }

    /**
     * Método genérico para cargar contenido FXML en el área central.
     * También pasa el usuario actual si el controlador de la vista cargada tiene un método setUsuarioActual.
     *
     * @param fxmlPath Ruta al archivo FXML (ej. "/fxml/admin/UsuariosVista.fxml").
     * @param title    Título opcional para el contenido (puede usarse para actualizar una etiqueta de título).
     */
    private void cargarContenido(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent content = loader.load();

            // Intentar pasar el usuario actual al controlador de la vista cargada
            Object controller = loader.getController();
            if (controller != null && controller instanceof ControladorConUsuario) {
                ((ControladorConUsuario) controller).setUsuarioActual(usuarioActual);
            } else {
                System.out.println("Controlador de " + fxmlPath + " no implementa ControladorConUsuario o es null.");
            }

            contentArea.getChildren().setAll(content);
            System.out.println("Cargada vista: " + title + " en " + fxmlPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista " + fxmlPath + ": " + e.getMessage());
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Carga", "No se pudo cargar la vista: " + title + ".\nVerifique la ruta del archivo FXML o la implementación del controlador.");
        }
    }

    /**
     * Muestra una alerta simple al usuario.
     * (Asegúrate de tener esta utilidad o implementarla en una clase de utilidades)
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/LoginVista.fxml")));
            Parent loginRoot = loader.load();
            Scene scene = new Scene(loginRoot);
            Stage stage = (Stage) labelNombreUsuario.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión - Gestión de Equipos Tecnológicos");
            stage.show();
            // Limpiar datos del usuario actual
            this.usuarioActual = null;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista de Login para cerrar sesión.");
        }
    }
}
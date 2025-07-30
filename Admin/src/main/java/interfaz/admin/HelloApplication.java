package interfaz.admin;

import interfaz.admin.Config.ConexionBD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.sql.Connection;

public class HelloApplication extends Application {

    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        // Prueba de conexión a la base de datos al inicio de la aplicación
        Connection conexionTest = ConexionBD.obtenerConexion();
        if (conexionTest != null) {
            System.out.println("La aplicación se inició con éxito y la conexión a la BD está disponible.");

        } else {
            System.err.println("La aplicación no pudo establecer conexión con la base de datos al inicio. Revise la configuración.");

            return; // Salir del método start si no hay conexión
        }

        try {
            // Cargar la vista de Login
            Parent raiz = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/LoginVista.fxml")));
            Scene escena = new Scene(raiz);
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.setTitle("Inicio de Sesión - Gestión de Equipos Tecnológicos");
            escenarioPrincipal.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista FXML de Login: " + e.getMessage());
            e.printStackTrace();
            // Aquí podrías mostrar una alerta al usuario sobre el error y salir
        }
    }

    @Override
    public void stop() {
        // Cerrar la conexión de la base de datos cuando la aplicación se cierra
        ConexionBD.cerrarConexion();
        System.out.println("Aplicación cerrada. Conexión a la BD liberada.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
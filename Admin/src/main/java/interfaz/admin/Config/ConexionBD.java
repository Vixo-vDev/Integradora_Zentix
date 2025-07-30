package interfaz.admin.Config;

import interfaz.admin.dao.UsuarioDAO;
import interfaz.admin.dao.impl.UsuarioDAOImpl;
import interfaz.admin.modelos.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {

    // Configuración para la conexión con Oracle Cloud y Wallet
    private static final String TNS_ADMIN = "C:\\Users\\mapi1\\Desktop\\Integradora_Zentix\\Admin\\src\\main\\resources\\Wallet_YHA6ZS91V0ZCFRSH"; // Ruta a la carpeta de la wallet
    private static final String URL_BASEDATOS = "jdbc:oracle:thin:@yha6zs91v0zcfrsh_high";
    private static final String USUARIO_BD = "ADMIN";
    private static final String PASSWORD_BD = "DevP1kNatura1";

    private static Connection conexion; // La conexión ahora es estática para mantenerla abierta

    // Bloque estático para cargar el driver de Oracle
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, "Error al registrar el driver JDBC de Oracle.", ex);
        }
    }

    /**
     * Establece la conexión a la base de datos Oracle Cloud.
     * @return Una instancia de Connection si la conexión es exitosa, de lo contrario null.
     */
    public static Connection obtenerConexion() {
        // Solo establecer la conexión si no está ya establecida
        if (conexion == null) {
            System.setProperty("oracle.net.tns_admin", TNS_ADMIN); // Establece la propiedad del sistema para la wallet
            try {
                conexion = DriverManager.getConnection(URL_BASEDATOS, USUARIO_BD, PASSWORD_BD);
                System.out.println("Conexión a la base de datos establecida exitosamente.");
            } catch (SQLException ex) {
                Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, "Error al conectar con la base de datos.", ex);
                System.out.println("No se pudo establecer la conexión a la base de datos.");
            }
        }
        return conexion;
    }
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null; // Reinicia la conexión para futuras solicitudes (si se vuelve a abrir)
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException ex) {
                Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, "Error al cerrar la conexión con la base de datos.", ex);
            }
        }
    }

    // Método main para probar la conexión y listar usuarios
    public static void main(String[] args) {
        Connection testConexion = ConexionBD.obtenerConexion();
        if (testConexion != null) {
            System.out.println("¡Prueba de conexión exitosa!");

            // --- Código para mostrar los datos de la tabla USUARIO ---
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
            try {
                System.out.println("\n--- Listado de Usuarios ---");
                List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
                if (usuarios.isEmpty()) {
                    System.out.println("No hay usuarios registrados en la base de datos.");
                } else {
                    for (Usuario user : usuarios) {
                        System.out.println(user.toString()); // Usamos el método toString() del modelo Usuario
                    }
                }
                System.out.println("---------------------------\n");

            } catch (SQLException e) {
                System.err.println("Error al intentar obtener y mostrar los usuarios: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Siempre cierra la conexión al finalizar el main de prueba
                ConexionBD.cerrarConexion();
            }

        } else {
            System.out.println("¡Fallo en la prueba de conexión! No se pueden listar los usuarios.");
        }
    }
}
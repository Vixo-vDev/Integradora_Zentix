package Controladores;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {

    private static String URL      = "jdbc:oracle:thin:@ice4ge175s4y5vmw_low";
    private static String USER     = "ADMIN";
    private static String PASSWORD = "v1x0DeV1506!";

    // Obtiene una conexión nueva
    public static Connection getConnection() throws SQLException {
        // 1. Apunta al directorio donde descomprimiste el wallet
        System.setProperty("oracle.net.tns_admin", "C:\\Users\\mapi1\\Downloads\\Wallet_ICE4GE175S4Y5VMW");
        // 2. (Opcional) fuerza la validación de nombre de servidor en el certificado
        System.setProperty("oracle.net.ssl_server_dn_match", "true");
        // 3. Obtiene la conexión usando alias, user y pass
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = getConnection()) {
            System.out.println("¡Conexión exitosa!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*private static final String url ="jdbc:oracle:thin:@ice4ge175s4y5vmw_medium";
    private static final String user = "ADMIN";
    private static final String password = "v1x0DeV1506!";

    public static Connection conectar() {
        System.out.println("Conectando a: "+url);
        try {
            System.setProperty("oracle.net.tns_admin", "C:\\wallet_prueba");
            System.setProperty("oracle.net.ssl_server_dn_match", "true");
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Conexión exitosa a la base de datos");
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.out.println("Conexión fallida");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }*/
}

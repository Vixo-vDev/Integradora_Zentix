package Controladores;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {

    private static String URL      = "jdbc:oracle:thin:@ice4ge175s4y5vmw_low";
    private static String USER     = "ADMIN";
    private static String PASSWORD = "v1x0DeV1506!";

    // Obtiene una conexión nueva
    public void getConnection() throws SQLException {
        try {
            // 1. Apunta al directorio donde descomprimiste el wallet
            System.setProperty("oracle.net.tns_admin", "C:\\Users\\mapi1\\Downloads\\Wallet_ICE4GE175S4Y5VMW");
            // 2. (Opcional) fuerza la validación de nombre de servidor en el certificado
            System.setProperty("oracle.net.ssl_server_dn_match", "true");
            // 3. Obtiene la conexión usando alias, user y pass
            DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a la base de datos exitosa");
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }


}

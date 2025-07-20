package Controladores;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {

    private static String URL      = "jdbc:oracle:thin:@zentixdb_high";
    private static String USER     = "ADMIN";
    private static String PASSWORD = "Eliaquimzentix1";

    // Obtiene una conexión nueva
    public void getConnection() throws SQLException {
        try {
            // 1. Apunta al directorio donde descomprimiste el wallet
            System.setProperty("oracle.net.tns_admin", "C:\\Users\\mapi1\\Downloads\\netrix\\Wallet_zentixdb");
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

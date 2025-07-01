package Controladores;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {
    private static String url ="jdbc:oracle:thin:@localhost:1521:xe";
    private static String user = "SYSTEM";
    private static String password = "dataBase";

    public static Connection conectar() {
        System.out.println("Conectado a: "+url);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Conexión exitosa a la base de datos");
            return DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Conexión fallida");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

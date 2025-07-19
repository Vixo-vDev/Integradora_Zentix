package mx.edu.utez.demo.dao.impl;

import mx.edu.utez.demo.config.DBConnection;
import mx.edu.utez.demo.dao.IUsuarioDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDaoImpl implements IUsuarioDao {

    @Override
    public boolean login(String correo, String pass) throws Exception {
        String sql = "SELECT ID,CORREO,PASS FROM USUARIO WHERE CORREO=? and PASS=?";
        try {
            Connection con = DBConnection.getConnection(); // se estable la conexion
            PreparedStatement ps = con.prepareStatement(sql); //se prepara la consulta para evitar la inyecion de SQL
            ps.setString(1, correo); //se sustituye "?" por el correo
            ps.setString(2, pass); // se sustituye "?" por la pass
            ResultSet resultSet = ps.executeQuery(); //se ejecuta la consulta
            if (resultSet.next()) { //validacion que la consulta arroje un resultado
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public static void main(String[] args) {
        UsuarioDaoImpl dao = new UsuarioDaoImpl();
        try {
            System.out.println(dao.login("test1@utez.edu", "pass"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

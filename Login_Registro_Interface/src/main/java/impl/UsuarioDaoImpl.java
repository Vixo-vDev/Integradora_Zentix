package impl;

import Dao.IUsuarioDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UsuarioDaoImpl implements IUsuarioDao {

    @Override
    public boolean login(String correo, String pass) throws Exception {
        String sql= "SELECT ID_USUARIO,CORREO_INSTITUCIONAL,PASSWORD FROM USUARIO WHERE CORREO_INSTITUCIONAL=? and PASSWORD=?";
        try {
            Connection con = ConnectionBD.getConnection(); // se estable la conexion
            PreparedStatement ps =  con.prepareStatement(sql); //se prepara la consulta para evitar la inyecion de SQL
            ps.setString(1,correo); //se sustituye "?" por el correo
            ps.setString(2, pass); // se sustituye "?" por la pass
            ResultSet resultSet=ps.executeQuery(); //se ejecuta la consulta
            if(resultSet.next()){ //validacion que la consulta arroje un resultado
                return true;
            }
            return false;
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Override
    public List<Usuario> findAll() throws Exception {
        return List.of();
    }

    @Override
    public Usuario getById(int id) throws Exception {
        return null;
    }

    @Override
    public void create(Usuario user) throws Exception {

    }

    @Override
    public void update(Usuario user) throws Exception {

    }

    @Override
    public void delete(Usuario user) throws Exception {

    }
}

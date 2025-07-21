package impl;

import Dao.IUsuarioDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class UsuarioDaoImpl implements IUsuarioDao {

    @Override
    public Usuario login(String correo, String pass) throws Exception {
        String sql= "SELECT ID_USUARIO,NOMBRE, " +
                "APELLIDOS, CORREO_INSTITUCIONAL,DOMICILIO, LADA, TELEFONO, FECHA_NACIMIENTO" +
                "EDAD, ROL, MATRICULA, PASSWORD FROM USUARIO WHERE CORREO_INSTITUCIONAL=? and PASSWORD=?";
        try {
            Connection con = ConnectionBD.getConnection(); // se estable la conexion
            PreparedStatement ps =  con.prepareStatement(sql); //se prepara la consulta para evitar la inyecion de SQL
            ps.setString(1,correo); //se sustituye "?" por el correo
            ps.setString(2, pass); // se sustituye "?" por la pass
            ResultSet resultSet=ps.executeQuery(); //se ejecuta la consulta
            if(resultSet.next()){
                Usuario usuario = new Usuario();//validacion que la consulta arroje un resultado
                usuario.setNombre(resultSet.getString("NOMBRE"));
                usuario.setApellidos(resultSet.getString("APELLIDOS"));
                usuario.setCorreo(resultSet.getString("CORREO_INSTITUCIONAL"));
                usuario.setDireccion(resultSet.getString("DOMICILIO"));
                usuario.setLada(resultSet.getString("LADA"));
                usuario.setTelefono(resultSet.getString("TELEFONO"));
                usuario.setDate(LocalDate.parse(resultSet.getDate("FECHA_NACIMIENTO").toString()));
                usuario.setEdad(resultSet.getInt("EDAD"));
                usuario.setRol(resultSet.getString("ROL"));
                usuario.setMatricula(resultSet.getString("MATRICULA"));
                usuario.setPassword(resultSet.getString("PASSWORD"));

                return usuario;
            }
            return null;
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
        String sql="INSERT INTO USUARIO (NOMBRE, APELLIDOS, CORREO_INSTITUCIONAL, DOMICILIO," +
                "LADA, TELEFONO, FECHA_NACIMIENTO, EDAD, ROL, MATRICULA, PASSWORD) " +
                "VALUES (?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?)";
        try {
            Connection con= ConnectionBD.getConnection(); //Se establece la conexion
            PreparedStatement ps=con.prepareStatement(sql); // se prepara la consulta para evitar inyeccion sql

            ps.setString(1,user.getNombre());
            ps.setString(2,user.getApellidos());
            ps.setString(3,user.getCorreo());
            ps.setString(4,user.getDireccion());
            ps.setString(5,user.getLada());
            ps.setString(6,user.getTelefono());
            ps.setString(7,user.getDate().toString());
            ps.setInt(8,user.getEdad());
            ps.setString(9,user.getRol());
            ps.setString(10, user.getMatricula());
            ps.setString(11,user.getPassword());
            ps.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Usuario user) throws Exception {

    }

    @Override
    public void delete(Usuario user) throws Exception {

    }
}

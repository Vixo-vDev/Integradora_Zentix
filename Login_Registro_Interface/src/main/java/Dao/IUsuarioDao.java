package Dao;

import com.example.netrixapp.Modelos.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface IUsuarioDao {

    //Método para login
    Usuario login(String correo, String pass) throws Exception;

    List<Usuario> findAll() throws Exception;
    Usuario getById(int id) throws Exception;
    void create(Usuario user) throws Exception;
    void update(Usuario user, int id) throws Exception;
    void delete(Usuario user) throws Exception;
    int totalUsuarios() throws Exception;

}

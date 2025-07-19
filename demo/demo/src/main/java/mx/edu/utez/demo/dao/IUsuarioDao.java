package mx.edu.utez.demo.dao;

public interface IUsuarioDao {

    public boolean login(String correo, String pass) throws Exception;
}

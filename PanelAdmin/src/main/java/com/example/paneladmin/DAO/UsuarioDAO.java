package com.example.paneladmin.DAO;

import com.example.paneladmin.Modelo.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void crearUsuario(Usuario usuario);
    Usuario obtenerPorId(int id);
    Usuario obtenerPorUsername(String username);
    List<Usuario> obtenerTodos();
    List<Usuario> obtenerPorRol(String rol);
    void actualizarUsuario(Usuario usuario);
    void eliminarUsuario(int id);
    void desactivarUsuario(int id);
    void activarUsuario(int id);
}
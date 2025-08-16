package com.example.paneladmin.DAO;

import com.example.paneladmin.Modelo.Usuario;

import java.util.List;

public interface UsuarioDAO {
    // Métodos comunes a ambas interfaces
    void crearUsuario(Usuario usuario);
    Usuario obtenerPorId(int id);
    Usuario obtenerPorUsername(String username);
    List<Usuario> obtenerTodos();
    List<Usuario> obtenerPorRol(String rol);
    void actualizarUsuario(Usuario usuario);
    void eliminarUsuario(int id);
    void desactivarUsuario(int id);
    void activarUsuario(int id);

    // Métodos adicionales del segundo DAO
    List<Usuario> obtenerPorEstado(String estado);
    void cambiarPassword(int id, String nuevaPassword);
}
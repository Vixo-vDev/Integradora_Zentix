package com.example.paneladmin.DAO.Impl;

import com.example.paneladmin.DAO.UsuarioDAO;
import com.example.paneladmin.Modelo.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final List<Usuario> usuarios = new ArrayList<>();
    private int nextId = 1;

    public UsuarioDAOImpl() {
        // Datos de ejemplo
        crearUsuario(new Usuario("admin", "admin123", "Administrador", "admin@example.com", "Administrador"));
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        usuario.setId(nextId++);
        usuarios.add(usuario);
    }

    @Override
    public Usuario obtenerPorId(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Usuario obtenerPorUsername(String username) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public List<Usuario> obtenerPorRol(String rol) {
        return usuarios.stream()
                .filter(u -> u.getRol().equalsIgnoreCase(rol))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        Usuario existente = obtenerPorId(usuario.getId());
        if (existente != null) {
            existente.setUsername(usuario.getUsername());
            existente.setNombre(usuario.getNombre());
            existente.setEmail(usuario.getEmail());
            existente.setRol(usuario.getRol());
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                existente.setPassword(usuario.getPassword());
            }
        }
    }

    @Override
    public void eliminarUsuario(int id) {
        usuarios.removeIf(u -> u.getId() == id);
    }

    @Override
    public void desactivarUsuario(int id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.setActivo(false);
        }
    }

    @Override
    public void activarUsuario(int id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.setActivo(true);
        }
    }
}
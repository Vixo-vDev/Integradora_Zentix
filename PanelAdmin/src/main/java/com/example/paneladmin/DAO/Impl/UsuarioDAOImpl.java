package com.example.paneladmin.DAO.Impl;

import com.example.paneladmin.DAO.UsuarioDAO;
import com.example.paneladmin.Modelo.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final List<Usuario> usuarios = new ArrayList<>();
    private int nextId = 1;

    public UsuarioDAOImpl() {
        // Datos de ejemplo combinados
        crearUsuario(new Usuario("Admin", "admin", "admin@example.com", "admin123", "Administrador"));
        crearUsuario(new Usuario("Juan Pérez", "jperez", "jperez@example.com", "password123", "Usuario"));
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        usuario.setId(nextId++);
        usuario.setEstado("Activo"); // Estado por defecto
        usuario.setActivo(true);     // Activo por defecto
        usuario.setUltimoAcceso(LocalDateTime.now()); // Fecha actual
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
                .filter(u -> u.getNombreUsuario() != null && u.getNombreUsuario().equalsIgnoreCase(username))
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
                .filter(u -> u.getRol() != null && u.getRol().equalsIgnoreCase(rol))
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> obtenerPorEstado(String estado) {
        return usuarios.stream()
                .filter(u -> u.getEstado() != null && u.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        Usuario existente = obtenerPorId(usuario.getId());
        if (existente != null) {
            // Actualización de campos comunes
            existente.setNombreCompleto(usuario.getNombreCompleto());
            existente.setNombreUsuario(usuario.getNombreUsuario());
            existente.setEmail(usuario.getEmail());
            existente.setRol(usuario.getRol());

            // Campos específicos de la versión nueva
            if (usuario.getEstado() != null) {
                existente.setEstado(usuario.getEstado());
            }

            // Actualización de contraseña si se proporciona
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                existente.setPassword(usuario.getPassword());
            }

            // Actualizar último acceso
            existente.setUltimoAcceso(LocalDateTime.now());
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
            usuario.setEstado("Inactivo");
            usuario.setActivo(false);
            usuario.setUltimoAcceso(LocalDateTime.now());
        }
    }

    @Override
    public void activarUsuario(int id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.setEstado("Activo");
            usuario.setActivo(true);
            usuario.setUltimoAcceso(LocalDateTime.now());
        }
    }

    @Override
    public void cambiarPassword(int id, String nuevaPassword) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.setPassword(nuevaPassword);
            usuario.setUltimoAcceso(LocalDateTime.now());
        }
    }
}
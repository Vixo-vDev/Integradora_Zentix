package com.example.paneladmin.DAO.Impl;

import com.example.paneladmin.DAO.NotificacionDAO;
import com.example.paneladmin.Modelo.Notificacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificacionDAOImpl implements NotificacionDAO {
    private final List<Notificacion> notificaciones = new ArrayList<>();
    private int nextId = 1;

    public NotificacionDAOImpl() {
        // Datos de ejemplo
        crearNotificacion(new Notificacion("Nuevo artículo disponible",
                "Se ha agregado un nuevo artículo al inventario", "sistema", false));
        crearNotificacion(new Notificacion("Solicitud aprobada",
                "Su solicitud de equipo ha sido aprobada", "usuario", true));
    }

    @Override
    public void crearNotificacion(Notificacion notificacion) {
        notificacion.setId(nextId++);
        notificaciones.add(0, notificacion); // Agregar al inicio para las más recientes primero
    }

    @Override
    public void actualizarNotificacion(Notificacion notificacion) {
        notificaciones.stream()
                .filter(n -> n.getId() == notificacion.getId())
                .findFirst()
                .ifPresent(n -> {
                    n.setTitulo(notificacion.getTitulo());
                    n.setMensaje(notificacion.getMensaje());
                    n.setLeida(notificacion.isLeida());
                    n.setImportante(notificacion.isImportante());
                    n.setTipo(notificacion.getTipo());
                });
    }

    @Override
    public void eliminarNotificacion(int id) {
        notificaciones.removeIf(n -> n.getId() == id);
    }

    @Override
    public List<Notificacion> obtenerTodas() {
        return new ArrayList<>(notificaciones);
    }

    @Override
    public List<Notificacion> obtenerNoLeidas() {
        return notificaciones.stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
    }

    @Override
    public List<Notificacion> obtenerImportantes() {
        return notificaciones.stream()
                .filter(Notificacion::isImportante)
                .collect(Collectors.toList());
    }

    @Override
    public void marcarComoLeida(int id) {
        notificaciones.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .ifPresent(n -> n.setLeida(true));
    }

    @Override
    public void marcarTodasComoLeidas() {
        notificaciones.forEach(n -> n.setLeida(true));
    }

    @Override
    public void eliminarTodas() {
        notificaciones.clear();
    }
}
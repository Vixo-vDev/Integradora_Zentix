package com.example.paneladmin.DAO.Impl;

import com.example.paneladmin.DAO.ActividadDAO;
import com.example.paneladmin.Modelo.Actividad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActividadDAOImpl implements ActividadDAO {
    private final List<Actividad> actividades = new ArrayList<>();
    private int nextId = 1;

    public ActividadDAOImpl() {
        // Datos de ejemplo
        crearActividad(new Actividad("admin", "Inicio de sesión", LocalDateTime.now(), "Desde IP 192.168.1.100"));
    }

    @Override
    public void crearActividad(Actividad actividad) {
        actividad.setId(nextId++);
        actividad.setFechaHora(LocalDateTime.now());
        actividades.add(0, actividad); // Insertar al inicio para mantener orden reciente
    }

    @Override
    public Actividad obtenerPorId(int id) {
        return actividades.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Actividad> obtenerTodas() {
        return new ArrayList<>(actividades);
    }

    @Override
    public List<Actividad> obtenerPorUsuario(String usuario) {
        return actividades.stream()
                .filter(a -> a.getUsuario().equalsIgnoreCase(usuario))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarActividad(Actividad actividad) {
        Actividad existente = obtenerPorId(actividad.getId());
        if (existente != null) {
            existente.setUsuario(actividad.getUsuario());
            existente.setAccion(actividad.getAccion());
            existente.setDetalles(actividad.getDetalles());
        }
    }

    @Override
    public void eliminarActividad(int id) {
        actividades.removeIf(a -> a.getId() == id);
    }

    @Override
    public List<Actividad> obtenerRecientes(int limite) {
        return actividades.stream()
                .limit(limite)
                .collect(Collectors.toList());
    }
}
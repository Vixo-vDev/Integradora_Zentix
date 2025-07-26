package com.example.paneladmin.DAO.Impl;

import com.example.paneladmin.DAO.EstadisticaDAO;
import com.example.paneladmin.Modelo.Estadistica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EstadisticaDAOImpl implements EstadisticaDAO {
    private final List<Estadistica> estadisticas = new ArrayList<>();
    private int nextId = 1;

    public EstadisticaDAOImpl() {
        // Datos de ejemplo
        crearEstadistica(new Estadistica("Usuarios", "Registrados", "142", "↑ 12.5%"));
        crearEstadistica(new Estadistica("Usuarios", "Activos", "128 (90%)", "↑ 8.2%"));
        crearEstadistica(new Estadistica("Solicitudes", "Equipos", "56", "2.3 días"));
        crearEstadistica(new Estadistica("Inventario", "Laptops", "120/108", "90%"));
    }

    @Override
    public void crearEstadistica(Estadistica estadistica) {
        estadistica.setId(nextId++);
        estadisticas.add(estadistica);
    }

    @Override
    public void actualizarEstadistica(Estadistica estadistica) {
        estadisticas.stream()
                .filter(e -> e.getId() == estadistica.getId())
                .findFirst()
                .ifPresent(e -> {
                    e.setCategoria(estadistica.getCategoria());
                    e.setMetrica(estadistica.getMetrica());
                    e.setValor(estadistica.getValor());
                    e.setTendencia(estadistica.getTendencia());
                });
    }

    @Override
    public void eliminarEstadistica(int id) {
        estadisticas.removeIf(e -> e.getId() == id);
    }

    @Override
    public List<Estadistica> obtenerTodas() {
        return new ArrayList<>(estadisticas);
    }

    @Override
    public List<Estadistica> obtenerPorCategoria(String categoria) {
        return estadisticas.stream()
                .filter(e -> e.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    @Override
    public List<Estadistica> obtenerEstadisticasRecientes(int limite) {
        return estadisticas.stream()
                .limit(limite)
                .collect(Collectors.toList());
    }
}
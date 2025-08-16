package com.example.paneladmin.DAO;

import com.example.paneladmin.Modelo.Estadistica;

import java.util.List;

public interface EstadisticaDAO {
    void crearEstadistica(Estadistica estadistica);
    void actualizarEstadistica(Estadistica estadistica);
    void eliminarEstadistica(int id);
    List<Estadistica> obtenerTodas();
    List<Estadistica> obtenerPorCategoria(String categoria);
    List<Estadistica> obtenerEstadisticasRecientes(int limite);
}
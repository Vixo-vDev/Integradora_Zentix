package com.example.paneladmin.DAO;

import com.example.paneladmin.Modelo.Actividad;
import java.util.List;

public interface ActividadDAO {
    void crearActividad(Actividad actividad);
    Actividad obtenerPorId(int id);
    List<Actividad> obtenerTodas();
    List<Actividad> obtenerPorUsuario(String usuario);
    void actualizarActividad(Actividad actividad);
    void eliminarActividad(int id);
    List<Actividad> obtenerRecientes(int limite);
}
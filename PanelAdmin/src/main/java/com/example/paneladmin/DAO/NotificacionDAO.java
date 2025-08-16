package com.example.paneladmin.DAO;

import com.example.paneladmin.Modelo.Notificacion;

import java.util.List;

public interface NotificacionDAO {
    void crearNotificacion(Notificacion notificacion);
    void actualizarNotificacion(Notificacion notificacion);
    void eliminarNotificacion(int id);
    List<Notificacion> obtenerTodas();
    List<Notificacion> obtenerNoLeidas();
    List<Notificacion> obtenerImportantes();
    void marcarComoLeida(int id);
    void marcarTodasComoLeidas();
    void eliminarTodas();
}
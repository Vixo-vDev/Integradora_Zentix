package com.example.netrixapp.Modelos;

import java.time.LocalDate;

public class DetalleSolicitud {
    private int id_detalle;
    private int id_equipo;
    private int id_tipo_equipo;
    private int id_solicitud;
    private int cantidad;

    public DetalleSolicitud(int idEquipo, int idTipoEquipo, int idSolicitud, int cantidadSolicitud) {
        this.id_detalle = idEquipo;
        this.id_equipo = idEquipo;
        this.id_tipo_equipo = idTipoEquipo;
        this.id_solicitud = idSolicitud;
        this.cantidad = cantidadSolicitud;
    }

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    public int getId_tipo_equipo() {
        return id_tipo_equipo;
    }

    public void setId_tipo_equipo(int id_tipo_equipo) {
        this.id_tipo_equipo = id_tipo_equipo;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

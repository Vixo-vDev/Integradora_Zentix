package com.example.paneladmin.Modelo;

import java.time.LocalDateTime;

public class Actividad {
    private int id;
    private String usuario;
    private String accion;
    private LocalDateTime fechaHora;
    private String detalles;

    public Actividad() {
    }

    public Actividad(String usuario, String accion, LocalDateTime fechaHora, String detalles) {
        this.usuario = usuario;
        this.accion = accion;
        this.fechaHora = fechaHora;
        this.detalles = detalles;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", accion='" + accion + '\'' +
                ", fechaHora=" + fechaHora +
                ", detalles='" + detalles + '\'' +
                '}';
    }
}
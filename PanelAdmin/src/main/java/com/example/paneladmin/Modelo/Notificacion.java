package com.example.paneladmin.Modelo;

import java.time.LocalDateTime;

public class Notificacion {
    private int id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fecha;
    private boolean leida;
    private boolean importante;
    private String tipo; // "sistema", "usuario", "alerta"

    public Notificacion() {}

    public Notificacion(String titulo, String mensaje, String tipo, boolean importante) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.importante = importante;
        this.fecha = LocalDateTime.now();
        this.leida = false;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
    public boolean isImportante() { return importante; }
    public void setImportante(boolean importante) { this.importante = importante; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return titulo + " - " + fecha.toString();
    }
}
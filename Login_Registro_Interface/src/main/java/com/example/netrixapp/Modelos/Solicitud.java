package com.example.netrixapp.Modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Solicitud {

    private int id_usuario;
    private int id_solicitud;
    private LocalDate fecha_solicitud;
    private String articulo;
    private int cantidad;
    private LocalDate fecha_recibo;
    private String tiempo_uso;
    private String razon;
    private String estado;
    private String nombreUsuario;

    public Solicitud() {

    }

    public Solicitud(int id_usuario, LocalDate fecha_solicitud, String articulo, int cantidad, LocalDate fecha_recibo, String tiempo_uso, String razon, String estado) {
        this.id_usuario = id_usuario;
        this.fecha_solicitud = fecha_solicitud;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fecha_recibo = fecha_recibo;
        this.tiempo_uso = tiempo_uso;
        this.razon = razon;
        this.estado = estado;
    }

    public Solicitud(String articulo, LocalDate fecha_solicitud, int cantidad, String razon, String tiempo_uso) {
        this.articulo = articulo;
        this.fecha_solicitud = fecha_solicitud;
        this.cantidad = cantidad;
        this.razon = razon;
        this.tiempo_uso = tiempo_uso;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solitictud) {
        this.id_solicitud = id_solitictud;
    }

    public LocalDate getFecha_solicitud() {
        return fecha_solicitud;
    }

    public void setFecha_solicitud(LocalDate fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha_recibo() {
        return fecha_recibo;
    }

    public void setFecha_recibo(LocalDate fecha_recibo) {
        this.fecha_recibo = fecha_recibo;
    }

    public String getTiempo_uso() {
        return tiempo_uso;
    }

    public void setTiempo_uso(String tiempo_uso) {
        this.tiempo_uso = tiempo_uso;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsario) {
        this.nombreUsuario = nombreUsario;
    }
}

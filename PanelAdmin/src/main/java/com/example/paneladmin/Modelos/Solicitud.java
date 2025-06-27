package com.example.paneladmin.Modelos;

public class Solicitud {

    private String id;
    private String nombre;
    private String rol;
    private String articulo;
    private String estado;
    private String fecha;

    public Solicitud(String id, String nombre, String rol, String articulo, String estado, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.articulo = articulo;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

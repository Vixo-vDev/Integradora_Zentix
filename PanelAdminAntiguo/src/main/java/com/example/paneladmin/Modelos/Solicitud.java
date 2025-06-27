package com.example.paneladmin.Modelos;

public class Solicitud {

    private String id;
    private String tipo;
    private String usuario;
    private String fecha;

    public Solicitud(String id, String tipo, String usuario, String fecha) {
        this.id = id;
        this.tipo = tipo;
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

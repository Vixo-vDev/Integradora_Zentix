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
}

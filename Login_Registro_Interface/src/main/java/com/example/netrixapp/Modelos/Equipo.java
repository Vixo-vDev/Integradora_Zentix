package com.example.netrixapp.Modelos;

public class Equipo {
    private int id_equipo;
    private String codigo_bien;
    private String descripcion;
    private String marca;
    private String modelo;
    private String numero_serie;
    private int disponible;
    private int tipo_equipo;

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo_bien() {
        return codigo_bien;
    }

    public void setCodigo_bien(String codigo_bien) {
        this.codigo_bien = codigo_bien;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumero_serie() {
        return numero_serie;
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie = numero_serie;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public int getTipo_equipo() {
        return tipo_equipo;
    }

    public void setTipo_equipo(int tipo_equipo) {
        this.tipo_equipo = tipo_equipo;
    }
}

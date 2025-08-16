package com.example.paneladmin.Modelo;

import java.time.LocalDate;

public class Estadistica {
    private int id;
    private String categoria;
    private String metrica;
    private String valor;
    private String tendencia;
    private LocalDate fechaRegistro;

    public Estadistica() {}

    public Estadistica(String categoria, String metrica, String valor, String tendencia) {
        this.categoria = categoria;
        this.metrica = metrica;
        this.valor = valor;
        this.tendencia = tendencia;
        this.fechaRegistro = LocalDate.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getMetrica() { return metrica; }
    public void setMetrica(String metrica) { this.metrica = metrica; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    public String getTendencia() { return tendencia; }
    public void setTendencia(String tendencia) { this.tendencia = tendencia; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
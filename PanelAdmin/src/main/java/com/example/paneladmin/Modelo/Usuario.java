package com.example.paneladmin.Modelo;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String nombreCompleto;  // Combinación de nombre (primer código) y nombreCompleto (segundo código)
    private String nombreUsuario;   // Equivalente a username en el primer código
    private String email;
    private String password;
    private String rol;
    private String estado;          // Nuevo campo del segundo código
    private LocalDateTime ultimoAcceso; // Nuevo campo del segundo código
    private boolean activo;

    public Usuario() {
    }

    // Constructor con los campos básicos (combinación de ambos constructores)
    public Usuario(String nombreCompleto, String nombreUsuario, String email, String password, String rol) {
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = "Activo";     // Valor por defecto del segundo código
        this.ultimoAcceso = LocalDateTime.now(); // Inicializado al momento actual
        this.activo = true;         // Valor por defecto de ambos códigos
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    // Método alternativo para compatibilidad con el primer código
    public String getNombre() {
        return nombreCompleto;
    }

    public void setNombre(String nombre) {
        this.nombreCompleto = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Método alternativo para compatibilidad con el primer código
    public String getUsername() {
        return nombreUsuario;
    }

    public void setUsername(String username) {
        this.nombreUsuario = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", estado='" + estado + '\'' +
                ", ultimoAcceso=" + ultimoAcceso +
                ", activo=" + activo +
                '}';
    }
}
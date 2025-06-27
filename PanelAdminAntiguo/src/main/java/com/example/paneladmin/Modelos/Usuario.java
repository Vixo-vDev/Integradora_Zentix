package com.example.paneladmin.Modelos;

public class Usuario {

    private String nombreUsuario;
    private String rol;
    private String correo;

    public Usuario(String nombreUsuario, String rol, String correo) {
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}

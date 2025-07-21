package com.example.netrixapp.Modelos;

import java.time.LocalDate;

public class Usuario {
    //Aplicando la abtración y encapsulamiento

    //Atributos
    private int id_usuario;
    private String nombre;
    private String apellidos;
    private String correo;
    private String calle;
    private String lada;
    private String telefono;
    private LocalDate date;
    private int edad;
    private String rol;
    private String matricula;
    private String password;

    //Constructor vacío
    public Usuario(){}

    //Constructor con parámetros
    public Usuario(String nombre, String apellidos, String correo, String calle, String lada, String telefono, LocalDate date, int edad, String rol, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.calle = calle;
        this.lada = lada;
        this.telefono = telefono;
        this.date = date;
        this.edad = edad;
        this.rol = rol;
        this.password = password;
    }

    public int getId_usuario() {return id_usuario;}

    public void setId_usuario(int id_usuario) {this.id_usuario = id_usuario;}

    //Getters & Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getLada() {
        return lada;
    }

    public void setLada(String lada) {
        this.lada = lada;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String matricula() {return matricula;}
    public void setMatricula(String matricula) {this.matricula = matricula;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

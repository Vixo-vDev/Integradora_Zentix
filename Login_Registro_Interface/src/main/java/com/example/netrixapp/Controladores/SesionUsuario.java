package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Usuario;

public class SesionUsuario {

    private static Usuario usuarioActual;

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}

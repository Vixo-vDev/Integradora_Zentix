package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Usuario;

public class ControladorRegistro {

    public boolean registro(Usuario usuario, String confirmPassword) {
        if(usuario.getNombre().isEmpty() ||
                usuario.getApellidos().isEmpty() ||
                usuario.getCorreo().isEmpty() ||
                usuario.getLada().isEmpty() ||
                usuario.getTelefono().isEmpty() ||
                usuario.getDate() == null ||
                usuario.getRol().isEmpty() ||
                usuario.getPassword().isEmpty() ||
                confirmPassword.isEmpty()){

            return false;
        }

        if(!usuario.getPassword().equals(confirmPassword)){
            return false;
        }
        return true;
    }

}

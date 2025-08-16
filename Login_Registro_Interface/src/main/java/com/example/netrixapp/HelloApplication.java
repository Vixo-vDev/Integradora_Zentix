package com.example.netrixapp;

import com.example.netrixapp.Controladores.SesionUsuario;
import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.VistaPrincipal;
import javafx.application.Application;
import javafx.stage.Stage;


public class HelloApplication extends Application {
    @Override
    public void start(Stage escenario) {
        Usuario usuario = SesionUsuario.getUsuarioActual();
        System.out.println("Usuario actual: " + usuario.getNombre());
        System.out.println("id:"+ usuario.getId_usuario());
        VistaPrincipal vistaPrincipal = new VistaPrincipal();
        vistaPrincipal.mostrar(escenario);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
package com.example.paneladmin.Controladores;

import com.example.paneladmin.Vistas.*;
import javafx.scene.layout.Region;

public class ControladorMain {
    private ControladorPrincipal controladorPrincipal;
    private VistaPrincipal vistaPrincipal;

    public ControladorMain(ControladorPrincipal controladorPrincipal, VistaPrincipal vistaPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
        this.vistaPrincipal = vistaPrincipal;
        configurarEventos();
        mostrarVistaMain();
    }

    private void configurarEventos() {
        // Configurar eventos de los botones de la barra lateral
        vistaPrincipal.getBoton("Inventario").setOnAction(e -> mostrarVistaInventario());
        vistaPrincipal.getBoton("Usuarios").setOnAction(e -> mostrarVistaUsuarios());
        vistaPrincipal.getBoton("Estadísticas").setOnAction(e -> mostrarVistaEstadisticas());
        vistaPrincipal.getBoton("Formularios").setOnAction(e -> mostrarVistaFormularios());
    }

    private void mostrarVistaMain() {
        VistaMain vistaMain = new VistaMain();
        controladorPrincipal.cambiarContenido(vistaMain.getVista());
    }

    private void mostrarVistaInventario() {
        VistaInventario vistaInventario = new VistaInventario();
        controladorPrincipal.cambiarContenido(vistaInventario.getVista());
    }

    private void mostrarVistaUsuarios() {
        VistaUsuarios vistaUsuarios = new VistaUsuarios();
        controladorPrincipal.cambiarContenido(vistaUsuarios.getVista());
    }

    private void mostrarVistaEstadisticas() {
        VistaEstadisticas vistaEstadisticas = new VistaEstadisticas();
        controladorPrincipal.cambiarContenido(vistaEstadisticas.getVista());
    }

    private void mostrarVistaFormularios() {
        VistaFormularios vistaFormularios = new VistaFormularios();
        controladorPrincipal.cambiarContenido(vistaFormularios.getVista());
    }
}
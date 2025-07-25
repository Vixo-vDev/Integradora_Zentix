package com.example.netrixapp.Controladores;

import com.example.netrixapp.Vistas.VistaSolicitudes;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class ControladorSolicitudes {
    private VistaSolicitudes vista;

    //Declaracion de atributos
    String nota = vista.getNota();
    LocalDate fecha = vista.getFecha();
    String tiempoUso = vista.getTiempoUso();
    String equipos = vista.getEquipos();
    int cantidad = vista.getCantidad();

    public ControladorSolicitudes(VistaSolicitudes vista) {

        this.vista = vista;
        vista.getBtnAgregar().setOnAction(e -> agregarArticulo());
        vista.getBtnEnviar().setOnAction(e -> enviarSolicitud());
    }

    public void verificarCampos(){

    }

    public void agregarArticulo() {

    }

    public void enviarSolicitud() {

    }
}
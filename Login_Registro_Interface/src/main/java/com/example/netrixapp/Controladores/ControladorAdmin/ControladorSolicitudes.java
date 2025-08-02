package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistasAdmin.VistaSolicitudes;
import impl.SolicitudDaoImpl;
import impl.UsuarioDaoImpl;

import java.util.List;

public class ControladorSolicitudes {
    private VistaSolicitudes vista;
    private final SolicitudDaoImpl solicitudDao;

    public ControladorSolicitudes(VistaSolicitudes vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();
        cargarSolicitudes();
    }

    public void cargarSolicitudes() {
        try {
            List<Solicitud> listaSolicitudes = solicitudDao.findAllAdmin();
            vista.mostrarSolicitudes(listaSolicitudes);
        } catch (Exception e) {
            e.printStackTrace();
       }
    }
}
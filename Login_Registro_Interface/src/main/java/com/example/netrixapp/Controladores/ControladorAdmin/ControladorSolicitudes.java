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

    public void actualizarEstadoSolicitud(int idSolicitud, String nuevoEstado) {
        try {
            System.out.println(idSolicitud);
            solicitudDao.actualizarEstado(idSolicitud, nuevoEstado);
            cargarSolicitudes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filtrarPorEstado(String estado) {
        try {
            List<Solicitud> solicitudesFiltradas;
            if (estado.equals("Todas")) {
                solicitudesFiltradas = solicitudDao.findAllAdmin();
            } else {
                solicitudesFiltradas = solicitudDao.findByEstado(estado);
            }
            vista.mostrarSolicitudes(solicitudesFiltradas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistasAdmin.VistaSolicitudes;
import impl.DetalleSolicitudDaoImpl;
import impl.EquipoDaoImpl;
import impl.SolicitudDaoImpl;
import impl.UsuarioDaoImpl;

import java.util.List;

public class ControladorSolicitudes {
    private VistaSolicitudes vista;
    private final SolicitudDaoImpl solicitudDao;
    private final EquipoDaoImpl equipoDao;
    private final DetalleSolicitudDaoImpl detalleSolicitudDao;

    public ControladorSolicitudes(VistaSolicitudes vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();
        this.equipoDao = new EquipoDaoImpl();
        this.detalleSolicitudDao = new DetalleSolicitudDaoImpl();
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

    public void actualizarEstadoSolicitud(Solicitud solicitud, String nuevoEstado) {
        try {
            System.out.println(solicitud.getId_solicitud());

            solicitudDao.actualizarEstado(solicitud.getId_solicitud(), nuevoEstado);
            int id  = solicitud.getId_solicitud();
            System.out.println("El id de la solicitud es: "+id);
            int id_equipo = detalleSolicitudDao.searchIdequip(solicitud);
            System.out.println("El id del equipo: "+ id_equipo);
            equipoDao.setUso(id_equipo, nuevoEstado);
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
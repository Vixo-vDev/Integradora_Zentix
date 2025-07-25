package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistaHistorial;
import impl.SolicitudDaoImpl;

import java.util.List;

public class ControladorHistorial {
    private VistaHistorial vista;
    private final SolicitudDaoImpl solicitudDao;

    public ControladorHistorial(VistaHistorial vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();
        cargarHistorial();

    }

    private void cargarHistorial() {
        try {
            List<Solicitud> listaHistorial = solicitudDao.totalSolicitudes();
            vista.mostrarEquipos(listaEquipos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

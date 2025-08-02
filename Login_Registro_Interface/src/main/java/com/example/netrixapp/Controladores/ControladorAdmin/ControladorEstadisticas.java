package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Vistas.VistasAdmin.VistaEstadisticas;
import impl.SolicitudDaoImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ControladorEstadisticas {

    private final VistaEstadisticas vista;
    private final SolicitudDaoImpl solicitudDao;

    public ControladorEstadisticas(VistaEstadisticas vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();

        // Por defecto, mostrar la semana actual
        cargarDatosEstadisticos("semana");
    }

    public void cargarDatosEstadisticos(String periodo) {
        try {
            Date fechaFin = new Date(); // hoy
            Date fechaInicio = calcularFechaInicio(periodo);

            List<Object[]> masSolicitados = solicitudDao.findEquiposMasSolicitados(fechaInicio, fechaFin);
            List<Object[]> menosSolicitados = solicitudDao.findEquiposMenosSolicitados(fechaInicio, fechaFin);

            vista.mostrarEquiposMasSolicitados(masSolicitados);
            vista.mostrarEquiposMenosSolicitados(menosSolicitados);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date calcularFechaInicio(String periodo) {
        Calendar cal = Calendar.getInstance();
        switch (periodo.toLowerCase()) {
            case "semana":
                cal.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case "mes":
                cal.add(Calendar.MONTH, -1);
                break;
            default:
                cal.add(Calendar.DAY_OF_MONTH, -7); // predeterminado: semana
        }
        return cal.getTime();
    }
}

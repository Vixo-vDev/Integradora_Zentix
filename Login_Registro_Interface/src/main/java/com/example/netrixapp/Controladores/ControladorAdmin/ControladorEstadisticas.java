package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Vistas.VistasAdmin.VistaEstadisticas;
import impl.SolicitudDaoImpl;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class ControladorEstadisticas {

    private final VistaEstadisticas vista;
    private final SolicitudDaoImpl solicitudDao;

    public ControladorEstadisticas(VistaEstadisticas vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();
        cargarMasSolicitadosSinFiltro();
    }

    public void cargarMasSolicitadosSinFiltro() {
        try {
            int limite = 10;
            List<Object[]> masSolicitados = solicitudDao.findEquiposMasSolicitadosSinFiltro(limite);
            vista.mostrarEquiposMasSolicitados(masSolicitados);

            // Tabla de menos solicitados vacía inicialmente
            vista.mostrarEquiposMenosSolicitados(List.of());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarDatosEstadisticos(String periodo) {
        try {
            Date fechaFin = new Date(System.currentTimeMillis()); // fecha actual
            Date fechaInicio = calcularFechaInicio(periodo);

            int limite = 10;
            List<Object[]> masSolicitados = solicitudDao.findEquiposMasSolicitados(fechaInicio, fechaFin, limite);
            List<Object[]> menosSolicitados = solicitudDao.findEquiposMenosSolicitados(fechaInicio, fechaFin, limite);

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
                cal.add(Calendar.DAY_OF_MONTH, -7);
        }
        return new Date(cal.getTimeInMillis());
    }
}

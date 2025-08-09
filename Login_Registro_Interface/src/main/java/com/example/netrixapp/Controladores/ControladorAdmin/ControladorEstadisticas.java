package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Vistas.VistasAdmin.VistaEstadisticas;
import impl.SolicitudDaoImpl;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

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
            vista.mostrarGraficoBarras(getDatosParaGrafico(limite));
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

            vista.mostrarEquiposMasSolicitados(masSolicitados);
            vista.mostrarGraficoBarras(
                    masSolicitados.stream()
                            .map(data -> new EquipoChartData(data[0].toString(), Integer.parseInt(data[1].toString())))
                            .collect(Collectors.toList())
            );
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

    // Método para obtener datos formateados para el gráfico
    public List<EquipoChartData> getDatosParaGrafico(int limite) throws Exception {
        List<Object[]> rawData = solicitudDao.findEquiposMasSolicitadosSinFiltro(limite);
        return rawData.stream()
                .map(data -> new EquipoChartData(
                        data[0].toString(), // Nombre del equipo
                        Integer.parseInt(data[1].toString()) // Cantidad de solicitudes
                ))
                .collect(Collectors.toList());
    }

    // Clase interna para manejar los datos del gráfico
    public static class EquipoChartData {
        private final String nombreEquipo;
        private final int cantidad;

        public EquipoChartData(String nombreEquipo, int cantidad) {
            this.nombreEquipo = nombreEquipo;
            this.cantidad = cantidad;
        }

        // Getters
        public String getNombreEquipo() { return nombreEquipo; }
        public int getCantidad() { return cantidad; }
    }
}

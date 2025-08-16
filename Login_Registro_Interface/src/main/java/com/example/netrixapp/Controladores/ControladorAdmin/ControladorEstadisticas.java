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
        System.out.println("ControladorEstadisticas inicializado - cargando datos iniciales...");
        cargarMasSolicitadosSinFiltro();
    }

    public void cargarMasSolicitadosSinFiltro() {
        try {
            System.out.println("=== CARGANDO DATOS SIN FILTRO ===");
            int limite = 10;
            List<Object[]> masSolicitados = solicitudDao.findEquiposMasSolicitadosSinFiltro(limite);
            
            if (masSolicitados.isEmpty()) {
                System.out.println("ADVERTENCIA: No se encontraron datos de solicitudes en la base de datos");
                vista.mostrarMensajeSinDatos();
                return;
            }
            
            System.out.println("Total de equipos encontrados: " + masSolicitados.size());
            vista.mostrarEquiposMasSolicitados(masSolicitados);
            
            List<EquipoChartData> datosGrafico = getDatosParaGrafico(masSolicitados);
            vista.mostrarGraficoBarras(datosGrafico);
            
        } catch (Exception e) {
            System.err.println("ERROR al cargar datos sin filtro: " + e.getMessage());
            e.printStackTrace();
            vista.mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }

    public void cargarDatosEstadisticos(String periodo) {
        try {
            System.out.println("=== CARGANDO DATOS CON FILTRO: " + periodo.toUpperCase() + " ===");
            Date fechaFin = new Date(System.currentTimeMillis());
            Date fechaInicio = calcularFechaInicio(periodo);

            int limite = 10;
            List<Object[]> masSolicitados = solicitudDao.findEquiposMasSolicitados(fechaInicio, fechaFin, limite);
            
            if (masSolicitados.isEmpty()) {
                System.out.println("ADVERTENCIA: No se encontraron datos para el periodo " + periodo);
                vista.mostrarMensajeSinDatos();
                return;
            }
            
            System.out.println("Total de equipos encontrados para " + periodo + ": " + masSolicitados.size());
            vista.mostrarEquiposMasSolicitados(masSolicitados);
            
            List<EquipoChartData> datosGrafico = getDatosParaGrafico(masSolicitados);
            vista.mostrarGraficoBarras(datosGrafico);
            
        } catch (Exception e) {
            System.err.println("ERROR al cargar datos con filtro " + periodo + ": " + e.getMessage());
            e.printStackTrace();
            vista.mostrarError("Error al cargar datos para " + periodo + ": " + e.getMessage());
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

    // Método para obtener datos formateados para el gráfico con porcentajes
    private List<EquipoChartData> getDatosParaGrafico(List<Object[]> rawData) {
        System.out.println("=== PREPARANDO DATOS PARA GRÁFICO ===");
        
        if (rawData.isEmpty()) {
            System.out.println("No hay datos para generar gráfico");
            return List.of();
        }
        
        // Calcular total de solicitudes para porcentajes
        int totalSolicitudes = rawData.stream()
                .mapToInt(data -> Integer.parseInt(data[1].toString()))
                .sum();
        
        System.out.println("Total de solicitudes: " + totalSolicitudes);
        System.out.println("Distribución de porcentajes:");
        
        List<EquipoChartData> datosConPorcentajes = rawData.stream()
                .map(data -> {
                    String nombreEquipo = data[0].toString();
                    int cantidad = Integer.parseInt(data[1].toString());
                    
                    // Calcular porcentaje exacto
                    double porcentaje = totalSolicitudes > 0 ? (double) cantidad / totalSolicitudes * 100 : 0;
                    
                    System.out.println("  " + nombreEquipo + ": " + cantidad + " solicitudes (" + String.format("%.2f", porcentaje) + "%)");
                    
                    return new EquipoChartData(nombreEquipo, cantidad, porcentaje);
                })
                .collect(Collectors.toList());
        
        // Validar que los porcentajes sumen aproximadamente 100%
        double sumaPorcentajes = datosConPorcentajes.stream().mapToDouble(EquipoChartData::getPorcentaje).sum();
        System.out.println("Suma total de porcentajes: " + String.format("%.2f", sumaPorcentajes) + "%");
        
        // Verificar distribución
        if (datosConPorcentajes.size() == 1) {
            System.out.println("✓ 1 dispositivo: 100% distribuido correctamente");
        } else if (datosConPorcentajes.size() == 2) {
            System.out.println("✓ 2 dispositivos: distribución " + String.format("%.1f", datosConPorcentajes.get(0).getPorcentaje()) + "% / " + String.format("%.1f", datosConPorcentajes.get(1).getPorcentaje()) + "%");
        } else {
            System.out.println("✓ " + datosConPorcentajes.size() + " dispositivos: distribución proporcional según cantidad de solicitudes");
        }
        
        return datosConPorcentajes;
    }

    // Clase interna para manejar los datos del gráfico con porcentajes
    public static class EquipoChartData {
        private final String nombreEquipo;
        private final int cantidad;
        private final double porcentaje;

        public EquipoChartData(String nombreEquipo, int cantidad, double porcentaje) {
            this.nombreEquipo = nombreEquipo;
            this.cantidad = cantidad;
            this.porcentaje = porcentaje;
        }

        // Getters
        public String getNombreEquipo() { return nombreEquipo; }
        public int getCantidad() { return cantidad; }
        public double getPorcentaje() { return porcentaje; }
    }
}

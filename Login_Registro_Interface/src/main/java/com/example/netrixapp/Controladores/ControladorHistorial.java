package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistaHistorial;
import impl.SolicitudDaoImpl;
import java.time.LocalDate;
import java.util.List;

public class ControladorHistorial {

    private final VistaHistorial vista;
    private final SolicitudDaoImpl solicitudDao;
    private final int id_usuario;

    public ControladorHistorial(VistaHistorial vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();
        this.id_usuario = SesionUsuario.getUsuarioActual().getId_usuario();

        inicializarEventos();
        cargarHistorial(); // carga inicial sin filtros
    }

    private void inicializarEventos() {
        vista.getBtnAplicar().setOnAction(e -> aplicarFiltros());
        vista.getBtnLimpiar().setOnAction(e -> cargarHistorial());
        vista.limpiarFiltros();
        cargarHistorial();
    }

    private void cargarHistorial() {
        try {
            List<Solicitud> lista = solicitudDao.findAll(id_usuario);
            
            // Depuración: mostrar información de las solicitudes
            System.out.println("=== DEPURACIÓN: Datos obtenidos del historial ===");
            for (Solicitud s : lista) {
                System.out.println("Solicitud ID: " + s.getId_solicitud());
                System.out.println("  - Artículo: " + s.getArticulo());
                System.out.println("  - Cantidad: " + s.getCantidad());
                System.out.println("  - Fecha Solicitud: " + s.getFecha_solicitud());
                System.out.println("  - Fecha Recibo: " + s.getFecha_recibo());
                System.out.println("  - Tiempo Uso: " + s.getTiempo_uso());
                System.out.println("  - Razón: " + s.getRazon());
                System.out.println("  - Estado: " + s.getEstado());
                System.out.println("---");
            }
            
            vista.mostrarHistorial(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void aplicarFiltros() {
        try {
            String estado = vista.getComboEstado();
            LocalDate desde = vista.getdateInicio() != null ? LocalDate.parse(vista.getdateInicio()) : null;
            LocalDate hasta = vista.getdateFin() != null ? LocalDate.parse(vista.getdateFin()) : null;

            List<Solicitud> lista = solicitudDao.findByFilters(id_usuario, estado, desde, hasta);
            
            // Depuración: mostrar información de las solicitudes filtradas
            System.out.println("=== DEPURACIÓN: Datos filtrados del historial ===");
            for (Solicitud s : lista) {
                System.out.println("Solicitud ID: " + s.getId_solicitud());
                System.out.println("  - Artículo: " + s.getArticulo());
                System.out.println("  - Cantidad: " + s.getCantidad());
                System.out.println("  - Fecha Solicitud: " + s.getFecha_solicitud());
                System.out.println("  - Fecha Recibo: " + s.getFecha_recibo());
                System.out.println("  - Tiempo Uso: " + s.getTiempo_uso());
                System.out.println("  - Razón: " + s.getRazon());
                System.out.println("  - Estado: " + s.getEstado());
                System.out.println("---");
            }
            
            vista.mostrarHistorial(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

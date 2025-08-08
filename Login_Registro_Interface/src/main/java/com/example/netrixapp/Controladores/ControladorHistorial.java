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
            vista.mostrarHistorial(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

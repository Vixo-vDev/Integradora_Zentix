package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistasAdmin.VistaPrestamos;
import impl.DetalleSolicitudDaoImpl;
import impl.EquipoDaoImpl;
import impl.SolicitudDaoImpl;
import impl.UsuarioDaoImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class ControladorPrestamos {
    private VistaPrestamos vista;
    private final SolicitudDaoImpl solicitudDao;
    private final EquipoDaoImpl equipoDao;
    private final DetalleSolicitudDaoImpl detalleSolicitudDao;

    public ControladorPrestamos(VistaPrestamos vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();
        this.equipoDao = new EquipoDaoImpl();
        this.detalleSolicitudDao = new DetalleSolicitudDaoImpl();
        cargarPrestamos();
    }

    public void cargarPrestamos() {
        try {
            List<Solicitud> listaPrestamos = solicitudDao.findUso();
            vista.mostrarSolicitudes(listaPrestamos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarEstadoPrestamo(Solicitud solicitud, String nuevoEstado) {
        // Primero mostrar alerta de confirmación
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar Entrega");
        alertaConfirmacion.setHeaderText("¿Confirmar acción?");
        alertaConfirmacion.setContentText("¿El artículo: " + solicitud.getArticulo() + " ya ha sido devuelto?");

        // Botones personalizados (opcional)
        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alertaConfirmacion.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == btnConfirmar) {
            try {
                System.out.println("ID Solicitud: " + solicitud.getId_solicitud());
                int id_equipo = detalleSolicitudDao.searchIdequip(solicitud);
                System.out.println("ID Equipo: " + id_equipo);

                equipoDao.setUso(id_equipo, "pendiente");
                cargarPrestamos(); // Refrescar la tabla

                // Alert de éxito
                Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
                alertaExito.setTitle("Operación Exitosa");
                alertaExito.setHeaderText(null);
                alertaExito.setContentText("El préstamo ha sido marcado como entregado correctamente.");
                alertaExito.show();

            } catch (Exception e) {
                e.printStackTrace();

                // Alert de error detallado
                Alert alertaError = new Alert(Alert.AlertType.ERROR);
                alertaError.setTitle("Error en la Operación");
                alertaError.setHeaderText("No se pudo completar la operación");
                alertaError.setContentText("Error: " + e.getMessage());
                alertaError.show();
            }
        } else {
            // Opcional: Mensaje si cancela
            System.out.println("Operación cancelada por el usuario");
        }
        cargarPrestamos();
    }
}
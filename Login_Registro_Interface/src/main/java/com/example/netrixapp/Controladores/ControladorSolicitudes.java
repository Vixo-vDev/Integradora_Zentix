package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.DetalleSolicitud;
import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Vistas.VistaSolicitudes;
import impl.DetalleSolicitudDaoImpl;
import impl.SolicitudDaoImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.SpinnerValueFactory;

import java.time.LocalDate;

public class ControladorSolicitudes {

    private final VistaSolicitudes vista;
    private final SolicitudDaoImpl solicitudDao;
    private final DetalleSolicitudDaoImpl detalleSolicitudDao;

    public ControladorSolicitudes(VistaSolicitudes vista) {
        this.vista = vista;
        this.solicitudDao = new SolicitudDaoImpl();
        this.detalleSolicitudDao = new DetalleSolicitudDaoImpl();
        vista.getBtnEnviar().setOnAction(e -> enviarSolicitud());

    }



    private void enviarSolicitud() {
        try {
            int idTipoEquipo = vista.getIdTipoEquipoSeleccionado();
            Equipo equipoSeleccionado = vista.getEquipoSeleccionado();

            if (idTipoEquipo == -1 || equipoSeleccionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setHeaderText("Campos incompletos");
                alerta.setContentText("Por favor selecciona un tipo de equipo y un equipo válido.");
                alerta.showAndWait();
                return;
            }

            int idUsuario = SesionUsuario.getUsuarioActual().getId_usuario();
            LocalDate fechaSolicitud = LocalDate.now();
            String articulo = equipoSeleccionado.getDescripcion();
            int cantidadSolicitud = vista.getCantidad();
            LocalDate fechaRecibo = vista.getFechaRecibo();
            int tiempoUso = vista.getTiempoUso();
            String razon = vista.getNota();

            Solicitud solicitud = new Solicitud(
                    idUsuario,
                    fechaSolicitud,
                    articulo,
                    cantidadSolicitud,
                    fechaRecibo,
                    String.valueOf(tiempoUso),
                    razon,
                    "pendiente"
            );

            System.out.println("Solicitud a insertar:");
            System.out.println("Usuario ID: " + idUsuario);
            System.out.println("Fecha Solicitud: " + fechaSolicitud);
            System.out.println("Articulo: " + articulo);
            System.out.println("Cantidad: " + cantidadSolicitud);
            System.out.println("Fecha Recibo: " + fechaRecibo);
            System.out.println("Tiempo Uso: " + tiempoUso);
            System.out.println("Razon: " + razon);
            System.out.println("Estado: pendiente");

            int id_equipo = equipoSeleccionado.getId_equipo();
            int idSolicitud = solicitudDao.create(solicitud);

            System.out.println("Solicitud creada con ID: " + idSolicitud);

            DetalleSolicitud detalleSolicitud = new DetalleSolicitud(
                    id_equipo,
                    idTipoEquipo,
                    idSolicitud,
                    cantidadSolicitud
            );

            System.out.println("DetalleSolicitud a insertar:");
            System.out.println("ID Equipo: " + detalleSolicitud.getId_equipo());
            System.out.println("ID Tipo Equipo: " + detalleSolicitud.getId_tipo_equipo());
            System.out.println("ID Solicitud: " + detalleSolicitud.getId_solicitud());
            System.out.println("Cantidad: " + detalleSolicitud.getCantidad());

            detalleSolicitudDao.create(detalleSolicitud);
            vista.getTfNota().clear();
            vista.getDpFecha_recibo().setValue(null);

            SpinnerValueFactory.IntegerSpinnerValueFactory tiempoUsoFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) vista.getSpTiempoUso().getValueFactory();
            tiempoUsoFactory.setValue(tiempoUsoFactory.getMin());

            vista.getCbTipoEquipo().getSelectionModel().clearSelection();
            vista.getCbTipoEquipo().setValue(null);

            vista.getCbEquipos().getSelectionModel().clearSelection();
            vista.getCbEquipos().setValue(null);

            SpinnerValueFactory.IntegerSpinnerValueFactory cantidadFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) vista.getSpCantidad().getValueFactory();
            cantidadFactory.setValue(cantidadFactory.getMin());


            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Éxito");
            alerta.setContentText("Solicitud enviada correctamente.");
            alerta.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error");
            alerta.setContentText("No se pudo enviar la solicitud: " + ex.getMessage());
            alerta.showAndWait();
        }
    }
}

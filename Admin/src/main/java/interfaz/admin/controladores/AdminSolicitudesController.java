package interfaz.admin.controladores;

import interfaz.admin.dao.SolicitudDAO;
import interfaz.admin.dao.impl.SolicitudDAOImpl;
import interfaz.admin.modelos.Solicitud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.sql.Date; // IMPORTANTE: Usar java.sql.Date para compatibilidad con la base de datos
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Optional; // Para diálogos de confirmación
import javafx.scene.control.ButtonType; // Para diálogos de confirmación


public class AdminSolicitudesController implements Initializable {

    @FXML
    private TableView<Solicitud> solicitudesTable;
    @FXML
    private TableColumn<Solicitud, Integer> colIdSolicitud;
    @FXML
    private TableColumn<Solicitud, Integer> colIdUsuario;
    @FXML
    private TableColumn<Solicitud, String> colArticulo;
    @FXML
    private TableColumn<Solicitud, Integer> colCantidad;
    @FXML
    private TableColumn<Solicitud, Date> colFechaSolicitud;
    @FXML
    private TableColumn<Solicitud, Date> colFechaRecibo;
    @FXML
    private TableColumn<Solicitud, String> colTiempoUso;
    @FXML
    private TableColumn<Solicitud, String> colRazonUso;
    @FXML
    private TableColumn<Solicitud, String> colEstado;
    @FXML
    private TableColumn<Solicitud, Integer> colProfesorValida;
    @FXML
    private TableColumn<Solicitud, Date> colFechaValidacion;
    @FXML
    private TableColumn<Solicitud, Integer> colAdminEntrega;
    @FXML
    private TableColumn<Solicitud, Date> colFechaEntrega;
    @FXML
    private TableColumn<Solicitud, Integer> colAdminRecibe;
    @FXML
    private TableColumn<Solicitud, Date> colFechaDevolucion;

    @FXML
    private ComboBox<String> estadoFilterComboBox;
    @FXML
    private DatePicker fechaInicioDatePicker;
    @FXML
    private DatePicker fechaFinDatePicker;

    private SolicitudDAO solicitudDAO;
    private ObservableList<Solicitud> listaSolicitudes;

    // --- ID del usuario logueado (PARA PRUEBAS) ---
    // En una aplicación real, estos IDs se obtendrían del sistema de sesión/login.
    // Usamos IDs fijos basados en tu configuración: 1=Admin, 2=Profesor, 3=Estudiante
    private final int ID_ADMIN_LOGUEADO = 1;
    private final int ID_PROFESOR_LOGUEADO = 2;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        solicitudDAO = new SolicitudDAOImpl(); // Instanciar tu DAO

        // Configurar las celdas de las columnas para mapear a las propiedades del modelo Solicitud
        // Los nombres en PropertyValueFactory deben coincidir con los nombres de los atributos
        // (o los nombres que corresponden a los getters 'getXXX()') en tu clase Solicitud.java
        colIdSolicitud.setCellValueFactory(new PropertyValueFactory<>("idSolicitud"));
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fechaSolicitud"));
        colFechaRecibo.setCellValueFactory(new PropertyValueFactory<>("fechaRecibo"));
        colTiempoUso.setCellValueFactory(new PropertyValueFactory<>("tiempoUso"));
        colRazonUso.setCellValueFactory(new PropertyValueFactory<>("razonUso"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colProfesorValida.setCellValueFactory(new PropertyValueFactory<>("idProfesorValida"));
        colFechaValidacion.setCellValueFactory(new PropertyValueFactory<>("fechaValidacion"));
        colAdminEntrega.setCellValueFactory(new PropertyValueFactory<>("idAdminEntrega"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colAdminRecibe.setCellValueFactory(new PropertyValueFactory<>("idAdminRecibe"));
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        // Inicializar ComboBox de estados para filtrado
        estadoFilterComboBox.setItems(FXCollections.observableArrayList(
                "Todos", "PENDIENTE", "APROBADA", "RECHAZADA", "ENTREGADA", "FINALIZADA"
        ));
        estadoFilterComboBox.setValue("Todos"); // Establecer un valor por defecto

        // Cargar los datos iniciales al iniciar la vista
        cargarTodasLasSolicitudes();
    }

    /**
     * Carga todas las solicitudes sin aplicar filtros.
     */
    private void cargarTodasLasSolicitudes() {
        try {
            List<Solicitud> solicitudes = solicitudDAO.obtenerTodasLasSolicitudes();
            listaSolicitudes = FXCollections.observableArrayList(solicitudes);
            solicitudesTable.setItems(listaSolicitudes);
        } catch (SQLException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Base de Datos", "No se pudieron cargar las solicitudes: " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace para depuración
        }
    }

    /**
     * Aplica los filtros seleccionados (estado y/o rango de fechas) y actualiza la tabla.
     */
    @FXML
    private void aplicarFiltros() {
        String estado = estadoFilterComboBox.getValue();
        LocalDate localFechaInicio = fechaInicioDatePicker.getValue();
        LocalDate localFechaFin = fechaFinDatePicker.getValue();

        // Convertir LocalDate a java.sql.Date
        Date fechaInicio = (localFechaInicio != null) ? Date.valueOf(localFechaInicio) : null;
        Date fechaFin = (localFechaFin != null) ? Date.valueOf(localFechaFin) : null;

        try {
            List<Solicitud> solicitudes = solicitudDAO.obtenerTodasLasSolicitudesFiltradas(estado, fechaInicio, fechaFin);
            listaSolicitudes = FXCollections.observableArrayList(solicitudes);
            solicitudesTable.setItems(listaSolicitudes);
        } catch (SQLException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Base de Datos", "No se pudieron aplicar los filtros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Resetea los filtros y muestra todas las solicitudes nuevamente.
     */
    @FXML
    private void mostrarTodasSolicitudes() {
        estadoFilterComboBox.setValue("Todos"); // Reiniciar el ComboBox
        fechaInicioDatePicker.setValue(null); // Limpiar DatePicker
        fechaFinDatePicker.setValue(null);   // Limpiar DatePicker
        cargarTodasLasSolicitudes(); // Recargar todas las solicitudes
    }

    /**
     * Aprobar una solicitud seleccionada.
     */
    @FXML
    private void aprobarSolicitud() {
        Solicitud seleccionada = solicitudesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Seleccione una solicitud para aprobar.");
            return;
        }

        if (!"PENDIENTE".equals(seleccionada.getEstado())) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Solo se pueden aprobar solicitudes en estado PENDIENTE.");
            return;
        }

        Optional<ButtonType> result = mostrarConfirmacion("Confirmar Aprobación", "¿Está seguro de que desea APROBAR esta solicitud?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Aquí usamos el ID fijo del profesor logueado para la prueba
                seleccionada.setEstado("APROBADA");
                seleccionada.setIdProfesorValida(ID_PROFESOR_LOGUEADO);
                seleccionada.setFechaValidacion(new Date(System.currentTimeMillis())); // Fecha actual de validación

                boolean actualizado = solicitudDAO.actualizarSolicitud(seleccionada);
                if (actualizado) {
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Solicitud aprobada correctamente.");
                    aplicarFiltros(); // Recargar la tabla para ver el cambio
                } else {
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "No se pudo aprobar la solicitud.");
                }
            } catch (SQLException e) {
                mostrarAlerta(AlertType.ERROR, "Error de Base de Datos", "Error al aprobar solicitud: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Rechazar una solicitud seleccionada.
     */
    @FXML
    private void rechazarSolicitud() {
        Solicitud seleccionada = solicitudesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Seleccione una solicitud para rechazar.");
            return;
        }

        if (!("PENDIENTE".equals(seleccionada.getEstado()) || "APROBADA".equals(seleccionada.getEstado()))) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Solo se pueden rechazar solicitudes en estado PENDIENTE o APROBADA.");
            return;
        }

        Optional<ButtonType> result = mostrarConfirmacion("Confirmar Rechazo", "¿Está seguro de que desea RECHAZAR esta solicitud?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Aquí usamos el ID fijo del profesor logueado para la prueba
                seleccionada.setEstado("RECHAZADA");
                seleccionada.setIdProfesorValida(ID_PROFESOR_LOGUEADO); // El profesor que rechaza
                seleccionada.setFechaValidacion(new Date(System.currentTimeMillis()));

                // Si se rechaza, los campos de entrega/recepción se limpian si estaban establecidos
                seleccionada.setIdAdminEntrega(null);
                seleccionada.setFechaEntrega(null);
                seleccionada.setIdAdminRecibe(null);
                seleccionada.setFechaDevolucion(null);


                boolean actualizado = solicitudDAO.actualizarSolicitud(seleccionada);
                if (actualizado) {
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Solicitud rechazada correctamente.");
                    aplicarFiltros();
                } else {
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "No se pudo rechazar la solicitud.");
                }
            } catch (SQLException e) {
                mostrarAlerta(AlertType.ERROR, "Error de Base de Datos", "Error al rechazar solicitud: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Marcar una solicitud como entregada por el administrador.
     */
    @FXML
    private void entregarSolicitud() {
        Solicitud seleccionada = solicitudesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Seleccione una solicitud para marcar como entregada.");
            return;
        }

        if (!"APROBADA".equals(seleccionada.getEstado())) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Solo se pueden entregar solicitudes en estado APROBADA.");
            return;
        }

        Optional<ButtonType> result = mostrarConfirmacion("Confirmar Entrega", "¿Está seguro de que desea marcar esta solicitud como ENTREGADA?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Aquí usamos el ID fijo del admin logueado para la prueba
                seleccionada.setEstado("ENTREGADA");
                seleccionada.setIdAdminEntrega(ID_ADMIN_LOGUEADO);
                seleccionada.setFechaEntrega(new Date(System.currentTimeMillis()));

                boolean actualizado = solicitudDAO.actualizarSolicitud(seleccionada);
                if (actualizado) {
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Solicitud marcada como entregada.");
                    aplicarFiltros();
                } else {
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "No se pudo marcar la solicitud como entregada.");
                }
            } catch (SQLException e) {
                mostrarAlerta(AlertType.ERROR, "Error de Base de Datos", "Error al entregar solicitud: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Marcar una solicitud como devuelta/finalizada por el administrador.
     */
    @FXML
    private void recibirDevolucion() {
        Solicitud seleccionada = solicitudesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Seleccione una solicitud para recibir su devolución.");
            return;
        }

        if (!"ENTREGADA".equals(seleccionada.getEstado())) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Solo se pueden recibir devoluciones de solicitudes en estado ENTREGADA.");
            return;
        }

        Optional<ButtonType> result = mostrarConfirmacion("Confirmar Devolución", "¿Está seguro de que desea marcar esta solicitud como FINALIZADA?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Aquí usamos el ID fijo del admin logueado para la prueba
                seleccionada.setEstado("FINALIZADA");
                seleccionada.setIdAdminRecibe(ID_ADMIN_LOGUEADO);
                seleccionada.setFechaDevolucion(new Date(System.currentTimeMillis()));

                boolean actualizado = solicitudDAO.actualizarSolicitud(seleccionada);
                if (actualizado) {
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Solicitud marcada como finalizada (devuelta).");
                    aplicarFiltros();
                } else {
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "No se pudo marcar la devolución de la solicitud.");
                }
            } catch (SQLException e) {
                mostrarAlerta(AlertType.ERROR, "Error de Base de Datos", "Error al recibir devolución: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * Muestra una alerta simple al usuario.
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de confirmación al usuario.
     */
    private Optional<ButtonType> mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait();
    }
}
package interfaz.admin.controladores;


import interfaz.admin.dao.EquipoDAO;
import interfaz.admin.dao.SolicitudDAO;
import interfaz.admin.dao.UsuarioDAO;
import interfaz.admin.dao.impl.EquipoDAOImpl;
import interfaz.admin.dao.impl.SolicitudDAOImpl;
import interfaz.admin.dao.impl.UsuarioDAOImpl;
import interfaz.admin.modelos.Equipo;
import interfaz.admin.modelos.Solicitud;
import interfaz.admin.modelos.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty; // Importar para propiedades de String

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolicitudesControlador implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudesControlador.class.getName());

    @FXML private Label tituloVista;
    @FXML private HBox panelFiltros;
    @FXML private ComboBox<String> comboBoxEstadoFiltro;
    @FXML private DatePicker datePickerFechaInicioFiltro;
    @FXML private DatePicker datePickerFechaFinFiltro;
    @FXML private HBox panelAcciones;
    @FXML private Button btnNuevaSolicitud;
    @FXML private Button btnAprobar;
    @FXML private Button btnRechazar;
    @FXML private Button btnEntregar;
    @FXML private Button btnDevolver;
    @FXML private Label etiquetaMensaje;
    @FXML private TableView<Solicitud> tablaSolicitudes;

    @FXML private TableColumn<Solicitud, Integer> colIdSolicitud;
    @FXML private TableColumn<Solicitud, String> colUsuarioSolicitante; // Nombre del usuario
    @FXML private TableColumn<Solicitud, String> colEquipoSolicitado; // Descripción del equipo
    @FXML private TableColumn<Solicitud, Date> colFechaSolicitud;
    @FXML private TableColumn<Solicitud, Date> colFechaInicio;
    @FXML private TableColumn<Solicitud, Date> colFechaFin;
    @FXML private TableColumn<Solicitud, String> colEstado;
    @FXML private TableColumn<Solicitud, String> colObservaciones;
    @FXML private TableColumn<Solicitud, String> colUsuarioProfesor; // Nombre del profesor
    @FXML private TableColumn<Solicitud, Date> colFechaValidacion;
    @FXML private TableColumn<Solicitud, String> colUsuarioEntrega; // Nombre del admin entrega
    @FXML private TableColumn<Solicitud, Date> colFechaEntrega;
    @FXML private TableColumn<Solicitud, String> colUsuarioDevolucion; // Nombre del admin recibe
    @FXML private TableColumn<Solicitud, Date> colFechaDevolucion;

    private SolicitudDAO solicitudDAO;
    private UsuarioDAO usuarioDAO;
    private EquipoDAO equipoDAO;
    private ObservableList<Solicitud> listaSolicitudes;
    private Usuario usuarioActual; // El usuario que ha iniciado sesión
    private Solicitud solicitudSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        solicitudDAO = new SolicitudDAOImpl();
        usuarioDAO = new UsuarioDAOImpl();
        equipoDAO = new EquipoDAOImpl();
        listaSolicitudes = FXCollections.observableArrayList();

        // Configurar ComboBox de filtros de estado
        comboBoxEstadoFiltro.setItems(FXCollections.observableArrayList(
                "Todos", "Pendiente", "Aprobada", "Rechazada", "Entregada", "Devuelta", "Cancelada"
        ));
        comboBoxEstadoFiltro.getSelectionModel().select("Todos");

        // Configurar columnas de la tabla
        colIdSolicitud.setCellValueFactory(new PropertyValueFactory<>("idSolicitud"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fechaSolicitud"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

        // Columnas que requieren obtener datos de DAOs relacionados
        colUsuarioSolicitante.setCellValueFactory(cellData -> {
            Integer idUsuario = cellData.getValue().getIdUsuarioSolicitante();
            try {
                Usuario u = usuarioDAO.obtenerUsuarioPorId(idUsuario);
                return new SimpleStringProperty(u != null ? u.getNombre() + " " + u.getApellidos() : "Desconocido");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al obtener nombre de usuario solicitante.", e);
                return new SimpleStringProperty("Error");
            }
        });

        colEquipoSolicitado.setCellValueFactory(cellData -> {
            Integer idEquipo = cellData.getValue().getIdEquipo();
            try {
                Equipo e = equipoDAO.obtenerEquipoPorId(idEquipo);
                return new SimpleStringProperty(e != null ? e.getDescripcion() : "Desconocido");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al obtener descripción de equipo.", e);
                return new SimpleStringProperty("Error");
            }
        });

        colUsuarioProfesor.setCellValueFactory(cellData -> {
            Integer idProfesor = cellData.getValue().getIdProfesorValida();
            if (idProfesor == null) return new SimpleStringProperty("");
            try {
                Usuario u = usuarioDAO.obtenerUsuarioPorId(idProfesor);
                return new SimpleStringProperty(u != null ? u.getNombre() + " " + u.getApellidos() : "Desconocido");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al obtener nombre de profesor valida.", e);
                return new SimpleStringProperty("Error");
            }
        });
        colFechaValidacion.setCellValueFactory(new PropertyValueFactory<>("fechaValidacion"));

        colUsuarioEntrega.setCellValueFactory(cellData -> {
            Integer idAdminEntrega = cellData.getValue().getIdAdminEntrega();
            if (idAdminEntrega == null) return new SimpleStringProperty("");
            try {
                Usuario u = usuarioDAO.obtenerUsuarioPorId(idAdminEntrega);
                return new SimpleStringProperty(u != null ? u.getNombre() + " " + u.getApellidos() : "Desconocido");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al obtener nombre de admin entrega.", e);
                return new SimpleStringProperty("Error");
            }
        });
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));

        colUsuarioDevolucion.setCellValueFactory(cellData -> {
            Integer idAdminRecibe = cellData.getValue().getIdAdminRecibe();
            if (idAdminRecibe == null) return new SimpleStringProperty("");
            try {
                Usuario u = usuarioDAO.obtenerUsuarioPorId(idAdminRecibe);
                return new SimpleStringProperty(u != null ? u.getNombre() + " " + u.getApellidos() : "Desconocido");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al obtener nombre de admin recibe.", e);
                return new SimpleStringProperty("Error");
            }
        });
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));


        // Listener para la selección de filas en la tabla
        tablaSolicitudes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            solicitudSeleccionada = newSelection;
            // Aquí podrías habilitar/deshabilitar botones si se quiere editar una solicitud (aunque para solicitudes suele ser solo crear/cambiar estado)
            actualizarEstadoBotones();
        });
    }

    /**
     * Este método es llamado por el DashboardControlador para pasar el usuario actual.
     * Es crucial para adaptar la vista al rol.
     * @param usuario El usuario que ha iniciado sesión.
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        adaptarVistaSegunRol();
        cargarSolicitudes(); // Cargar solicitudes después de adaptar la vista
    }

    private void adaptarVistaSegunRol() {
        if (usuarioActual == null) return;

        // Ocultar todos los botones de acción por defecto
        btnNuevaSolicitud.setVisible(false);
        btnNuevaSolicitud.setManaged(false);
        btnAprobar.setVisible(false);
        btnAprobar.setManaged(false);
        btnRechazar.setVisible(false);
        btnRechazar.setManaged(false);
        btnEntregar.setVisible(false);
        btnEntregar.setManaged(false);
        btnDevolver.setVisible(false);
        btnDevolver.setManaged(false);
        panelFiltros.setVisible(false);
        panelFiltros.setManaged(false);

        // Ocultar columnas extendidas por defecto
        colUsuarioProfesor.setVisible(false);
        colFechaValidacion.setVisible(false);
        colUsuarioEntrega.setVisible(false);
        colFechaEntrega.setVisible(false);
        colUsuarioDevolucion.setVisible(false);
        colFechaDevolucion.setVisible(false);


        switch (usuarioActual.getRol()) {
            case "Administrador":
                tituloVista.setText("Panel de Administración de Solicitudes");
                btnAprobar.setVisible(true);
                btnAprobar.setManaged(true);
                btnRechazar.setVisible(true);
                btnRechazar.setManaged(true);
                btnEntregar.setVisible(true);
                btnEntregar.setManaged(true);
                btnDevolver.setVisible(true);
                btnDevolver.setManaged(true);
                panelFiltros.setVisible(true);
                panelFiltros.setManaged(true);
                // Mostrar todas las columnas de auditoría
                colUsuarioProfesor.setVisible(true);
                colFechaValidacion.setVisible(true);
                colUsuarioEntrega.setVisible(true);
                colFechaEntrega.setVisible(true);
                colUsuarioDevolucion.setVisible(true);
                colFechaDevolucion.setVisible(true);
                break;
            case "Profesor":
                tituloVista.setText("Mis Solicitudes de Alumnos");
                btnAprobar.setVisible(true);
                btnAprobar.setManaged(true);
                btnRechazar.setVisible(true);
                btnRechazar.setManaged(true);
                panelFiltros.setVisible(true);
                panelFiltros.setManaged(true);
                colUsuarioProfesor.setVisible(true);
                colFechaValidacion.setVisible(true);
                break;
            case "Estudiante":
                tituloVista.setText("Mis Solicitudes de Equipo");
                btnNuevaSolicitud.setVisible(true);
                btnNuevaSolicitud.setManaged(true);
                break;
            default:
                // No mostrar nada o un mensaje de error
                break;
        }
        actualizarEstadoBotones(); // Asegurar que los botones estén correctamente habilitados/deshabilitados
    }

    private void cargarSolicitudes() {
        try {
            List<Solicitud> solicitudes;
            String rol = usuarioActual.getRol();

            if ("Administrador".equals(rol)) {
                String estadoFiltro = comboBoxEstadoFiltro.getValue();
                LocalDate fechaInicio = datePickerFechaInicioFiltro.getValue();
                LocalDate fechaFin = datePickerFechaFinFiltro.getValue();

                solicitudes = solicitudDAO.obtenerTodasLasSolicitudesFiltradas(
                        "Todos".equals(estadoFiltro) ? null : estadoFiltro,
                        fechaInicio != null ? Date.valueOf(fechaInicio) : null,
                        fechaFin != null ? Date.valueOf(fechaFin) : null
                );
            } else if ("Profesor".equals(rol)) {
                solicitudes = solicitudDAO.obtenerSolicitudesPorProfesorValida(usuarioActual.getIdUsuario());
            } else if ("Estudiante".equals(rol)) {
                solicitudes = solicitudDAO.obtenerSolicitudesPorUsuarioSolicitante(usuarioActual.getIdUsuario());
            } else {
                solicitudes = FXCollections.emptyObservableList(); // No hay solicitudes para otros roles
            }
            listaSolicitudes.setAll(solicitudes);
            tablaSolicitudes.setItems(listaSolicitudes);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar solicitudes para el rol " + usuarioActual.getRol(), e);
            mostrarMensaje("Error al cargar solicitudes: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void aplicarFiltros() {
        cargarSolicitudes(); // Recarga las solicitudes con los filtros aplicados
    }

    @FXML
    private void limpiarFiltros() {
        comboBoxEstadoFiltro.getSelectionModel().select("Todos");
        datePickerFechaInicioFiltro.setValue(null);
        datePickerFechaFinFiltro.setValue(null);
        cargarSolicitudes(); // Recarga las solicitudes sin filtros
    }

    @FXML
    private void nuevaSolicitud() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/CrearSolicitudVista.fxml")));
            Parent parent = loader.load();

            CrearSolicitudControlador controller = loader.getController();
            controller.setUsuarioSolicitante(usuarioActual);
            controller.setSolicitudesControlador(this); // Para que pueda recargar la tabla después de crear

            Stage stage = new Stage();
            stage.setTitle("Crear Nueva Solicitud de Préstamo");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal
            stage.showAndWait(); // Espera a que se cierre la ventana
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana para crear solicitud.", e);
            mostrarMensaje("Error al abrir ventana de nueva solicitud.", "error");
        }
    }

    @FXML
    private void aprobarSolicitud() {
        if (solicitudSeleccionada == null) {
            mostrarMensaje("Seleccione una solicitud para aprobar.", "error");
            return;
        }
        if (!"Pendiente".equals(solicitudSeleccionada.getEstado())) {
            mostrarMensaje("Solo se pueden aprobar solicitudes en estado Pendiente.", "error");
            return;
        }

        try {
            solicitudSeleccionada.setEstado("Aprobada");
            solicitudSeleccionada.setIdProfesorValida(usuarioActual.getIdUsuario());
            solicitudSeleccionada.setFechaValidacion(Date.valueOf(LocalDate.now()));

            if (solicitudDAO.actualizarSolicitud(solicitudSeleccionada)) {
                mostrarMensaje("Solicitud aprobada con éxito.", "exito");
                cargarSolicitudes();
            } else {
                mostrarMensaje("No se pudo aprobar la solicitud.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al aprobar solicitud.", e);
            mostrarMensaje("Error de base de datos al aprobar solicitud: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void rechazarSolicitud() {
        if (solicitudSeleccionada == null) {
            mostrarMensaje("Seleccione una solicitud para rechazar.", "error");
            return;
        }
        if (!"Pendiente".equals(solicitudSeleccionada.getEstado()) && !"Aprobada".equals(solicitudSeleccionada.getEstado())) {
            mostrarMensaje("Solo se pueden rechazar solicitudes en estado Pendiente o Aprobada.", "error");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rechazar Solicitud");
        dialog.setHeaderText("Ingrese el motivo del rechazo:");
        dialog.setContentText("Motivo:");

        dialog.showAndWait().ifPresent(observaciones -> {
            try {
                solicitudSeleccionada.setEstado("Rechazada");
                solicitudSeleccionada.setObservaciones(observaciones); // Opcional: registrar el motivo
                solicitudSeleccionada.setIdProfesorValida(usuarioActual.getIdUsuario()); // Si es profesor
                solicitudSeleccionada.setFechaValidacion(Date.valueOf(LocalDate.now())); // Si es profesor o admin

                if (solicitudDAO.actualizarSolicitud(solicitudSeleccionada)) {
                    mostrarMensaje("Solicitud rechazada con éxito.", "exito");
                    cargarSolicitudes();
                } else {
                    mostrarMensaje("No se pudo rechazar la solicitud.", "error");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al rechazar solicitud.", e);
                mostrarMensaje("Error de base de datos al rechazar solicitud: " + e.getMessage(), "error");
            }
        });
    }

    @FXML
    private void entregarEquipo() {
        if (solicitudSeleccionada == null) {
            mostrarMensaje("Seleccione una solicitud para registrar la entrega.", "error");
            return;
        }
        if (!"Aprobada".equals(solicitudSeleccionada.getEstado())) {
            mostrarMensaje("Solo se pueden entregar equipos de solicitudes en estado Aprobada.", "error");
            return;
        }

        try {
            // Actualizar estado de la solicitud
            solicitudSeleccionada.setEstado("Entregada");
            solicitudSeleccionada.setIdAdminEntrega(usuarioActual.getIdUsuario());
            solicitudSeleccionada.setFechaEntrega(Date.valueOf(LocalDate.now()));

            // Marcar el equipo como NO disponible
            Equipo equipo = equipoDAO.obtenerEquipoPorId(solicitudSeleccionada.getIdEquipo());
            if (equipo != null) {
                equipo.setDisponible('0'); // '0' = No disponible
                equipoDAO.actualizarEquipo(equipo); // Actualiza la disponibilidad en la BD
            }

            if (solicitudDAO.actualizarSolicitud(solicitudSeleccionada)) {
                mostrarMensaje("Equipo entregado con éxito.", "exito");
                cargarSolicitudes();
            } else {
                mostrarMensaje("No se pudo registrar la entrega del equipo.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al entregar equipo.", e);
            mostrarMensaje("Error de base de datos al entregar equipo: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void devolverEquipo() {
        if (solicitudSeleccionada == null) {
            mostrarMensaje("Seleccione una solicitud para registrar la devolución.", "error");
            return;
        }
        if (!"Entregada".equals(solicitudSeleccionada.getEstado())) {
            mostrarMensaje("Solo se pueden devolver equipos de solicitudes en estado Entregada.", "error");
            return;
        }

        try {
            // Actualizar estado de la solicitud
            solicitudSeleccionada.setEstado("Devuelta");
            solicitudSeleccionada.setIdAdminRecibe(usuarioActual.getIdUsuario());
            solicitudSeleccionada.setFechaDevolucion(Date.valueOf(LocalDate.now()));

            // Marcar el equipo como DISPONIBLE nuevamente
            Equipo equipo = equipoDAO.obtenerEquipoPorId(solicitudSeleccionada.getIdEquipo());
            if (equipo != null) {
                equipo.setDisponible('1'); // '1' = Disponible
                equipoDAO.actualizarEquipo(equipo); // Actualiza la disponibilidad en la BD
            }

            if (solicitudDAO.actualizarSolicitud(solicitudSeleccionada)) {
                mostrarMensaje("Equipo devuelto con éxito.", "exito");
                cargarSolicitudes();
            } else {
                mostrarMensaje("No se pudo registrar la devolución del equipo.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al devolver equipo.", e);
            mostrarMensaje("Error de base de datos al devolver equipo: " + e.getMessage(), "error");
        }
    }

    private void actualizarEstadoBotones() {
        if (solicitudSeleccionada == null) {
            btnAprobar.setDisable(true);
            btnRechazar.setDisable(true);
            btnEntregar.setDisable(true);
            btnDevolver.setDisable(true);
            return;
        }

        String estado = solicitudSeleccionada.getEstado();
        String rol = usuarioActual.getRol();

        btnAprobar.setDisable(true);
        btnRechazar.setDisable(true);
        btnEntregar.setDisable(true);
        btnDevolver.setDisable(true);

        if ("Administrador".equals(rol)) {
            if ("Pendiente".equals(estado)) {
                btnAprobar.setDisable(false);
                btnRechazar.setDisable(false);
            } else if ("Aprobada".equals(estado)) {
                btnRechazar.setDisable(false); // Admin puede rechazar una aprobada
                btnEntregar.setDisable(false);
            } else if ("Entregada".equals(estado)) {
                btnDevolver.setDisable(false);
            }
        } else if ("Profesor".equals(rol)) {
            if ("Pendiente".equals(estado)) {
                btnAprobar.setDisable(false);
                btnRechazar.setDisable(false);
            } else if ("Aprobada".equals(estado)) {
                btnRechazar.setDisable(false); // Profesor puede anular su aprobación
            }
        }
        // Estudiante no tiene botones de acción de estado aquí
    }


    private void mostrarMensaje(String mensaje, String tipo) {
        etiquetaMensaje.setText(mensaje);
        if ("exito".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #27ae60;"); // Verde
        } else if ("error".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #e74c3c;"); // Rojo
        } else {
            etiquetaMensaje.setStyle("-fx-text-fill: #2c3e50;"); // Azul oscuro (por defecto)
        }
    }
    // Método público para que otras vistas puedan pedir una recarga
    public void recargarSolicitudes() {
        cargarSolicitudes();
    }
}
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrearSolicitudControlador implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(CrearSolicitudControlador.class.getName());

    @FXML private ComboBox<String> comboBoxEquipo;
    @FXML private DatePicker datePickerFechaInicio;
    @FXML private DatePicker datePickerFechaFin;
    @FXML private TextArea textAreaObservaciones;
    @FXML private ComboBox<String> comboBoxProfesor;
    @FXML private Label etiquetaMensaje;

    private SolicitudDAO solicitudDAO;
    private EquipoDAO equipoDAO;
    private UsuarioDAO usuarioDAO;

    private ObservableList<String> listaEquiposDisponibles;
    private ObservableList<String> listaProfesores;

    private Map<String, Integer> equipoNameToIdMap;
    private Map<String, Integer> profesorNameToIdMap;

    private Usuario usuarioSolicitante; // El estudiante que está creando la solicitud
    private SolicitudesControlador solicitudesControlador; // Referencia al controlador principal de solicitudes

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        solicitudDAO = new SolicitudDAOImpl();
        equipoDAO = new EquipoDAOImpl();
        usuarioDAO = new UsuarioDAOImpl();

        listaEquiposDisponibles = FXCollections.observableArrayList();
        listaProfesores = FXCollections.observableArrayList();
        equipoNameToIdMap = new HashMap<>();
        profesorNameToIdMap = new HashMap<>();

        cargarEquiposDisponibles();
        cargarProfesores();
    }

    /**
     * Establece el usuario que está creando la solicitud.
     */
    public void setUsuarioSolicitante(Usuario usuario) {
        this.usuarioSolicitante = usuario;
    }

    /**
     * Establece la referencia al controlador principal de Solicitudes para poder recargar la tabla.
     */
    public void setSolicitudesControlador(SolicitudesControlador controlador) {
        this.solicitudesControlador = controlador;
    }


    private void cargarEquiposDisponibles() {
        try {
            // Solo mostrar equipos marcados como disponibles ('1')
            List<Equipo> equipos = equipoDAO.obtenerEquiposDisponibles();
            listaEquiposDisponibles.clear();
            equipoNameToIdMap.clear();

            for (Equipo equipo : equipos) {
                String nombreEquipo = equipo.getDescripcion() + " (" + equipo.getCodigoBien() + ")";
                listaEquiposDisponibles.add(nombreEquipo);
                equipoNameToIdMap.put(nombreEquipo, equipo.getIdEquipo());
            }
            comboBoxEquipo.setItems(listaEquiposDisponibles);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar equipos disponibles.", e);
            mostrarMensaje("Error al cargar equipos disponibles.", "error");
        }
    }

    private void cargarProfesores() {
        try {
            List<Usuario> profesores = usuarioDAO.obtenerUsuariosPorRol("Profesor");
            listaProfesores.clear();
            profesorNameToIdMap.clear();

            for (Usuario prof : profesores) {
                String nombreProfesor = prof.getNombre() + " " + prof.getApellidos();
                listaProfesores.add(nombreProfesor);
                profesorNameToIdMap.put(nombreProfesor, prof.getIdUsuario());
            }
            comboBoxProfesor.setItems(listaProfesores);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores.", e);
            mostrarMensaje("Error al cargar profesores.", "error");
        }
    }

    @FXML
    private void guardarSolicitud(ActionEvent event) {
        if (!validarCampos()) return;

        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setIdUsuarioSolicitante(usuarioSolicitante.getIdUsuario());
        nuevaSolicitud.setIdEquipo(equipoNameToIdMap.get(comboBoxEquipo.getValue()));
        nuevaSolicitud.setFechaSolicitud(Date.valueOf(LocalDate.now())); // Fecha actual
        nuevaSolicitud.setFechaInicio(Date.valueOf(datePickerFechaInicio.getValue()));
        nuevaSolicitud.setFechaFin(Date.valueOf(datePickerFechaFin.getValue()));
        nuevaSolicitud.setEstado("Pendiente"); // Estado inicial
        nuevaSolicitud.setObservaciones(textAreaObservaciones.getText().trim());
        nuevaSolicitud.setIdProfesorValida(profesorNameToIdMap.get(comboBoxProfesor.getValue()));

        try {
            if (solicitudDAO.insertarSolicitud(nuevaSolicitud)) {
                mostrarMensaje("Solicitud creada exitosamente.", "exito");
                // Recargar la tabla en el controlador principal si existe la referencia
                if (solicitudesControlador != null) {
                    solicitudesControlador.recargarSolicitudes();
                }
                // Cerrar la ventana modal después de guardar
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                mostrarMensaje("No se pudo crear la solicitud.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear solicitud.", e);
            mostrarMensaje("Error de base de datos al crear solicitud: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Cierra la ventana modal sin hacer nada
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        if (comboBoxEquipo.getValue() == null ||
                datePickerFechaInicio.getValue() == null ||
                datePickerFechaFin.getValue() == null ||
                comboBoxProfesor.getValue() == null) {
            mostrarMensaje("Por favor, complete todos los campos obligatorios.", "error");
            return false;
        }

        if (datePickerFechaInicio.getValue().isBefore(LocalDate.now())) {
            mostrarMensaje("La fecha de inicio no puede ser anterior a hoy.", "error");
            return false;
        }

        if (datePickerFechaFin.getValue().isBefore(datePickerFechaInicio.getValue())) {
            mostrarMensaje("La fecha de fin no puede ser anterior a la fecha de inicio.", "error");
            return false;
        }

        return true;
    }

    private void mostrarMensaje(String mensaje, String tipo) {
        etiquetaMensaje.setText(mensaje);
        if ("exito".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #27ae60;");
        } else if ("error".equals(tipo)) {
            etiquetaMensaje.setStyle("-fx-text-fill: #e74c3c;");
        } else {
            etiquetaMensaje.setStyle("-fx-text-fill: #2c3e50;");
        }
    }
}
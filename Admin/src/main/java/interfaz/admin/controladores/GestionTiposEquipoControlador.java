package interfaz.admin.controladores;

import interfaz.admin.dao.TipoEquipoDAO;
import interfaz.admin.dao.impl.TipoEquipoDAOImpl;
import interfaz.admin.modelos.TipoEquipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionTiposEquipoControlador implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(GestionTiposEquipoControlador.class.getName());

    @FXML private TextField campoNombreTipo;
    @FXML private Label etiquetaMensaje;
    @FXML private TableView<TipoEquipo> tablaTiposEquipo;
    @FXML private TableColumn<TipoEquipo, Integer> colIdTipoEquipo;
    @FXML private TableColumn<TipoEquipo, String> colNombreTipo;

    private TipoEquipoDAO tipoEquipoDAO;
    private ObservableList<TipoEquipo> listaTiposEquipo;
    private TipoEquipo tipoEquipoSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipoEquipoDAO = new TipoEquipoDAOImpl();
        listaTiposEquipo = FXCollections.observableArrayList();

        // Configurar columnas de la tabla
        colIdTipoEquipo.setCellValueFactory(new PropertyValueFactory<>("idTipoEquipo"));
        colNombreTipo.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Cargar tipos de equipo al iniciar la vista
        cargarTiposEquipo();

        // Listener para la selección de filas en la tabla
        tablaTiposEquipo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tipoEquipoSeleccionado = newSelection;
                llenarCamposConTipoEquipo(newSelection);
            } else {
                tipoEquipoSeleccionado = null;
                limpiarCampos();
            }
        });
    }

    private void cargarTiposEquipo() {
        try {
            listaTiposEquipo.setAll(tipoEquipoDAO.obtenerTodosLosTiposEquipo());
            tablaTiposEquipo.setItems(listaTiposEquipo);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar tipos de equipo desde la base de datos.", e);
            mostrarMensaje("Error al cargar tipos de equipo: " + e.getMessage(), "error");
        }
    }

    private void llenarCamposConTipoEquipo(TipoEquipo tipo) {
        campoNombreTipo.setText(tipo.getNombre());
    }

    @FXML
    private void crearTipoEquipo(ActionEvent event) {
        if (!validarCampos()) return;

        TipoEquipo nuevoTipo = new TipoEquipo();
        nuevoTipo.setNombre(campoNombreTipo.getText().trim());

        try {
            if (tipoEquipoDAO.insertarTipoEquipo(nuevoTipo)) {
                mostrarMensaje("Tipo de equipo creado exitosamente.", "exito");
                limpiarCampos();
                cargarTiposEquipo(); // Recargar la tabla
            } else {
                mostrarMensaje("No se pudo crear el tipo de equipo.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear tipo de equipo.", e);
            mostrarMensaje("Error de base de datos al crear tipo de equipo: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void actualizarTipoEquipo(ActionEvent event) {
        if (tipoEquipoSeleccionado == null) {
            mostrarMensaje("Seleccione un tipo de equipo de la tabla para actualizar.", "error");
            return;
        }
        if (!validarCampos()) return;

        TipoEquipo tipoActualizado = new TipoEquipo();
        tipoActualizado.setIdTipoEquipo(tipoEquipoSeleccionado.getIdTipoEquipo());
        tipoActualizado.setNombre(campoNombreTipo.getText().trim());

        try {
            if (tipoEquipoDAO.actualizarTipoEquipo(tipoActualizado)) {
                mostrarMensaje("Tipo de equipo actualizado exitosamente.", "exito");
                limpiarCampos();
                cargarTiposEquipo(); // Recargar la tabla
                // También es crucial recargar los tipos en el controlador de Gestión de Equipos
                // Esto podría requerir un patrón de Observador o una recarga manual al volver a esa vista
            } else {
                mostrarMensaje("No se pudo actualizar el tipo de equipo.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar tipo de equipo.", e);
            mostrarMensaje("Error de base de datos al actualizar tipo de equipo: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void eliminarTipoEquipo(ActionEvent event) {
        if (tipoEquipoSeleccionado == null) {
            mostrarMensaje("Seleccione un tipo de equipo de la tabla para eliminar.", "error");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea eliminar el tipo de equipo: " + tipoEquipoSeleccionado.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirmar eliminación");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                // Validación adicional: ¿Hay equipos asociados a este tipo?
                // En un sistema real, no permitirías eliminar un tipo si tiene equipos asociados.
                // Aquí, la FK en la BD manejará esto con un error si no se ha configurado ON DELETE CASCADE.
                if (tipoEquipoDAO.eliminarTipoEquipo(tipoEquipoSeleccionado.getIdTipoEquipo())) {
                    mostrarMensaje("Tipo de equipo eliminado exitosamente.", "exito");
                    limpiarCampos();
                    cargarTiposEquipo(); // Recargar la tabla
                    // También es crucial recargar los tipos en el controlador de Gestión de Equipos
                } else {
                    mostrarMensaje("No se pudo eliminar el tipo de equipo.", "error");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al eliminar tipo de equipo.", e);
                // Si la FK de EQUIPO_TIPO_EQUIPO está configurada como RESTRICT o NO ACTION,
                // obtendrás un error SQL aquí si hay equipos de ese tipo.
                if (e.getErrorCode() == 2292) { // ORA-02292: integrity constraint violated - child record found
                    mostrarMensaje("No se puede eliminar el tipo de equipo porque hay equipos asociados a él.", "error");
                } else {
                    mostrarMensaje("Error de base de datos al eliminar tipo de equipo: " + e.getMessage(), "error");
                }
            }
        }
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        limpiarCampos();
    }

    private void limpiarCampos() {
        campoNombreTipo.clear();
        etiquetaMensaje.setText("");
        tablaTiposEquipo.getSelectionModel().clearSelection();
        tipoEquipoSeleccionado = null;
    }

    private boolean validarCampos() {
        if (campoNombreTipo.getText().trim().isEmpty()) {
            mostrarMensaje("El nombre del tipo de equipo no puede estar vacío.", "error");
            return false;
        }
        return true;
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
}
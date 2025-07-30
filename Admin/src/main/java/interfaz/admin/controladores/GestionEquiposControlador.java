package interfaz.admin.controladores;

import interfaz.admin.dao.EquipoDAO;
import interfaz.admin.dao.TipoEquipoDAO;
import interfaz.admin.dao.impl.EquipoDAOImpl;
import interfaz.admin.dao.impl.TipoEquipoDAOImpl;
import interfaz.admin.modelos.Equipo;
import interfaz.admin.modelos.TipoEquipo;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionEquiposControlador implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(GestionEquiposControlador.class.getName());

    @FXML private TextField campoCodigoBien;
    @FXML private TextField campoDescripcion;
    @FXML private TextField campoMarca;
    @FXML private TextField campoModelo;
    @FXML private TextField campoNumeroSerie;
    @FXML private ComboBox<String> campoTipoEquipo; // Mostrará el nombre del tipo de equipo
    @FXML private CheckBox checkBoxDisponible;
    @FXML private Label etiquetaMensaje;
    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, Integer> colIdEquipo;
    @FXML private TableColumn<Equipo, String> colCodigoBien;
    @FXML private TableColumn<Equipo, String> colDescripcion;
    @FXML private TableColumn<Equipo, String> colMarca;
    @FXML private TableColumn<Equipo, String> colModelo;
    @FXML private TableColumn<Equipo, String> colNumeroSerie;
    @FXML private TableColumn<Equipo, Character> colDisponible;
    @FXML private TableColumn<Equipo, String> colTipoEquipo; // Mostrará el nombre del tipo de equipo

    private EquipoDAO equipoDAO;
    private TipoEquipoDAO tipoEquipoDAO;
    private ObservableList<Equipo> listaEquipos;
    private Equipo equipoSeleccionado; // Para almacenar el equipo seleccionado en la tabla

    // Mapa para convertir entre nombre de tipo de equipo y su ID
    private Map<String, Integer> tipoEquipoNameToIdMap;
    private Map<Integer, String> tipoEquipoIdToNameMap;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipoDAO = new EquipoDAOImpl();
        tipoEquipoDAO = new TipoEquipoDAOImpl();
        listaEquipos = FXCollections.observableArrayList();
        tipoEquipoNameToIdMap = new HashMap<>();
        tipoEquipoIdToNameMap = new HashMap<>();

        // Cargar los tipos de equipo al iniciar
        cargarTiposEquipo();

        // Configurar columnas de la tabla
        colIdEquipo.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        colCodigoBien.setCellValueFactory(new PropertyValueFactory<>("codigoBien"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colNumeroSerie.setCellValueFactory(new PropertyValueFactory<>("numeroSerie"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        // Para mostrar el nombre del tipo de equipo en lugar del ID
        colTipoEquipo.setCellValueFactory(cellData -> {
            Integer idTipo = cellData.getValue().getIdTipoEquipo();
            return new ReadOnlyStringWrapper(tipoEquipoIdToNameMap.getOrDefault(idTipo, "Desconocido"));
        });

        // Cargar equipos al iniciar la vista
        cargarEquipos();

        // Listener para la selección de filas en la tabla
        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                equipoSeleccionado = newSelection;
                llenarCamposConEquipo(newSelection);
            } else {
                equipoSeleccionado = null;
                limpiarCampos();
            }
        });
    }

    private void cargarTiposEquipo() {
        try {
            List<TipoEquipo> tipos = tipoEquipoDAO.obtenerTodosLosTiposEquipo();
            ObservableList<String> nombresTipos = FXCollections.observableArrayList();
            tipoEquipoNameToIdMap.clear();
            tipoEquipoIdToNameMap.clear();

            for (TipoEquipo tipo : tipos) {
                nombresTipos.add(tipo.getNombre());
                tipoEquipoNameToIdMap.put(tipo.getNombre(), tipo.getIdTipoEquipo());
                tipoEquipoIdToNameMap.put(tipo.getIdTipoEquipo(), tipo.getNombre());
            }
            campoTipoEquipo.setItems(nombresTipos);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar tipos de equipo.", e);
            mostrarMensaje("Error al cargar tipos de equipo: " + e.getMessage(), "error");
        }
    }

    private void cargarEquipos() {
        try {
            listaEquipos.setAll(equipoDAO.obtenerTodosLosEquipos());
            tablaEquipos.setItems(listaEquipos);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar equipos desde la base de datos.", e);
            mostrarMensaje("Error al cargar equipos: " + e.getMessage(), "error");
        }
    }

    private void llenarCamposConEquipo(Equipo equipo) {
        campoCodigoBien.setText(equipo.getCodigoBien());
        campoDescripcion.setText(equipo.getDescripcion());
        campoMarca.setText(equipo.getMarca());
        campoModelo.setText(equipo.getModelo());
        campoNumeroSerie.setText(equipo.getNumeroSerie());
        checkBoxDisponible.setSelected(equipo.getDisponible() == '1'); // '1' significa true, '0' false
        campoTipoEquipo.setValue(tipoEquipoIdToNameMap.get(equipo.getIdTipoEquipo())); // Mostrar nombre del tipo
    }

    @FXML
    private void crearEquipo(ActionEvent event) {
        if (!validarCampos()) return;

        Equipo nuevoEquipo = construirEquipoDesdeCampos();

        try {
            if (equipoDAO.insertarEquipo(nuevoEquipo)) {
                mostrarMensaje("Equipo creado exitosamente.", "exito");
                limpiarCampos();
                cargarEquipos(); // Recargar la tabla
            } else {
                mostrarMensaje("No se pudo crear el equipo.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear equipo.", e);
            mostrarMensaje("Error de base de datos al crear equipo: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void actualizarEquipo(ActionEvent event) {
        if (equipoSeleccionado == null) {
            mostrarMensaje("Seleccione un equipo de la tabla para actualizar.", "error");
            return;
        }
        if (!validarCampos()) return;

        Equipo equipoActualizado = construirEquipoDesdeCampos();
        equipoActualizado.setIdEquipo(equipoSeleccionado.getIdEquipo()); // Mantener el ID del equipo seleccionado

        try {
            if (equipoDAO.actualizarEquipo(equipoActualizado)) {
                mostrarMensaje("Equipo actualizado exitosamente.", "exito");
                limpiarCampos();
                cargarEquipos(); // Recargar la tabla
            } else {
                mostrarMensaje("No se pudo actualizar el equipo.", "error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar equipo.", e);
            mostrarMensaje("Error de base de datos al actualizar equipo: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void eliminarEquipo(ActionEvent event) {
        if (equipoSeleccionado == null) {
            mostrarMensaje("Seleccione un equipo de la tabla para eliminar.", "error");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea eliminar el equipo con código: " + equipoSeleccionado.getCodigoBien() + "?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirmar eliminación");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                if (equipoDAO.eliminarEquipo(equipoSeleccionado.getIdEquipo())) {
                    mostrarMensaje("Equipo eliminado exitosamente.", "exito");
                    limpiarCampos();
                    cargarEquipos(); // Recargar la tabla
                } else {
                    mostrarMensaje("No se pudo eliminar el equipo.", "error");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al eliminar equipo.", e);
                mostrarMensaje("Error de base de datos al eliminar equipo: " + e.getMessage(), "error");
            }
        }
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        limpiarCampos();
    }

    private void limpiarCampos() {
        campoCodigoBien.clear();
        campoDescripcion.clear();
        campoMarca.clear();
        campoModelo.clear();
        campoNumeroSerie.clear();
        campoTipoEquipo.getSelectionModel().clearSelection();
        checkBoxDisponible.setSelected(false);
        etiquetaMensaje.setText("");
        tablaEquipos.getSelectionModel().clearSelection();
        equipoSeleccionado = null;
    }

    private Equipo construirEquipoDesdeCampos() {
        Equipo equipo = new Equipo();
        equipo.setCodigoBien(campoCodigoBien.getText().trim());
        equipo.setDescripcion(campoDescripcion.getText().trim());
        equipo.setMarca(campoMarca.getText().trim());
        equipo.setModelo(campoModelo.getText().trim());
        equipo.setNumeroSerie(campoNumeroSerie.getText().trim().isEmpty() ? null : campoNumeroSerie.getText().trim());
        equipo.setDisponible(checkBoxDisponible.isSelected() ? '1' : '0');

        String tipoSeleccionado = campoTipoEquipo.getValue();
        if (tipoSeleccionado != null && tipoEquipoNameToIdMap.containsKey(tipoSeleccionado)) {
            equipo.setIdTipoEquipo(tipoEquipoNameToIdMap.get(tipoSeleccionado));
        } else {
            // Esto no debería ocurrir si validarCampos() funciona correctamente
            equipo.setIdTipoEquipo(0); // O manejar como un error
        }
        return equipo;
    }

    private boolean validarCampos() {
        if (campoCodigoBien.getText().trim().isEmpty() ||
                campoDescripcion.getText().trim().isEmpty() ||
                campoMarca.getText().trim().isEmpty() ||
                campoModelo.getText().trim().isEmpty() ||
                campoTipoEquipo.getValue() == null) {
            mostrarMensaje("Todos los campos obligatorios (excepto Número de Serie) deben ser llenados.", "error");
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
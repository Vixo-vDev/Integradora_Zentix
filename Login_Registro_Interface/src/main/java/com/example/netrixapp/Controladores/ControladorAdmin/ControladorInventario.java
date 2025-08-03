package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.VistasAdmin.VistaInventario;
import impl.EquipoDaoImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ControladorInventario {
    private VistaInventario vista;
    private final EquipoDaoImpl equipoDao;

    public ControladorInventario(VistaInventario vista) {
        this.vista = vista;
        this.equipoDao = new EquipoDaoImpl();
        cargarInventario();

    }

    public void cargarInventario() {
        try {
            List<Equipo> listaEquipos = equipoDao.findAll();
            vista.mostrarEquipos(listaEquipos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarEquipo() {
        Alert editar = vista.mostrarFormularioAgregar();
        Optional<ButtonType> resultado = editar.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                String codidgo = vista.getCodigoBien();
                String descripcion = vista.getDescripcion();
                String marca = vista.getMarca();
                String modelo = vista.getModelo();
                String numero_serie = vista.getNumeroSerie();
                int tipo_equipo = vista.getTipoEquipo();

                if (codidgo.isEmpty() || descripcion.isEmpty() || marca == null || modelo == null ||
                        numero_serie.isEmpty()) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setHeaderText("Campos vacíos");
                    alerta.setContentText("Por favor completa todos los campos requeridos.");
                    alerta.showAndWait();
                    return;
                }

                Equipo equipo = new Equipo();
                equipo.setCodigo_bien(codidgo);
                equipo.setDescripcion(descripcion);
                equipo.setMarca(marca);
                equipo.setModelo(modelo);
                equipo.setNumero_serie(numero_serie);
                equipo.setTipo_equipo(tipo_equipo);

                equipoDao.create(equipo);
                cargarInventario();

                Alert exito = new Alert(Alert.AlertType.INFORMATION);
                exito.setTitle("Éxito");
                exito.setHeaderText(null);
                exito.setContentText("Equipo agregado correctamente.");
                exito.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setHeaderText("Error al guardar");
                error.setContentText("Verifica los datos e intenta nuevamente.");
                error.showAndWait();
            }
        }
    }
        public void eliminarUsuario(Equipo equipo) {
            Alert alerta = vista.confirmareliminarEquipo(equipo);
            Optional<ButtonType> resultado = alerta.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    equipoDao.delete(equipo);

                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Equipo eliminado");
                    info.setHeaderText(null);
                    info.setContentText("Equipo eliminado correctamente.");
                    info.showAndWait();

                    cargarInventario();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("No se pudo eliminar el equipo.");
                    error.setContentText("Ocurrió un error inesperado.");
                    error.showAndWait();
                }
            }
        }
    }

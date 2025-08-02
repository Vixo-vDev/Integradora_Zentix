package com.example.netrixapp.Controladores.ControladorAdmin;

import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.VistasAdmin.VistaUsuarios;
import impl.UsuarioDaoImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ControladorUsuarios {
    private VistaUsuarios vista;
    private final UsuarioDaoImpl usuarioDao;

    public ControladorUsuarios(VistaUsuarios vista) {
        this.vista = vista;
        this.usuarioDao = new UsuarioDaoImpl();
        cargarUsuarios();
    }

    public void cargarUsuarios() {
        try {
            List<Usuario> listaUsuarios = usuarioDao.findAll();
            vista.mostrarUsuarios(listaUsuarios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarUsuarioEditado(Usuario usuarioOriginal) {
        Alert editar = vista.editarUsuario(usuarioOriginal);
        Optional<ButtonType> resultado = editar.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            int id = usuarioOriginal.getId_usuario();
            System.out.println(id);
            try {
                String nombre = vista.getNombre();
                String apellidos = vista.getApellidos();
                String correo = vista.getCorreo();
                String direccion = vista.getCalle();
                String lada = vista.getLada();
                String telefono = vista.getTelefono();
                String matricula = vista.getmatricula();
                String rol = vista.getRol();
                int edad = vista.getEdad();
                LocalDate fechaNacimiento = vista.getFecha();

                if (nombre.isEmpty() || correo.isEmpty() || rol == null || fechaNacimiento == null) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setHeaderText("Campos vacíos");
                    alerta.setContentText("Por favor completa todos los campos requeridos.");
                    alerta.showAndWait();
                    return;
                }

                // Actualizar el objeto usuario
                usuarioOriginal.setNombre(nombre);
                usuarioOriginal.setApellidos(apellidos);
                usuarioOriginal.setCorreo(correo);
                usuarioOriginal.setDireccion(direccion);
                usuarioOriginal.setLada(lada);
                usuarioOriginal.setTelefono(telefono);
                usuarioOriginal.setDate(fechaNacimiento);
                usuarioOriginal.setEdad(edad);
                usuarioOriginal.setRol(rol);
                usuarioOriginal.setMatricula(matricula);

                // Actualizar en base de datos
                usuarioDao.update(usuarioOriginal, id);

                // Recargar lista
                cargarUsuarios();

                Alert exito = new Alert(Alert.AlertType.INFORMATION);
                exito.setTitle("Éxito");
                exito.setHeaderText(null);
                exito.setContentText("Usuario actualizado correctamente.");
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

    public void eliminarUsuario(Usuario usuario) {
        Alert alerta = vista.confirmarEliminacion(usuario);
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                usuarioDao.delete(usuario);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Usuario eliminado");
                info.setHeaderText(null);
                info.setContentText("Usuario eliminado correctamente.");
                info.showAndWait();

                cargarUsuarios();
            } catch (Exception e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("No se pudo eliminar el usuario.");
                error.setContentText("Ocurrió un error inesperado.");
                error.showAndWait();
            }
        }
    }
}

package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.VistaRegistro;
import impl.UsuarioDaoImpl;
import javafx.scene.control.Alert;

import java.time.LocalDate;

public class ControladorRegistro {

    UsuarioDaoImpl  usuarioDao = new UsuarioDaoImpl();
    private VistaRegistro vista;

    public ControladorRegistro(VistaRegistro vista) {
        this.vista = vista;

        vista.getBotonContinuar().setOnAction(e -> registrarUsuario());
    }

    public void registrarUsuario() {
        try {
            String nombre = vista.getNombre();
            String apellidos = vista.getApellidos();
            String correo = vista.getCorreo();
            String calle = vista.getCalle();
            String lada = vista.getLada();
            String telefono = vista.getTelefono();
            LocalDate date = vista.getFecha();
            int edad = vista.getEdad();
            String rol = vista.getRol();
            String password = vista.getPassword();
            String passwordConfirmar = vista.getConfirmarPassword();

            Usuario usuario = new Usuario(nombre, apellidos, correo, calle, lada, telefono, date, edad, rol, password);


            if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || calle.isEmpty() || lada.isEmpty()
                    || telefono.isEmpty() || date == null || rol.isEmpty() || password.isEmpty() || passwordConfirmar.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("Error en registro");
                alerta.setContentText("Por favor revisa los campos e intenta nuevamente");
                alerta.showAndWait();
            }
            else{

                usuarioDao.create(usuario);
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("Registro exitoso");
                alerta.setContentText("Tu cuenta ha sido registrada.");
                alerta.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Edad inválida");
            alerta.setContentText("Por favor ingresa un número válido en el campo de edad.");
            alerta.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean registro(Usuario usuario, String confirmPassword) {
        if(usuario.getNombre().isEmpty() ||
                usuario.getApellidos().isEmpty() ||
                usuario.getCorreo().isEmpty() ||
                usuario.getLada().isEmpty() ||
                usuario.getTelefono().isEmpty() ||
                usuario.getDate() == null ||
                usuario.getRol().isEmpty() ||
                usuario.getPassword().isEmpty() ||
                confirmPassword.isEmpty()){

            return false;
        }

        if(!usuario.getPassword().equals(confirmPassword)){
            return false;
        }
        return true;
    }

}

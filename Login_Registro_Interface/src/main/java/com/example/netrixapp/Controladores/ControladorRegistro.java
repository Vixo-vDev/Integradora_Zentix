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
            String matricula = vista.getmatricula();
            String password = vista.getPassword();
            String confirmarPassword = vista.getConfirmarPassword();
            String passwordConfirmar = vista.getConfirmarPassword();

            Usuario usuario = new Usuario(nombre, apellidos, correo, calle, lada, telefono, date, edad, rol, matricula, password);


            if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || calle.isEmpty() || lada.isEmpty()
                    || telefono.isEmpty() || date == null || rol.isEmpty() || matricula.isEmpty() || password.isEmpty() || passwordConfirmar.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("Error en registro");
                alerta.setContentText("Por favor revisa los campos e intenta nuevamente");
                alerta.showAndWait();
            }

            else if(password.equals(confirmarPassword)){
                usuarioDao.create(usuario);
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setHeaderText("Registro exitoso");
                alerta.setContentText("Tu cuenta ha sido registrada.");
                alerta.showAndWait();
            }
            else{
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("Contraseñas incorrectas");
                alerta.setContentText("Las contraseñas deben ser iguales");
                alerta.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Edad inválida");
            alerta.setContentText("Por favor ingresa un número válido en el campo de edad.");
            alerta.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

}

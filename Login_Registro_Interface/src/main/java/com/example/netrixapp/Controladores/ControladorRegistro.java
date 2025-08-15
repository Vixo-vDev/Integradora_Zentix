package com.example.netrixapp.Controladores;

import java.time.LocalDate;

import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.VistaLogin;
import com.example.netrixapp.Vistas.VistaRegistro;

import impl.UsuarioDaoImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

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

            // Validar formato de correo institucional
            if (!validarCorreoInstitucional(correo)) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setHeaderText("Correo inválido");
                alerta.setContentText("Solo se permiten correos institucionales '@utez.edu.mx'");
                alerta.showAndWait();
                return;
            }

            // Validar contraseña segura
            if (!validarContrasenaSegura(password)) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setHeaderText("Contraseña insegura");
                alerta.setContentText("La contraseña debe contener al menos:\n• 8 caracteres\n• Una letra\n• Un número\n• Un carácter especial");
                alerta.showAndWait();
                return;
            }

            Usuario usuario = new Usuario(nombre, apellidos, correo, calle, lada, telefono, date, edad, rol, matricula, password, "ACTIVO");


            if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || calle.isEmpty() || lada.isEmpty()
                    || telefono.isEmpty() || date == null || rol.isEmpty() || matricula.isEmpty() || password.isEmpty() || passwordConfirmar.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("Error en registro");
                alerta.setContentText("Por favor revisa los campos e intenta nuevamente");
                alerta.showAndWait();
            }

            else if(password.equals(confirmarPassword)){
                usuarioDao.create(usuario);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Registro exitoso");
                ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(btnConfirmar);

                    if (alert.showAndWait().orElse(btnConfirmar) == btnConfirmar) {

                        Stage stage = (Stage) vista.getBotonContinuar().getScene().getWindow();
                        VistaLogin siguienteVentana = new VistaLogin();
                        siguienteVentana.start(stage);
                    }
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

    /**
     * Valida que el correo electrónico sea un correo institucional válido de UTEZ
     * @param correo El correo a validar
     * @return true si el correo es válido, false en caso contrario
     */
    private boolean validarCorreoInstitucional(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        
        // Convertir a minúsculas para la validación
        String correoLower = correo.trim().toLowerCase();
        
        // Verificar que termine con @utez.edu.mx
        if (!correoLower.endsWith("@utez.edu.mx")) {
            return false;
        }
        
        // Verificar que tenga al menos un carácter antes del @
        String parteLocal = correoLower.substring(0, correoLower.indexOf('@'));
        if (parteLocal.isEmpty()) {
            return false;
        }
        
        // Verificar que no contenga espacios
        if (correoLower.contains(" ")) {
            return false;
        }
        
        // Verificar que solo contenga caracteres válidos (letras, números, puntos, guiones bajos)
        if (!parteLocal.matches("^[a-zA-Z0-9._-]+$")) {
            return false;
        }
        
        return true;
    }

    /**
     * Valida que la contraseña cumpla con los requisitos de seguridad
     * @param contrasena La contraseña a validar
     * @return true si la contraseña es segura, false en caso contrario
     */
    private boolean validarContrasenaSegura(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }
        
        // Verificar que contenga al menos una letra
        boolean tieneLetra = contrasena.matches(".*[a-zA-Z].*");
        
        // Verificar que contenga al menos un número
        boolean tieneNumero = contrasena.matches(".*\\d.*");
        
        // Verificar que contenga al menos un carácter especial
        boolean tieneCaracterEspecial = contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        
        return tieneLetra && tieneNumero && tieneCaracterEspecial;
    }
}

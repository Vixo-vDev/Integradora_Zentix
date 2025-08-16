package com.example.netrixapp.Controladores;

import com.example.netrixapp.HelloApplication;
import com.example.netrixapp.HelloApplicationAdmin;
import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.VistaLogin;
import impl.UsuarioDaoImpl;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControladorLogin {

    UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
    private VistaLogin vista;

    public ControladorLogin(VistaLogin vista) {
        this.vista = vista;
        vista.getBtnConfirmar().setOnAction(e -> loginUsuario());
    }

    public void loginUsuario() {
        Button btn = vista.getBtnConfirmar();
        String textoOriginal = btn.getText();

        // Cambiar el texto del botón y deshabilitarlo
        btn.setText("Iniciando...");
        btn.setDisable(true);

        // Ejecutar en un hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                String user = vista.getCampoUsuario();
                String pass = vista.getCampoPassword();
                Usuario usuario = usuarioDao.login(user, pass);

                Platform.runLater(() -> {
                    if(user.isEmpty() || pass.isEmpty()){
                        mostrarAlerta("ERROR", "Ingresa todo lo que se te pide.", Alert.AlertType.INFORMATION);
                        restaurarBoton(btn, textoOriginal);
                        return;
                    }

                    // Validar formato de correo institucional
                    if (!validarCorreoInstitucional(user)) {
                        mostrarAlerta("ERROR", "Solo se permiten correos institucionales que terminen con @utez.edu.mx", Alert.AlertType.WARNING);
                        restaurarBoton(btn, textoOriginal);
                        return;
                    }

                    if(usuario != null){
                        if(user.equals("veronicasanchez@utez.edu.mx") || pass.equals("adminveronica")){
                            SesionUsuario.setUsuarioActual(usuario);
                            Stage stage = (Stage) btn.getScene().getWindow();
                            Platform.runLater(() -> {
                                try {
                                    HelloApplicationAdmin siguienteVentana = new HelloApplicationAdmin();
                                    siguienteVentana.start(stage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        else if(usuario.getEstado().equals("ACTIVO")){
                            SesionUsuario.setUsuarioActual(usuario);
                            Stage stage = (Stage) btn.getScene().getWindow();
                            Platform.runLater(() -> {
                                try {
                                    HelloApplication siguienteVentana = new HelloApplication();
                                    siguienteVentana.start(stage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        else{
                            mostrarAlerta("ERROR", "Esta cuenta no está activa", Alert.AlertType.CONFIRMATION);
                            restaurarBoton(btn, textoOriginal);
                        }
                    }
                    else{
                        mostrarAlerta("ERROR", "Esta cuenta no existe o credenciales incorrectas", Alert.AlertType.CONFIRMATION);
                        restaurarBoton(btn, textoOriginal);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    e.printStackTrace();
                    mostrarAlerta("Error", "Ocurrió un error durante el inicio de sesión", Alert.AlertType.ERROR);
                    restaurarBoton(btn, textoOriginal);
                });
            }
        }).start();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void restaurarBoton(Button btn, String textoOriginal) {
        btn.setText(textoOriginal);
        btn.setDisable(false);
    }

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
}
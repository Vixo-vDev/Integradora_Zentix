package com.example.panelnormal.Controladores;


import com.example.panelnormal.Vistas.VistaConfiguracion;
import javafx.scene.control.Alert;

public class ControladorConfiguracion {
    private VistaConfiguracion vista;

    public ControladorConfiguracion(VistaConfiguracion vista) {
        this.vista = vista;
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.getDialogPane().setStyle("-fx-background-color: #f5f7fa; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
        alert.showAndWait();
    }
}
package mx.edu.utez.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import mx.edu.utez.demo.dao.impl.UsuarioDaoImpl;

import java.util.EventListener;

public class UsuarioController {

    @FXML
    private TextField txtcorreo;
    @FXML
    private TextField txtPass;

    @FXML
    private void onLogin(ActionEvent e) {
        String correo = txtcorreo.getText().trim();
        String pass = txtPass.getText().trim();

        UsuarioDaoImpl dao = new UsuarioDaoImpl();
        try {
            if (dao.login(correo, pass)) {
                System.out.println("Se pudo logear con Exito!");

            } else {
                showAlert("Error", "Credenciales incorrectas");
                System.out.println("Credenciales incorrectas!");
            }

        } catch (Exception err) {
            showAlert("Error", "Hubo un error en la aplicación");
            System.out.println(err.getMessage());
        }

    }

    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}

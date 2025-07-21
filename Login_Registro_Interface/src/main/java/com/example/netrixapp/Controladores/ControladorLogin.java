package com.example.netrixapp.Controladores;


import com.example.netrixapp.HelloApplication;
import com.example.netrixapp.Vistas.VistaLogin;
import impl.UsuarioDaoImpl;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ControladorLogin {

    UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
    private VistaLogin vista;

    public ControladorLogin(VistaLogin vista) {
        this.vista = vista;

        vista.getBtnConfirmar().setOnAction(e -> loginUsuario());
    }

    public void loginUsuario() {
        try{

            String user = vista.getCampoUsuario();
            String pass = vista.getCampoPassword();
            boolean loginexitoso = usuarioDao.login(user, pass);

            if(user.isEmpty() || pass.isEmpty()){
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("Ingresa todo lo que se te pide");
                alerta.showAndWait();
            }

            else if(loginexitoso){
                Stage stage = (Stage) vista.getBtnConfirmar().getScene().getWindow();
                HelloApplication siguienteVentana = new HelloApplication();
                siguienteVentana.start(stage);
            }
            else{
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("Esta cuenta no existe o credenciales incorrectas");
                alerta.showAndWait();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

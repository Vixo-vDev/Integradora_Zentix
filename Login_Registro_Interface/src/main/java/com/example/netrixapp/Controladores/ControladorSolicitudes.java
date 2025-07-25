package com.example.netrixapp.Controladores;

import com.example.netrixapp.Vistas.VistaSolicitudes;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class ControladorSolicitudes {
    private VistaSolicitudes vista;

    //Declaracion de atributos
    String nota = vista.getNota();
    LocalDate fecha = vista.getFecha();
    String tiempoUso = vista.getTiempoUso();
    String equipos = vista.getEquipos();
    int cantidad = vista.getCantidad();

    public ControladorSolicitudes(VistaSolicitudes vista) {

        this.vista = vista;
        vista.getBtnAgregar().setOnAction(e -> agregarArticulo());
        vista.getBtnEnviar().setOnAction(e -> enviarSolicitud());
    }

    public boolean verificarCampos(){
        if(nota.isEmpty() || fecha == null || tiempoUso.isEmpty() || equipos.isEmpty() || cantidad == 0){
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("ERROR");
            alerta.setContentText("Hay campos sin completar");
            alerta.showAndWait();

            return false;
        }

        else{
            return true;
        }
    }

    public void agregarArticulo() {
        if(verificarCampos()){

        }
    }

    public void enviarSolicitud() {
        if(verificarCampos()){
            try{

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
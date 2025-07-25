package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;
import com.example.netrixapp.Vistas.VistaSolicitudes;
import impl.SolicitudDaoImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class ControladorSolicitudes {

    SolicitudDaoImpl solicitudDao = new SolicitudDaoImpl();
    Usuario usuario = SesionUsuario.getUsuarioActual();
    int id_usuario = usuario.getId_usuario();
    private VistaSolicitudes vista;

    //Declaracion de atributos
    String razon = vista.getNota();
    LocalDate fecha_solicitud = vista.getFecha();
    String tiempoUso = vista.getTiempoUso();
    String articulo = vista.getEquipos();
    int cantidad = vista.getCantidad();

    public ControladorSolicitudes(VistaSolicitudes vista) {

        this.vista = vista;
        vista.getBtnAgregar().setOnAction(e -> agregarArticulo());
        vista.getBtnEnviar().setOnAction(e -> enviarSolicitud());
    }

    public boolean verificarCampos(){
        if(razon.isEmpty() || fecha_solicitud == null || tiempoUso.isEmpty() || articulo.isEmpty() || cantidad == 0){
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
                Solicitud solicitud = new Solicitud(id_usuario,fecha_solicitud, articulo, cantidad, null, tiempoUso, razon, "Pendiente");
                solicitudDao.create(solicitud);
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("ÉXTIO");
                alerta.setContentText("Solicitud enviada con éxito");
                alerta.showAndWait();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
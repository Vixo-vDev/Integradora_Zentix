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
    String razon;
    LocalDate fecha_solicitud;
    String tiempoUso;
    String articulo;
    int cantidad;
    LocalDate fecha_registro;

    public ControladorSolicitudes(VistaSolicitudes vista) {

        this.vista = vista;
        vista.getBtnAgregar().setOnAction(e -> agregarArticulo());
        vista.getBtnEnviar().setOnAction(e -> enviarSolicitud());
    }

    public boolean verificarCampos(){

        razon = vista.getNota();
        fecha_solicitud = LocalDate.now();
        tiempoUso = vista.getTiempoUso();
        articulo = vista.getEquipos();
        cantidad = vista.getCantidad();
        fecha_registro = vista.getFecha_recibo();


        if(razon.isEmpty() || fecha_registro == null || tiempoUso.isEmpty() || articulo.isEmpty() || articulo == "" || cantidad == 0 ){
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
            System.out.println("ola1");
        }
    }

    public void enviarSolicitud() {
        System.out.println("ola");
        if(verificarCampos()){
            try{
                Solicitud solicitud = new Solicitud(id_usuario,fecha_solicitud, articulo, cantidad, fecha_registro, tiempoUso, razon, "pendiente");
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
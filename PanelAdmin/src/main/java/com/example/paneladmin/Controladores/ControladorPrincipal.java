package com.example.paneladmin.Controladores;


import com.example.paneladmin.Vistas.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ControladorPrincipal {
    private VistaPrincipal vista;
    private VBox barraLateral;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        barraLateral = crearBarraLateral();
        vista.getRaiz().setLeft(barraLateral);
        mostrarContenidoBienvenida();
    }

    private VBox crearBarraLateral() {
        VBox barraLateral = new VBox(15);
        barraLateral.setStyle("-fx-background-color: #2c3e50;");
        barraLateral.setPadding(new Insets(20, 15, 20, 15));
        barraLateral.setMinWidth(250);
        barraLateral.setMaxWidth(250);

        // Información del usuario
        Label nombreUsuario = new Label("Tony");
        nombreUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nombreUsuario.setTextFill(Color.WHITE);

        Label rolUsuario = new Label("Administrador");
        rolUsuario.setFont(Font.font("Arial", 14));
        rolUsuario.setTextFill(Color.LIGHTGRAY);

        barraLateral.getChildren().addAll(nombreUsuario, rolUsuario, new Separator());

        // Botones del menú
        Button btnInventario = crearBotonMenu("Inventario", e -> mostrarVistaInventario());
        Button btnUsuarios = crearBotonMenu("Usuarios", e -> mostrarVistaUsuarios());
        Button btnEstadisticas = crearBotonMenu("Estadisticas", e -> mostrarVistaEstadisticas());
        Button btnFormularios = crearBotonMenu("Formularios", e -> mostrarVistaFormularios());
        Button btnSolicitudes = crearBotonMenu("Solicitudes", e -> mostrarVistaSolicitudes());

        barraLateral.getChildren().addAll(btnInventario, btnUsuarios, btnEstadisticas,
                btnFormularios, btnSolicitudes, new Separator());

        // Botones inferiores
        Button btnConfig = crearBotonMenu("Configuracion", e -> mostrarVistaConfiguracion());
        Button btnCerrarSesion = crearBotonCerrarSesion();

        barraLateral.getChildren().addAll(btnConfig, btnCerrarSesion);

        return barraLateral;
    }

    private Button crearBotonMenu(String texto, javafx.event.EventHandler<javafx.event.ActionEvent> accion) {
        Button boton = new Button(texto);
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setAlignment(Pos.CENTER_LEFT);
        boton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 15;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 15;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 15;"));
        boton.setOnAction(accion);
        return boton;
    }

    private Button crearBotonCerrarSesion() {
        Button boton = new Button("Cerrar Sesion");
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setAlignment(Pos.CENTER_LEFT);
        boton.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-padding: 10 15;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: #34495e; -fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-padding: 10 15;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-padding: 10 15;"));
        boton.setOnAction(e -> mostrarConfirmacionCerrarSesion());
        return boton;
    }



    private void mostrarVistaInventario() {
        VistaInventario vistaInventario = new VistaInventario();
        vista.getRaiz().setCenter(vistaInventario.getVista());
    }

    private void mostrarVistaUsuarios() {
        VistaUsuarios vistaUsuarios = new VistaUsuarios();
        vista.getRaiz().setCenter(vistaUsuarios.getVista());
    }

    private void mostrarVistaEstadisticas() {
        VistaEstadisticas vistaEstadisticas = new VistaEstadisticas();
        vista.getRaiz().setCenter(vistaEstadisticas.getVista());
    }

    private void mostrarVistaFormularios() {
        VistaFormularios vistaFormularios = new VistaFormularios();
        vista.getRaiz().setCenter(vistaFormularios.getVista());
    }

    private void mostrarVistaSolicitudes() {
        VistaSolicitudes vistaSolicitudes = new VistaSolicitudes();
        vista.getRaiz().setCenter(vistaSolicitudes.getVista());
    }

    private void mostrarVistaConfiguracion() {
        VistaConfiguracion vistaConfiguracion = new VistaConfiguracion();
        vista.getRaiz().setCenter(vistaConfiguracion.getVista());
    }

    private void mostrarConfirmacionCerrarSesion() {
        System.out.println("Cierre de sesion");
    }
}
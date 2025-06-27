package com.example.paneladmin.Controladores;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    private void mostrarVistaSolicitudes() {
    // Ocultar la barra lateral
    vista.getRaiz().setLeft(null);

    HBox barraSuperior = crearBarraSuperior();

    VistaSolicitudes vistaSolicitudes = new VistaSolicitudes();

    BorderPane contenedor = new BorderPane();
    contenedor.setTop(barraSuperior);
    contenedor.setCenter(vistaSolicitudes.getVista());

    vista.getRaiz().setCenter(contenedor);
}
    //logos
    private HBox crearBarraSuperior() {
    HBox barraSuperior = new HBox();
    barraSuperior.setPadding(new Insets(20));
    barraSuperior.setSpacing(10);
    barraSuperior.setStyle("-fx-background-color: #D9D9D9; -fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");

    ImageView logoIzquierdo = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/usuario.png")));
    logoIzquierdo.setFitHeight(60);  
    logoIzquierdo.setPreserveRatio(true);

    Label nombreUsuario = new Label("ADMI");
    nombreUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 30));
    nombreUsuario.setTextFill(Color.web("#2c3e50"));
    nombreUsuario.setAlignment(Pos.CENTER_LEFT);

    HBox usuarioBox = new HBox(nombreUsuario);
    usuarioBox.setAlignment(Pos.CENTER_LEFT);

    Region espacioCentro = new Region();
    Region espacioDerecha = new Region();
    HBox.setHgrow(espacioCentro, Priority.ALWAYS);
    HBox.setHgrow(espacioDerecha, Priority.ALWAYS);

    ImageView logoDerecho = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/Logo-utez (1).png")));
    logoDerecho.setFitHeight(60);
    logoDerecho.setPreserveRatio(true);

    barraSuperior.getChildren().addAll(logoIzquierdo, usuarioBox, espacioCentro, espacioDerecha, logoDerecho);
    return barraSuperior;
}

    private VBox crearBarraLateral() {
        VBox barraLateral = new VBox(15);
        barraLateral.setStyle("-fx-background-color: #d9d9d9;");
        barraLateral.setPadding(new Insets(20, 15, 20, 15));
        barraLateral.setMinWidth(250);
        barraLateral.setMaxWidth(250);

        // Imagen de usuario arriba
        ImageView iconoUsuario = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/usuario.png")));
        iconoUsuario.setFitHeight(60);
        iconoUsuario.setPreserveRatio(true);

        // Nombre de usuario y rol
        Label nombreUsuario = new Label("ADMIN");
        nombreUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nombreUsuario.setTextFill(Color.web("#2c3e50")); // Mejor visible con tu fondo

        Label rolUsuario = new Label("Administrador");
        rolUsuario.setFont(Font.font("Arial", 14));
        rolUsuario.setTextFill(Color.GRAY);

        // Ahora agrega icono, nombre y rol en orden
        barraLateral.getChildren().addAll(iconoUsuario, nombreUsuario, rolUsuario, new Separator());

        // Botones de menú
        Button btnInventario = crearBotonMenu("Inventario", e -> mostrarVistaInventario());
        Button btnUsuarios = crearBotonMenu("Usuarios", e -> mostrarVistaUsuarios());
        Button btnEstadisticas = crearBotonMenu("Estadisticas", e -> mostrarVistaEstadisticas());
        Button btnFormularios = crearBotonMenu("Formularios", e -> mostrarVistaFormularios());
        Button btnSolicitudes = crearBotonMenu("Solicitudes", e -> mostrarVistaSolicitudes());

        barraLateral.getChildren().addAll(btnInventario, btnUsuarios, btnEstadisticas,
                btnFormularios, btnSolicitudes, new Separator());

        Button btnConfig = crearBotonMenu("Configuracion", e -> mostrarVistaConfiguracion());
        Button btnCerrarSesion = crearBotonCerrarSesion();

        barraLateral.getChildren().addAll(btnConfig, btnCerrarSesion);

        return barraLateral;
    }

    private Button crearBotonMenu(String texto, javafx.event.EventHandler<javafx.event.ActionEvent> accion) {
        Button boton = new Button(texto);
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setAlignment(Pos.CENTER_LEFT);
        boton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-size: 14px; -fx-padding: 10 15;");
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

    private void mostrarContenidoBienvenida() {
        VBox contenidoBienvenida = new VBox(20);
        contenidoBienvenida.setAlignment(Pos.CENTER);

        Label etiquetaBienvenida = new Label("Bienvenido al Panel de Administracion");
        etiquetaBienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        etiquetaBienvenida.setTextFill(Color.web("#2c3e50"));

        Label etiquetaInstrucciones = new Label("Seleccione una opcion del menu lateral para comenzar");
        etiquetaInstrucciones.setFont(Font.font("Arial", 16));
        etiquetaInstrucciones.setTextFill(Color.GRAY);

        contenidoBienvenida.getChildren().addAll(etiquetaBienvenida, etiquetaInstrucciones);
        vista.getRaiz().setCenter(contenidoBienvenida);
    }

    private void mostrarVistaInventario() {
        VistaInventario vistaInventario = new VistaInventario();

        BorderPane contenedor = new BorderPane();
        contenedor.setCenter(vistaInventario.getVista());

        vista.getRaiz().setCenter(contenedor);
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
    /*private void mostrarVistaSolicitudes() {
        VistaSolicitudes vistaSolicitudes = new VistaSolicitudes();
        vista.getRaiz().setCenter(vistaSolicitudes.getVista());
    }*/

    private void mostrarVistaConfiguracion() {
        VistaConfiguracion vistaConfiguracion = new VistaConfiguracion();
        vista.getRaiz().setCenter(vistaConfiguracion.getVista());
    }

    private void mostrarConfirmacionCerrarSesion() {
        System.out.println("Cierre de sesion");
    }
}
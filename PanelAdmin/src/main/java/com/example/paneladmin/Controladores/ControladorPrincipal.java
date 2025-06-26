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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class ControladorPrincipal {
    private VistaPrincipal vista;
    private VBox barraLateral;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        barraLateral = crearBarraLateral();
        vista.getRaiz().setLeft(barraLateral);
        mostrarContenidoPrincipal();
    }

    private VBox crearBarraLateral() {
        VBox barraLateral = new VBox(15);
        barraLateral.setStyle("-fx-background-color: #a8a8a8;");
        barraLateral.setPadding(new Insets(20, 15, 20, 15));
        barraLateral.setMinWidth(250);
        barraLateral.setMaxWidth(250);


        ImageView logoUtez = new ImageView();



            logoUtez.setImage(new Image(getClass().getResourceAsStream("/com/example/paneladmin/imagenes/logo_utez.png")));
            logoUtez.setFitWidth(100);
            logoUtez.setPreserveRatio(true);



        VBox logoContainer = new VBox(5, logoUtez);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0));


        // Información del usuario
        ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/paneladmin/imagenes/user_placeholder.png"))); // Icono de usuario
        userIcon.setFitWidth(60);
        userIcon.setFitHeight(60);
        Label nombreUsuario = new Label("[Name]"); // Placeholder para el nombre
        nombreUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nombreUsuario.setTextFill(Color.WHITE);

        Label rolUsuario = new Label("[Rol]"); // Placeholder para el rol
        rolUsuario.setFont(Font.font("Arial", 14));
        rolUsuario.setTextFill(Color.LIGHTGRAY);

        VBox userInfo = new VBox(5, userIcon, nombreUsuario, rolUsuario);
        userInfo.setAlignment(Pos.CENTER);
        userInfo.setPadding(new Insets(0, 0, 20, 0));

        barraLateral.getChildren().addAll(logoContainer, userInfo, new Separator());

        // Botones del menú
        Button btnInventario = crearBotonMenu("Inventario", e -> mostrarVistaInventario());
        Button btnUsuarios = crearBotonMenu("Usuarios", e -> mostrarVistaUsuarios());
        Button btnEstadisticas = crearBotonMenu("Estadisticas", e -> mostrarVistaEstadisticas());
        Button btnFormularios = crearBotonMenu("Formularios", e -> mostrarVistaFormularios());
        Button btnSolicitudes = crearBotonMenu("Solicitudes", e -> mostrarVistaSolicitudes());

        barraLateral.getChildren().addAll(btnInventario, btnUsuarios, btnEstadisticas,
                btnFormularios, btnSolicitudes, new Separator());

        // Botones inferiores
        Button btnConfig = crearBotonMenu("Configuración", e -> mostrarVistaConfiguracion());
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
        Button boton = new Button("Cerrar Sesión");
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setAlignment(Pos.CENTER_LEFT);
        boton.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-padding: 10 15;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: #34495e; -fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-padding: 10 15;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-padding: 10 15;"));
        boton.setOnAction(e -> mostrarConfirmacionCerrarSesion());
        return boton;
    }

    private void mostrarContenidoPrincipal() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(50));
        grid.setHgap(40); // Espacio horizontal entre tarjetas
        grid.setVgap(40); // Espacio vertical entre tarjetas
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #ecf0f1;"); // Fondo gris claro

        VBox tarjetaInventario = crearTarjeta("Inventario", "/com/example/paneladmin/iconos/inventario_icon.png", e -> mostrarVistaInventario());
        VBox tarjetaFormulario = crearTarjeta("Formulario", "/com/example/paneladmin/iconos/formulario_icon.png", e -> mostrarVistaFormularios());
        VBox tarjetaSolicitudes = crearTarjeta("Solicitudes", "/com/example/paneladmin/iconos/solicitudes_icon.png", e -> mostrarVistaSolicitudes());
        VBox tarjetaUsuarios = crearTarjeta("Usuarios", "/com/example/paneladmin/iconos/usuarios_icon.png", e -> mostrarVistaUsuarios());
        VBox tarjetaEstadisticas = crearTarjeta("Estadísticas", "/com/example/paneladmin/iconos/estadisticas_icon.png", e -> mostrarVistaEstadisticas());

        grid.add(tarjetaInventario, 0, 0);
        grid.add(tarjetaFormulario, 1, 0);
        grid.add(tarjetaSolicitudes, 2, 0);
        grid.add(tarjetaUsuarios, 0, 1);
        grid.add(tarjetaEstadisticas, 1, 1);

        vista.getRaiz().setCenter(grid);
    }

    private VBox crearTarjeta(String titulo, String iconoRuta, javafx.event.EventHandler<javafx.event.ActionEvent> accionVer) {
        VBox tarjeta = new VBox(10);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefSize(180, 180); // Ajusta el tamaño de la tarjeta
        tarjeta.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-border-width: 1;"); // Fondo gris claro, bordes redondeados

        ImageView icono = new ImageView();

            icono.setImage(new Image(getClass().getResourceAsStream(iconoRuta)));
            icono.setFitWidth(80); // Tamaño del icono
            icono.setFitHeight(80);


        Label etiquetaTitulo = new Label(titulo);
        etiquetaTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        etiquetaTitulo.setTextFill(Color.web("#2c3e50"));

        Button btnVer = new Button("Ver");
        btnVer.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 20; -fx-background-radius: 5;");
        btnVer.setOnMouseEntered(e -> btnVer.setStyle("-fx-background-color: derive(#2ecc71, -15%); -fx-text-fill: white; -fx-padding: 5 20; -fx-background-radius: 5;"));
        btnVer.setOnMouseExited(e -> btnVer.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 5 20; -fx-background-radius: 5;"));
        btnVer.setOnAction(accionVer);

        tarjeta.getChildren().addAll(icono, etiquetaTitulo, btnVer);

        return tarjeta;
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
        System.out.println("Cierre de sesión: Implementar diálogo de confirmación aquí.");
        // Puedes agregar un Alert para confirmar el cierre de sesión si lo deseas.
        // Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea cerrar la sesión?");
        // Optional<ButtonType> result = alert.showAndWait();
        // if (result.isPresent() && result.get() == ButtonType.OK) {
        //     // Lógica para cerrar la sesión (ej. volver a la pantalla de login)
        // }
    }
}
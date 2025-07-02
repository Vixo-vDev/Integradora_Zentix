package com.example.panelnormal.Controladores;

import com.example.panelnormal.Vistas.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.List;

public class ControladorPrincipal {

    private VistaPrincipal vista;

    // Carrusel
    private List<Image> imagenesCarrusel;
    private int indiceCarrusel = 0;
    private ImageView imagenCarruselView;
    private Label descripcionCarrusel;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        inicializarUI();
    }

    private void inicializarUI() {
        VBox barraLateral = crearBarraLateral();
        vista.getRaiz().setLeft(barraLateral);
        mostrarDashboardInicio();
    }

    private VBox crearBarraLateral() {
        VBox barra = new VBox();
        barra.setStyle("-fx-background-color: #D9D9D9; -fx-padding: 20;");
        barra.setAlignment(Pos.TOP_CENTER);
        barra.setSpacing(20);
        barra.setMinWidth(200);

        ImageView imagenPerfil = new ImageView(new Image("file:src/main/resources/imagenes/Utez-Logo.png"));
        imagenPerfil.setFitWidth(180);
        imagenPerfil.setFitHeight(180);
        imagenPerfil.setPreserveRatio(true);

        Label iconoUsuario = new Label("\uD83D\uDC64");
        iconoUsuario.setStyle("-fx-font-size: 36;");

        Label lblNombre = new Label("[Name]");
        lblNombre.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label lblRol = new Label("[Rol]");
        lblRol.setStyle("-fx-font-size: 14;");

        VBox infoUsuario = new VBox(5, imagenPerfil, iconoUsuario, lblNombre, lblRol);
        infoUsuario.setAlignment(Pos.CENTER);

        Button btnConfiguracion = crearBotonLateral("Perfil", "#009475");
        btnConfiguracion.setOnAction(e -> vista.getRaiz().setCenter(new VistaPerfil(() -> {
            mostrarDashboardInicio();
        }).getVista()));

        Button btnCerrarSesion = crearBotonLateral("Cerrar Sesión", "#e74c3c");
        btnCerrarSesion.setOnAction(e -> mostrarConfirmacionCerrarSesion());

        Pane espaciador = new Pane();
        VBox.setVgrow(espaciador, Priority.ALWAYS);

        barra.getChildren().addAll(infoUsuario, espaciador, btnConfiguracion, btnCerrarSesion);
        return barra;
    }

    private Button crearBotonLateral(String texto, String color) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-radius: 10; -fx-min-width: 160; -fx-min-height: 40;");
        return btn;
    }

    public void mostrarDashboardInicio() {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: white; -fx-padding: 30;");
        grid.setHgap(20);
        grid.setVgap(20);

        ColumnConstraints colCentro = new ColumnConstraints();
        colCentro.setPercentWidth(80);
        colCentro.setHgrow(Priority.ALWAYS);

        ColumnConstraints colDerecha = new ColumnConstraints();
        colDerecha.setPercentWidth(20);
        colDerecha.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(colCentro, colDerecha);

        for (int i = 0; i < 3; i++) {
            RowConstraints fila = new RowConstraints();
            fila.setPercentHeight(33.33);
            fila.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(fila);
        }

        HBox cardUsuarios = crearCardSeccion("Formulario", "file:src/main/resources/imagenes/usuarios.png", "[Descripción]");
        HBox cardSolicitudes = crearCardSeccion("Configuracion", "file:src/main/resources/imagenes/solicitudes.png", "[Descripción]");
        HBox cardEstadisticas = crearCardSeccion("Historial", "file:src/main/resources/imagenes/estadisticas.png", "[Descripción]");

        VBox carrusel = crearCarrusel("Inventario");

        GridPane.setHgrow(cardUsuarios, Priority.ALWAYS);
        GridPane.setVgrow(cardUsuarios, Priority.ALWAYS);

        GridPane.setHgrow(cardSolicitudes, Priority.ALWAYS);
        GridPane.setVgrow(cardSolicitudes, Priority.ALWAYS);

        GridPane.setHgrow(cardEstadisticas, Priority.ALWAYS);
        GridPane.setVgrow(cardEstadisticas, Priority.ALWAYS);

        grid.add(cardUsuarios, 0, 0);
        grid.add(cardSolicitudes, 0, 1);
        grid.add(cardEstadisticas, 0, 2);
        grid.add(carrusel, 1, 0, 1, 3);

        vista.getRaiz().setCenter(grid);
    }

    private HBox crearCardSeccion(String titulo, String rutaIcono, String descripcion) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 15;");
        card.setPadding(new Insets(20));
        card.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        card.setAlignment(Pos.CENTER_LEFT);

        VBox contenedorImagen = new VBox();
        contenedorImagen.setAlignment(Pos.CENTER);
        contenedorImagen.setMinWidth(100);

        ImageView icono = new ImageView(new Image(rutaIcono));
        icono.setFitWidth(80);
        icono.setFitHeight(80);
        icono.setPreserveRatio(true);
        contenedorImagen.getChildren().add(icono);

        VBox contenedorTexto = new VBox(10);
        contenedorTexto.setAlignment(Pos.CENTER_LEFT);
        contenedorTexto.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contenedorTexto, Priority.ALWAYS);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label lblDescripcion = new Label(descripcion);
        lblDescripcion.setStyle("-fx-font-size: 14;");
        lblDescripcion.setWrapText(true);
        lblDescripcion.setMaxWidth(300);

        Button btnVer = new Button("VER");
        btnVer.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 8 20; -fx-background-radius: 5;");

        DropShadow sombraNormal = new DropShadow(5, Color.rgb(0, 0, 0, 0.1));
        sombraNormal.setOffsetY(2);
        DropShadow sombraHover = new DropShadow(8, Color.rgb(0, 0, 0, 0.2));
        sombraHover.setOffsetY(3);

        btnVer.setEffect(sombraNormal);

        btnVer.setOnMouseEntered(e -> {
            btnVer.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 8 20; -fx-background-radius: 5;");
            btnVer.setEffect(sombraHover);
            btnVer.setCursor(Cursor.HAND);
        });

        btnVer.setOnMouseExited(e -> {
            btnVer.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 8 20; -fx-background-radius: 5;");
            btnVer.setEffect(sombraNormal);
        });

        btnVer.setOnAction(e -> {
            switch (titulo) {
                case "Formulario" -> vista.getRaiz().setCenter(new VistaFormulario(() -> mostrarDashboardInicio()).getVista());
                case "Configuracion" -> vista.getRaiz().setCenter(new VistaConfiguracion(() -> mostrarDashboardInicio()).getVista());
                case "Historial" -> vista.getRaiz().setCenter(new VistaHistorial(() -> mostrarDashboardInicio()).getVista());
            }
        });

        contenedorTexto.getChildren().addAll(lblTitulo, lblDescripcion, btnVer);
        card.getChildren().addAll(contenedorImagen, contenedorTexto);

        return card;
    }

    private VBox crearCarrusel(String titulo) {
        imagenesCarrusel = List.of(
                new Image("file:src/main/resources/imagenes/inventario1.png"),
                new Image("file:src/main/resources/imagenes/inventario2.png"),
                new Image("file:src/main/resources/imagenes/inventario3.png")
        );

        VBox contenedor = new VBox(10);
        contenedor.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 15;");
        contenedor.setPadding(new Insets(20));
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setMinWidth(250);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        imagenCarruselView = new ImageView(imagenesCarrusel.get(indiceCarrusel));
        imagenCarruselView.setFitWidth(120);
        imagenCarruselView.setFitHeight(90);

        Button btnIzquierda = new Button("\u25C4");
        Button btnDerecha = new Button("\u25BA");
        btnIzquierda.setOnAction(e -> mostrarImagenAnterior());
        btnDerecha.setOnAction(e -> mostrarImagenSiguiente());

        HBox navegacion = new HBox(10, btnIzquierda, btnDerecha);
        navegacion.setAlignment(Pos.CENTER);

        descripcionCarrusel = new Label("Descripción del inventario 1");
        descripcionCarrusel.setStyle("-fx-font-size: 12;");
        descripcionCarrusel.setWrapText(true);
        descripcionCarrusel.setAlignment(Pos.CENTER);

        Button btnVer = new Button("VER");
        btnVer.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 8 20; -fx-background-radius: 5;");

        DropShadow sombraNormal = new DropShadow(5, Color.rgb(0, 0, 0, 0.1));
        sombraNormal.setOffsetY(2);
        DropShadow sombraHover = new DropShadow(8, Color.rgb(0, 0, 0, 0.2));
        sombraHover.setOffsetY(3);

        btnVer.setEffect(sombraNormal);

        btnVer.setOnMouseEntered(e -> {
            btnVer.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 8 20; -fx-background-radius: 5;");
            btnVer.setEffect(sombraHover);
            btnVer.setCursor(Cursor.HAND);
        });

        btnVer.setOnMouseExited(e -> {
            btnVer.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 8 20; -fx-background-radius: 5;");
            btnVer.setEffect(sombraNormal);
        });

        btnVer.setOnAction(e -> vista.getRaiz().setCenter(new VistaInventario(() -> mostrarDashboardInicio()).getVista()));

        contenedor.getChildren().addAll(lblTitulo, imagenCarruselView, navegacion, descripcionCarrusel, btnVer);
        return contenedor;
    }

    private void mostrarImagenAnterior() {
        if (indiceCarrusel > 0) {
            indiceCarrusel--;
        } else {
            indiceCarrusel = imagenesCarrusel.size() - 1;
        }
        actualizarCarrusel();
    }

    private void mostrarImagenSiguiente() {
        if (indiceCarrusel < imagenesCarrusel.size() - 1) {
            indiceCarrusel++;
        } else {
            indiceCarrusel = 0;
        }
        actualizarCarrusel();
    }

    private void actualizarCarrusel() {
        imagenCarruselView.setImage(imagenesCarrusel.get(indiceCarrusel));
        descripcionCarrusel.setText("Descripción del inventario " + (indiceCarrusel + 1));
    }

    private void mostrarConfirmacionCerrarSesion() {
        System.out.println("Cerrando sesión");
    }
}
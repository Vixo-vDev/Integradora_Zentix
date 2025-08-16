package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;

public class VistaPerfil {

    private final BorderPane vista;  // Cambiado a BorderPane para mejor organización
    private final ControladorBarraNavegacion controladorBarra;

    // Colores específicos para esta vista
    private final String COLOR_ACCION_PRINCIPAL = "#3498DB";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_FONDO = "#ECF0F1";
    private final String COLOR_TEXTO = "#2C3E50";
    private final String COLOR_BORDE = "#BDC3C7";

    public VistaPerfil(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Panel de contenido principal
        VBox panelContenido = new VBox(20);
        panelContenido.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        panelContenido.setPadding(new Insets(20));
        VBox.setVgrow(panelContenido, Priority.ALWAYS);

        // Sección de foto de perfil
        VBox seccionFoto = crearSeccionFoto();

        // Formulario de perfil
        GridPane formularioPerfil = crearFormularioPerfil();

        // Botones de acción
        HBox panelBotones = crearPanelBotonesAccion();

        panelContenido.getChildren().addAll(seccionFoto, formularioPerfil, panelBotones);

        // ScrollPane para contenido principal
        ScrollPane scrollPane = new ScrollPane(panelContenido);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Construir vista completa
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setCenter(scrollPane);

        // Configurar márgenes
        BorderPane.setMargin(panelContenido, new Insets(20));
        BorderPane.setMargin(controladorBarra.getBarraLateral(), new Insets(0, 20, 0, 0));
    }

    private VBox crearSeccionFoto() {
        VBox seccionFoto = new VBox(15);
        seccionFoto.setAlignment(Pos.TOP_CENTER);
        seccionFoto.setPadding(new Insets(0, 0, 20, 0));

        // Título "Foto de perfil"
        Label lblTitulo = new Label("Foto de perfil");
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: " + COLOR_TEXTO + ";");

        ImageView imagenPerfil = new ImageView(new Image("file:src/main/resources/imagenes/perfil-default.png"));
        imagenPerfil.setPreserveRatio(true);
        imagenPerfil.setFitWidth(120);
        imagenPerfil.setFitHeight(120);
        imagenPerfil.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        HBox botonesFoto = new HBox(10);
        botonesFoto.setAlignment(Pos.CENTER);

        Button btnEliminarFoto = new Button("Eliminar foto");
        btnEliminarFoto.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + COLOR_PELIGRO + "; " +
                "-fx-border-color: " + COLOR_PELIGRO + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 3; " +
                "-fx-padding: 5 10;");

        Button btnCambiarFoto = new Button("Cambiar foto");
        btnCambiarFoto.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + COLOR_ACCION_PRINCIPAL + "; " +
                "-fx-border-color: " + COLOR_ACCION_PRINCIPAL + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 3; " +
                "-fx-padding: 5 10;");

        btnCambiarFoto.setOnAction(e -> cambiarFotoPerfil(imagenPerfil));
        btnEliminarFoto.setOnAction(e -> imagenPerfil.setImage(new Image("file:src/main/resources/imagenes/perfil-default.png")));

        botonesFoto.getChildren().addAll(btnEliminarFoto, btnCambiarFoto);
        seccionFoto.getChildren().addAll(lblTitulo, imagenPerfil, botonesFoto);

        return seccionFoto;
    }

    private void cambiarFotoPerfil(ImageView imagenPerfil) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagenPerfil.setImage(new Image(file.toURI().toString()));
        }
    }

    private GridPane crearFormularioPerfil() {
        GridPane formularioPerfil = new GridPane();
        formularioPerfil.setHgap(20);
        formularioPerfil.setVgap(15);
        formularioPerfil.setPadding(new Insets(10, 0, 20, 0));

        // Configurar columnas con porcentajes
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        formularioPerfil.getColumnConstraints().addAll(col1, col2);

        // Campos del formulario
        agregarCampoFormulario(formularioPerfil, "Nombre:", new TextField("Administrador"), 0);
        agregarCampoFormulario(formularioPerfil, "Correo:", new TextField("admin@utez.edu.mx"), 1);
        agregarCampoFormulario(formularioPerfil, "División Académica:", new TextField("Sistemas"), 2);
        agregarCampoFormulario(formularioPerfil, "Teléfono:", new TextField("7771234567"), 3);

        // Botón cambiar contraseña
        Button btnCambiarContrasena = new Button("Cambiar Contraseña");
        btnCambiarContrasena.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + COLOR_ACCION_PRINCIPAL + "; " +
                "-fx-border-color: " + COLOR_ACCION_PRINCIPAL + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 3; " +
                "-fx-padding: 5 10;");
        formularioPerfil.add(btnCambiarContrasena, 1, 4);

        return formularioPerfil;
    }

    private void agregarCampoFormulario(GridPane formulario, String etiqueta, Control control, int fila) {
        Label label = new Label(etiqueta);
        label.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        formulario.add(label, 0, fila);

        control.setStyle("-fx-pref-height: 35; -fx-padding: 0 10; -fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; -fx-border-radius: 3; -fx-min-width: 200;");
        HBox.setHgrow(control, Priority.ALWAYS);
        formulario.add(control, 1, fila);
    }

    private HBox crearPanelBotonesAccion() {
        HBox panelBotones = new HBox(15);
        panelBotones.setAlignment(Pos.CENTER_RIGHT);
        panelBotones.setPadding(new Insets(20, 0, 0, 0));

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-border-color: " + COLOR_TEXTO + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 3; " +
                "-fx-padding: 8 20;");

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setStyle("-fx-background-color: " + COLOR_ACCION_PRINCIPAL + "; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 8 20; " +
                "-fx-border-radius: 3;");

        panelBotones.getChildren().addAll(btnCancelar, btnGuardar);
        return panelBotones;
    }

    public BorderPane getVista() {
        return vista;
    }
}
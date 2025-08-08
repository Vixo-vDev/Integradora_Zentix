package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.SesionUsuario;
import com.example.netrixapp.Modelos.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;

public class VistaPerfil {

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final Usuario usuario = SesionUsuario.getUsuarioActual();

    // Paleta de colores moderna
    private final String COLOR_PRIMARIO = "#4F46E5";
    private final String COLOR_SECUNDARIO = "#10B981";
    private final String COLOR_PELIGRO = "#EF4444";
    private final String COLOR_TEXTO_OSCURO = "#1F2937";
    private final String COLOR_TEXTO_NORMAL = "#6B7280";
    private final String COLOR_BORDE = "#E5E7EB";
    private final String COLOR_FONDO = "#FFFFFF";

    public VistaPerfil(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        // Configuración del layout principal
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenedor principal con scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: " + COLOR_FONDO + ";");

        // Panel de contenido principal
        VBox panelContenido = new VBox(25);
        panelContenido.setPadding(new Insets(30));
        panelContenido.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        panelContenido.setAlignment(Pos.TOP_CENTER);

        // Sección de foto de perfil
        VBox seccionFoto = crearSeccionFoto();
        panelContenido.getChildren().add(seccionFoto);

        // Formulario de perfil
        GridPane formularioPerfil = crearFormularioPerfil();
        panelContenido.getChildren().add(formularioPerfil);

        // Botones de acción
        HBox panelBotones = crearPanelBotonesAccion();
        panelContenido.getChildren().add(panelBotones);

        scrollPane.setContent(panelContenido);
        vista.setCenter(scrollPane);
    }

    private VBox crearSeccionFoto() {
        VBox seccionFoto = new VBox(15);
        seccionFoto.setAlignment(Pos.TOP_CENTER);
        seccionFoto.setPadding(new Insets(0, 0, 20, 0));

        // Título "Foto de perfil"
        Label lblTitulo = new Label("Foto de perfil");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        // Imagen de perfil
        ImageView imagenPerfil = new ImageView(new Image("file:src/main/resources/imagenes/perfil-default.png"));
        imagenPerfil.setPreserveRatio(true);
        imagenPerfil.setFitWidth(150);
        imagenPerfil.setFitHeight(150);
        imagenPerfil.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Botones de acción para la foto
        HBox botonesFoto = new HBox(15);
        botonesFoto.setAlignment(Pos.CENTER);

        Button btnEliminarFoto = crearBotonSecundario("Eliminar foto", COLOR_PELIGRO);
        Button btnCambiarFoto = crearBotonSecundario("Cambiar foto", COLOR_PRIMARIO);

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
        formularioPerfil.setPadding(new Insets(20));
        formularioPerfil.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8;");

        // Configuración de columnas
        ColumnConstraints colLabel = new ColumnConstraints();
        colLabel.setPrefWidth(150);
        ColumnConstraints colField = new ColumnConstraints();
        colField.setHgrow(Priority.ALWAYS);
        formularioPerfil.getColumnConstraints().addAll(colLabel, colField);

        // Campos del formulario
        agregarCampoFormulario(formularioPerfil, "Nombre:", new TextField(usuario.getNombre()), 0);
        agregarCampoFormulario(formularioPerfil, "Apellidos:", new TextField(usuario.getApellidos()), 1);
        agregarCampoFormulario(formularioPerfil, "Correo:", new TextField(usuario.getCorreo()), 2);
        agregarCampoFormulario(formularioPerfil, "Domicilio:", new TextField(usuario.getDireccion()), 3);
        agregarCampoFormulario(formularioPerfil, "Lada:", new TextField(usuario.getLada()), 4);
        agregarCampoFormulario(formularioPerfil, "Teléfono:", new TextField(usuario.getTelefono()), 5);

        // Botón cambiar contraseña
        Button btnCambiarContrasena = crearBotonSecundario("Cambiar Contraseña", COLOR_PRIMARIO);
        formularioPerfil.add(btnCambiarContrasena, 1, 6);

        return formularioPerfil;
    }

    private void agregarCampoFormulario(GridPane formulario, String etiqueta, Control control, int fila) {
        Label label = new Label(etiqueta);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");
        formulario.add(label, 0, fila);

        control.setStyle("-fx-font-size: 14px; -fx-padding: 8 12; -fx-background-radius: 6;");
        control.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(control, Priority.ALWAYS);
        formulario.add(control, 1, fila);
    }

    private HBox crearPanelBotonesAccion() {
        HBox panelBotones = new HBox(20);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(20, 0, 0, 0));

        Button btnCancelar = crearBotonSecundario("Cancelar", COLOR_TEXTO_NORMAL);
        Button btnGuardar = crearBotonPrimario("Guardar Cambios");

        panelBotones.getChildren().addAll(btnCancelar, btnGuardar);
        return panelBotones;
    }

    private Button crearBotonPrimario(String texto) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + COLOR_SECUNDARIO + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10 25; " +
                "-fx-background-radius: 6;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: #0D9488; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10 25; " +
                "-fx-background-radius: 6;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: " + COLOR_SECUNDARIO + "; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10 25; " +
                "-fx-background-radius: 6;"));
        return boton;
    }

    private Button crearBotonSecundario(String texto, String color) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + color + "; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10 25; " +
                "-fx-border-color: " + color + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 6;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: rgba(0,0,0,0.05); " +
                "-fx-text-fill: " + color + "; " +
                "-fx-padding: 10 25; " +
                "-fx-border-color: " + color + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 6;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + color + "; " +
                "-fx-padding: 10 25; " +
                "-fx-border-color: " + color + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 6;"));
        return boton;
    }

    public BorderPane getVista() {
        return vista;
    }
}
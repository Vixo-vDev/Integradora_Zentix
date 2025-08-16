package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.SesionUsuario;
import com.example.netrixapp.Modelos.Usuario;
import impl.UsuarioDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class VistaPerfil {

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final Usuario usuario = SesionUsuario.getUsuarioActual();
    
    // Campos del formulario
    private TextField campoNombre;
    private TextField campoApellidos;
    private TextField campoCorreo;
    private TextField campoDireccion;
    private TextField campoLada;
    private TextField campoTelefono;
    
    // DAO para operaciones de base de datos
    private final UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();

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
        campoNombre = new TextField(usuario.getNombre());
        campoApellidos = new TextField(usuario.getApellidos());
        campoCorreo = new TextField(usuario.getCorreo());
        campoDireccion = new TextField(usuario.getDireccion());
        campoLada = new TextField(usuario.getLada());
        campoTelefono = new TextField(usuario.getTelefono());

        agregarCampoFormulario(formularioPerfil, "Nombre:", campoNombre, 0);
        agregarCampoFormulario(formularioPerfil, "Apellidos:", campoApellidos, 1);
        agregarCampoFormulario(formularioPerfil, "Correo:", campoCorreo, 2);
        agregarCampoFormulario(formularioPerfil, "Domicilio:", campoDireccion, 3);
        agregarCampoFormulario(formularioPerfil, "Lada:", campoLada, 4);
        agregarCampoFormulario(formularioPerfil, "Teléfono:", campoTelefono, 5);

        // Botón cambiar contraseña
        Button btnCambiarContrasena = crearBotonSecundario("Cambiar Contraseña", COLOR_PRIMARIO);
        btnCambiarContrasena.setOnAction(e -> mostrarDialogoCambiarContrasena());
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

        // Configurar eventos de los botones
        btnCancelar.setOnAction(e -> restaurarValoresOriginales());
        btnGuardar.setOnAction(e -> guardarCambios());

        panelBotones.getChildren().addAll(btnCancelar, btnGuardar);
        return panelBotones;
    }

    private void restaurarValoresOriginales() {
        campoNombre.setText(usuario.getNombre());
        campoApellidos.setText(usuario.getApellidos());
        campoCorreo.setText(usuario.getCorreo());
        campoDireccion.setText(usuario.getDireccion());
        campoLada.setText(usuario.getLada());
        campoTelefono.setText(usuario.getTelefono());
        
        mostrarAlerta("Información", "Cambios cancelados", "Los cambios han sido cancelados y se han restaurado los valores originales.", Alert.AlertType.INFORMATION);
    }

    private void guardarCambios() {
        try {
            // Obtener valores de los campos
            String nombre = campoNombre.getText().trim();
            String apellidos = campoApellidos.getText().trim();
            String correo = campoCorreo.getText().trim();
            String direccion = campoDireccion.getText().trim();
            String lada = campoLada.getText().trim();
            String telefono = campoTelefono.getText().trim();

            // Validar campos obligatorios
            if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || direccion.isEmpty() || 
                lada.isEmpty() || telefono.isEmpty()) {
                mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios. Por favor, completa toda la información.", Alert.AlertType.ERROR);
                return;
            }

            // Validar formato de correo institucional
            if (!validarCorreoInstitucional(correo)) {
                mostrarAlerta("Error", "Correo inválido", "Solo se permiten correos institucionales que terminen con @utez.edu.mx", Alert.AlertType.ERROR);
                return;
            }

            // Validar formato de teléfono
            if (!validarTelefono(telefono)) {
                mostrarAlerta("Error", "Teléfono inválido", "El teléfono debe contener solo números y tener entre 7 y 10 dígitos.", Alert.AlertType.ERROR);
                return;
            }

            // Validar formato de lada
            if (!validarLada(lada)) {
                mostrarAlerta("Error", "Lada inválida", "La lada debe contener solo números y tener entre 2 y 4 dígitos.", Alert.AlertType.ERROR);
                return;
            }

            // Crear objeto Usuario actualizado
            Usuario usuarioActualizado = new Usuario();
            usuarioActualizado.setId_usuario(usuario.getId_usuario());
            usuarioActualizado.setNombre(nombre);
            usuarioActualizado.setApellidos(apellidos);
            usuarioActualizado.setCorreo(correo);
            usuarioActualizado.setDireccion(direccion);
            usuarioActualizado.setLada(lada);
            usuarioActualizado.setTelefono(telefono);
            usuarioActualizado.setDate(usuario.getDate());
            usuarioActualizado.setEdad(usuario.getEdad());
            usuarioActualizado.setRol(usuario.getRol());
            usuarioActualizado.setMatricula(usuario.getMatricula());
            usuarioActualizado.setPassword(usuario.getPassword());
            usuarioActualizado.setEstado(usuario.getEstado());

            // Actualizar en la base de datos
            usuarioDao.updatePerfil(usuarioActualizado, usuario.getId_usuario());

            // Actualizar el usuario en la sesión
            SesionUsuario.setUsuarioActual(usuarioActualizado);

            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "Perfil actualizado", "Los cambios en tu perfil se han guardado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al guardar", "Ocurrió un error al intentar guardar los cambios. Por favor, inténtalo nuevamente.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarCorreoInstitucional(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        
        // Convertir a minúsculas para la validación
        String correoLower = correo.trim().toLowerCase();
        
        // Verificar que termine con @utez.edu.mx
        if (!correoLower.endsWith("@utez.edu.mx")) {
            return false;
        }
        
        // Verificar que tenga al menos un carácter antes del @
        String parteLocal = correoLower.substring(0, correoLower.indexOf('@'));
        if (parteLocal.isEmpty()) {
            return false;
        }
        
        // Verificar que no contenga espacios
        if (correoLower.contains(" ")) {
            return false;
        }
        
        // Verificar que solo contenga caracteres válidos (letras, números, puntos, guiones bajos)
        if (!parteLocal.matches("^[a-zA-Z0-9._-]+$")) {
            return false;
        }
        
        return true;
    }

    private boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }
        
        // Verificar que solo contenga números
        String telefonoLimpio = telefono.trim().replaceAll("\\s", "");
        if (!telefonoLimpio.matches("^\\d{7,10}$")) {
            return false;
        }
        
        return true;
    }

    private boolean validarLada(String lada) {
        if (lada == null || lada.trim().isEmpty()) {
            return false;
        }
        
        // Verificar que solo contenga números
        String ladaLimpia = lada.trim().replaceAll("\\s", "");
        if (!ladaLimpia.matches("^\\d{2,4}$")) {
            return false;
        }
        
        return true;
    }

    private void mostrarDialogoCambiarContrasena() {
        // Crear diálogo personalizado
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Cambiar Contraseña");
        dialog.setHeaderText("Ingresa tu nueva contraseña");

        // Configurar botones
        ButtonType btnCambiar = new ButtonType("Cambiar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCambiar, btnCancelar);

        // Crear campos de contraseña
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField campoContrasenaActual = new PasswordField();
        PasswordField campoContrasenaNueva = new PasswordField();
        PasswordField campoConfirmarContrasena = new PasswordField();

        grid.add(new Label("Contraseña actual:"), 0, 0);
        grid.add(campoContrasenaActual, 1, 0);
        grid.add(new Label("Nueva contraseña:"), 0, 1);
        grid.add(campoContrasenaNueva, 1, 1);
        grid.add(new Label("Confirmar contraseña:"), 0, 2);
        grid.add(campoConfirmarContrasena, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Configurar resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnCambiar) {
                return "cambiar";
            }
            return null;
        });

        // Mostrar diálogo y procesar resultado
        dialog.showAndWait().ifPresent(result -> {
            if ("cambiar".equals(result)) {
                cambiarContrasena(campoContrasenaActual.getText(), 
                                campoContrasenaNueva.getText(), 
                                campoConfirmarContrasena.getText());
            }
        });
    }

    private void cambiarContrasena(String contrasenaActual, String contrasenaNueva, String confirmarContrasena) {
        try {
            // Validar contraseña actual
            if (!usuario.getPassword().equals(contrasenaActual)) {
                mostrarAlerta("Error", "Contraseña incorrecta", "La contraseña actual no es correcta.", Alert.AlertType.ERROR);
                return;
            }

            // Validar que la nueva contraseña no esté vacía
            if (contrasenaNueva.isEmpty()) {
                mostrarAlerta("Error", "Contraseña vacía", "La nueva contraseña no puede estar vacía.", Alert.AlertType.ERROR);
                return;
            }

            // Validar que las contraseñas coincidan
            if (!contrasenaNueva.equals(confirmarContrasena)) {
                mostrarAlerta("Error", "Contraseñas diferentes", "La nueva contraseña y la confirmación no coinciden.", Alert.AlertType.ERROR);
                return;
            }

            // Validar que la nueva contraseña sea segura
            if (!validarContrasenaSegura(contrasenaNueva)) {
                mostrarAlerta("Error", "Contraseña insegura", "La nueva contraseña debe contener al menos:\n• 8 caracteres\n• Una letra\n• Un número\n• Un carácter especial", Alert.AlertType.ERROR);
                return;
            }

            // Actualizar contraseña en la base de datos
            actualizarContrasenaEnBD(contrasenaNueva);

            // Actualizar contraseña en la sesión
            usuario.setPassword(contrasenaNueva);
            SesionUsuario.setUsuarioActual(usuario);

            mostrarAlerta("Éxito", "Contraseña actualizada", "Tu contraseña se ha cambiado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cambiar contraseña", "Ocurrió un error al intentar cambiar la contraseña.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarContrasenaEnBD(String nuevaContrasena) throws Exception {
        Connection con = null;
        String sql = "UPDATE USUARIO SET PASSWORD = ? WHERE ID_USUARIO = ?";

        try {
            con = config.ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevaContrasena);
            ps.setInt(2, usuario.getId_usuario());
            ps.executeUpdate();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private boolean validarContrasenaSegura(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }
        
        // Verificar que contenga al menos una letra
        boolean tieneLetra = contrasena.matches(".*[a-zA-Z].*");
        
        // Verificar que contenga al menos un número
        boolean tieneNumero = contrasena.matches(".*\\d.*");
        
        // Verificar que contenga al menos un carácter especial
        boolean tieneCaracterEspecial = contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        
        return tieneLetra && tieneNumero && tieneCaracterEspecial;
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private Button crearBotonPrimario(String texto) {
        Button boton = new Button(texto);

        // Estilo INICIAL (sin hover)
        boton.setStyle("-fx-background-color: transparent; " +  // Fondo transparente inicial
                "-fx-text-fill: " + COLOR_SECUNDARIO + "; " +  // Texto en color primario
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10 25; " +
                "-fx-border-color: " + COLOR_SECUNDARIO + "; " +  // Borde del color primario
                "-fx-border-width: 1; " +
                "-fx-border-radius: 6;");

        // Al pasar el mouse: Relleno de color + texto blanco
        boton.setOnMouseEntered(e -> {
            boton.setStyle("-fx-background-color: " + COLOR_SECUNDARIO + "; " +  // Relleno al hover
                    "-fx-text-fill: white; " +  // Texto blanco
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10 25; " +
                    "-fx-border-color: " + COLOR_SECUNDARIO + "; " +
                    "-fx-border-width: 1; " +
                    "-fx-border-radius: 6;");
            boton.setCursor(Cursor.HAND);  // Cursor de mano
        });

        // Al salir el mouse: Vuelve al estado inicial (transparente)
        boton.setOnMouseExited(e -> {
            boton.setStyle("-fx-background-color: transparent; " +
                    "-fx-text-fill: " + COLOR_SECUNDARIO + "; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10 25; " +
                    "-fx-border-color: " + COLOR_SECUNDARIO + "; " +
                    "-fx-border-width: 1; " +
                    "-fx-border-radius: 6;");
            boton.setCursor(Cursor.DEFAULT);  // Cursor normal
        });

        // Eliminamos cualquier efecto al hacer clic
        boton.setOnMousePressed(null);
        boton.setOnMouseReleased(null);

        return boton;
    }

    private Button crearBotonSecundario(String texto, String color) {
        Button boton = new Button(texto);

        // Estilo INICIAL (sin hover)
        boton.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + color + "; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10 25; " +
                "-fx-border-color: " + color + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 6;");

        // Al pasar el mouse: Relleno de color + texto blanco
        boton.setOnMouseEntered(e -> {
            boton.setStyle("-fx-background-color: " + color + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10 25; " +
                    "-fx-border-color: " + color + "; " +
                    "-fx-border-width: 1; " +
                    "-fx-border-radius: 6;");
            boton.setCursor(Cursor.HAND);  // Cursor de mano
        });

        // Al salir el mouse: Vuelve al estado inicial (transparente)
        boton.setOnMouseExited(e -> {
            boton.setStyle("-fx-background-color: transparent; " +
                    "-fx-text-fill: " + color + "; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10 25; " +
                    "-fx-border-color: " + color + "; " +
                    "-fx-border-width: 1; " +
                    "-fx-border-radius: 6;");
            boton.setCursor(Cursor.DEFAULT);  // Cursor normal
        });

        // Eliminamos cualquier efecto al hacer clic
        boton.setOnMousePressed(null);
        boton.setOnMouseReleased(null);

        return boton;
    }

    public BorderPane getVista() {
        return vista;
    }
}
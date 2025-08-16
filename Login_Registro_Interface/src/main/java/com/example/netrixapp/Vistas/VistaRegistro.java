/*package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorRegistro;
import com.example.netrixapp.Modelos.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;

public class VistaRegistro {
    private TextField campoNombre;
    private TextField campoApellidos;
    private TextField campoCorreo;
    private TextField campoCalle;
    private TextField campoLada;
    private TextField campoTelefono;
    private DatePicker campoFecha;
    private TextField campoEdad;
    private ComboBox<String> comboRol;
    private TextField matricula;
    private PasswordField campoContrasena;
    private PasswordField campoConfirmar;
    private Button botonContinuar;

    public String getNombre() { return campoNombre.getText().trim(); }
    public String getApellidos() { return campoApellidos.getText().trim(); }
    public String getCorreo() { return campoCorreo.getText().trim().toLowerCase(); }
    public String getCalle() { return campoCalle.getText().trim(); }
    public String getLada() { return campoLada.getText().trim(); }
    public String getTelefono() { return campoTelefono.getText().trim(); }
    public LocalDate getFecha() { return campoFecha.getValue(); }
    public int getEdad() { return Integer.parseInt(campoEdad.getText().trim()); }
    public String getRol() { return comboRol.getSelectionModel().getSelectedItem().trim().toUpperCase();  }
    public String getmatricula() { return matricula.getText().trim().toLowerCase(); }
    public String getPassword() { return campoContrasena.getText().trim(); }
    public String getConfirmarPassword() { return campoConfirmar.getText().trim(); }
    public Button getBotonContinuar() {
        return botonContinuar;
    }

    public void start(Stage ventanaRegistro) {

        //Asignando dimensiones de la vetana registro
        ventanaRegistro.setWidth(1000);
        ventanaRegistro.setHeight(600);


        HBox contenedorPrincipal = new HBox();

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.setPrefWidth(300);

        Image logoUtez = new Image(getClass().getResourceAsStream("/imagenes/logo_utez.png"));
        ImageView logoUtezV = new ImageView(logoUtez);
        logoUtezV.setFitHeight(100);
        logoUtezV.setFitWidth(200);
        logoUtezV.setPreserveRatio(true);

        VBox contenedorVertical = new VBox(logoUtezV);
        contenedorVertical.setAlignment(Pos.TOP_CENTER);  // imagen arriba y centrada
        contenedorVertical.setPadding(new Insets(20));    // espacio superior


        Rectangle rectanguloFondo = new Rectangle();
        rectanguloFondo.setFill(Color.LIGHTGRAY);
        rectanguloFondo.widthProperty().bind(panelIzquierdo.widthProperty());
        rectanguloFondo.heightProperty().bind(panelIzquierdo.heightProperty());

        panelIzquierdo.getChildren().addAll(rectanguloFondo);

        VBox panelDerecho = new VBox(20);
        panelDerecho.setPadding(new Insets(40));
        panelDerecho.setAlignment(Pos.TOP_LEFT);
        panelDerecho.setPrefWidth(700);

        Label titulo = new Label("Registrarse");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(15);
        gridCampos.setVgap(15);
        gridCampos.setAlignment(Pos.TOP_LEFT);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(150);
        col1.setHgrow(Priority.NEVER);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(150);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(150);
        col3.setHgrow(Priority.ALWAYS);

        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPrefWidth(150);
        col4.setHgrow(Priority.ALWAYS);

        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPrefWidth(150);
        col5.setHgrow(Priority.ALWAYS);

        gridCampos.getColumnConstraints().addAll(col1, col2, col3, col4, col5);

        Label infoPersonal = new Label("Información personal");
        infoPersonal.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridCampos.add(infoPersonal, 0, 0, 5, 1);

        // Línea 1: Nombre, Apellidos, Correo institucional
        Label lblNombre = new Label("Nombre:");
        campoNombre = new TextField();
        GridPane.setHgrow(campoNombre, Priority.ALWAYS);
        gridCampos.add(lblNombre, 0, 1);
        gridCampos.add(campoNombre, 0, 2);

        Label lblApellidos = new Label("Apellidos:");
        campoApellidos = new TextField();
        GridPane.setHgrow(campoApellidos, Priority.ALWAYS);
        gridCampos.add(lblApellidos, 1, 1, 2 ,1);
        gridCampos.add(campoApellidos, 1, 2, 2 ,1);

        Label lblCorreo = new Label("Correo institucional:");
        lblCorreo.setWrapText(true);
        campoCorreo = new TextField();
        GridPane.setHgrow(campoCorreo, Priority.ALWAYS);
        
        // Agregar tooltip explicativo
        Tooltip tooltipCorreo = new Tooltip(
            "Ingresa tu correo institucional de UTEZ\n" +
            "Formato: usuario@utez.edu.mx\n" +
            "Ejemplo: juan.perez@utez.edu.mx"
        );
        tooltipCorreo.setStyle("-fx-font-size: 11px;");
        campoCorreo.setTooltip(tooltipCorreo);
        
        // Agregar placeholder text
        campoCorreo.setPromptText("usuario@utez.edu.mx");
        
        // Indicador de validación del correo
        Label lblValidacionCorreo = new Label();
        lblValidacionCorreo.setStyle("-fx-font-size: 11px; -fx-font-style: italic;");
        lblValidacionCorreo.setPadding(new Insets(-5, 0, 5, 0));
        
        // Validación en tiempo real del correo
        campoCorreo.textProperty().addListener((observable, oldValue, newValue) -> {
            validarCorreoTiempoReal(newValue, lblValidacionCorreo);
        });
        
        gridCampos.add(lblCorreo, 3, 1, 2, 1);
        gridCampos.add(campoCorreo, 3, 2, 2, 1);
        gridCampos.add(lblValidacionCorreo, 3, 3, 2, 1);

        // Línea 2: Calle, Lada, Teléfono, Fecha de nacimiento, Edad
        Label lblCalle = new Label("Calle:");
        campoCalle = new TextField();
        GridPane.setHgrow(campoCalle, Priority.ALWAYS);
        gridCampos.add(lblCalle, 0, 3);
        gridCampos.add(campoCalle, 0, 4);

        Label lblLada = new Label("Lada:");
        campoLada = new TextField();
        campoLada.setPrefWidth(60);
        gridCampos.add(lblLada, 1, 3);
        gridCampos.add(campoLada, 1, 4);

        Label lblTelefono = new Label("Teléfono:");
        campoTelefono = new TextField();
        GridPane.setHgrow(campoTelefono, Priority.ALWAYS);
        gridCampos.add(lblTelefono, 2, 3);
        gridCampos.add(campoTelefono, 2, 4);

        Label lblFecha = new Label("Fecha de nacimiento:");
        campoFecha = new DatePicker();
        GridPane.setHgrow(campoTelefono, Priority.ALWAYS);
        gridCampos.add(lblFecha, 3, 3, 2, 1);
        gridCampos.add(campoFecha, 3, 4);

        Label lblEdad = new Label("Edad:");
        campoEdad = new TextField();
        campoEdad.setPrefWidth(60);
        campoEdad.setEditable(false);
        gridCampos.add(lblEdad, 4, 3);
        gridCampos.add(campoEdad, 4, 4);

        campoFecha.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int edadCalculada = Period.between(newValue, LocalDate.now()).getYears();
                campoEdad.setText(String.valueOf(edadCalculada));
            } else {
                campoEdad.clear(); // Si se borra la fecha, limpia edad también
            }
        });

        Label infoAcademica = new Label("Información académica");
        infoAcademica.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridCampos.add(infoAcademica, 0, 5, 5, 1);

        // Línea 3: ComboBox debajo del label
        Label lblRol = new Label("Selecciona tu Rol:");
        comboRol = new ComboBox<>();
        comboRol.getItems().addAll("Alumno", "Docente");
        comboRol.setPromptText("Selecciona tu Rol");
        GridPane.setHgrow(comboRol, Priority.ALWAYS);
        gridCampos.add(lblRol, 0, 6, 5, 1);
        gridCampos.add(comboRol, 0, 7, 3, 1);

        Label lblMatricula = new Label("Matricula:");
        matricula = new TextField();
        GridPane.setHgrow(matricula, Priority.ALWAYS);
        gridCampos.add(lblMatricula, 3, 6, 5, 1);
        gridCampos.add(matricula, 3, 7, 3, 1);


        Label infoCredenciales = new Label("Credenciales");
        infoCredenciales.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridCampos.add(infoCredenciales, 0, 8, 5, 1);


        Label lblContrasena = new Label("Contraseña:");
        campoContrasena = new PasswordField();
        campoContrasena.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(campoContrasena, Priority.ALWAYS);
        gridCampos.add(lblContrasena, 0, 9);
        gridCampos.add(campoContrasena, 0, 10, 2, 1);

        Label lblConfirmar = new Label("Confirmar contraseña:");
        lblConfirmar.setWrapText(true);
        campoConfirmar = new PasswordField();
        campoConfirmar.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(campoConfirmar, Priority.ALWAYS);
        gridCampos.add(lblConfirmar, 2, 9, 2, 1);
        gridCampos.add(campoConfirmar, 2, 10, 2, 1);

        botonContinuar = new Button("Continuar");
        botonContinuar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonContinuar.setStyle("-fx-background-color: #009475; -fx-text-fill: white;");
        botonContinuar.setPrefWidth(200);
        botonContinuar.setPrefHeight(30);
        new ControladorRegistro(this);

        Button botonCancelar = new Button("Regresar");
        botonCancelar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonCancelar.setStyle("-fx-background-color: #005994; -fx-text-fill: white;");
        botonCancelar.setPrefWidth(200);
        botonCancelar.setPrefHeight(30);



        botonCancelar.setOnAction(e -> {
           VistaLogin regresar = new VistaLogin();
           regresar.start(ventanaRegistro);
        });

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(botonContinuar, botonCancelar);

        //Etiquetas Columna 0
        /*gridCampos.add(etiquetaName, 0, 0);
        gridCampos.add(etiquetaLastName, 1, 0);
        gridCampos.add(etiquetaEmail, 2, 0);

        //Etiquetas Columna 2
        gridCampos.add(etiquetaCalle, 0,2);
        gridCampos.add(etiquetaLada, 1, 2);
        gridCampos.add(etiquetaTelefono, 2, 2);
        gridCampos.add(eitquetaDate, 3 ,2);
        gridCampos.add(etiquetaEdad, 4,2);

        //TextField Columna 0
        gridCampos.add(campoName, 0, 1);
        gridCampos.add(campoLastName, 1, 1);
        gridCampos.add(campoEmail, 2, 1);

        //TextField Columna 1
        gridCampos.add(campoCalle, 0,3);
        gridCampos.add(campoLada,1,3);
        gridCampos.add(campoTelefono,2,3);
        gridCampos.add(campofecha, 3,3);
        gridCampos.add(campoEdad,4,3);

        panelIzquierdo.getChildren().add(contenedorVertical);
        panelDerecho.getChildren().addAll(titulo, gridCampos,buttons);
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaRegistro.setScene(escena);
        ventanaRegistro.setTitle("Registrarse");
        ventanaRegistro.show();
    }*/


package com.example.netrixapp.Vistas;

import java.time.LocalDate;
import java.time.Period;

import com.example.netrixapp.Controladores.ControladorRegistro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VistaRegistro {
    // Campos de texto y controles (mantener los mismos)
    private TextField campoNombre, campoApellidos, campoCorreo, campoCalle, campoLada, campoTelefono, campoEdad, matricula;
    private DatePicker campoFecha;
    private ComboBox<String> comboRol;
    private PasswordField campoContrasena, campoConfirmar;
    private Button botonContinuar;

    // Getters (mantener los mismos)
    public String getNombre() { return campoNombre.getText().trim(); }
    public String getApellidos() { return campoApellidos.getText().trim(); }
    public String getCorreo() { return campoCorreo.getText().trim().toLowerCase(); }
    public String getCalle() { return campoCalle.getText().trim(); }
    public String getLada() { return campoLada.getText().trim(); }
    public String getTelefono() { return campoTelefono.getText().trim(); }
    public LocalDate getFecha() { return campoFecha.getValue(); }
    public int getEdad() { return Integer.parseInt(campoEdad.getText().trim()); }
    public String getRol() { return comboRol.getSelectionModel().getSelectedItem().trim().toUpperCase(); }
    public String getmatricula() { return matricula.getText().trim().toLowerCase(); }
    public String getPassword() { return campoContrasena.getText().trim(); }
    public String getConfirmarPassword() { return campoConfirmar.getText().trim(); }
    public Button getBotonContinuar() { return botonContinuar; }

    public void start(Stage ventanaRegistro) {
        // Configuración de ventana maximizada
        ventanaRegistro.setMaximized(true);
        ventanaRegistro.setTitle("Registro - NetrixApp");

        // Contenedor principal con scroll
        ScrollPane scrollPrincipal = new ScrollPane();
        scrollPrincipal.setFitToWidth(true);
        scrollPrincipal.setFitToHeight(true);
        scrollPrincipal.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPrincipal.setStyle("-fx-background: #f5f7fa;");

        // Contenedor de una sola columna
        VBox contenedorPrincipal = new VBox();
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);
        contenedorPrincipal.setStyle("-fx-background-color: #f5f7fa;");
        contenedorPrincipal.setSpacing(20);
        contenedorPrincipal.setPadding(new Insets(20));

        // Logo en la parte superior
        ImageView logoUtezV = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/logo_utez.png")));
        logoUtezV.setPreserveRatio(true);
        logoUtezV.setFitWidth(200);
        logoUtezV.setEffect(new DropShadow(10, Color.BLACK));

        HBox logoContainer = new HBox(logoUtezV);
        logoContainer.setAlignment(Pos.CENTER);

        // Panel del formulario
        VBox panelFormulario = new VBox(20);
        panelFormulario.setAlignment(Pos.TOP_CENTER);
        panelFormulario.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        panelFormulario.setPadding(new Insets(30));
        panelFormulario.setMaxWidth(800);

        // Título del formulario
        Label titulo = new Label("Registro de Usuario");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.web("#333333"));

        // GridPane para el formulario (ahora de una sola columna)
        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(20);
        gridCampos.setVgap(15);
        gridCampos.setAlignment(Pos.TOP_CENTER);
        gridCampos.setPadding(new Insets(20, 40, 20, 40));

        // Configuración de columnas responsivas (ahora solo una columna)
        ColumnConstraints colLabel = new ColumnConstraints();
        colLabel.setHgrow(Priority.NEVER);
        ColumnConstraints colField = new ColumnConstraints();
        colField.setHgrow(Priority.ALWAYS);
        colField.setFillWidth(true);
        gridCampos.getColumnConstraints().addAll(colLabel, colField);

        // Sección de Información Personal
        Label infoPersonal = createSectionLabel("Información Personal");
        gridCampos.add(infoPersonal, 0, 0, 2, 1);

        // Campos de información personal (ahora en una columna)
        addFormField(gridCampos, "Nombre:", campoNombre = new TextField(), 0, 1);
        addFormField(gridCampos, "Apellidos:", campoApellidos = new TextField(), 0, 3);
        addFormField(gridCampos, "Correo institucional:", campoCorreo = new TextField(), 0, 5);
        addFormField(gridCampos, "Calle:", campoCalle = new TextField(), 0, 7);

        // Fila para Lada y Teléfono
        HBox telefonoBox = new HBox(10);
        telefonoBox.setAlignment(Pos.CENTER_LEFT);

        campoLada = new TextField();
        campoLada.setPromptText("Lada");
        campoLada.setPrefWidth(60);
        campoLada.setStyle("-fx-font-size: 14; -fx-padding: 5 10; -fx-background-radius: 4;");
        verificarCampo(campoLada, 3);

        campoTelefono = new TextField();
        campoTelefono.setPromptText("Teléfono");
        campoTelefono.setStyle("-fx-font-size: 14; -fx-padding: 5 10; -fx-background-radius: 4;");
        HBox.setHgrow(campoTelefono, Priority.ALWAYS);
        verificarCampo(campoTelefono, 8);

        telefonoBox.getChildren().addAll(new Label("Teléfono:"), campoLada, campoTelefono);
        gridCampos.add(telefonoBox, 0, 9, 2, 1);

        // Fecha de nacimiento y edad
        HBox fechaEdadBox = new HBox(10);
        fechaEdadBox.setAlignment(Pos.CENTER_LEFT);

        campoFecha = new DatePicker();
        campoFecha.setPrefWidth(200);
        campoFecha.setStyle("-fx-font-size: 14;");
        LocalDate hoy = LocalDate.now();
        campoFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(
                        date.isAfter(hoy) || // No fechas futuras
                                date.isBefore(hoy.minusYears(100)) // No más de 100 años atrás
                );
                if (date.isAfter(hoy)) {
                    setTooltip(new Tooltip("No se permiten fechas futuras"));
                } else if (date.isBefore(hoy.minusYears(100))) {
                    setTooltip(new Tooltip("La fecha no puede ser mayor a 100 años"));
                }
            }
        });

        campoEdad = new TextField();
        campoEdad.setEditable(false);
        campoEdad.setStyle("-fx-font-size: 14; -fx-background-color: #f0f0f0;");
        campoEdad.setPrefWidth(60);

        fechaEdadBox.getChildren().addAll(
                new Label("Fecha de nacimiento:"), campoFecha,
                new Label("Edad:"), campoEdad
        );
        gridCampos.add(fechaEdadBox, 0, 11, 2, 1);

        // Listener para calcular edad automáticamente
        campoFecha.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int edad = Period.between(newVal, LocalDate.now()).getYears();
                campoEdad.setText(String.valueOf(edad));
            } else {
                campoEdad.clear();
            }
        });

        // Sección de Información Académica
        Label infoAcademica = createSectionLabel("Información Académica");
        gridCampos.add(infoAcademica, 0, 13, 2, 1);

        // Rol
        addFormField(gridCampos, "Rol:", comboRol = new ComboBox<>(), 0, 15);
        comboRol.getItems().addAll("Alumno", "Docente");
        comboRol.setStyle("-fx-font-size: 14;");
        comboRol.setPrefWidth(Double.MAX_VALUE);

        // Matrícula
        addFormField(gridCampos, "Matrícula:", matricula = new TextField(), 0, 17);

        // Sección de Credenciales
        Label infoCredenciales = createSectionLabel("Credenciales de Acceso");
        gridCampos.add(infoCredenciales, 0, 19, 2, 1);

        // Contraseñas
        addFormField(gridCampos, "Contraseña:", campoContrasena = new PasswordField(), 0, 21);
        
        // Nota sobre requisitos de contraseña (POSICIONADA ARRIBA DEL CAMPO)
        Label lblRequisitosContrasena = new Label(
            "La contraseña debe contener al menos: 8 caracteres, una letra, un número, un carácter especial"
        );
        lblRequisitosContrasena.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666; -fx-font-style: italic;");
        lblRequisitosContrasena.setWrapText(true);
        lblRequisitosContrasena.setPadding(new Insets(5, 0, 5, 0));
        gridCampos.add(lblRequisitosContrasena, 0, 20, 2, 1);
        
        // Indicador de validación de contraseña (POSICIONADO ABAJO DEL CAMPO)
        Label lblValidacionContrasena = new Label();
        lblValidacionContrasena.setStyle("-fx-font-size: 11px; -fx-font-style: italic;");
        lblValidacionContrasena.setPadding(new Insets(5, 0, 5, 0));
        
        // Validación en tiempo real de la contraseña
        campoContrasena.textProperty().addListener((observable, oldValue, newValue) -> {
            validarContrasenaTiempoReal(newValue, lblValidacionContrasena);
        });
        
        gridCampos.add(lblValidacionContrasena, 0, 23, 2, 1);
        
        addFormField(gridCampos, "Confirmar contraseña:", campoConfirmar = new PasswordField(), 0, 25);

        // Botones en la parte inferior
        HBox panelBotones = new HBox(20);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setStyle("-fx-padding: 20 0 0 0;");

        botonContinuar = createActionButton("Continuar", "#009475");
        Button botonCancelar = createActionButton("Regresar", "#005994");

        botonCancelar.setOnAction(e -> {
            VistaLogin regresar = new VistaLogin();
            regresar.start(ventanaRegistro);
        });

        panelBotones.getChildren().addAll(botonCancelar, botonContinuar);

        // Ensamblar la interfaz
        panelFormulario.getChildren().addAll(titulo, gridCampos, panelBotones);
        contenedorPrincipal.getChildren().addAll(logoContainer, panelFormulario);
        scrollPrincipal.setContent(contenedorPrincipal);

        // Configurar escena
        Scene escena = new Scene(scrollPrincipal);
        ventanaRegistro.setScene(escena);

        // Inicializar controlador
        new ControladorRegistro(this);

        ventanaRegistro.show();
    }

    // Métodos auxiliares
    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        label.setTextFill(Color.web("#009475"));
        label.setPadding(new Insets(10, 0, 5, 0));
        return label;
    }

    private void addFormField(GridPane grid, String labelText, Control field, int col, int row) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", 14));
        label.setTextFill(Color.web("#444444"));
        grid.add(label, col, row);

        field.setStyle("-fx-font-size: 14; -fx-padding: 5 10; -fx-background-radius: 4;");
        field.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(field, Priority.ALWAYS);
        grid.add(field, col, row + 1, 2, 1);
    }

    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 4;");
        button.setPrefHeight(40);
        button.setPrefWidth(200);

        // Efecto hover
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: derive(" + color + ", -20%); -fx-text-fill: white; -fx-background-radius: 4;"));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 4;"));

        return button;
    }

    public void verificarCampo(TextField campo, int limite){
        campo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > limite) {
                campo.setText(oldValue);
            }
            if (!newValue.matches("\\d*")) { // Solo dígitos
                campo.setText(oldValue); // Revierte al valor anterior
            }
        });
    }

    /**
     * Valida en tiempo real si el correo ingresado es un correo institucional válido
     */
    private void validarCorreoTiempoReal(String correo, Label lblValidacion) {
        if (correo == null || correo.trim().isEmpty()) {
            lblValidacion.setText("");
            lblValidacion.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }

        String correoLower = correo.trim().toLowerCase();
        
        if (!correoLower.endsWith("@utez.edu.mx")) {
            lblValidacion.setText("❌ Solo se permiten correos @utez.edu.mx");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }
        
        String parteLocal = correoLower.substring(0, correoLower.indexOf('@'));
        if (parteLocal.isEmpty()) {
            lblValidacion.setText("❌ El correo debe tener un nombre de usuario");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }
        
        if (correoLower.contains(" ")) {
            lblValidacion.setText("❌ El correo no puede contener espacios");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }
        
        if (!parteLocal.matches("^[a-zA-Z0-9._-]+$")) {
            lblValidacion.setText("❌ Solo se permiten letras, números, puntos, guiones y guiones bajos");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }
        
        lblValidacion.setText("✅ Correo institucional válido");
        lblValidacion.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 11px; -fx-font-style: italic;");
    }

    /**
     * Valida en tiempo real si la contraseña cumple con los requisitos de seguridad
     */
    private void validarContrasenaTiempoReal(String contrasena, Label lblValidacion) {
        if (contrasena == null || contrasena.isEmpty()) {
            lblValidacion.setText("");
            lblValidacion.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }

        // Verificar longitud mínima
        if (contrasena.length() < 8) {
            lblValidacion.setText("❌ Mínimo 8 caracteres");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }

        // Verificar que contenga al menos una letra
        boolean tieneLetra = contrasena.matches(".*[a-zA-Z].*");
        if (!tieneLetra) {
            lblValidacion.setText("❌ Debe contener al menos una letra");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }

        // Verificar que contenga al menos un número
        boolean tieneNumero = contrasena.matches(".*\\d.*");
        if (!tieneNumero) {
            lblValidacion.setText("❌ Debe contener al menos un número");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }

        // Verificar que contenga al menos un carácter especial
        boolean tieneCaracterEspecial = contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        if (!tieneCaracterEspecial) {
            lblValidacion.setText("❌ Debe contener al menos un carácter especial");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }

        lblValidacion.setText("✅ Contraseña segura");
        lblValidacion.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 11px; -fx-font-style: italic;");
    }
}
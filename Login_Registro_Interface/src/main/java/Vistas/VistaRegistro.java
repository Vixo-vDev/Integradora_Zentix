package Vistas;

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

public class VistaRegistro {

    public void start(Stage ventanaRegistro) {

        //Asignando dimensiones de la vetana registro
        ventanaRegistro.setWidth(1000);
        ventanaRegistro.setHeight(600);


        HBox contenedorPrincipal = new HBox();

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.setPrefWidth(300);

        Image logoUtez = new Image(getClass().getResourceAsStream("/img/logo_utez.png"));
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
        TextField campoNombre = new TextField();
        GridPane.setHgrow(campoNombre, Priority.ALWAYS);
        gridCampos.add(lblNombre, 0, 1);
        gridCampos.add(campoNombre, 0, 2);

        Label lblApellidos = new Label("Apellidos:");
        TextField campoApellidos = new TextField();
        GridPane.setHgrow(campoApellidos, Priority.ALWAYS);
        gridCampos.add(lblApellidos, 1, 1, 2 ,1);
        gridCampos.add(campoApellidos, 1, 2, 2 ,1);

        Label lblCorreo = new Label("Correo institucional:");
        lblCorreo.setWrapText(true);
        TextField campoCorreo = new TextField();
        GridPane.setHgrow(campoCorreo, Priority.ALWAYS);
        gridCampos.add(lblCorreo, 3, 1, 2, 1);
        gridCampos.add(campoCorreo, 3, 2, 2, 1);

            // Línea 2: Calle, Lada, Teléfono, Fecha de nacimiento, Edad
        Label lblCalle = new Label("Calle:");
        TextField campoCalle = new TextField();
        GridPane.setHgrow(campoCalle, Priority.ALWAYS);
        gridCampos.add(lblCalle, 0, 3);
        gridCampos.add(campoCalle, 0, 4);

        Label lblLada = new Label("Lada:");
        TextField campoLada = new TextField();
        campoLada.setPrefWidth(60);
        gridCampos.add(lblLada, 1, 3);
        gridCampos.add(campoLada, 1, 4);

        Label lblTelefono = new Label("Teléfono:");
        TextField campoTelefono = new TextField();
        GridPane.setHgrow(campoTelefono, Priority.ALWAYS);
        gridCampos.add(lblTelefono, 2, 3);
        gridCampos.add(campoTelefono, 2, 4);

        Label lblFecha = new Label("Fecha de nacimiento:");
        DatePicker campoFecha = new DatePicker();
        GridPane.setHgrow(campoTelefono, Priority.ALWAYS);
        gridCampos.add(lblFecha, 3, 3, 2, 1);
        gridCampos.add(campoFecha, 3, 4);

        Label lblEdad = new Label("Edad:");
        TextField campoEdad = new TextField();
        campoEdad.setPrefWidth(60);
        campoEdad.setEditable(false);
        gridCampos.add(lblEdad, 4, 3);
        gridCampos.add(campoEdad, 4, 4);

        Label infoAcademica = new Label("Información académica");
        infoAcademica.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridCampos.add(infoAcademica, 0, 5, 5, 1);

        // Línea 3: ComboBox debajo del label
        Label lblRol = new Label("Selecciona tu Rol:");
        ComboBox<String> comboRol = new ComboBox<>();
        comboRol.getItems().addAll("Alumno", "Docente");
        comboRol.setPromptText("Selecciona tu Rol");
        GridPane.setHgrow(comboRol, Priority.ALWAYS);
        gridCampos.add(lblRol, 0, 6, 5, 1);
        gridCampos.add(comboRol, 0, 7, 3, 1);

        Label infoCredenciales = new Label("Credenciales");
        infoCredenciales.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridCampos.add(infoCredenciales, 0, 8, 5, 1);


        Label lblContrasena = new Label("Contraseña:");
        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(campoContrasena, Priority.ALWAYS);
        gridCampos.add(lblContrasena, 0, 9);
        gridCampos.add(campoContrasena, 0, 10, 2, 1);

        Label lblConfirmar = new Label("Confirmar contraseña:");
        lblConfirmar.setWrapText(true);
        PasswordField campoConfirmar = new PasswordField();
        campoConfirmar.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(campoConfirmar, Priority.ALWAYS);
        gridCampos.add(lblConfirmar, 2, 9, 2, 1);
        gridCampos.add(campoConfirmar, 2, 10, 2, 1);

        Button botonContinuar = new Button("Continuar");
        botonContinuar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonContinuar.setStyle("-fx-background-color: #009475; -fx-text-fill: white;");
        botonContinuar.setPrefWidth(200);
        botonContinuar.setPrefHeight(30);

        Button botonCancelar = new Button("Regresar");
        botonCancelar.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        botonCancelar.setStyle("-fx-background-color: #005994; -fx-text-fill: white;");
        botonCancelar.setPrefWidth(200);
        botonCancelar.setPrefHeight(30);

        botonContinuar.setOnAction(e -> {
            String nombre = campoNombre.getText();
            String apellidos = campoApellidos.getText();
            String correo = campoCorreo.getText();
            String calle = campoCalle.getText();
            String lada = campoLada.getText();
            String telefono = campoTelefono.getText();
            LocalDate date = campoFecha.getValue();
            String rol = comboRol.getSelectionModel().getSelectedItem();
            String password = campoContrasena.getText();
            String passwordConfirmar = campoConfirmar.getText();

            if(nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || calle.isEmpty() || lada.isEmpty()
            || telefono.isEmpty()  || date== null || rol.isEmpty() || password.isEmpty() || passwordConfirmar.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("LOS CAMPOS NO DEBEN ESTAR VACÍOS");
                alert.setContentText("Por favor ingrese los campos");
                alert.showAndWait();
            }
        });

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
        gridCampos.add(campoEdad,4,3);*/


        panelIzquierdo.getChildren().add(contenedorVertical);
        panelDerecho.getChildren().addAll(titulo, gridCampos,buttons);
        contenedorPrincipal.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene escena = new Scene(contenedorPrincipal);
        ventanaRegistro.setScene(escena);
        ventanaRegistro.setTitle("Registrarse");
        ventanaRegistro.show();
    }

}

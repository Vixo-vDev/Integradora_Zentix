package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorUsuarios;
import com.example.netrixapp.Modelos.Usuario;
import impl.UsuarioDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VistaUsuarios {

    UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
    private ButtonType btnGuardar;
    private TextField txtNombre;
    private TextField txtAepellidos;
    private TextField txtCorreo;
    private TextField txtDireccion;
    private TextField txtLada;
    private TextField txtTelefono;
    private DatePicker fecha;
    private TextField edad;
    private ComboBox<String> comboRol;
    private TextField matricula;

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private ControladorUsuarios controladorUsuarios;

    private TableView<Usuario> tablaUsuarios;
    private Pagination paginador;
    private List<Usuario> todosLosUsuarios = new ArrayList<>();

    private static final int FILAS_POR_PAGINA = 10;

    // Colores para estilo
    private final String COLOR_EXITO = "#2ECC71";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_PRIMARIO = "#3498DB";
    private final String COLOR_FONDO = "#F5F7FA";
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";

    public VistaUsuarios(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        this.controladorUsuarios = new ControladorUsuarios(this);
    }

    public BorderPane getVista() {
        return vista;
    }

    // Métodos para obtener datos del formulario (con validación simple)
    public String getNombre() {
        return txtNombre.getText().trim();
    }
    public String getApellidos() {
        return txtAepellidos.getText().trim();
    }
    public String getCorreo() {
        return txtCorreo.getText().trim();
    }
    public String getCalle() {
        return txtDireccion.getText().trim();
    }
    public String getLada() {
        return txtLada.getText().trim();
    }
    public String getTelefono() {
        return txtTelefono.getText().trim();
    }
    public LocalDate getFecha() {
        return fecha.getValue();
    }
    public int getEdad() {
        return Integer.parseInt(edad.getText().trim());
    }
    public String getRol() {
        return comboRol.getSelectionModel().getSelectedItem().trim().toUpperCase();
    }
    public String getmatricula() {
        return matricula.getText().trim();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));

        // Encabezado con botón para nuevo usuario
        HBox encabezado = new HBox(20);
        encabezado.setAlignment(Pos.CENTER_LEFT);

        Label lblTitulo = new Label("Gestión de Usuarios Registrados");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        Button btnAgregar = new Button("+ Nuevo Usuario");
        btnAgregar.setStyle("-fx-background-color: " + COLOR_PRIMARIO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 4px;");
        btnAgregar.setOnAction(e -> mostrarFormularioNuevoUsuario());

        encabezado.getChildren().addAll(lblTitulo, btnAgregar);
        contenido.getChildren().add(encabezado);

        // Tabla
        tablaUsuarios = new TableView<>();
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        configurarColumnasTabla();

        paginador = new Pagination();
        paginador.setPageFactory(this::crearPagina);

        contenido.getChildren().addAll(tablaUsuarios, paginador);

        vista.setCenter(contenido);
    }

    private void configurarColumnasTabla() {
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNombre() + " " + cell.getValue().getApellidos()));

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        TableColumn<Usuario, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        TableColumn<Usuario, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cell -> {
            String lada = cell.getValue().getLada();
            String tel = cell.getValue().getTelefono();
            return new SimpleStringProperty((lada != null ? lada + " " : "") + (tel != null ? tel : ""));
        });

        TableColumn<Usuario, String> colFecha = new TableColumn<>("Fecha Nac.");
        colFecha.setCellValueFactory(cell -> {
            if (cell.getValue().getDate() != null) {
                return new SimpleStringProperty(cell.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<Usuario, String> colMatricula = new TableColumn<>("Matricula");
        colMatricula.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMatricula()));

        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        TableColumn<Usuario, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox cajaBotones = new HBox(5, btnEditar, btnEliminar);

            {
                btnEditar.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-padding: 4 8;");
                btnEliminar.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-padding: 4 8;");

                btnEditar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    Alert alerta = editarUsuario(usuario);
                    Optional<ButtonType> resultado = alerta.showAndWait();
                    if (resultado.isPresent() && resultado.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                        controladorUsuarios.mostrarDialogoEdicion(usuario);
                    }
                });

                btnEliminar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    eliminarUsuario(usuario);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cajaBotones);
            }
        });

        tablaUsuarios.getColumns().addAll(colNombre, colCorreo, colDireccion, colTelefono, colFecha, colMatricula, colRol, colAcciones);
    }

    private VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todosLosUsuarios.size());
        tablaUsuarios.getItems().setAll(todosLosUsuarios.subList(fromIndex, toIndex));
        return new VBox(tablaUsuarios);
    }

    public void mostrarUsuarios(List<Usuario> usuarios) {
        this.todosLosUsuarios = usuarios;
        int totalPaginas = (int) Math.ceil((double) todosLosUsuarios.size() / FILAS_POR_PAGINA);
        if (totalPaginas == 0) totalPaginas = 1;
        paginador.setPageCount(totalPaginas);
        paginador.setCurrentPageIndex(0);
        paginador.setPageFactory(this::crearPagina);
    }

    private Alert editarUsuario(Usuario usuario) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Editar usuario");
        alerta.setHeaderText("Modifica los campos necesarios");

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        txtNombre = new TextField(usuario.getNombre());
        txtAepellidos = new TextField(usuario.getApellidos());
        txtCorreo = new TextField(usuario.getCorreo());
        txtDireccion = new TextField(usuario.getDireccion());
        txtLada = new TextField(usuario.getLada());
        txtTelefono = new TextField(usuario.getTelefono());
        fecha = new DatePicker(usuario.getDate());
        edad = new TextField(String.valueOf(usuario.getEdad()));
        comboRol = new ComboBox<>();
        comboRol.getItems().addAll("Alumno", "Docente");
        comboRol.setValue(usuario.getRol());
        matricula = new TextField(usuario.getMatricula());

        formulario.addRow(0, new Label("Nombre:"), txtNombre);
        formulario.addRow(1, new Label("Apellidos:"), txtAepellidos);
        formulario.addRow(2, new Label("Correo:"), txtCorreo);
        formulario.addRow(3, new Label("Dirección:"), txtDireccion);
        formulario.addRow(4, new Label("Lada:"), txtLada);
        formulario.addRow(5, new Label("Teléfono:"), txtTelefono);
        formulario.addRow(6, new Label("Fecha Nacimiento:"), fecha);
        formulario.addRow(7, new Label("Edad:"), edad);
        formulario.addRow(8, new Label("Rol:"), comboRol);
        formulario.addRow(9, new Label("Matrícula:"), matricula);

        alerta.getDialogPane().setContent(formulario);

        btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(btnGuardar, btnCancelar);

        return alerta;
    }

    private void eliminarUsuario(Usuario usuario) {
        int id = usuario.getId_usuario();
        System.out.println(id);
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Usuario");
        confirmacion.setHeaderText("¿Eliminar usuario: " + usuario.getNombre() + " " + usuario.getApellidos() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");


            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                try {
                    ControladorUsuarios controladorUsuarios1 = null;
                    usuarioDao.delete(usuario);
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Usuario eliminado");
                    info.setHeaderText(null);
                    info.setContentText("Usuario eliminado correctamente.");
                    info.showAndWait();
                    controladorUsuarios1.cargarUsuarios();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
    }

    private void mostrarFormularioNuevoUsuario() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad no implementada");
        alert.setHeaderText(null);
        alert.setContentText("Aquí puedes implementar el formulario para crear un nuevo usuario.");
        alert.showAndWait();
    }
}

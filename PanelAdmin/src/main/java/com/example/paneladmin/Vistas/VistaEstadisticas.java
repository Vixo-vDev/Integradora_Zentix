package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class VistaEstadisticas {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    public VistaEstadisticas(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        // Configurar el layout principal con la barra de navegación
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setStyle("-fx-background-color: #ECF0F1;");

        // Contenido principal
        VBox contenidoPrincipal = new VBox(20);
        contenidoPrincipal.setPadding(new Insets(20));

        // Título
        Label lblTitulo = new Label("Estadísticas del Sistema");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        contenidoPrincipal.getChildren().add(lblTitulo);

        // Tablas de estadísticas
        VBox tablasContainer = new VBox(20);

        // Tabla 1: Estadísticas de Usuarios
        VBox tablaUsuarios = crearTablaDetallada(
                "Estadísticas de Usuarios",
                new String[]{"Métrica", "Valor", "Tendencia"},
                new Object[][]{
                        {"Usuarios Registrados", "142", "↑ 12.5%"},
                        {"Usuarios Activos", "128 (90%)", "↑ 8.2%"},
                        {"Nuevos Usuarios (30d)", "15", "↓ 2.3%"},
                        {"Accesos Diarios Prom.", "87", "→ Estable"}
                }
        );

        // Tabla 2: Estadísticas de Solicitudes
        VBox tablaSolicitudes = crearTablaDetallada(
                "Estadísticas de Solicitudes",
                new String[]{"Tipo", "Total", "Pendientes", "Tiempo Prom."},
                new Object[][]{
                        {"Equipos", "56", "12", "2.3 días"},
                        {"Software", "34", "8", "1.7 días"},
                        {"Accesos", "22", "3", "0.5 días"},
                        {"Otros", "15", "0", "1.2 días"}
                }
        );

        // Tabla 3: Estadísticas de Inventario
        VBox tablaInventario = crearTablaDetallada(
                "Estadísticas de Inventario",
                new String[]{"Categoría", "Total", "Disponibles", "% Disponibilidad"},
                new Object[][]{
                        {"Laptops", "120", "108", "90%"},
                        {"Monitores", "85", "82", "96%"},
                        {"Teléfonos", "65", "58", "89%"},
                        {"Periféricos", "314", "302", "96%"}
                }
        );

        tablasContainer.getChildren().addAll(tablaUsuarios, tablaSolicitudes, tablaInventario);
        contenidoPrincipal.getChildren().add(tablasContainer);

        // Configurar el centro del BorderPane con ScrollPane
        ScrollPane scrollPane = new ScrollPane(contenidoPrincipal);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        vista.setCenter(scrollPane);
    }

    private VBox crearTablaDetallada(String titulo, String[] columnas, Object[][] datos) {
        VBox contenedorTabla = new VBox(10);
        contenedorTabla.setPadding(new Insets(15));
        contenedorTabla.setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        TableView<Object[]> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setStyle("-fx-font-size: 14px;");

        // Crear columnas dinámicamente
        for (int i = 0; i < columnas.length; i++) {
            final int colIndex = i;
            TableColumn<Object[], String> columna = new TableColumn<>(columnas[i]);
            columna.setCellValueFactory(data -> {
                Object value = data.getValue()[colIndex];
                return new javafx.beans.property.SimpleStringProperty(value == null ? "" : value.toString());
            });
            tabla.getColumns().add(columna);
        }

        // Agregar datos
        for (Object[] fila : datos) {
            tabla.getItems().add(fila);
        }

        tabla.setRowFactory(tv -> new TableRow<Object[]>() {
            @Override
            protected void updateItem(Object[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #F8F9F9;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }
                }
            }
        });

        contenedorTabla.getChildren().addAll(lblTitulo, tabla);
        return contenedorTabla;
    }

    public BorderPane getVista() {
        return vista;
    }
}
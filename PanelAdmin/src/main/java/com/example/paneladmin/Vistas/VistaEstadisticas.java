package com.example.paneladmin.Vistas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.function.Consumer;

public class VistaEstadisticas {
    private final VBox vista;
    private final Consumer<Void> volverCallback;

    public VistaEstadisticas(Consumer<Void> volverCallback) {
        this.volverCallback = volverCallback;
        this.vista = crearVista();
    }

    private VBox crearVista() {
        VBox contenedorPrincipal = new VBox(20);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setStyle("-fx-background-color: #ECF0F1;");

        // Barra superior con título y botón de volver
        HBox barraSuperior = new HBox();
        barraSuperior.setAlignment(Pos.CENTER_LEFT);
        barraSuperior.setSpacing(15);

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold;");
        btnVolver.setOnAction(e -> volverCallback.accept(null));

        Label lblTitulo = new Label("Estadísticas del Sistema");
        lblTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        barraSuperior.getChildren().addAll(btnVolver, lblTitulo);

        // Tarjetas de resumen estadístico
        GridPane gridResumen = crearTarjetasResumen();

        // Tabla de estadísticas detalladas
        VBox tablaEstadisticas = crearTablaEstadisticas();

        contenedorPrincipal.getChildren().addAll(barraSuperior, gridResumen, tablaEstadisticas);
        return contenedorPrincipal;
    }

    private GridPane crearTarjetasResumen() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        // Tarjeta 1: Usuarios
        VBox cardUsuarios = crearCardEstadistica(
                "👥",
                "Usuarios",
                "#3498DB",
                "142 registrados",
                "128 activos (90%)",
                "+12.5% crecimiento"
        );

        // Tarjeta 2: Solicitudes
        VBox cardSolicitudes = crearCardEstadistica(
                "📋",
                "Solicitudes",
                "#9B59B6",
                "56 este mes",
                "23 pendientes",
                "3.2 días promedio"
        );

        // Tarjeta 3: Inventario
        VBox cardInventario = crearCardEstadistica(
                "📦",
                "Inventario",
                "#2ECC71",
                "584 artículos",
                "92% disponibilidad",
                "15 nuevos"
        );

        // Tarjeta 4: Actividad
        VBox cardActividad = crearCardEstadistica(
                "📊",
                "Actividad",
                "#E74C3C",
                "1,245 accesos",
                "78% móvil",
                "22% escritorio"
        );

        grid.add(cardUsuarios, 0, 0);
        grid.add(cardSolicitudes, 1, 0);
        grid.add(cardInventario, 0, 1);
        grid.add(cardActividad, 1, 1);

        return grid;
    }

    private VBox crearCardEstadistica(String emoji, String titulo, String color,
                                      String dato1, String dato2, String dato3) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(250);

        Label lblIcono = new Label(emoji);
        lblIcono.setStyle("-fx-font-size: 36;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        VBox contenedorDatos = new VBox(8);
        contenedorDatos.setAlignment(Pos.CENTER_LEFT);

        Label lblDato1 = new Label("• " + dato1);
        Label lblDato2 = new Label("• " + dato2);
        Label lblDato3 = new Label("• " + dato3);

        // Estilo para los datos
        String estiloDato = "-fx-font-size: 14; -fx-text-fill: #2C3E50;";
        lblDato1.setStyle(estiloDato);
        lblDato2.setStyle(estiloDato);
        lblDato3.setStyle(estiloDato);

        contenedorDatos.getChildren().addAll(lblDato1, lblDato2, lblDato3);

        card.getChildren().addAll(lblIcono, lblTitulo, contenedorDatos);
        return card;
    }

    private VBox crearTablaEstadisticas() {
        VBox contenedorTabla = new VBox(15);
        contenedorTabla.setPadding(new Insets(20));
        contenedorTabla.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblTitulo = new Label("Estadísticas Detalladas");
        lblTitulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // Crear tabla
        TableView<Estadistica> tabla = new TableView<>();

        // Columnas de la tabla
        TableColumn<Estadistica, String> colCategoria = new TableColumn<>("1");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("1"));
        colCategoria.setPrefWidth(200);

        TableColumn<Estadistica, String> colMetrica = new TableColumn<>("2");
        colMetrica.setCellValueFactory(new PropertyValueFactory<>("2"));
        colMetrica.setPrefWidth(150);

        TableColumn<Estadistica, String> colValor = new TableColumn<>("3");
        colValor.setCellValueFactory(new PropertyValueFactory<>("3"));
        colValor.setPrefWidth(150);

        TableColumn<Estadistica, String> colTendencia = new TableColumn<>("4");
        colTendencia.setCellValueFactory(new PropertyValueFactory<>("4"));
        colTendencia.setPrefWidth(150);

        tabla.getColumns().addAll(colCategoria, colMetrica, colValor, colTendencia);

        // Agregar datos de ejemplo
        tabla.getItems().addAll(
                new Estadistica("Usuarios", "Registrados", "142", "↑ 12.5%"),
                new Estadistica("Usuarios", "Activos", "128 (90%)", "↑ 8.2%"),
                new Estadistica("Solicitudes", "Este mes", "56", "↓ 3.1%"),
                new Estadistica("Solicitudes", "Pendientes", "23", "↑ 5.4%"),
                new Estadistica("Inventario", "Artículos", "584", "↑ 15 nuevos"),
                new Estadistica("Inventario", "Disponibilidad", "92%", "→ Estable"),
                new Estadistica("Actividad", "Accesos", "1,245", "↑ 22.3%"),
                new Estadistica("Actividad", "Dispositivos", "78% móvil", "↑ 10.1%")
        );

        // Configuración adicional para la tabla
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setStyle("-fx-font-size: 14;");

        contenedorTabla.getChildren().addAll(lblTitulo, tabla);
        return contenedorTabla;
    }

    public VBox getVista() {
        return vista;
    }

    // Clase modelo para las estadísticas
    public static class Estadistica {
        private final String categoria;
        private final String metrica;
        private final String valor;
        private final String tendencia;

        public Estadistica(String categoria, String metrica, String valor, String tendencia) {
            this.categoria = categoria;
            this.metrica = metrica;
            this.valor = valor;
            this.tendencia = tendencia;
        }

        public String getCategoria() {
            return categoria;
        }

        public String getMetrica() {
            return metrica;
        }

        public String getValor() {
            return valor;
        }

        public String getTendencia() {
            return tendencia;
        }
    }
}
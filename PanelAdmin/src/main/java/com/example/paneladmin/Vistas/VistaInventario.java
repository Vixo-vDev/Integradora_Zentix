package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

public class VistaInventario {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    // Colores minimalistas
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";
    private final String COLOR_TEXTO_NORMAL = "#4B5563";
    private final String COLOR_ADVERTENCIA = "#F59E0B";

    public VistaInventario(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        // Eliminamos todos los fondos extras
        vista.setStyle("-fx-background-color: transparent;");

        // Barra superior minimalista
        vista.setTop(controladorBarra.getBarraSuperior());

        // Barra lateral minimalista
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenido principal sin fondos extras
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: transparent;");

        // 1. Fila horizontal con cards minimalistas
        HBox filaMetricas = new HBox(15);
        filaMetricas.setAlignment(Pos.TOP_CENTER);

        // Cards sin fondo, solo bordes
        VBox cardArticulos = crearCardMetrica("Artículos", "200", COLOR_TEXTO_OSCURO);
        VBox cardStockBajo = crearCardMetrica("Stock bajo", "12", COLOR_ADVERTENCIA);
        VBox cardDisponibles = crearCardMetrica("Disponibles", "188", COLOR_TEXTO_OSCURO);

        filaMetricas.getChildren().addAll(cardArticulos, cardStockBajo, cardDisponibles);

        // Configuración de crecimiento
        for (Node card : filaMetricas.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxWidth(Double.MAX_VALUE);
        }

        contenido.getChildren().add(filaMetricas);

        // Separador minimalista
        Separator separator = new Separator();
        separator.setPadding(new Insets(15, 0, 15, 0));
        contenido.getChildren().add(separator);

        // 2. Sección Código minimalista
        VBox cardCodigo = new VBox(10);
        cardCodigo.setStyle("-fx-background-color: transparent; -fx-padding: 16;");
        cardCodigo.setMaxWidth(Double.MAX_VALUE);

        Label lblTituloCodigo = new Label("Código");
        lblTituloCodigo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        // Tabla de código minimalista
        GridPane tablaCodigo = new GridPane();
        tablaCodigo.setHgap(30);
        tablaCodigo.setVgap(10);

        // Encabezados
        tablaCodigo.add(crearLabelEncabezado("Nombre"), 0, 0);
        tablaCodigo.add(crearLabelEncabezado("Disponibles"), 1, 0);

        // Configuración de columnas
        ColumnConstraints colNombre = new ColumnConstraints();
        colNombre.setHgrow(Priority.ALWAYS);
        ColumnConstraints colDisponibles = new ColumnConstraints();
        colDisponibles.setHgrow(Priority.SOMETIMES);
        tablaCodigo.getColumnConstraints().addAll(colNombre, colDisponibles);

        cardCodigo.getChildren().addAll(lblTituloCodigo, tablaCodigo);
        contenido.getChildren().add(cardCodigo);

        // Configuración final
        vista.setCenter(contenido);
    }

    private VBox crearCardMetrica(String titulo, String valor, String colorTexto) {
        VBox card = new VBox(8);
        // Card sin fondo, solo contenido
        card.setStyle("-fx-background-color: transparent; -fx-padding: 20;");
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(200);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + colorTexto + ";");

        card.getChildren().addAll(lblTitulo, lblValor);
        return card;
    }

    private Label crearLabelEncabezado(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        return lbl;
    }

    public BorderPane getVista() {
        return vista;
    }
}
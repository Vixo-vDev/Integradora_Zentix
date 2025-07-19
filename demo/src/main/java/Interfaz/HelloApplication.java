package Interfaz;

import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.File;

public class HelloApplication extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private String currentUser = "Usuario Normal";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Gestión de Inventario - " + currentUser);

        initRootLayout();
        showDashboard();
    }

    private void initRootLayout() {
        // Crear el menú lateral
        VBox leftMenu = new VBox(10);
        leftMenu.setPadding(new Insets(15));
        leftMenu.setStyle("-fx-background-color: #005994;");
        leftMenu.setPrefWidth(200);

        Button btnInventory = createMenuButton("Inventario");
        Button btnDashboard = createMenuButton("Dashboard");
        Button btnSolicitar = createMenuButton("Solicitar");
        Button btnHistory = createMenuButton("Historial");
        Button btnNotify = createMenuButton("Notificaciones");
        Button btnProfile = createMenuButton("Perfil");
        Button btnExit = createMenuButton("Salir");

        leftMenu.getChildren().addAll(
                btnDashboard,
                btnInventory,
                btnHistory,
                new Separator(),
                btnSolicitar,
                btnNotify,
                new Separator(),
                btnExit
        );

        // Barra superior con foto de perfil clickeable
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #009475;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label lblTitle = new Label("Sistema de Gestión de Inventario");
        lblTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18pt;");

        HBox userBox = new HBox(5);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        Label lblUser = new Label(currentUser);
        lblUser.setStyle("-fx-text-fill: white;");

        // Foto de perfil clickeable
        StackPane profilePicture = new StackPane();
        Circle circle = new Circle(15, Color.WHITE);
        Label initials = new Label("UP"); // Iniciales Usuario Perfil
        initials.setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold;");
        profilePicture.getChildren().addAll(circle, initials);
        profilePicture.setOnMouseClicked(e -> showProfile());

        userBox.getChildren().addAll(lblUser, profilePicture);

        topBar.getChildren().addAll(lblTitle, new Region(), userBox);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);

        // Área principal
        StackPane mainArea = new StackPane();
        mainArea.setStyle("-fx-background-color: #ecf0f1;");

        // Diseño principal
        rootLayout = new BorderPane();
        rootLayout.setTop(topBar);
        rootLayout.setLeft(leftMenu);
        rootLayout.setCenter(mainArea);

        Scene scene = new Scene(rootLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Manejo de eventos del menú
        btnDashboard.setOnAction(e -> showDashboard());
        btnInventory.setOnAction(e -> showInventory()); // Nueva acción para inventario
        btnSolicitar.setOnAction(e -> showRequestForm());
        btnHistory.setOnAction(e -> showHistory());
        btnProfile.setOnAction(e -> showProfile());
        btnNotify.setOnAction(e -> showNotifications());
        btnExit.setOnAction(e -> primaryStage.close());
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: center-left;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #009475; -fx-text-fill: white;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        return btn;
    }

    private void showDashboard() {
        // Contenedor principal del dashboard
        BorderPane dashboard = new BorderPane();
        dashboard.setPadding(new Insets(15));
        dashboard.setStyle("-fx-background-color: #f5f7fa;");

        // Sección superior - Título
        HBox topSection = new HBox();
        topSection.setPadding(new Insets(0, 0, 20, 0));
        topSection.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        topSection.getChildren().add(title);
        dashboard.setTop(topSection);

        // Sección central - Contenido principal
        ScrollPane centerSection = new ScrollPane();
        centerSection.setFitToWidth(true);
        centerSection.setStyle("-fx-background: #f5f7fa; -fx-border-color: #f5f7fa;");
        centerSection.setPadding(new Insets(5));

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(10));

        // Fila 1: Cartas de resumen
        HBox cardsRow = new HBox(20);
        cardsRow.setAlignment(Pos.CENTER);

        VBox card1 = createUserCard("Artículos Disponibles", "1,245", "#3498db", "Puedes solicitar");
        VBox card2 = createUserCard("Mis Solicitudes", "12", "#2ecc71", "Tus últimos pedidos");
        VBox card3 = createUserCard("Pendientes", "3", "#f39c12", "En revisión");
        VBox card4 = createUserCard("Rechazados", "1", "#e74c3c", "Ver motivos");

        cardsRow.getChildren().addAll(card1, card2, card3, card4);

        // Fila 2: Tabla de artículos recientes
        VBox recentItemsSection = new VBox(10);
        recentItemsSection.setPadding(new Insets(15));
        recentItemsSection.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label recentTitle = new Label("Tus Últimas Solicitudes");
        recentTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16pt; -fx-text-fill: #2c3e50;");

        TableView<RecentItem> recentTable = createUserRecentItemsTable();

        recentItemsSection.getChildren().addAll(recentTitle, recentTable);

        // Agregar contenido al centro
        centerContent.getChildren().addAll(cardsRow, recentItemsSection);
        centerSection.setContent(centerContent);
        dashboard.setCenter(centerSection);

        // Configurar el dashboard en el área principal
        StackPane mainArea = (StackPane) rootLayout.getCenter();
        mainArea.getChildren().clear();
        mainArea.getChildren().add(dashboard);
    }

    private VBox createUserCard(String title, String value, String color, String description) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPrefSize(250, 150);
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);"));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12pt;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 28pt; -fx-font-weight: bold;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 10pt;");

        // Línea decorativa inferior
        Pane line = new Pane();
        line.setPrefHeight(2);
        line.setStyle("-fx-background-color: " + color + "; -fx-opacity: 0.3;");

        card.getChildren().addAll(titleLabel, valueLabel, descLabel, line);
        return card;
    }

    private TableView<RecentItem> createUserRecentItemsTable() {
        TableView<RecentItem> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: transparent;");
        table.setPrefHeight(250);

        TableColumn<RecentItem, String> nameCol = new TableColumn<>("Artículo");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setStyle("-fx-alignment: CENTER_LEFT;");

        TableColumn<RecentItem, String> dateCol = new TableColumn<>("Fecha Solicitud");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<RecentItem, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(column -> new TableCell<RecentItem, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Aprobado":
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                            break;
                        case "Pendiente":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                            break;
                        case "Rechazado":
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                    }
                }
            }
        });

        table.getColumns().addAll(nameCol, dateCol, statusCol);

        ObservableList<RecentItem> recentData = FXCollections.observableArrayList(
                new RecentItem("Laptop HP", "15/06/2023", "Aprobado"),
                new RecentItem("Mouse Inalámbrico", "18/06/2023", "Entregado"),
                new RecentItem("Monitor 24\"", "20/06/2023", "Pendiente"),
                new RecentItem("Teclado", "22/06/2023", "Rechazado"),
                new RecentItem("Impresora", "25/06/2023", "Aprobado")
        );
        table.setItems(recentData);

        // Estilo alternado para filas
        table.setRowFactory(tv -> new TableRow<RecentItem>() {
            @Override
            protected void updateItem(RecentItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #f8f9fa;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }
                }
            }
        });

        return table;
    }

    private void showRequestForm() {
        // Contenedor principal
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(25));
        formContainer.setStyle("-fx-background-color: #f8f9fa;");

        // Título
        Label title = new Label("Nueva Solicitud de Artículos");
        title.setStyle("-fx-font-size: 22pt; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Panel del formulario
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(20));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 0);");

        // Lista de artículos a solicitar
        VBox itemsContainer = new VBox(10);
        Label itemsTitle = new Label("Artículos a solicitar:");
        itemsTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");

        // Tabla de artículos agregados
        TableView<RequestItem> itemsTable = new TableView<>();
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        itemsTable.setStyle("-fx-background-color: transparent;");

        TableColumn<RequestItem, String> itemCol = new TableColumn<>("Artículo");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<RequestItem, Integer> qtyCol = new TableColumn<>("Cantidad");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<RequestItem, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Eliminar");
            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 3 8 3 8;");
                deleteBtn.setOnAction(event -> {
                    RequestItem item = getTableView().getItems().get(getIndex());
                    itemsTable.getItems().remove(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });

        itemsTable.getColumns().addAll(itemCol, qtyCol, actionsCol);
        itemsTable.setPlaceholder(new Label("No hay artículos agregados"));

        // Formulario para agregar nuevo artículo
        GridPane addItemForm = new GridPane();
        addItemForm.setVgap(10);
        addItemForm.setHgap(15);
        addItemForm.setPadding(new Insets(10));

        ComboBox<String> itemCombo = new ComboBox<>();
        itemCombo.getItems().addAll("Laptop HP", "Mouse Inalámbrico", "Teclado", "Monitor 24\"", "Impresora", "Disco Duro");
        itemCombo.setPromptText("Seleccione artículo");
        itemCombo.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf;");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Cantidad");
        quantityField.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf;");

        Button addBtn = new Button("Agregar Artículo");
        addBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            if (itemCombo.getValue() != null && !quantityField.getText().isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    RequestItem newItem = new RequestItem(itemCombo.getValue(), quantity);
                    itemsTable.getItems().add(newItem);
                    itemCombo.setValue(null);
                    quantityField.clear();
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "La cantidad debe ser un número válido");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Datos incompletos", "Seleccione un artículo e ingrese la cantidad");
            }
        });

        addItemForm.add(new Label("Artículo:"), 0, 0);
        addItemForm.add(itemCombo, 1, 0);
        addItemForm.add(new Label("Cantidad:"), 0, 1);
        addItemForm.add(quantityField, 1, 1);
        addItemForm.add(addBtn, 1, 2);
        GridPane.setHalignment(addBtn, HPos.RIGHT);

        // Fecha y notas
        VBox detailsContainer = new VBox(15);
        detailsContainer.setPadding(new Insets(15, 0, 0, 0));

        DatePicker requestDate = new DatePicker();
        requestDate.setPromptText("Fecha requerida");
        requestDate.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf;");

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notas adicionales (opcional)...");
        notesArea.setPrefRowCount(3);
        notesArea.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf;");

        detailsContainer.getChildren().addAll(
                new Label("Detalles adicionales:"),
                new HBox(10, new Label("Fecha requerida:"), requestDate),
                new VBox(5, new Label("Notas:"), notesArea)
        );

        // Botones de acción
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Cancelar");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        cancelBtn.setOnAction(e -> showDashboard());

        Button submitBtn = new Button("Enviar Solicitud");
        submitBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        submitBtn.setOnAction(e -> {
            if (itemsTable.getItems().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Solicitud vacía", "Debe agregar al menos un artículo");
            } else {
                // Aquí iría la lógica para procesar la solicitud
                StringBuilder summary = new StringBuilder("Solicitud enviada con:\n");
                for (RequestItem item : itemsTable.getItems()) {
                    summary.append("- ").append(item.getName()).append(": ").append(item.getQuantity()).append(" unidades\n");
                }
                showAlert(Alert.AlertType.INFORMATION, "Solicitud completada", summary.toString());
                showDashboard();
            }
        });

        buttonBox.getChildren().addAll(cancelBtn, submitBtn);

        // Ensamblar el formulario
        itemsContainer.getChildren().addAll(itemsTitle, itemsTable, addItemForm);
        formPanel.getChildren().addAll(itemsContainer, new Separator(), detailsContainer);
        formContainer.getChildren().addAll(title, formPanel, buttonBox);

        // Mostrar en el área principal
        StackPane mainArea = (StackPane) rootLayout.getCenter();
        mainArea.getChildren().clear();
        mainArea.getChildren().add(formContainer);
    }

    // Clase para representar los items de la solicitud
    public static class RequestItem {
        private final String name;
        private final int quantity;

        public RequestItem(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    private void showHistory() {
        // Contenedor principal
        VBox historyContainer = new VBox(20);
        historyContainer.setPadding(new Insets(25));
        historyContainer.setStyle("-fx-background-color: #f8f9fa;");

        // Título
        Label title = new Label("Mi Historial de Solicitudes");
        title.setStyle("-fx-font-size: 22pt; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Panel principal
        VBox mainPanel = new VBox(15);
        mainPanel.setPadding(new Insets(20));
        mainPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 0);");

        // Barra de herramientas con filtros
        HBox toolbar = new HBox(15);
        toolbar.setPadding(new Insets(0, 0, 15, 0));
        toolbar.setAlignment(Pos.CENTER_LEFT);

        // Filtro por estado
        VBox statusFilterBox = new VBox(5);
        Label statusLabel = new Label("Estado:");
        statusLabel.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Todos", "Aprobado", "Pendiente", "Rechazado", "Entregado");
        statusFilter.setValue("Todos");
        statusFilter.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf;");
        statusFilterBox.getChildren().addAll(statusLabel, statusFilter);

        // Filtro por fecha
        VBox dateFilterBox = new VBox(5);
        Label dateLabel = new Label("Rango de fechas:");
        dateLabel.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");

        HBox dateRangeBox = new HBox(10);
        DatePicker fromDate = new DatePicker();
        fromDate.setPromptText("Desde");
        fromDate.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf;");

        Label toLabel = new Label("a");
        toLabel.setStyle("-fx-text-fill: #7f8c8d;");

        DatePicker toDate = new DatePicker();
        toDate.setPromptText("Hasta");
        toDate.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf;");

        dateRangeBox.getChildren().addAll(fromDate, toLabel, toDate);
        dateFilterBox.getChildren().addAll(dateLabel, dateRangeBox);

        // Botones de acción
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);

        Button clearBtn = new Button("Limpiar");
        clearBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");

        Button filterBtn = new Button("Aplicar Filtros");
        filterBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");

        buttonBox.getChildren().addAll(clearBtn, filterBtn);

        toolbar.getChildren().addAll(statusFilterBox, dateFilterBox, buttonBox);

        // Tabla de solicitudes
        TableView<Request> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: transparent;");
        table.setPlaceholder(new Label("No se encontraron solicitudes"));

        // Columnas de la tabla
        TableColumn<Request, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setStyle("-fx-alignment: CENTER;");
        idCol.setPrefWidth(80);

        TableColumn<Request, String> itemCol = new TableColumn<>("Artículo(s)");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemCol.setStyle("-fx-alignment: CENTER_LEFT;");
        itemCol.setPrefWidth(200);

        TableColumn<Request, Integer> qtyCol = new TableColumn<>("Cantidad");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setStyle("-fx-alignment: CENTER;");
        qtyCol.setPrefWidth(80);

        TableColumn<Request, String> dateCol = new TableColumn<>("Fecha");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setStyle("-fx-alignment: CENTER;");
        dateCol.setPrefWidth(120);

        TableColumn<Request, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(column -> new TableCell<Request, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                    switch (item) {
                        case "Aprobado":
                            setTextFill(Color.web("#27ae60"));
                            break;
                        case "Pendiente":
                            setTextFill(Color.web("#f39c12"));
                            break;
                        case "Rechazado":
                            setTextFill(Color.web("#e74c3c"));
                            break;
                        case "Entregado":
                            setTextFill(Color.web("#3498db"));
                            break;
                        default:
                            setTextFill(Color.BLACK);
                    }
                }
            }
        });
        statusCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, itemCol, qtyCol, dateCol, statusCol);

        // Datos de ejemplo con más detalle
        ObservableList<Request> data = FXCollections.observableArrayList(
                new Request("#1234", "Laptop HP (x2)", 2, "15/06/2023", "Aprobado"),
                new Request("#1235", "Mouse Inalámbrico (x5)\nTeclado (x1)", 6, "18/06/2023", "Entregado"),
                new Request("#1236", "Monitor 24\" (x1)", 1, "20/06/2023", "Pendiente"),
                new Request("#1237", "Teclado (x3)\nMouse Inalámbrico (x2)", 5, "22/06/2023", "Rechazado")
        );
        table.setItems(data);

        // Botón de regreso
        Button backBtn = new Button("Volver al Dashboard");
        backBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white;");
        backBtn.setOnAction(e -> showDashboard());
        HBox bottomBox = new HBox(backBtn);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);

        // Configurar filas alternadas
        table.setRowFactory(tv -> new TableRow<Request>() {
            @Override
            protected void updateItem(Request item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #f8f9fa;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }
                }
            }
        });

        // Ensamblar la interfaz
        mainPanel.getChildren().addAll(toolbar, table);
        historyContainer.getChildren().addAll(title, mainPanel, bottomBox);

        // Mostrar en el área principal
        StackPane mainArea = (StackPane) rootLayout.getCenter();
        mainArea.getChildren().clear();
        mainArea.getChildren().add(historyContainer);

        // Configurar eventos
        filterBtn.setOnAction(e -> {
            // Lógica de filtrado aquí
            showAlert(Alert.AlertType.INFORMATION, "Filtros aplicados",
                    "Filtrado por: " + statusFilter.getValue() +
                            "\nDesde: " + (fromDate.getValue() != null ? fromDate.getValue() : "Ninguna") +
                            "\nHasta: " + (toDate.getValue() != null ? toDate.getValue() : "Ninguna"));
        });

        clearBtn.setOnAction(e -> {
            statusFilter.setValue("Todos");
            fromDate.setValue(null);
            toDate.setValue(null);
        });
    }

    private void showProfile() {
        // Contenedor principal
        VBox profileContainer = new VBox(20);
        profileContainer.setPadding(new Insets(25));
        profileContainer.setStyle("-fx-background-color: #f8f9fa;");

        // Título
        Label title = new Label("Mi Perfil");
        title.setStyle("-fx-font-size: 22pt; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Panel del formulario
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(25));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 0);");

        // Sección de foto de perfil
        HBox profilePictureSection = new HBox(20);
        profilePictureSection.setAlignment(Pos.CENTER_LEFT);

        // Foto de perfil (círculo)
        StackPane profilePictureContainer = new StackPane();
        Circle profilePicture = new Circle(50, Color.LIGHTGRAY); // Radio de 50px
        profilePicture.setStroke(Color.WHITE);
        profilePicture.setStrokeWidth(3);

        // Label para iniciales (o podrías cargar una imagen)
        Label initialsLabel = new Label("A"); // Iniciales del usuario
        initialsLabel.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-text-fill: white;");

        profilePictureContainer.getChildren().addAll(profilePicture, initialsLabel);

        // Botones para foto de perfil
        VBox pictureButtons = new VBox(10);
        Button changePictureBtn = new Button("Cambiar Foto");
        changePictureBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15 8 15;");

        Button removePictureBtn = new Button("Eliminar Foto");
        removePictureBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 15 8 15;");

        pictureButtons.getChildren().addAll(changePictureBtn, removePictureBtn);
        profilePictureSection.getChildren().addAll(profilePictureContainer, pictureButtons);

        // Formulario de información
        GridPane form = new GridPane();
        form.setVgap(15);
        form.setHgap(20);
        form.setPadding(new Insets(20, 10, 10, 10));

        // Campos editables
        TextField nameField = new TextField("Juan Pérez");
        nameField.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf; -fx-padding: 8;");

        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Contraseña actual");
        currentPasswordField.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf; -fx-padding: 8;");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nueva contraseña");
        newPasswordField.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf; -fx-padding: 8;");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmar nueva contraseña");
        confirmPasswordField.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf; -fx-padding: 8;");

        // Campos no editables
        TextField emailField = new TextField("juan.perez@empresa.com");
        emailField.setEditable(false);
        emailField.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #d6dbdf; -fx-padding: 8;");

        TextField departmentField = new TextField("Ventas");
        departmentField.setEditable(false);
        departmentField.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #d6dbdf; -fx-padding: 8;");

        TextField positionField = new TextField("Asesor Comercial");
        positionField.setEditable(false);
        positionField.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #d6dbdf; -fx-padding: 8;");

        // Configurar etiquetas
        Label pictureLabel = createFormLabel("Foto de Perfil:");
        Label nameLabel = createFormLabel("Nombre:");
        Label emailLabel = createFormLabel("Correo:");
        Label deptLabel = createFormLabel("Departamento:");
        Label positionLabel = createFormLabel("Cargo:");
        Label passwordLabel = createFormLabel("Cambiar Contraseña:");

        // Agregar elementos al formulario
        form.add(pictureLabel, 0, 0);
        form.add(profilePictureSection, 1, 0);
        form.add(nameLabel, 0, 1);
        form.add(nameField, 1, 1);
        form.add(emailLabel, 0, 2);
        form.add(emailField, 1, 2);
        form.add(deptLabel, 0, 3);
        form.add(departmentField, 1, 3);
        form.add(positionLabel, 0, 4);
        form.add(positionField, 1, 4);
        form.add(passwordLabel, 0, 5);
        form.add(currentPasswordField, 1, 5);
        form.add(new Label(""), 0, 6); // Espacio
        form.add(newPasswordField, 1, 6);
        form.add(new Label(""), 0, 7); // Espacio
        form.add(confirmPasswordField, 1, 7);

        // Sección de botones
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancelar");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 12pt; -fx-padding: 8 20 8 20;");
        cancelBtn.setOnAction(e -> showDashboard());

        Button saveBtn = new Button("Guardar Cambios");
        saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 12pt; -fx-padding: 8 20 8 20;");
        saveBtn.setOnAction(e -> showSaveConfirmation(nameField.getText(),
                currentPasswordField.getText(),
                newPasswordField.getText(),
                confirmPasswordField.getText()));

        buttonBox.getChildren().addAll(cancelBtn, saveBtn);

        // Configurar eventos para los botones de foto
        changePictureBtn.setOnAction(e -> changeProfilePicture(profilePicture, initialsLabel));
        removePictureBtn.setOnAction(e -> removeProfilePicture(profilePicture, initialsLabel));

        // Ensamblar la interfaz
        formPanel.getChildren().addAll(form, buttonBox);
        profileContainer.getChildren().addAll(title, formPanel);

        // Mostrar en el área principal
        StackPane mainArea = (StackPane) rootLayout.getCenter();
        mainArea.getChildren().clear();
        mainArea.getChildren().add(profileContainer);
    }

    private void changeProfilePicture(Circle profilePicture, Label initialsLabel) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto de Perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                ImagePattern pattern = new ImagePattern(image);
                profilePicture.setFill(pattern);
                initialsLabel.setVisible(false); // Ocultar iniciales cuando hay imagen
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la imagen seleccionada.");
            }
        }
    }

    private void removeProfilePicture(Circle profilePicture, Label initialsLabel) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Eliminar Foto");
        confirmation.setHeaderText("¿Estás seguro de eliminar tu foto de perfil?");
        confirmation.setContentText("Se restaurará la imagen predeterminada.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                profilePicture.setFill(Color.LIGHTGRAY);
                initialsLabel.setVisible(true);
            }
        });
    }

    private void showSaveConfirmation(String newName, String currentPassword,
                                      String newPassword, String confirmPassword) {
        // Validar campos primero
        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Las contraseñas nuevas no coinciden.");
            return;
        }

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmar Cambios");
        confirmationDialog.setHeaderText("¿Estás seguro de guardar los cambios?");

        // Crear contenido detallado
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label changesLabel = new Label("Se guardarán los siguientes cambios:");
        changesLabel.setStyle("-fx-font-weight: bold;");

        VBox changesList = new VBox(5);
        if (!newName.equals("Juan Pérez")) {
            changesList.getChildren().add(new Label("- Nombre: " + newName));
        }
        if (!newPassword.isEmpty()) {
            changesList.getChildren().add(new Label("- Contraseña: ********"));
        }

        if (changesList.getChildren().isEmpty()) {
            changesList.getChildren().add(new Label("No se detectaron cambios"));
        }

        content.getChildren().addAll(changesLabel, changesList);
        confirmationDialog.getDialogPane().setContent(content);

        // Personalizar botones
        ButtonType yesButton = new ButtonType("Sí, Guardar", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Cancelar", ButtonBar.ButtonData.NO);
        confirmationDialog.getButtonTypes().setAll(yesButton, noButton);

        // Mostrar diálogo y procesar respuesta
        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                // Aquí iría la lógica para guardar los cambios
                showAlert(Alert.AlertType.INFORMATION, "Perfil Actualizado",
                        "Tus cambios han sido guardados exitosamente.");
                showDashboard();
            }
        });
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 12pt; -fx-text-fill: #34495e; -fx-font-weight: bold;");
        return label;
    }

    private void showNotifications() {
        // Contenedor principal
        VBox notificationsContainer = new VBox(15);
        notificationsContainer.setPadding(new Insets(20));
        notificationsContainer.setStyle("-fx-background-color: #f5f7fa;");

        // Título y barra superior
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("Mis Notificaciones");
        title.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button markAllReadBtn = new Button("Marcar todo como leído");
        markAllReadBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15 8 15;");

        header.getChildren().addAll(title, new Region(), markAllReadBtn);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        // Panel de notificaciones
        VBox notificationsPanel = new VBox();
        notificationsPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        // Filtros
        HBox filterBar = new HBox(15);
        filterBar.setPadding(new Insets(15, 15, 10, 15));
        filterBar.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");

        ToggleGroup filterGroup = new ToggleGroup();

        RadioButton allBtn = new RadioButton("Todas");
        allBtn.setToggleGroup(filterGroup);
        allBtn.setSelected(true);
        allBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 3 8 3 8;");

        RadioButton unreadBtn = new RadioButton("No leídas");
        unreadBtn.setToggleGroup(filterGroup);
        unreadBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 3 8 3 8;");

        RadioButton importantBtn = new RadioButton("Importantes");
        importantBtn.setToggleGroup(filterGroup);
        importantBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 3 8 3 8;");

        RadioButton systemBtn = new RadioButton("Sistema");
        systemBtn.setToggleGroup(filterGroup);
        systemBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 3 8 3 8;");

        filterBar.getChildren().addAll(
                new Label("Filtrar:"), allBtn, unreadBtn, importantBtn, systemBtn
        );

        // Lista de notificaciones
        ListView<Notification> notificationsList = new ListView<>();
        notificationsList.setCellFactory(param -> new NotificationListCell());
        notificationsList.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

        // Datos de ejemplo
        ObservableList<Notification> notifications = FXCollections.observableArrayList(
                new Notification("Solicitud aprobada", "Tu solicitud #1234 ha sido aprobada",
                        "hace 2 horas", false, "IMPORTANT"),
                new Notification("Nuevo mensaje", "Tienes un nuevo mensaje de María González",
                        "hace 1 día", false, "MESSAGE"),
                new Notification("Recordatorio de inventario", "Revisión mensual de inventario programada para mañana",
                        "hace 3 días", true, "SYSTEM")
        );
        notificationsList.setItems(notifications);

        // Panel inferior
        HBox bottomBar = new HBox(15);
        bottomBar.setPadding(new Insets(10, 15, 15, 15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button deleteAllBtn = new Button("Eliminar Todas");
        deleteAllBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 15 8 15;");

        Button backBtn = new Button("Volver al Dashboard");
        backBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 8 15 8 15;");

        bottomBar.getChildren().addAll(deleteAllBtn, backBtn);

        // Ensamblar el panel
        notificationsPanel.getChildren().addAll(filterBar, notificationsList, bottomBar);
        notificationsContainer.getChildren().addAll(header, notificationsPanel);

        // Mostrar en el área principal
        StackPane mainArea = (StackPane) rootLayout.getCenter();
        mainArea.getChildren().clear();
        mainArea.getChildren().add(notificationsContainer);

        // Manejadores de eventos
        markAllReadBtn.setOnAction(e -> {
            notifications.forEach(n -> n.setRead(true));
            notificationsList.refresh();
        });

        deleteAllBtn.setOnAction(e -> {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmar eliminación");
            confirmDialog.setHeaderText("¿Eliminar todas las notificaciones?");
            confirmDialog.setContentText("Esta acción no se puede deshacer.");

            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    notifications.clear();
                }
            });
        });

        backBtn.setOnAction(e -> showDashboard());
    }

    // Clase de modelo para notificaciones (simplificada)
    public class Notification {
        private final String title;
        private final String message;
        private final String time;
        private boolean read;
        private final String type;

        public Notification(String title, String message, String time, boolean read, String type) {
            this.title = title;
            this.message = message;
            this.time = time;
            this.read = read;
            this.type = type;
        }

        // Getters y setters
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getTime() { return time; }
        public boolean isRead() { return read; }
        public void setRead(boolean read) { this.read = read; }
        public String getType() { return type; }
    }

    // Clase para celdas personalizadas de notificaciones
    private class NotificationListCell extends ListCell<Notification> {
        private final HBox content;
        private final Label titleLabel;
        private final Label messageLabel;
        private final Label timeLabel;

        public NotificationListCell() {
            super();

            titleLabel = new Label();
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt;");

            messageLabel = new Label();
            messageLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11pt;");
            messageLabel.setWrapText(true);

            timeLabel = new Label();
            timeLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 10pt;");

            VBox textBox = new VBox(3, titleLabel, messageLabel, timeLabel);
            textBox.setPadding(new Insets(5));

            content = new HBox(15, textBox);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(10));
            content.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
        }

        @Override
        protected void updateItem(Notification item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                titleLabel.setText(item.getTitle());
                messageLabel.setText(item.getMessage());
                timeLabel.setText(item.getTime());

                // Estilo según estado
                if (!item.isRead()) {
                    content.setStyle("-fx-background-color: #e8f4fc; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt; -fx-text-fill: #2c3e50;");
                } else {
                    content.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
                    titleLabel.setStyle("-fx-font-weight: normal; -fx-font-size: 12pt; -fx-text-fill: #7f8c8d;");
                }

                setGraphic(content);
            }
        }
    }
    private void showInventory() {
        // Contenedor principal
        BorderPane inventoryContainer = new BorderPane();
        inventoryContainer.setPadding(new Insets(20));
        inventoryContainer.setStyle("-fx-background-color: #f5f7fa;");

        // Barra superior - Título y buscador
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Inventario");
        title.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar productos...");
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #d6dbdf; -fx-pref-width: 300;");

        Button searchBtn = new Button("Buscar");
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 20 8 20;");

        topBar.getChildren().addAll(title, new Region(), searchField, searchBtn);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        inventoryContainer.setTop(topBar);

        // Panel central - Tabla de productos
        VBox centerPanel = new VBox();
        centerPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        // Tarjetas de resumen
        HBox summaryCards = new HBox(20);
        summaryCards.setPadding(new Insets(15));
        summaryCards.setAlignment(Pos.CENTER);

        VBox totalCard = createSummaryCard("Total Productos", "142", "#3498db");
        VBox availableCard = createSummaryCard("Disponibles", "128", "#2ecc71");
        VBox lowStockCard = createSummaryCard("Stock Bajo", "14", "#f39c12");

        summaryCards.getChildren().addAll(totalCard, availableCard, lowStockCard);

        // Tabla de productos (solo lectura)
        TableView<Product> productsTable = new TableView<>();
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columnas de la tabla (solo las relevantes para usuarios normales)
        TableColumn<Product, String> codeCol = new TableColumn<>("Código");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setStyle("-fx-alignment: CENTER_LEFT;");

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Disponible");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setStyle("-fx-alignment: CENTER;");


        productsTable.getColumns().addAll(codeCol, nameCol, stockCol);

        // Datos de ejemplo
        ObservableList<Product> products = FXCollections.observableArrayList(
                new Product("LP-1001", "Laptop HP", 15, "Almacén A", "Electrónicos"),
                new Product("MS-2005", "Mouse Inalámbrico", 42, "Almacén B", "Electrónicos"),
                new Product("PP-3040", "Papel A4", 120, "Oficina Principal", "Oficina"),
                new Product("TN-4550", "Tóner Impresora", 5, "Almacén A", "Oficina"),
                new Product("KB-1122", "Teclado USB", 32, "Almacén B", "Electrónicos")
        );
        productsTable.setItems(products);

        // Configurar filas alternadas
        productsTable.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #f8f9fa;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }

                    // Resaltar productos con bajo stock
                    if (item.getStock() < 10) {
                        setStyle("-fx-background-color: #fff3f3;");
                    }
                }
            }
        });

        centerPanel.getChildren().addAll(summaryCards, productsTable);
        inventoryContainer.setCenter(centerPanel);

        // Barra inferior - Solo botón de volver
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(20, 0, 0, 0));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button backBtn = new Button("Volver al Dashboard");
        backBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 8 20 8 20;");
        backBtn.setOnAction(e -> showDashboard());

        bottomBar.getChildren().add(backBtn);
        inventoryContainer.setBottom(bottomBar);

        // Manejador de búsqueda
        searchBtn.setOnAction(e -> {
            String searchText = searchField.getText().toLowerCase();
            if (searchText.isEmpty()) {
                productsTable.setItems(products);
            } else {
                ObservableList<Product> filtered = products.filtered(p ->
                        p.getCode().toLowerCase().contains(searchText) ||
                                p.getName().toLowerCase().contains(searchText) ||
                                p.getLocation().toLowerCase().contains(searchText)
                );
                productsTable.setItems(filtered);
            }
        });

        // Mostrar en el área principal
        StackPane mainArea = (StackPane) rootLayout.getCenter();
        mainArea.getChildren().clear();
        mainArea.getChildren().add(inventoryContainer);
    }

    private VBox createSummaryCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPrefSize(180, 120);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12pt;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24pt; -fx-font-weight: bold;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    // Clase modelo para Producto (versión simplificada para usuario normal)
    public class Product {
        private final String code;
        private final String name;
        private final int stock;
        private final String location;
        private final String category;

        public Product(String code, String name, int stock, String location, String category) {
            this.code = code;
            this.name = name;
            this.stock = stock;
            this.location = location;
            this.category = category;
        }

        // Getters
        public String getCode() { return code; }
        public String getName() { return name; }
        public int getStock() { return stock; }
        public String getLocation() { return location; }
        public String getCategory() { return category; }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Request {
        private final String id;
        private final String item;
        private final int quantity;
        private final String date;
        private final String status;

        public Request(String id, String item, int quantity, String date, String status) {
            this.id = id;
            this.item = item;
            this.quantity = quantity;
            this.date = date;
            this.status = status;
        }

        public String getId() { return id; }
        public String getItem() { return item; }
        public int getQuantity() { return quantity; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }

    public static class RecentItem {
        private final String name;
        private final String date;
        private final String status;

        public RecentItem(String name, String date, String status) {
            this.name = name;
            this.date = date;
            this.status = status;
        }

        public String getName() { return name; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
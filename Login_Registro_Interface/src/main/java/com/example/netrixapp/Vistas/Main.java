package com.example.netrixapp.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Main {  // <<--- AQUÍ está el cambio de nombre
    private TableView<User> table = new TableView<>();

    public void mostrar (Stage stage) {
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> userCol = new TableColumn<>("Usuario");
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));

        TableColumn<User, String> firstNameCol = new TableColumn<>("Nombre");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameCol = new TableColumn<>("Apellido");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> roleCol = new TableColumn<>("Rol");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, Boolean> statusCol = new TableColumn<>("Activo");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<User, String> keyCol = new TableColumn<>("Clave");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));

        table.getColumns().addAll(idCol, userCol, firstNameCol, lastNameCol, roleCol, statusCol, keyCol);

        List<User> users = fetchUsersFromOracle();
        table.getItems().addAll(users);

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Lista de Usuarios");
        stage.show();
    }

    private List<User> fetchUsersFromOracle() {
        List<User> users = new ArrayList<>();
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "SYSTEM";
        String password = "tu_contraseña";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM USERS")) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("ID_USER"),
                        rs.getString("USER_NAME"),
                        rs.getString("USER_FIRST_NAME"),
                        rs.getString("USER_LAST_NAME"),
                        rs.getString("USER_ROLE"),
                        rs.getBoolean("USER_STATUS"),
                        rs.getString("USER_KEY")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static class User {
        private int id;
        private String user;
        private String firstName;
        private String lastName;
        private String role;
        private boolean status;
        private String key;

        public User(int id, String user, String firstName, String lastName, String role, boolean status, String key) {
            this.id = id;
            this.user = user;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.status = status;
            this.key = key;
        }

        public int getId() { return id; }
        public String getUser() { return user; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getRole() { return role; }
        public boolean getStatus() { return status; }
        public String getKey() { return key; }
    }
}

package com.sportsmanager.controller;

import com.sportsmanager.dao.SupabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class RegistrationController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;
    @FXML private Hyperlink backLink;

    private Stage stage;

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll("player", "manager", "admin");
        roleCombo.setValue("player");

        usernameField.textProperty().addListener((obs, old, val) -> {
            if (!val.isEmpty()) {
                errorLabel.setText("");
            }
        });
        passwordField.textProperty().addListener((obs, old, val) -> {
            if (!val.isEmpty()) {
                errorLabel.setText("");
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleCombo.getValue();

        // Validation
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        if (username.length() < 3) {
            showError("Username must be at least 3 characters!");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match!");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email address!");
            return;
        }

        registerButton.setDisable(true);
        registerButton.setText("Registering...");

        try {
            if (userExists(username)) {
                showError("Username already exists!");
                return;
            }

            if (emailExists(email)) {
                showError("Email already exists!");
                return;
            }

            registerUser(username, fullName, email, password, role);
            showSuccess("Registration successful! Redirecting to login...");

            // Wait 2 seconds then go to login
            javafx.util.Duration delay = javafx.util.Duration.seconds(2);
            javafx.animation.PauseTransition pt = new javafx.animation.PauseTransition(delay);
            pt.setOnFinished(e -> goToLogin());
            pt.play();

        } catch (SQLException e) {
            showError("Registration failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            registerButton.setDisable(false);
            registerButton.setText("Create Account");
        }
    }

    private void registerUser(String username, String fullName, String email, String password, String role) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String userId = java.util.UUID.randomUUID().toString();

        // Direct insert approach (after dropping foreign key constraint)
        String sqlProfile = "INSERT INTO profiles (id, username, full_name, email, role, is_active, created_at, updated_at) " +
                "VALUES (?::uuid, ?, ?, ?, ?, true, NOW(), NOW())";

        String sqlCredentials = "INSERT INTO user_credentials (user_id, password_hash, created_at, updated_at) " +
                "VALUES (?::uuid, ?, NOW(), NOW())";

        Connection conn = null;
        try {
            conn = SupabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert profile
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlProfile)) {
                pstmt1.setString(1, userId);
                pstmt1.setString(2, username);
                pstmt1.setString(3, fullName);
                pstmt1.setString(4, email);
                pstmt1.setString(5, role);

                int rowsAffected = pstmt1.executeUpdate();
                System.out.println("Profile inserted: " + rowsAffected + " rows, ID: " + userId);
            }

            // Insert credentials
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlCredentials)) {
                pstmt2.setString(1, userId);
                pstmt2.setString(2, hashedPassword);

                int rowsAffected = pstmt2.executeUpdate();
                System.out.println("Credentials inserted: " + rowsAffected + " rows");
            }

            conn.commit();
            System.out.println("Registration committed successfully for user: " + username);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback error: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private boolean userExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM profiles WHERE username = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    private boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM profiles WHERE email = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #f56565; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #48bb78; -fx-font-weight: bold;");
    }

    @FXML
    private void handleBack() {
        goToLogin();
    }

    private void goToLogin() {
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 450, 620);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            LoginController controller = loader.getController();
            controller.setStage(stage);

            stage.setTitle("🏆 Sports Manager Pro - Login");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            showError("Navigation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
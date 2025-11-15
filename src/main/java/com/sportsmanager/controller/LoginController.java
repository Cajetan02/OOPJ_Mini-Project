package com.sportsmanager.controller;

import com.sportsmanager.dao.SupabaseConnection;
import com.sportsmanager.model.User;
import com.sportsmanager.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Label connectionLabel;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;

    private Stage stage;

    @FXML
    public void initialize() {
        connectionLabel.setText("✅ Connected");
        usernameField.textProperty().addListener((obs, old, val) -> errorLabel.setText(""));
        passwordField.textProperty().addListener((obs, old, val) -> errorLabel.setText(""));
        passwordField.setOnAction(e -> handleLogin());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }

        loginButton.setDisable(true);
        loginButton.setText("Logging in...");

        try {
            User user = authenticateUser(username, password);

            if (user != null) {
                System.out.println("✅ User authenticated: " + user.getUsername());
                String sessionToken = java.util.UUID.randomUUID().toString();
                SessionManager.getInstance().login(user, sessionToken);

                // Verify session
                if (SessionManager.getInstance().isLoggedIn()) {
                    System.out.println("✅ Session created successfully");
                    System.out.println("👤 Current user: " + SessionManager.getInstance().getCurrentUser().getFullName());
                    System.out.println("🔑 User role: " + SessionManager.getInstance().getCurrentUser().getRole());
                    loadMainApplication();
                } else {
                    System.err.println("❌ Session creation failed!");
                    showError("Session creation failed. Please try again.");
                }
            } else {
                showError("Invalid username or password");
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            loginButton.setDisable(false);
            loginButton.setText("Login");
        }
    }

    private User authenticateUser(String username, String password) throws SQLException {
        // Modified query to join tables and get password in one go
        String sql = "SELECT p.id, p.username, p.full_name, p.email, p.role, uc.password_hash " +
                "FROM profiles p " +
                "LEFT JOIN user_credentials uc ON p.id = uc.user_id " +
                "WHERE p.username = ? AND p.is_active = true";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                if (storedHash == null) {
                    System.err.println("No password hash found for user: " + username);
                    return null;
                }

                if (BCrypt.checkpw(password, storedHash)) {
                    User user = new User(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                    updateLastLogin(user.getId());
                    return user;
                } else {
                    System.out.println("Password verification failed for user: " + username);
                }
            } else {
                System.out.println("User not found: " + username);
            }
        }

        return null;
    }

    private void updateLastLogin(String userId) {
        String sql = "UPDATE profiles SET last_login = CURRENT_TIMESTAMP WHERE id = ?::uuid";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            System.out.println("Last login updated for user ID: " + userId);
        } catch (SQLException e) {
            System.err.println("Failed to update last login: " + e.getMessage());
        }
    }

    private void loadMainApplication() {
        try {
            System.out.println("🔄 Loading main application...");
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Check if FXML file exists
            java.net.URL fxmlUrl = getClass().getResource("/fxml/main.fxml");
            if (fxmlUrl == null) {
                showError("FXML file not found at /fxml/main.fxml");
                System.err.println("❌ FXML file not found!");
                return;
            }
            System.out.println("✅ FXML file found: " + fxmlUrl);

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            System.out.println("🔄 Starting FXML load...");
            Parent root = loader.load();
            System.out.println("✅ FXML loaded successfully");

            Scene scene = new Scene(root, 1728, 972);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("🏆 Sports Manager Pro");
            stage.centerOnScreen();

            System.out.println("✅ Main application loaded successfully");

        } catch (IOException e) {
            System.err.println("❌ IOException while loading main screen:");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error cause: " + e.getCause());
            e.printStackTrace();
            showError("Failed to load main screen: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error while loading main screen:");
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            showError("Unexpected error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #f56565; -fx-font-weight: bold;");
    }

    @FXML
    private void handleRegister() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 550, 750);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            RegistrationController controller = loader.getController();
            controller.setStage(stage);

            stage.setTitle("🏆 Sports Manager Pro - Register");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            showError("Failed to open registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTestConnection() {
        connectionLabel.setText("Testing...");
        new Thread(() -> {
            boolean connected = SupabaseConnection.testConnection();
            javafx.application.Platform.runLater(() -> {
                connectionLabel.setText(connected ? "✅ Connected" : "❌ Disconnected");
            });
        }).start();
    }

    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText("Password Recovery");
        alert.setContentText("Please contact your administrator for password reset.");
        alert.showAndWait();
    }
}
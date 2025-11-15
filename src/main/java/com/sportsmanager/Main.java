package com.sportsmanager;

import com.sportsmanager.util.SessionManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            boolean isLoggedIn = SessionManager.getInstance().isLoggedIn();
            String fxmlFile = isLoggedIn ? "/fxml/main.fxml" : "/fxml/login.fxml";
            String windowTitle = isLoggedIn ? "🏆 Sports Manager Pro" : "🏆 Sports Manager Pro - Login";

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            javafx.scene.Parent root = loader.load();

            Scene scene = new Scene(root);

            String css = Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setTitle(windowTitle);

            // Optimized for 16-inch laptop screen
            primaryStage.setWidth(1536);    // Full width for 16-inch
            primaryStage.setHeight(864);    // Full height for 16-inch

            // Center on screen
            primaryStage.setX(0);
            primaryStage.setY(0);

            // Minimum size
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(700);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
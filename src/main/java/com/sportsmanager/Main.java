package com.sportsmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.sportsmanager.dao.DatabaseConnection;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseConnection.initializeDatabase();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), 1400, 800);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setTitle("🏆 Sports Event Manager - Multi-Sport Tournament System");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Start maximized for better view
        primaryStage.show();

        System.out.println("UI shown: primaryStage displayed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
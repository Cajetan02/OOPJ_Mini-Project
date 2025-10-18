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
        Scene scene = new Scene(loader.load(), 1000, 700);
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        
        primaryStage.setTitle("Sports Event Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
        // Helpful log when running headless/Xvfb so we can see UI reached show()
        System.out.println("UI shown: primaryStage displayed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.sportsmanager.util;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Utility class for displaying toast notifications
 */
public class NotificationUtil {

    private static final Duration DEFAULT_DURATION = Duration.seconds(3);
    private static final int NOTIFICATION_WIDTH = 350;
    private static final int NOTIFICATION_SPACING = 10;

    /**
     * Show success notification (green)
     */
    public static void success(Stage owner, String message) {
        showNotification(owner, "✅ " + message, "#48bb78", "#f0fff4");
    }

    /**
     * Show error notification (red)
     */
    public static void error(Stage owner, String message) {
        showNotification(owner, "❌ " + message, "#f56565", "#fff5f5");
    }

    /**
     * Show warning notification (orange)
     */
    public static void warning(Stage owner, String message) {
        showNotification(owner, "⚠️ " + message, "#ed8936", "#fffaf0");
    }

    /**
     * Show info notification (blue)
     */
    public static void info(Stage owner, String message) {
        showNotification(owner, "ℹ️ " + message, "#4299e1", "#ebf8ff");
    }

    /**
     * Show custom notification with specified colors
     */
    public static void showNotification(Stage owner, String message, String borderColor, String backgroundColor) {
        if (owner == null || !owner.isShowing()) {
            System.err.println("Cannot show notification: Stage is null or not showing");
            return;
        }

        Popup popup = new Popup();

        // Create notification content
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle(
                "-fx-text-fill: #2d3748; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 20;"
        );

        VBox container = new VBox(messageLabel);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: " + borderColor + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2); " +
                        "-fx-min-width: " + NOTIFICATION_WIDTH + "; " +
                        "-fx-max-width: " + NOTIFICATION_WIDTH + ";"
        );

        StackPane root = new StackPane(container);
        root.setStyle("-fx-background-color: transparent;");
        popup.getContent().add(root);

        // Position at top-right corner
        Scene scene = owner.getScene();
        double x = owner.getX() + scene.getWidth() - NOTIFICATION_WIDTH - 20;
        double y = owner.getY() + 80;

        popup.setX(x);
        popup.setY(y);
        popup.setAutoHide(true);

        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Fade out animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> popup.hide());

        // Auto-hide after duration
        PauseTransition delay = new PauseTransition(DEFAULT_DURATION);
        delay.setOnFinished(e -> fadeOut.play());

        // Show popup and start animations
        popup.show(owner);
        fadeIn.play();
        fadeIn.setOnFinished(e -> delay.play());

        // Allow click to dismiss
        root.setOnMouseClicked(e -> {
            delay.stop();
            fadeOut.play();
        });
    }

    /**
     * Show notification with custom duration
     */
    public static void showNotification(Stage owner, String message, String borderColor,
                                        String backgroundColor, Duration duration) {
        if (owner == null || !owner.isShowing()) {
            System.err.println("Cannot show notification: Stage is null or not showing");
            return;
        }

        Popup popup = new Popup();

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle(
                "-fx-text-fill: #2d3748; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 20;"
        );

        VBox container = new VBox(messageLabel);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: " + borderColor + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2); " +
                        "-fx-min-width: " + NOTIFICATION_WIDTH + "; " +
                        "-fx-max-width: " + NOTIFICATION_WIDTH + ";"
        );

        StackPane root = new StackPane(container);
        root.setStyle("-fx-background-color: transparent;");
        popup.getContent().add(root);

        Scene scene = owner.getScene();
        double x = owner.getX() + scene.getWidth() - NOTIFICATION_WIDTH - 20;
        double y = owner.getY() + 80;

        popup.setX(x);
        popup.setY(y);
        popup.setAutoHide(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> popup.hide());

        PauseTransition delay = new PauseTransition(duration);
        delay.setOnFinished(e -> fadeOut.play());

        popup.show(owner);
        fadeIn.play();
        fadeIn.setOnFinished(e -> delay.play());

        root.setOnMouseClicked(e -> {
            delay.stop();
            fadeOut.play();
        });
    }

    /**
     * Show persistent notification (doesn't auto-hide)
     */
    public static Popup showPersistentNotification(Stage owner, String message, String borderColor, String backgroundColor) {
        if (owner == null || !owner.isShowing()) {
            System.err.println("Cannot show notification: Stage is null or not showing");
            return null;
        }

        Popup popup = new Popup();

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle(
                "-fx-text-fill: #2d3748; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 20;"
        );

        Label closeLabel = new Label("✖");
        closeLabel.setStyle(
                "-fx-text-fill: #718096; " +
                        "-fx-font-size: 16px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 5 10;"
        );
        closeLabel.setOnMouseClicked(e -> popup.hide());
        closeLabel.setOnMouseEntered(e -> closeLabel.setStyle(
                "-fx-text-fill: #2d3748; " +
                        "-fx-font-size: 16px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 5 10;"
        ));
        closeLabel.setOnMouseExited(e -> closeLabel.setStyle(
                "-fx-text-fill: #718096; " +
                        "-fx-font-size: 16px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 5 10;"
        ));

        javafx.scene.layout.HBox content = new javafx.scene.layout.HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(messageLabel, closeLabel);

        VBox container = new VBox(content);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: " + borderColor + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2); " +
                        "-fx-min-width: " + NOTIFICATION_WIDTH + "; " +
                        "-fx-max-width: " + NOTIFICATION_WIDTH + ";"
        );

        StackPane root = new StackPane(container);
        root.setStyle("-fx-background-color: transparent;");
        popup.getContent().add(root);

        Scene scene = owner.getScene();
        double x = owner.getX() + scene.getWidth() - NOTIFICATION_WIDTH - 20;
        double y = owner.getY() + 80;

        popup.setX(x);
        popup.setY(y);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        popup.show(owner);
        fadeIn.play();

        return popup;
    }
}
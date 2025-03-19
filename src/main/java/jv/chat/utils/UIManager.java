package jv.chat.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jv.chat.controllers.ChatController;

import java.io.IOException;

public class UIManager {
    private static Stage primaryStage = new Stage();
    private static String currentUsername;

    public static void setPrimaryStage(Stage stage) {
        System.out.println("primary stage set");
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile) {
        System.out.println("switch scene");
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            primaryStage.setMaximized(true);
            primaryStage.setTitle(fxmlFile.replace(".fxml", ""));
            primaryStage.setMinWidth(1430);
            primaryStage.setMinHeight(750);
            primaryStage.centerOnScreen();
            primaryStage.setScene(new Scene(root));


            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }
}
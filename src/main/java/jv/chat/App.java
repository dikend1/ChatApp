package jv.chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import jv.chat.utils.UIManager;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(true);
        System.out.println("i started");
        UIManager.setPrimaryStage(primaryStage);
        UIManager.switchScene("login.fxml");

        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (Exception e) {
                System.out.println("Disconnected from server.");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("i stopped");
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
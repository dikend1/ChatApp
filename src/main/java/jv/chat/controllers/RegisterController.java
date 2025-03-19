package jv.chat.controllers;

import jv.chat.database.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import jv.chat.utils.UIManager;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userDAO.registerUser(username, password)) {
            showAlert("Успех", "Регистрация прошла успешно!");
        } else {
            showAlert("Ошибка", "Пользователь уже существует.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void goToLogin() {
        UIManager.switchScene("login.fxml");
    }

}
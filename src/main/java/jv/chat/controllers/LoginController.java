package jv.chat.controllers;

import jv.chat.database.UserDAO;
import jv.chat.models.User;
import jv.chat.utils.UIManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {

            UIManager.setCurrentUsername(username);

            UIManager.switchScene("chat.fxml");
        } else {
            showAlert("Ошибка входа", "Неверное имя пользователя или пароль.");
        }

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    @FXML
    private void goToRegister() {
        UIManager.switchScene("register.fxml");
    }
}
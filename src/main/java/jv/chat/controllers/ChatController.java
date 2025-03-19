package jv.chat.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import jv.chat.database.MessageDAO;
import jv.chat.database.UserDAO;
import jv.chat.models.Message;
import jv.chat.network.ChatClient;
import jv.chat.utils.UIManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static jv.chat.database.UserDAO.getAllUsernames;
import static jv.chat.database.UserDAO.getUserIdByUsername;
import static jv.chat.network.ChatServer.PORT;

public class ChatController {
    @FXML
    private ListView<String> contactsList;
    @FXML
    private Label chatHeader;
    @FXML
    private VBox chatHistory;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField messageInput;
    @FXML
    private Button sendButton;
    @FXML
    private Label accountName = new Label();


    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static ChatClient client;
    private static String username;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;




    @FXML
    public void initialize() {
        username = UIManager.getCurrentUsername();
        accountName.setText(username);

        sendButton.setOnAction(event -> sendMessageUI());
        messageInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                sendMessageUI();
                event.consume();
            }
        });

        try {
            client = new ChatClient(SERVER_ADDRESS, PORT, username);
            startReceivingMessages();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        loadContacts();

        contactsList.getSelectionModel().selectedItemProperty().addListener((obs, oldContact, newContact) -> {
            if (newContact != null) {
                System.out.println("Selected contact: " + newContact);
                loadChatHistory(newContact);
                chatHeader.setText(newContact);
            }
        });
    }

    private void startReceivingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    Message received = ChatClient.receiveMessage();
                    Platform.runLater(() ->{
                        if(received == null) return;
                        System.out.println("received message from : " + received.getSenderId() + " message " + received.getContent());
                        displayReceivedMessage(received);

                        String selectedContact = contactsList.getSelectionModel().getSelectedItem();
                        int selectedContactId = getUserIdByUsername(selectedContact);
                        if (received != null && received.getSenderId() == selectedContactId) {
                            loadChatHistory(selectedContact);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void loadContacts() {
        List<String> contacts = getAllUsernames(UserDAO.getUserIdByUsername(username));
        Platform.runLater(() -> contactsList.getItems().setAll(contacts));
    }

    private void sendMessageUI() {
        String selectedContact = contactsList.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            System.out.println("choose contact before sending message");
            return;
        }

        int receiverId = UserDAO.getUserIdByUsername(selectedContact);
        int senderId = UserDAO.getUserIdByUsername(username);

        Message message = new Message(senderId, receiverId, messageInput.getText().trim());
        if (message.getContent().trim().isEmpty()) return;



        Platform.runLater(() -> {
            displaySentMessage(message);
            messageInput.clear();
        });

        new Thread(() -> {
            try {
                ChatClient.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void displaySentMessage(Message message) {
        HBox messageBox = createMessageBubble(message.getContent(), "#3498db", Pos.CENTER_RIGHT);
        System.out.println("displaying sent message");
        chatHistory.getChildren().add(messageBox);
        chatHistory.layout();
    }

    private void displayReceivedMessage(Message message) {
        if (message == null || message.getContent().isEmpty())
            return;
        if(message.getReceiverId() == UserDAO.getUserIdByUsername(username)) {
            HBox messageBox = createMessageBubble(message.getContent(), "#2ecc71", Pos.CENTER_LEFT);
            System.out.println("displaying received message");
            chatHistory.getChildren().add(messageBox);
            chatHistory.layout();
        }
    }

    private HBox createMessageBubble(String text, String color, Pos alignment) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5));
        messageBox.setAlignment(alignment);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        messageLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 5px; -fx-background-radius: 5px;");

        messageBox.getChildren().add(messageLabel);
        return messageBox;
    }


    public void loadChatHistory(String contact) {
        int senderId = getUserIdByUsername(username);
        int receiverId = getUserIdByUsername(contact);

        List<Message> messages = MessageDAO.getChatHistory(senderId, receiverId);

        Platform.runLater(() -> {
            chatHistory.getChildren().clear();

            for (Message message : messages) {
                if(message.getSenderId() == senderId) {
                    displaySentMessage(message);
                }else{
                    displayReceivedMessage(message);
                }

            }
            Platform.runLater(() -> { chatHistory.requestLayout();
                scrollPane.requestLayout();scrollPane.setVvalue(1.0);});
        });
    }


}
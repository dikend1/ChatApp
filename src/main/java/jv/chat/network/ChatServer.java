package jv.chat.network;

import jv.chat.controllers.LoginController;
import jv.chat.database.MessageDAO;
import jv.chat.models.Message;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;

public class ChatServer {
    public static final int PORT = 12346;
    private Map<Integer, ClientHandler> clients = new ConcurrentHashMap<>();


    public ChatServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new Thread(new ClientHandler(clientSocket, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessageToDB(int receiverId, Message message) {
        MessageDAO.saveMessage(message.getSenderId(), receiverId, message.getContent());
    }


    public synchronized void addClient(int userID, ClientHandler clientHandler) {
        if(clients.containsKey(userID)) {
            System.out.println("Client is already connected");
        }
        clients.put(userID, clientHandler);
    }

    public synchronized void removeClient(int userID) {
        ClientHandler handler = clients.remove(userID);
        if (handler != null) {
            handler.closeConnection();
        }
    }

    public synchronized ClientHandler getClient(int userId) {
        return clients.get(userId);
    }

    public static void main(String[] args) {
        new ChatServer(PORT);
    }

    public static void stop() {
        System.exit(0);
    }
}
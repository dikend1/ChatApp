package jv.chat.network;

import jv.chat.models.Message;

import java.io.*;
import java.net.*;

import static jv.chat.database.UserDAO.extractReceiverId;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int userId;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            userId = 1;

            server.addClient(userId, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String username = (String) in.readObject();
            System.out.println("User connected: " + username);

            Message message;
            while ((message = (Message) in.readObject()) != null) {
                int receiverId = extractReceiverId(message);

                if (receiverId == -1) {
                    System.out.println("Invalid recipient. Message not sent.");
                    return;
                }

                server.sendMessageToDB(receiverId, message);
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(userId);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
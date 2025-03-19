package jv.chat.network;

import jv.chat.models.Message;

import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;


    public ChatClient(String serverAddress, int port, String username) {
        try {
            socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Message message) {
        try {
            synchronized (out) {
                out.writeObject(message);
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Message receiveMessage() throws IOException, InterruptedException {
        try {
            return (Message) in.readObject();
        } catch (EOFException e) {
            System.out.println("Connection closed.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Thread.sleep(100);
        }
        return null;
    }

    public Socket getSocket() {
        return socket;
    }
}
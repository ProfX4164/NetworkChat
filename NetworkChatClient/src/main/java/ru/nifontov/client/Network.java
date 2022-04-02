package ru.nifontov.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class Network {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8189;

    private int port;
    private String host;
    private Socket socket;
    private DataOutputStream socketOutput;
    private DataInputStream socketInput;

    private static Network INSTANCE;

    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
        }
        return INSTANCE;
    }

    private Network(String host, int port) {
        this.port = port;
        this.host = host;
    }

    private Network() {
        this(SERVER_HOST, SERVER_PORT);
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            socketInput = new DataInputStream(socket.getInputStream());
            socketOutput = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            String errorMessage = "Error with connect to server!";
            System.err.println(errorMessage);
            e.printStackTrace();
            return false;
        }

    }

    public void sendMessage(String message) throws IOException {
        try {
            socketOutput.writeUTF(message);
        } catch (IOException e) {
            System.err.println("Message could not be delivered to server");
            throw (e);
        }
    }

    public void waitMessage(Consumer<String> messageHandler) {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                try {
                    String message = socketInput.readUTF();
                    messageHandler.accept(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Message can not be written");
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void close() {
        try {
            socketInput.close();
            socketOutput.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close network");
            e.printStackTrace();
        }
    }
}

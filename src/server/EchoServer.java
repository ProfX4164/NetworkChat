package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static final int PORT = 8189;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start working. It is waiting some new connections");
            Socket clientSocket = serverSocket.accept();

            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("Client connected on");
            processLineConnection(input, output);
        } catch (IOException e) {
            System.err.println("Error server connect! Port: " + PORT);
            e.printStackTrace();
        }
    }

    private static void processLineConnection(DataInputStream input, DataOutputStream output) throws  IOException {
        while (true) {
            try {
                String message = input.readUTF();
                if (message.equals("/end")) {
                    break;
                }
                output.writeUTF("Echo " + message);
            } catch (IOException e) {
                System.out.println("Connection was lost");
                break;
            }
        }
    }
}

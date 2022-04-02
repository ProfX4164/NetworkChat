package ru.nifontov.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nifontov.client.ClientChat;
import ru.nifontov.client.Network;

import java.io.IOException;
import java.util.function.Consumer;

public class AuthController {
    public static final String AUTH_COMMAND = "/auth";
    public static final String AUTH_OK_COMMAND = "/authOk";

    @FXML
    private PasswordField passwordField;
    @FXML
    private Button authButton;
    @FXML
    private TextField loginField;
    private ClientChat clientChat;

    @FXML
    public void executeAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            clientChat.showErrorDialog("Login and password must be not empty!");
        }

        String authCommandMessage = String.format("%s %s %s",AUTH_COMMAND, login, password);
        try {
            Network.getInstance().sendMessage(authCommandMessage);
        } catch (IOException e) {
            clientChat.showErrorDialog("Failed to connection");
            e.printStackTrace();
        }
    }

    public void setClientChat(ClientChat clientChat) {
        this.clientChat = clientChat;
    }

    public void initializeMessageHandler() {
        Network.getInstance().waitMessage(new Consumer<String>() {
            @Override
            public void accept(String message) {
                if(message.startsWith(AUTH_OK_COMMAND)) {
                    String[] split = message.split(" ");
                    String userName = split[1];
                    Thread.currentThread().interrupt();
                    Platform.runLater(() -> {
                        clientChat.getStageChat().setTitle(userName);
                        clientChat.getAuthStage().close();
                    });
                } else {
                    Platform.runLater(() -> clientChat.showErrorDialog("User doesn't exist"));
                }
            }
        });
    }
}

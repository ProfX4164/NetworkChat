package ru.nifontov.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.nifontov.client.ClientChat;
import ru.nifontov.client.Network;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class ClientController {

    @FXML private TextField textField;
    @FXML private Button sendButton;
    @FXML private TextArea textArea;
    @FXML public ListView<String> userList;

    private ClientChat application;

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        String message = textField.getText().trim();

        String sender = null;
        if (!userList.getSelectionModel().isEmpty()) {
            sender = userList.getSelectionModel().getSelectedItem();
        }

        appendMessageToChat(message, sender);
        try {
            Network.getInstance().sendMessage(message);
        } catch (IOException e) {
            application.showErrorDialog("Error transport message through network");
            e.printStackTrace();
        }
    }

    private void appendMessageToChat(String message, String sender) {
        textArea.appendText(DateFormat.getDateTimeInstance().format(new Date()) + System.lineSeparator());
        if (sender != null) {
            textArea.appendText(sender + ":");
            textArea.appendText(System.lineSeparator());
        }
        if (!message.isEmpty()) {
            textArea.appendText(message);
            textArea.appendText(System.lineSeparator());
            textArea.appendText(System.lineSeparator());
            textField.setFocusTraversable(true);
            textField.clear();
        }
    }

    public void setApplication(ClientChat application) {
        this.application = application;
    }

    public void initializeNetworkHandler() {
        Network.getInstance().waitMessage(new Consumer<String>() {
            @Override
            public void accept(String message) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        appendMessageToChat(message, "Server");
                    }
                });
            }
        });
    }
}
package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.function.Consumer;

public class ClientController {

    @FXML private TextField textField;
    @FXML private Button sendButton;
    @FXML private TextArea textArea;
    @FXML public ListView<String> userList;

    private ClientChat application;
    private Network network;

    public void sendMessage(ActionEvent actionEvent) {
        String message = textField.getText();
        appendMessageToChat(message);
        try {
            network.sendMessage(message);
        } catch (IOException e) {
            application.showErrorDialog("Error transport message through network");
            e.printStackTrace();
        }
    }

    private void appendMessageToChat(String message) {
        if (!message.isEmpty()) {
            textArea.appendText(message);
            textArea.appendText(System.lineSeparator());
            textField.clear();
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
        network.waitMessage(new Consumer<String>() {
            @Override
            public void accept(String message) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        appendMessageToChat(message);
                    }
                });
            }
        });
    }

    public void setApplication(ClientChat application) {
        this.application = application;
    }
}
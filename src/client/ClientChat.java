package client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientChat extends Application {

    public static final String CONNECTION_ERROR_MESSAGE = "Error with connect to server!";
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("client-chat-template.fxml"));

        Parent loader = fxmlLoader.load();
        Scene scene = new Scene(loader);

        this.stage.setTitle("Network Chat");
        this.stage.setScene(scene);

        ClientController controller = fxmlLoader.getController();

        controller.userList.getItems().addAll("user1", "user2");

        stage.show();
        connectToServer(controller);
    }

    private void connectToServer(ClientController clientController) {
        Network network = new Network();
        boolean connectResult = network.connect();
        if (!connectResult) {
            String errorMessage = CONNECTION_ERROR_MESSAGE;
            System.err.println(errorMessage);
            return;
        }
        clientController.setNetwork(network);
        clientController.setApplication(this);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                stage.close();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

    public void showErrorDialog (String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
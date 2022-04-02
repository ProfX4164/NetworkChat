package ru.nifontov.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.nifontov.client.controllers.AuthController;
import ru.nifontov.client.controllers.ClientController;

import java.io.IOException;

public class ClientChat extends Application {

    public static final String CONNECTION_ERROR_MESSAGE = "Error with connect to server!";

    private Stage primaryStage;
    private Stage authStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        ClientController controller = createChatDialog(stage);

        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(getClass().getResource("auth-dialog-template.fxml"));

        createAuthDialog(authLoader);
        controller.initializeNetworkHandler();
    }

    private void createAuthDialog(FXMLLoader authLoader) throws IOException {
        AnchorPane authDialogPanel = authLoader.load();
        authStage = new Stage();
        authStage.initOwner(this.primaryStage);
        authStage.initModality(Modality.WINDOW_MODAL);

        authStage.setScene(new Scene(authDialogPanel));
        AuthController authController = authLoader.getController();
        authController.setClientChat(this);
        authController.initializeMessageHandler();
        authStage.showAndWait();
    }

    private ClientController createChatDialog(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("client-chat-template.fxml"));

        Parent loader = fxmlLoader.load();
        Scene scene = new Scene(loader);

        this.primaryStage.setTitle("Network Chat");
        this.primaryStage.setScene(scene);

        ClientController controller = fxmlLoader.getController();

        controller.userList.getItems().addAll("username1", "username2", "username3");

        stage.show();
        connectToServer(controller);
        return controller;
    }

    private void connectToServer(ClientController clientController) {
        boolean connectResult = Network.getInstance().connect();
        if (!connectResult) {
            String errorMessage = CONNECTION_ERROR_MESSAGE;
            System.err.println(errorMessage);
            showErrorDialog(errorMessage);
            return;
        }
        clientController.setApplication(this);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Network.getInstance().close();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

    public void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Stage getAuthStage() {
        return authStage;
    }

    public Stage getStageChat() {
        return primaryStage;
    }
}
package ru.nsu.fit.traffic.javafx.controller.rooms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SignUpController {
    @FXML TextField usernameText;
    @FXML TextField passwordText;
    @FXML TextField passwordText2;
    @FXML Button signUp;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void signUp() {
        //todo: Привет, Артур
    }
}

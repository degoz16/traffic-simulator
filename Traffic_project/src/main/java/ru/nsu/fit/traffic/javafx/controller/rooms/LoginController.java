package ru.nsu.fit.traffic.javafx.controller.rooms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.MainMenu;

import java.io.IOException;

public class LoginController {
  @FXML TextField usernameText;
  @FXML TextField passwordText;
  @FXML Button logIn;
  @FXML Button signUp;
  private Stage stage;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void logIn() {
    // todo: привет, артур
  }

  @FXML
  private void signUp() {
    System.out.println("sign up");
    MainMenu.setSignUp(stage);
  }
}

package ru.nsu.fit.traffic.javafx.controller.rooms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    try {
      System.out.println(getClass());
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../view/Signup.fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root);

      stage.setScene(scene);
      stage.show();

      SignUpController controller = loader.getController();
      controller.setStage(stage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

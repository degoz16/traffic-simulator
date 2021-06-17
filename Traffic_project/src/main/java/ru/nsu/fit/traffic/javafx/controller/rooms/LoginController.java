package ru.nsu.fit.traffic.javafx.controller.rooms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.controller.network.LoginControl;

public class LoginController {
  @FXML TextField usernameText;
  @FXML TextField passwordText;
  @FXML Button logIn;
  @FXML Button signUp;
  private Stage stage;
  private LoginControl loginControl;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void logIn() {
    loginControl = new LoginControl();
    loginControl.login(usernameText.getText(), passwordText.getText());
    //TODO
  }

  @FXML
  private void signUp() {
    System.out.println("sign up");
    try {
      System.out.println(getClass());
      FXMLLoader loader = new FXMLLoader(App.class.getResource("view/Signup.fxml"));
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

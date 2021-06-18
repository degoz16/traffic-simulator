package ru.nsu.fit.traffic.javafx.controller.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;


public class SignUpController {
  @FXML
  TextField usernameText;
  @FXML
  TextField passwordText;
  @FXML
  TextField passwordText2;
  @FXML
  Button signUpButton;
  private Stage stage;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void signUp() {
    if (passwordText.getText().equals(passwordText2.getText())) {

      if (ConnectionConfig.getConnectionConfig().getConnection().registration(
        usernameText.getText(),
        passwordText.getText(),
        passwordText2.getText()
      )) {
        ConnectionConfig.getConnectionConfig().setUsername(usernameText.getText());
        ConnectionConfig.getConnectionConfig().setPassword(passwordText.getText());
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/Login.fxml"));
        Parent root;
        try {
          root = loader.load();
          stage.getScene().setRoot(root);
          LoginController loginController = loader.getController();
          loginController.setStage(stage);
        } catch (IOException e) {
          e.printStackTrace();
        }
        stage.show();
      }
    } else {
      //TODO не совпало
    }
  }
}

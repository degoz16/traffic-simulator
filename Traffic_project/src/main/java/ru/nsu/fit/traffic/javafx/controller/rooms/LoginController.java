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
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.network.LoginControl;
import ru.nsu.fit.traffic.network.ConnectionImpl;

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
    if (loginControl.login(usernameText.getText(), passwordText.getText())) {
      try {
        ConnectionConfig.getConnectionConfig().setUsername(usernameText.getText());
        ConnectionConfig.getConnectionConfig().setPassword(passwordText.getText());
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/RoomButtonsView.fxml"));
        Parent root;
        root = loader.load();
        stage.getScene().setRoot(root);
        RoomController roomController = loader.getController();
        roomController.setStage(stage);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      
    }
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

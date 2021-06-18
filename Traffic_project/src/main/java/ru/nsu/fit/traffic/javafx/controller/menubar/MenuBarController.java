package ru.nsu.fit.traffic.javafx.controller.menubar;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.interfaces.control.MenuControlInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.javafx.controller.rooms.LoginController;
import ru.nsu.fit.traffic.javafx.controller.rooms.RoomController;
import ru.nsu.fit.traffic.network.ConnectionImpl;

public class MenuBarController {

  private MenuControlInterface saveLoadControl;

  private Stage stage;

  private ConnectionConfig connectionConfig = ConnectionConfig.getConnectionConfig();

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setSaveLoadControl(MenuControlInterface saveLoadControl) {
    this.saveLoadControl = saveLoadControl;
  }

  /** инициализация. */
  @FXML
  public void initialize() {

  }

  @FXML
  public void onSaveAs() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save project");
    fileChooser
            .getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("Traffic Simulator Projects", "*.tsp"));
    File file = fileChooser.showSaveDialog(stage);
    saveLoadControl.onSaveAs(file);
  }

  @FXML
  public void onSave() {
    saveLoadControl.onSave();
  }

  @FXML
  public void onNewProject() {
    saveLoadControl.onNewProject();
  }

  @FXML
  public void onOpenProject() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save project");
    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Traffic Simulator Projects", "*.tsp"));
    File file = fileChooser.showOpenDialog(stage);
    saveLoadControl.onOpenProject(file);
  }

  @FXML
  public void onConnectToServer() {
    TextInputDialog dialog = new TextInputDialog("localhost:8080");
    dialog.setTitle("Connection");
    dialog.setHeaderText("Enter ip address:port");
    dialog.setContentText("Address:");

    Optional<String> result = dialog.showAndWait();

    result.ifPresent(name -> {
      connectionConfig.setConnection(new ConnectionImpl(name));
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
    });


  }
}

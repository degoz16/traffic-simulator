package ru.nsu.fit.traffic.javafx.controller.menubar;

import java.io.File;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.controller.saveload.SaveLoadControlInterface;

public class MenuBarController {

  private SaveLoadControlInterface saveLoadControl;

  private Stage stage;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setSaveLoadControl(SaveLoadControlInterface saveLoadControl) {
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
}

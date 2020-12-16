package ru.nsu.fit.traffic.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.road.Spawner;

public class NodeSettingsController {

  private MainController mainController;

  @FXML private JFXTimePicker startTime;
  @FXML private Pane nodeSettingPane;
  @FXML private JFXTimePicker endTime;
  @FXML private TextField spawnerRate;

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public Pane getNodeSettingPane() {
    return nodeSettingPane;
  }

  public JFXTimePicker getStartTime() {
    return startTime;
  }

  public JFXTimePicker getEndTime() {
    return endTime;
  }

  public TextField getSpawnerRate() {
    return spawnerRate;
  }

  @FXML
  public void confirmSpawnerCreationAction() {
    Node lastNodeClick = mainController.getLastNodeClicked();
    String startTime =
        this.startTime.getValue() != null
            ? this.startTime.getValue().toString()
            : LocalTime.now().toString();
    String endTime =
        this.endTime.getValue() != null
            ? this.endTime.getValue().toString()
            : LocalTime.now().toString();
    lastNodeClick.setSpawner(
        new Spawner(startTime, endTime, Integer.parseInt(spawnerRate.getText())));
    mainController.updateMapView();
  }

  @FXML
  public void initialize() {
    spawnerRate.setStyle("-fx-control-inner-background: #454545;" + "-fx-text-inner-color: white;");
  }
}

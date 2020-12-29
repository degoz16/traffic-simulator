package ru.nsu.fit.traffic.javafx.controller.settings;

import com.jfoenix.controls.JFXTimePicker;
import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.interfaces.control.NodeSettingsControlInterface;

public class NodeSettingsController {

  @FXML
  private JFXTimePicker startTime;
  @FXML
  private Pane nodeSettingPane;
  @FXML
  private JFXTimePicker endTime;
  @FXML
  private TextField spawnerRate;

  private NodeSettingsControlInterface nodeSettingsControl;

  public void setNodeSettingsControl(NodeSettingsControlInterface nodeSettingsControl) {
    this.nodeSettingsControl = nodeSettingsControl;
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
    String startTime =
            this.startTime.getValue() != null
                    ? this.startTime.getValue().toString()
                    : LocalTime.now().toString();
    String endTime =
            this.endTime.getValue() != null
                    ? this.endTime.getValue().toString()
                    : LocalTime.now().toString();
    nodeSettingsControl.confirmSpawnerCreationAction(
            startTime, endTime, Integer.parseInt(spawnerRate.getText()));
  }

  @FXML
  public void initialize() {
    spawnerRate.setStyle("-fx-control-inner-background: #454545;" + "-fx-text-inner-color: white;");
  }
}

package ru.nsu.fit.traffic.javafx.controller.settings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.interfaces.control.RoadSettingsControlInterface;

import java.util.function.UnaryOperator;

public class RoadSettingsController {
    @FXML
    private Pane roadSettingsHelperPane;
    @FXML
    private Pane roadSettingsPane;
    @FXML
    private TextField lanesTextField;
    @FXML
    private TextField streetName;

    private RoadSettingsControlInterface roadSettingsControl;

    public void setRoadSettingsControl(RoadSettingsControlInterface roadSettingsControl) {
        this.roadSettingsControl = roadSettingsControl;
    }

    public TextField getLanesTextField() {
        return lanesTextField;
    }

    public Pane getRoadSettingsHelperPane() {
        return roadSettingsHelperPane;
    }

    public Pane getRoadSettingsPane() {
        return roadSettingsPane;
    }

    public TextField getStreetName() {
        return streetName;
    }

    @FXML
    public void deleteRoad() {
        roadSettingsControl.deleteRoad();
    }

    @FXML
    public void confirmRoadSettings() {
        roadSettingsControl.confirmRoadSettings(
                Integer.parseInt(lanesTextField.getText()),
                streetName.getText());
    }

//    public void updateRoad(Road road) {
//        lastRoadClicked = road;
//        getLanesTextField().setText(String.valueOf(road.getLanesNum()));
//        if (road.getStreet() != null) {
//            streetName.setText(road.getStreet().getName());
//        } else {
//            streetName.setText("");
//        }
//    }

    @FXML
    public void closeRoadSettings() {
        roadSettingsPane.setVisible(false);
    }

    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            int text_size = change.getControlNewText().length();
            lanesTextField.setStyle("-fx-control-inner-background: #454545;" + "-fx-text-inner-color: white;");
            streetName.setStyle("-fx-control-inner-background: #454545;" + "-fx-text-inner-color: white;");
            if (text_size <= 14) {
                return change;
            }
            return null;
        };

        streetName.setTextFormatter(new TextFormatter<>(integerFilter));
    }
}

package ru.nsu.fit.traffic.javafx.controller.settings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.controller.settings.TrafficLightSettingsControl;
import ru.nsu.fit.traffic.javafx.controller.edit.MainController;

import java.util.function.UnaryOperator;

public class TrafficLightController {

    private TrafficLightSettingsControl trafficLightSettingsControl;

    @FXML private TextField greenDelay;
    @FXML private Pane trafficLightPane;
    @FXML private TextField redDelay;

    public void setTrafficLightSettingsControl(TrafficLightSettingsControl trafficLightSettingsControl) {
        this.trafficLightSettingsControl = trafficLightSettingsControl;
    }

    @FXML
    public void closeSettings(){
//        trafficLightPane.setVisible(false);
//        mainController.getEditOperationManager().resetRoadsHighLight(lastNodeClicked);
//        mainController.getViewUpdater()
//                .updateMapView(mainController.getEditOperationManager());
        trafficLightSettingsControl.closeSettings();
    }

    @FXML
    public void confirmTrafficLightSettings() {
//        trafficLightPane.setVisible(false);
//        mainController.getEditOperationManager().resetRoadsHighLight(lastNodeClicked);
//        mainController.getEditOperationManager().applyTrafficLightSettings(
//                lastNodeClicked,
//                Integer.parseInt(greenDelay.getText()),
//                Integer.parseInt(redDelay.getText()));
        trafficLightSettingsControl.confirmSettings(
                Integer.parseInt(greenDelay.getText()),
                Integer.parseInt(redDelay.getText())
        );
    }

    public Pane getTrafficLightPane() {
        return trafficLightPane;
    }

    @FXML
    public void initialize() {
        greenDelay.setStyle("-fx-control-inner-background: #454545;" +
                "-fx-text-inner-color: white;");
        redDelay.setStyle("-fx-control-inner-background: #454545;" +
                "-fx-text-inner-color: white;");

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            int text_size = change.getControlNewText().length();

            if (input.matches("[0-9]*") && text_size <= 2) {
                return change;
            }
            return null;
        };

        greenDelay.setTextFormatter(new TextFormatter<>(integerFilter));
        redDelay.setTextFormatter(new TextFormatter<>(integerFilter));
    }

    public TextField getGreenDelay() {
        return greenDelay;
    }

    public TextField getRedDelay() {
        return redDelay;
    }
}

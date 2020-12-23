package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.node.Node;
import java.util.function.UnaryOperator;

public class TrafficLightController {
    private MainController mainController;

    @FXML private TextField greenDelay;
    @FXML private Pane trafficLightPane;
    @FXML private TextField redDelay;
    private Node lastNodeClicked;

    @FXML
    public void closeSettings(){
        trafficLightPane.setVisible(false);
        mainController.getEditOperationManager().resetRoadsHighLight(lastNodeClicked);
        mainController.getViewUpdater()
                .updateMapView(mainController.getEditOperationManager());
    }

    @FXML
    public void confirmTrafficLightSettings() {
        trafficLightPane.setVisible(false);
        mainController.getEditOperationManager().resetRoadsHighLight(lastNodeClicked);
        mainController.getEditOperationManager().applyTrafficLightSettings(
                lastNodeClicked,
                Integer.parseInt(greenDelay.getText()),
                Integer.parseInt(redDelay.getText()));
    }

    public void setLastNodeClicked(Node node){
        lastNodeClicked = node;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Pane getTrafficLightPane() {
        return trafficLightPane;
    }

    public void updateDelay(Node node){
        if (node.getTrafficLight() == null){
            greenDelay.setText("30");
            redDelay.setText("30");
        }
        else{
            greenDelay.setText(String.valueOf(lastNodeClicked.getTrafficLight().getGreenDelay()));
            redDelay.setText(String.valueOf(lastNodeClicked.getTrafficLight().getRedDelay()));
        }
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
}

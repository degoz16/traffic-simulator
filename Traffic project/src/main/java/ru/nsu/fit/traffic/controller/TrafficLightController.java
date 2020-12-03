package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class TrafficLightController {
    private MainController mainController;

    @FXML
    private TextField greenDelay;
    @FXML
    private Pane trafficLightPane;
    @FXML
    private TextField redDelay;

    @FXML
    public void confirmTrafficLightSettings() {
        System.out.println("confirmed");
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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
    }

}

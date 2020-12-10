package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.Node;

import java.util.List;

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

    public boolean isCorrectNode(Node node){
        return node.getRoadsInNum() >= 3;
    }

    public List<Integer> getPairsOfRoads(Node node){
        int[] a = new int[node.getRoadsInNum()];
        int[] b = new int[node.getRoadsInNum()];
        return null;
    }

    @FXML
    public void initialize() {
        greenDelay.setStyle("-fx-control-inner-background: #454545;" +
                "-fx-text-inner-color: white;");
        redDelay.setStyle("-fx-control-inner-background: #454545;" +
                "-fx-text-inner-color: white;");
    }
}

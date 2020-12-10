package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.TrafficMap;

public class BuildingController {
    @FXML
    Slider slider;

    @FXML
    Pane pane;

    private TrafficMap map;
    private MainController mainController;

    public Pane getPane() {
        return pane;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        map = mainController.getCurrMap();
    }

    @FXML
    public void initialize() {
        System.out.println(slider.getMax());
        System.out.println(slider.getMin());
    }

    @FXML
    public void closeSettings(){
        pane.setVisible(false);
    }

    @FXML
    public void confirmSettings(){
        mainController.lastPOIClicked.setWeight(slider.getValue());
        pane.setVisible(false);
    }

    @FXML
    public void deleteBuilding(){
        map.removePOI(mainController.lastPOIClicked);
        pane.setVisible(false);
    }
}

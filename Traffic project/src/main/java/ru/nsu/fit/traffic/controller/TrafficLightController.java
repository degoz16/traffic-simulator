package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TrafficLightController {
    private MainController mainController;

    @FXML TextField greenDelay;

    @FXML TextField redDelay;

    @FXML
    public void confirmTrafficLightSettings(){
        System.out.println("confirmed");
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize(){

    }

}

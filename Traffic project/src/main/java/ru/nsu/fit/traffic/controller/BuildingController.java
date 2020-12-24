package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.map.TrafficMap;

import java.util.function.UnaryOperator;

public class BuildingController {
    @FXML
    private Slider slider;

    @FXML
    private Pane pane;

    @FXML
    private TextField parkingPlaces;

    private TrafficMap map;
    private MainController mainController;

    public Pane getPane() {
        return pane;
    }

    public TextField getParkingPlaces() {
        return parkingPlaces;
    }

    public Slider getSlider() {
        return slider;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        map = mainController.getCurrMap();
    }

    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            int text_size = change.getControlNewText().length();

            if (input.matches("[0-9]*") && text_size <= 4) {
                return change;
            }
            return null;
        };

        parkingPlaces.setTextFormatter(new TextFormatter<>(integerFilter));
        System.out.println(slider.getMax());
        System.out.println(slider.getMin());
        parkingPlaces.setStyle("-fx-control-inner-background: #454545;" +
                "-fx-text-inner-color: white;");
    }

    @FXML
    public void closeSettings() {
        pane.setVisible(false);
    }

    @FXML
    public void confirmSettings() {
        mainController.getLastPOIClicked().setWeight(slider.getValue());
        mainController.getLastPOIClicked().setNumberOfParkingPlaces(Integer.parseInt(parkingPlaces.getText()));
        pane.setVisible(false);
        mainController.updateStatistics();
    }

    @FXML
    public void deleteBuilding() {
        map.removePOI(mainController.getLastPOIClicked());
        mainController.getViewUpdater()
                .updateMapView(mainController.getEditOperationManager());
        pane.setVisible(false);
        mainController.updateStatistics();
    }
}

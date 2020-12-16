package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.TrafficMap;

public class RoadSettingsController {
    @FXML private Pane roadSettingsHelperPane;
    @FXML private Pane roadSettingsPane;
    @FXML private TextField lanesTextField;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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

    @FXML
    public void deleteRoad() {
        Road lastRoadClicked = mainController.getLastRoadClicked();
        TrafficMap currMap = mainController.getCurrMap();

        if (lastRoadClicked.getBackRoad().getLanesNum() == 0) {
            if (lastRoadClicked.getFrom().getRoadsOutNum() <= 1) {
                lastRoadClicked.getFrom().removeFromPlaceOfInterest();
                currMap.removeNode(lastRoadClicked.getFrom());
            }
            if (lastRoadClicked.getTo().getRoadsInNum() <= 1) {
                lastRoadClicked.getTo().removeFromPlaceOfInterest();
                currMap.removeNode(lastRoadClicked.getTo());
            }
            lastRoadClicked.getBackRoad().disconnect();
            currMap.removeRoad(lastRoadClicked.getBackRoad());
            lastRoadClicked.disconnect();
            currMap.removeRoad(lastRoadClicked);
        } else {
            lastRoadClicked.clearLanes();
        }
        mainController.updateMapView();
    }

    @FXML
    public void confirmRoadSettings() {
        Road lastRoadClicked = mainController.getLastRoadClicked();

        int oldLanesNum = lastRoadClicked.getLanesNum();
        int newLanesNum = Integer.parseInt(lanesTextField.getText());
        if (newLanesNum > 0) {
            if (newLanesNum < oldLanesNum) {
                for (int i = newLanesNum; i < oldLanesNum; i++) {
                    lastRoadClicked.removeLane(i);
                }
            } else {
                for (int i = oldLanesNum; i < newLanesNum; i++) {
                    lastRoadClicked.addLane(i);
                }
            }
            mainController.updateMapView();
        } else {
            deleteRoad();
        }
    }

    @FXML
    public void closeRoadSettings() {
        roadSettingsPane.setVisible(false);
    }


}

package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.road.Street;

import java.util.function.UnaryOperator;

public class RoadSettingsController {
    @FXML
    private Pane roadSettingsHelperPane;
    @FXML
    private Pane roadSettingsPane;
    @FXML
    private TextField lanesTextField;

    private Road lastRoadClicked;
    private MainController mainController;

    @FXML
    private TextField streetName;

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
        if (lastRoadClicked.getBackRoad().getLanesNum() == 0) {
            if (lastRoadClicked.getFrom().getRoadsOutNum() <= 1) {
                lastRoadClicked.getFrom().removeFromPlaceOfInterest();
                mainController.getCurrMap().removeNode(lastRoadClicked.getFrom());
            }
            if (lastRoadClicked.getTo().getRoadsInNum() <= 1) {
                lastRoadClicked.getTo().removeFromPlaceOfInterest();
                mainController.getCurrMap().removeNode(lastRoadClicked.getTo());
            }
            lastRoadClicked.getBackRoad().disconnect();
            mainController.getCurrMap().removeRoad(lastRoadClicked.getBackRoad());
            lastRoadClicked.disconnect();
            mainController.getCurrMap().removeRoad(lastRoadClicked);
        } else {
            lastRoadClicked.clearLanes();
        }
        lastRoadClicked.getTo().setTrafficLight(null);
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
            return;
        }
        if (streetName.getText().equals("") && lastRoadClicked.getStreet() != null) {
            lastRoadClicked.disconnectWithStreet();
        } else if (!streetName.getText().equals("")) {
            if (lastRoadClicked.getStreet() != null && !streetName.getText().equals(lastRoadClicked.getStreet().getName())) {
                lastRoadClicked.disconnectWithStreet();
            }
            Street currStreet = null;
            for (Street s : mainController.getCurrMap().getStreets()) {
                if (s.getName().equals(streetName.getText())) {
                    currStreet = s;
                    break;
                }
            }
            if (currStreet == null) {
                currStreet = new Street(streetName.getText());
                mainController.getCurrMap().getStreets().add(currStreet);
            }
            if (lastRoadClicked.getBackRoad() != null) {
                lastRoadClicked.getBackRoad().disconnectWithStreet();
                currStreet.addRoad(lastRoadClicked.getBackRoad());
                lastRoadClicked.getBackRoad().setCurrStreet(currStreet);
            }
            currStreet.addRoad(lastRoadClicked);
            lastRoadClicked.setCurrStreet(currStreet);
           // System.out.println(currStreet);
        }

    }

    public void updateRoad(Road road) {
        lastRoadClicked = road;
        getLanesTextField().setText(String.valueOf(road.getLanesNum()));
        if (road.getStreet() != null) {
            streetName.setText(road.getStreet().getName());
        } else {
            streetName.setText("");
        }
    }

    @FXML
    public void closeRoadSettings() {
        roadSettingsPane.setVisible(false);
    }

    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            int text_size = change.getControlNewText().length();

            if (text_size <= 14) {
                return change;
            }
            return null;
        };

        streetName.setTextFormatter(new TextFormatter<Object>(integerFilter));
    }


}

package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.road.Street;

public class StatisticsController {
    @FXML
    private Text carSpawners;
    @FXML
    private Text streets;
    @FXML
    private Text roads;
    @FXML
    private Text buildings;
    @FXML
    private Text connectivity;
    @FXML
    private VBox streetsView;
    @FXML
    private Pane statistics, s;
    @FXML
    private ScrollPane scrollView;
    private MainController mainController;
    private TrafficMap trafficMap;

    @FXML
    public void initialize() {
        scrollView.setStyle("-fx-background: #454545;\n -fx-background-color: #454545");
        scrollView.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollView.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.trafficMap = mainController.getCurrMap();
    }

    public Pane getStatistics() {
        return statistics;
    }

    public void updateStatistics() {
        int car = 0;
        streets.setText(String.valueOf(trafficMap.getStreets().size()));
        streetsView.getChildren().clear();
        for (Street s : trafficMap.getStreets()) {
            Text text = new Text(s.getName());
            text.setFill(Paint.valueOf("#ffffff"));
            streetsView.getChildren().add(text);
        }
        int roads = 0;
        for (int i = 0; i < trafficMap.getRoadCount(); i++) {
            Road r = trafficMap.getRoad(i);
            if (trafficMap.getRoads().contains(r.getBackRoad()) && trafficMap.getRoads().indexOf(r.getBackRoad()) < i)
                continue;
            roads++;
        }
        this.roads.setText(String.valueOf(roads));
        buildings.setText(String.valueOf(trafficMap.getPlaceOfInterest().size()));
        for (Node n : trafficMap.getNodes()) {
            if (n.getSpawners() != null && n.getSpawners().size() != 0) {
                car++;
            }
        }
        carSpawners.setText(String.valueOf(car));

    }
}

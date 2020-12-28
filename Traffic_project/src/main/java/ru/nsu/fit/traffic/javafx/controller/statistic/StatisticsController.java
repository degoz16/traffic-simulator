package ru.nsu.fit.traffic.javafx.controller.statistic;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;


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
    private ScrollPane scrollView;

    public Text getCarSpawners() {
        return carSpawners;
    }

    public Text getStreets() {
        return streets;
    }

    public Text getRoads() {
        return roads;
    }

    public Text getBuildings() {
        return buildings;
    }

    public Text getConnectivity() {
        return connectivity;
    }

    public VBox getStreetsView() {
        return streetsView;
    }

    @FXML
    public void initialize() {
        scrollView.setStyle("-fx-background: #454545;\n -fx-background-color: #454545");
        scrollView.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollView.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        carSpawners.setFill(Paint.valueOf("white"));
        streets.setFill(Paint.valueOf("white"));
        roads.setFill(Paint.valueOf("white"));
        buildings.setFill(Paint.valueOf("white"));
        connectivity.setFill(Paint.valueOf("white"));
    }
}

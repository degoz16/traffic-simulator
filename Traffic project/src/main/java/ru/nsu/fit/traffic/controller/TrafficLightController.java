package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.trafficlight.TrafficLight;
import ru.nsu.fit.traffic.model.trafficlight.TrafficLightConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.UnaryOperator;

public class TrafficLightController {
    private MainController mainController;

    @FXML
    private TextField greenDelay;
    @FXML
    private Pane trafficLightPane;
    @FXML
    private TextField redDelay;
    private Node lastNodeClicked;

    @FXML
    public void closeSettings(){
        trafficLightPane.setVisible(false);
        mainController.updateMapView();
    }

    @FXML
    public void confirmTrafficLightSettings() {
        trafficLightPane.setVisible(false);
        List<Road> roadsGreen = new ArrayList<>();
        List<Road> roadsRed = new ArrayList<>();
        List<Integer> roadsIndexes= findPairOfRoad(lastNodeClicked);
        for (int i = 0; i < lastNodeClicked.getRoadsInNum(); ++i){
            if (roadsIndexes.get(0) != i && roadsIndexes.get(1) != i)
                roadsRed.add((Road)lastNodeClicked.getRoadInStream().toArray()[i]);
            else roadsGreen.add((Road)lastNodeClicked.getRoadInStream().toArray()[i]);
        }
        TrafficLightConfig greenConfig = new TrafficLightConfig(
                Integer.parseInt(greenDelay.getText()),roadsGreen);
        TrafficLightConfig redConfig = new TrafficLightConfig(
                Integer.parseInt(redDelay.getText()),roadsRed);
        TrafficLight trafficLight = new TrafficLight(greenConfig, redConfig);
        lastNodeClicked.setTrafficLight(trafficLight);
        mainController.getEOM().setCurrentOperation(EditOperation.NONE);
        mainController.updateMapView();
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

    public boolean isCorrectNode(Node node){
        return node.getRoadsInNum() >= 3;
    }

    public List<Integer> findPairOfRoad(Node node){
        List<Double> k = new ArrayList<>();
        for (Iterator<Road> it = node.getRoadInStream().iterator(); it.hasNext(); ) {
            Road r = it.next();
            double a = -(r.getFrom().getY() - r.getTo().getY()) / (r.getFrom().getX() - r.getTo().getX());
            k.add(Math.atan(a));
            System.out.println(Math.atan(a));
        }
        double d2 = Double.MAX_VALUE;
        double d1 = d2;
        int curr = -1;

        for (int i = 1; i < k.size(); ++i){
            if (Math.abs(k.get(0) - k.get(i)) < d1){
                d1 = Math.abs(k.get(0) - k.get(i));
                curr = i;
            }
        }

        int curr2 = -1;
        for (int i = 1; i < k.size(); ++i){
            if (curr != i && Math.abs(k.get(0) - k.get(i)) < d1){
                d2 = Math.abs(k.get(0) - k.get(i));
                curr2 = i;
            }
        }
        List<Integer> res = new ArrayList<>();
        res.add(curr);
        if (d1 < d2){
           res.add(0);
        }
        else {
            res.add(curr2);
        }
        return res;
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

        greenDelay.setTextFormatter(new TextFormatter<Object>(integerFilter));
        redDelay.setTextFormatter(new TextFormatter<Object>(integerFilter));
    }
}

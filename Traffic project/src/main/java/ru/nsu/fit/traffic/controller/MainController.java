package ru.nsu.fit.traffic.controller;

import java.util.List;
import java.util.function.UnaryOperator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.model.*;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.controller.painters.ObjectPainter;

/**
 * Контроллер основной сцены, на которой располагаются все остальные.
 */
public class MainController {

    private final int NODE_SIZE = 10;
    private final int LANE_SIZE = 10;
    private final int POINT_SIZE = 1;

    @FXML
    public ScrollPane mainScrollPane;
    @FXML
    public AnchorPane mainPane;
    @FXML
    public Pane numberOfLanesPane,
                roadSignPane;
    @FXML       //Settings number of lanes on creation
    public TextField backLanesTextField,
            forwardLanesTextField,
    //Settings number of lanes on road menu
    lanesTextField;

    @FXML
    public ComboBox<Integer> speedComboBox;

    @FXML
    public Pane roadSettingsPane;

    private Road lastRoadClicked;
    private final TrafficMap currMap = new TrafficMap();
    private Stage stage;
    //private boolean shapeChanged = false;
    private final UpdateListener updateListener = (ListenerAction action) -> {
        switch (action) {
            case MAP_UPDATE -> updateMapView();
        }
    };

    private final EditOperationsManager editOperationsManager = new EditOperationsManager(currMap);
    private final ObjectPainter objectPainter = new ObjectPainter(LANE_SIZE, NODE_SIZE);

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    private void stopOperation() {
        numberOfLanesPane.setVisible(false);
        editOperationsManager.resetLastNode();
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
    }

    /**
     * инициализация.
     */
    @FXML
    public void initialize() {
        numberOfLanesPane.setVisible(false);
        roadSettingsPane.setVisible(false);
        roadSignPane.setVisible(false);
        //roadBuilder = new RoadBuilder(currMap, LANE_SIZE);
        speedComboBox.setItems(FXCollections.observableArrayList(20,30,40,50,60,70,80,90,100,110,120));

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            int text_size = change.getControlNewText().length();

            if (input.matches("[0-5]*") && text_size <= 1) {
                return change;
            }
            return null;
        };

        backLanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        backLanesTextField.setText("1");
        backLanesTextField.textProperty().addListener((observable, oldValue, newValue) ->
                editOperationsManager.setLanesNumRight(Integer.parseInt(newValue)));

        lanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        lanesTextField.setText("1");

        forwardLanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        forwardLanesTextField.setText("1");
        forwardLanesTextField.textProperty().addListener((observable, oldValue, newValue) ->
                editOperationsManager.setLanesNumLeft(Integer.parseInt(newValue)));

        mainPane.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> {
                    event.consume();
                    System.out.println("MAIN PANE CLICK");
                    switch (editOperationsManager.getCurrentOperation()) {
                        case ROAD_CREATION -> {
                            editOperationsManager.buildRoadOnEmpty(event.getX(), event.getY());
                            updateMapView();
                        }
                    }
                    //todo другие операции
                }
                case SECONDARY -> stopOperation();
                //todo другие функции
            }

        });
    }

    @FXML
    public void startSimulation() {
        System.out.println("пуск");
    }

    @FXML
    public void pauseSimulation() {
        System.out.println("пауза");
    }

    @FXML
    public void stopSimulation() {
        System.out.println("стоп");
    }

    @FXML
    public void roadButtonClicked() {
        roadSignPane.setVisible(false);
        switch (editOperationsManager.getCurrentOperation()) {
            case NONE -> {
                numberOfLanesPane.setVisible(true);
                editOperationsManager.setCurrentOperation(EditOperation.ROAD_CREATION);
            }
            case ROAD_CREATION -> {
                numberOfLanesPane.setVisible(false);
                editOperationsManager.resetLastNode();
                editOperationsManager.setCurrentOperation(EditOperation.NONE);
            }
        }
    }

    @FXML
    public void trafficLightButtonClicked() {
        System.out.println("светофор");
    }

    @FXML
    public void buildingButtonClicked() {
        System.out.println("точки интереса");
    }

    @FXML
    public void carButtonClicked() {
        System.out.println("машина");
    }

    @FXML
    public void roadSignButtonClicked() {
        roadSignPane.setVisible(true);
        roadSettingsPane.setVisible(false);
        numberOfLanesPane.setVisible(false);
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
    }

    @FXML
    public void showStatistic() {
        System.out.println("статистика");
    }

    @FXML
    public void speedUpSimulation() {
        System.out.println("быстрее");
    }

    @FXML
    public void speedDownSimulation() {
        System.out.println("медленнее");
    }


    @FXML
    public void closeRoadSettings() {
        roadSettingsPane.setVisible(false);
    }

    @FXML
    public void confirmRoadSettings() {
        int oldLanesNum = lastRoadClicked.getLanesNum();
        int newLanesNum = Integer.parseInt(lanesTextField.getText());
        if (newLanesNum > 0) {
            if (newLanesNum < oldLanesNum) {
                for (int i = newLanesNum; i < oldLanesNum; i++) {
                    lastRoadClicked.removeLane(i);
                }
            }
            else {
                for (int i = oldLanesNum; i < newLanesNum; i++) {
                    lastRoadClicked.addLane(i);
                }
            }
            updateMapView();
        } else {
            deleteRoad();
        }
    }

    @FXML
    public void deleteRoad() {
        if (lastRoadClicked.getBackRoad().getLanesNum() == 0) {
            if (lastRoadClicked.getFrom().getRoadsOutNum() <= 1) {
                currMap.removeNode(lastRoadClicked.getFrom());
            }
            if (lastRoadClicked.getTo().getRoadsInNum() <= 1) {
                currMap.removeNode(lastRoadClicked.getTo());
            }
            lastRoadClicked.getBackRoad().disconnect();
            currMap.removeRoad(lastRoadClicked.getBackRoad());
            lastRoadClicked.disconnect();
            currMap.removeRoad(lastRoadClicked);
        } else {
            lastRoadClicked.clearLanes();
        }
        updateMapView();
    }


    /**
     * Метод отрисовки текущего состояния карты
     */
    private void updateMapView() {
        Platform.runLater(() -> {
            mainPane.getChildren().clear();
            roadSettingsPane.setVisible(false);
            roadSignPane.setVisible(false);

            currMap.forEachRoads(road -> {
                List<List<Shape>> roadShape = objectPainter.paintRoad(road);
                if (roadShape.size() != road.getLanesNum()) {
                    System.err.println(roadShape.size() + "!=" + road.getLanesNum());
                    throw new RuntimeException();
                }
                for (int i = 0; i < road.getLanesNum(); i++) {
                    int finalI = i;
                    roadShape.get(i).forEach(shape -> {
                        shape.setOnMouseClicked(event -> {
                            System.out.println("ROAD CLICK");
                            Lane lane = road.getLane(finalI);
                            switch (event.getButton()) {
                                case PRIMARY -> {
                                    event.consume();
                                    //Point2D parentCoords = shape.localToParent(event.getX(), event.getY());
                                    switch (editOperationsManager.getCurrentOperation()) {
                                        case ROAD_CREATION -> {
                                            editOperationsManager.buildRoadOnRoad(event.getX(), event.getY(), road);
                                            updateMapView();
                                        }
                                        case NONE -> {
                                            lastRoadClicked = road;
                                            roadSettingsPane.setVisible(true);
                                            roadSettingsPane.setLayoutX(event.getX());
                                            roadSettingsPane.setLayoutY(event.getY());
                                            lanesTextField.setText(String.valueOf(road.getLanesNum()));
                                        }
                                    }
                                }
                            }
                            //todo другие операции
                        });
                        mainPane.getChildren().add(shape);
                    });
                }
            });
            currMap.forEachNodes(node -> {
                Shape nodeShape = objectPainter.paintNode(node);
                nodeShape.setOnMouseClicked(event -> {
                    System.out.println("NODE CLICK");
                    //Node nodeRef = node;
                    switch (event.getButton()) {
                        case PRIMARY -> {
                            event.consume();
                            switch (editOperationsManager.getCurrentOperation()) {
                                case ROAD_CREATION -> {
                                    editOperationsManager.buildRoadOnNode(node);
                                    updateMapView();
                                }
                            }
                        }
                    }
                    //todo другие операции
                });
                mainPane.getChildren().add(nodeShape);
            });
            mainPane.getChildren().add(roadSettingsPane);
        });
    }
}

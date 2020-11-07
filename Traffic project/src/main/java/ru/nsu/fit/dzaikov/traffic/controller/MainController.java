package ru.nsu.fit.dzaikov.traffic.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.nsu.fit.dzaikov.traffic.logic.*;
import ru.nsu.fit.dzaikov.traffic.model.*;
import ru.nsu.fit.dzaikov.traffic.painted.*;

/**
 * Контроллер основной сцены, на которой располагаются все остальные.
 */
public class MainController {

    public TrafficMap currMap;
    public RoadBuilder roadBuilder;

    private EditOperation currOperation;
    private final int POINT_SIZE = 1;
    private Stage stage;

    public void setPrimaryStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    public ScrollPane mainScrollPane;

    @FXML
    public AnchorPane mainPane;

    /**
     * инициализация.
     */
    @FXML
    public void initialize() {
        currMap = new TrafficMap();
        roadBuilder = new RoadBuilder(currMap);

        mainPane.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent t) -> {
            if (currOperation == null)
                return;
            int x = (int)(t.getX() + mainScrollPane.getVvalue())/ POINT_SIZE;
            int y = (int)(t.getY() + mainScrollPane.getHvalue())/ POINT_SIZE;
            switch (currOperation){
                case ROAD_CREATION_STEP_1 ->{
                    roadBuilder.handleOperation(currOperation, x, y);
                    currOperation = EditOperation.ROAD_CREATION_STEP_2;
                }
                case ROAD_CREATION_STEP_2 ->{
                    Road curr = roadBuilder.handleOperation(currOperation, x, y);
                    if (curr == null){
                        return;
                    }
                    mainPane.getChildren().addAll(PaintedRoad.getShape(curr));
                    stage.show();
                    currOperation = EditOperation.NONE;
                }
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
        if (currOperation == EditOperation.ROAD_CREATION_STEP_1 ||
                currOperation == EditOperation.ROAD_CREATION_STEP_2) {
            currOperation = EditOperation.NONE;
            return;
        }

        currOperation = EditOperation.ROAD_CREATION_STEP_1;
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
    public void trafficSignButtonClicked() {
        System.out.println("дорожный знак");
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
}

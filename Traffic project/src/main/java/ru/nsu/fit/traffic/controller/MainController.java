package ru.nsu.fit.traffic.controller;

import java.util.UUID;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.logic.EditOperation;
import ru.nsu.fit.traffic.logic.RoadBuilder;
import ru.nsu.fit.traffic.model.Road;
import ru.nsu.fit.traffic.model.TrafficMap;
import ru.nsu.fit.traffic.painted.ObjectPainter;

/**
 * Контроллер основной сцены, на которой располагаются все остальные.
 */
public class MainController {

    private final int NODE_SIZE = 15;
    private final int LANE_SIZE = 15;
    private final int POINT_SIZE = 1;
    @FXML
    public ScrollPane mainScrollPane;
    @FXML
    public AnchorPane mainPane;
    private TrafficMap currMap;
    private RoadBuilder roadBuilder;
    private EditOperation currOperation;
    private Stage stage;
    private ObjectPainter painter;
    private boolean shapeChanged = false;
    private String lastPeekedShape = UUID.randomUUID().toString();

    public void setPrimaryStage(Stage stage){
        this.stage = stage;
    }

    /**
     * инициализация.
     */
    @FXML
    public void initialize() {
        currMap = new TrafficMap();
        roadBuilder = new RoadBuilder(currMap, LANE_SIZE);
        painter = new ObjectPainter(LANE_SIZE, NODE_SIZE);

        mainPane.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent t) -> {
            if (shapeChanged) {
                shapeChanged = false;
            } else {
                lastPeekedShape = UUID.randomUUID().toString();
            }
            if (currOperation == null)
                return;
            int x = (int)(t.getX() + mainScrollPane.getVvalue())/ POINT_SIZE;
            int y = (int)(t.getY() + mainScrollPane.getHvalue())/ POINT_SIZE;
            switch (currOperation){
                case ROAD_CREATION_STEP_1 ->{
                    roadBuilder.handleOperation(currOperation, x, y, lastPeekedShape);
                    currOperation = EditOperation.ROAD_CREATION_STEP_2;
                }
                case ROAD_CREATION_STEP_2 ->{
                    Road curr = roadBuilder.handleOperation(currOperation,
                      x, y, lastPeekedShape);

                    if (curr == null){
                        return;
                    }
                    Shape road = painter.paintRoad(roadBuilder.getRoadId(), curr);
                    road.onMouseClickedProperty().set(lastPressedShape());

                    Shape nodeTo = painter.paintNode(roadBuilder.getToId(), curr.getTo());
                    nodeTo.onMouseClickedProperty().set(lastPressedShape());

                    Shape nodeFrom = painter.paintNode(roadBuilder.getFromId(), curr.getFrom());
                    nodeFrom.onMouseClickedProperty().set(lastPressedShape());

                    mainPane.getChildren().add(road);
                    mainPane.getChildren().add(nodeTo);
                    mainPane.getChildren().add(nodeFrom);

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

    private EventHandler<MouseEvent> lastPressedShape() {
        return ((EventHandler<MouseEvent>)(MouseEvent x) -> {
            shapeChanged = true;
            lastPeekedShape = x.getPickResult().getIntersectedNode().getId();
            System.out.println(lastPeekedShape);
        });
    }
}

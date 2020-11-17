package ru.nsu.fit.traffic.controller;

import java.util.function.UnaryOperator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.model.ListenerAction;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.UpdateListener;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.TrafficMap;
import ru.nsu.fit.traffic.controller.painters.ObjectPainter;

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
    @FXML
    public Pane numberOfLanesPane;
    @FXML
    public TextField backLanesTextField,
            forwardLanesTextField;
    private TrafficMap currMap = new TrafficMap();;
    //private RoadBuilder roadBuilder;
    private EditOperation currOperation;
    private Stage stage;
    private boolean shapeChanged = false;
    //private String lastPeekedShape = UUID.randomUUID().toString();
    private UpdateListener updateListener = (ListenerAction action) -> {
      switch (action) {
          case MAP_UPDATE -> updateMapView();
      }
    };

    private final EditOperationsManager editOperationsManager = new EditOperationsManager(currMap);
    private final ObjectPainter objectPainter = new ObjectPainter(LANE_SIZE, NODE_SIZE);

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * инициализация.
     */
    @FXML
    public void initialize() {
        numberOfLanesPane.setVisible(false);
        //roadBuilder = new RoadBuilder(currMap, LANE_SIZE);

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            int text_size = change.getControlNewText().length();

            if (input.matches("[0-5]*") && text_size <=1) {
                return change;
            }
            return null;
        };

        backLanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        forwardLanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));

        mainPane.setOnMouseClicked(event -> {
            System.out.println("MAIN PANE CLICK");
            switch (editOperationsManager.getCurrentOperation()) {
                case ROAD_CREATION -> {
                    editOperationsManager.buildRoadOnEmpty(event.getX(), event.getY());
                    updateMapView();
                }
            }
/*            if (shapeChanged) {
                shapeChanged = false;
            } else {
                lastPeekedShape = UUID.randomUUID().toString();
            }
            if (currOperation == null)
                return;

            int x = (int) (t.getX() + mainScrollPane.getVvalue()) / POINT_SIZE;
            int y = (int) (t.getY() + mainScrollPane.getHvalue()) / POINT_SIZE;

            switch (currOperation) {
                case ROAD_CREATION_STEP_1 -> {
                    //todo: нужно бы чекать, не ставим ли мы на дорогу
                    roadBuilder.handleOperation(currOperation, x, y, lastPeekedShape, -1, -1);
                    currOperation = EditOperation.ROAD_CREATION_STEP_2;
                    numberOfLanesPane.setVisible(false);

                }
                case ROAD_CREATION_STEP_2 -> {
                    //todo: нужно бы чекать, не ставим ли мы на дорогу
                    Road[] roadArr = roadBuilder.handleOperation(currOperation,
                            x, y, lastPeekedShape,
                            Integer.parseInt(backLanesTextField.getText()),
                            Integer.parseInt(forwardLanesTextField.getText()));

                    if (roadArr == null) {
                        return;
                    }

                    for (Road curr: roadArr) {
                        Shape road = painter.paintRoad(roadBuilder.getRoadId(), curr);
                        road.onMouseClickedProperty().set(lastPressedShape());
                        mainPane.getChildren().add(road);
                    }
                    //todo: мы не удаляем Node на карте, если он уже существовал, мы их можем очень много друг на друга настакать
                    Shape nodeTo = painter.paintNode(roadBuilder.getToId(), roadArr[0].getTo());
                    nodeTo.onMouseClickedProperty().set(lastPressedShape());

                    Shape nodeFrom = painter.paintNode(roadBuilder.getFromId(), roadArr[0].getFrom());
                    nodeFrom.onMouseClickedProperty().set(lastPressedShape());

                    mainPane.getChildren().add(nodeTo);
                    mainPane.getChildren().add(nodeFrom);

                    stage.show();
                    currOperation = EditOperation.NONE;
                }
            }*/
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
        /*if (currOperation == EditOperation.ROAD_CREATION_STEP_1 ||
                currOperation == EditOperation.ROAD_CREATION_STEP_2) {
            currOperation = EditOperation.NONE;
            return;
        }
        numberOfLanesPane.setVisible(true);
        currOperation = EditOperation.ROAD_CREATION_STEP_1;*/
        editOperationsManager.setCurrentOperation(EditOperation.ROAD_CREATION);
        //todo: там кнопка отжиается, когда мы настраиваем число полос
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

    private void updateMapView() {
        Platform.runLater(() -> {
            mainPane.getChildren().clear();
            currMap.forEachRoads(road -> {
                Shape roadShape = objectPainter.paintRoad(road);
                //Временный код
                //TODO: переделать обработчики на полосы
                roadShape.setOnMouseClicked(event -> {
                    event.consume();
                    //Road roadRef = road;
                    System.out.println("ROAD CLICK");
                    switch (editOperationsManager.getCurrentOperation()) {
                        case ROAD_CREATION -> {
                            Point2D parentCoords = roadShape.localToParent(event.getX(), event.getY());
                            editOperationsManager.buildRoadOnRoad(parentCoords.getX(), parentCoords.getY(), road);
                            updateMapView();
                        }
                    }
                });
                mainPane.getChildren().add(roadShape);
            });
            currMap.forEachNodes(node -> {
                Shape nodeShape = objectPainter.paintNode(node);
                nodeShape.setOnMouseClicked(event -> {
                    event.consume();
                    System.out.println("NODE CLICK");
                    //Node nodeRef = node;
                    switch (editOperationsManager.getCurrentOperation()) {
                        case ROAD_CREATION -> {
                            editOperationsManager.buildRoadOnNode(node);
                            updateMapView();
                        }
                    }
                });
                mainPane.getChildren().add(nodeShape);
            });

        });
    }

/*    private EventHandler<MouseEvent> lastPressedShape() {
        return ((EventHandler<MouseEvent>) (MouseEvent x) -> {
            shapeChanged = true;
            lastPeekedShape = x.getPickResult().getIntersectedNode().getId();
            System.out.println(lastPeekedShape);
        });
    }*/
}

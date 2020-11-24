package ru.nsu.fit.traffic.controller;

import java.util.List;
import java.util.function.UnaryOperator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.model.*;
import ru.nsu.fit.traffic.model.logic.*;
import ru.nsu.fit.traffic.controller.painters.ObjectPainter;
import ru.nsu.fit.traffic.model.trafficsign.MainRoadSign;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;
import ru.nsu.fit.traffic.model.trafficsign.SignType;
import ru.nsu.fit.traffic.model.trafficsign.SpeedLimitSign;

/**
 * Контроллер основной сцены, на которой располагаются все остальные.
 */
public class MainController {

    private final int NODE_SIZE = 10;
    private final int LANE_SIZE = 10;
    private final int POINT_SIZE = 1;

    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private Pane mainPane;
    @FXML
    private AnchorPane basePane;
    @FXML
    private Pane settingsWindowsPane;
    @FXML
    private Pane numberOfLanesPane;
    @FXML
    private Pane roadSignPane;
    @FXML
    private TextField backLanesTextField;
    @FXML
    private TextField forwardLanesTextField;
    //Settings number of lanes on road menu
    @FXML
    private TextField lanesTextField;
    @FXML
    private ComboBox<Integer> speedComboBox;
    @FXML
    private Pane roadSettingsPane;
    @FXML
    private Group scrollPaneContent;
    @FXML
    private VBox centeredField;

    private Road lastRoadClicked = null;
    private final TrafficMap currMap = new TrafficMap();
    private Stage stage;
    private RoadSign currSign;
    private double scaleValue = 1;

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
        speedComboBox.setItems(FXCollections.observableArrayList(20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120));
        speedComboBox.setValue(60);

        centeredField.setOnScroll(event -> {
            event.consume();
            zoom(event);
        });

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
                    System.out.println("MAIN PANE CLICK");
                    switch (editOperationsManager.getCurrentOperation()) {
                        case ROAD_CREATION -> {
                            event.consume();
                            editOperationsManager.buildRoadOnEmpty(event.getX(), event.getY());
                            updateMapView();
                        }
                        case SIGN_CREATION -> {
                            event.consume();
                            editOperationsManager.setCurrentOperation(EditOperation.NONE);
                            roadSignPane.setVisible(false);
                        }
                        case NONE -> {
                            roadSettingsPane.setVisible(false);
                        }
                    }
                    //todo другие операции
                }
                case SECONDARY -> stopOperation();
                //todo другие функции
            }
        });

        basePane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            roadSettingsPane.setLayoutX(event.getX());
            roadSettingsPane.setLayoutY(event.getY());
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
            case ROAD_CREATION -> {
                numberOfLanesPane.setVisible(false);
                editOperationsManager.resetLastNode();
                editOperationsManager.setCurrentOperation(EditOperation.NONE);
            }
            default -> {
                numberOfLanesPane.setVisible(true);
                editOperationsManager.setCurrentOperation(EditOperation.ROAD_CREATION);
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
        switch (editOperationsManager.getCurrentOperation()) {
            case SIGN_CREATION:
                roadSignPane.setVisible(false);
                editOperationsManager.setCurrentOperation(EditOperation.NONE);
                break;
            default:
                roadSignPane.setVisible(true);
                roadSettingsPane.setVisible(false);
                numberOfLanesPane.setVisible(false);
                editOperationsManager.setCurrentOperation(EditOperation.SIGN_CREATION);
                break;
        }
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
            } else {
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


    @FXML
    public void setSpeedSign() {
        currSign = new SpeedLimitSign(speedComboBox.getValue());
    }

    @FXML
    public void setMainRoad() {
        currSign = new MainRoadSign();
    }

    /**
     * Метод отрисовки текущего состояния карты
     */
    private void updateMapView() {
        Platform.runLater(() -> {
            mainPane.getChildren().clear();
            roadSettingsPane.setVisible(false);
            if (editOperationsManager.getCurrentOperation() != EditOperation.SIGN_CREATION) {
                roadSignPane.setVisible(false);
            }

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
                                    //Point2D parentCoords = shape.localToParent(event.getX(), event.getY());
                                    switch (editOperationsManager.getCurrentOperation()) {
                                        case ROAD_CREATION -> {
                                            event.consume();
                                            editOperationsManager.buildRoadOnRoad(event.getX(), event.getY(), road);
                                            updateMapView();
                                        }
                                        case NONE -> {
                                            event.consume();
                                            lastRoadClicked = road;
                                            lanesTextField.setText(String.valueOf(road.getLanesNum()));
                                            roadSettingsPane.setVisible(true);
                                        }
                                        case SIGN_CREATION -> {
                                            RoadSign addedSign = currSign.getCopySign();
                                            lane.addSign(addedSign);
                                            if (currSign.getSignType() == SignType.MAIN_ROAD)
                                                for (int k = 0; k < road.getLanesNum(); k++) {
                                                    road.getLane(k).addSign(addedSign);
                                                }
                                            updateMapView();
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
        });
    }

    private void zoom(ScrollEvent event) {
        double zoomFactor = Math.exp(event.getDeltaY() * 0.001);

        Bounds innerBounds = scrollPaneContent.getLayoutBounds();
        Bounds viewportBounds = mainScrollPane.getViewportBounds();

        double valX = mainScrollPane.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = mainScrollPane.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        scaleValue = scaleValue * zoomFactor;
        mainPane.setScaleX(scaleValue);
        mainPane.setScaleY(scaleValue);
        mainScrollPane.layout();

        Point2D posInZoomTarget = mainPane
                .parentToLocal(scrollPaneContent
                        .parentToLocal(new Point2D(
                                event.getX(),
                                event.getY())));

        // calculate adjustment of scroll position (pixels)
        Point2D adjustment = mainPane.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        Bounds updatedInnerBounds = scrollPaneContent.getBoundsInLocal();
        mainScrollPane.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        mainScrollPane.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}

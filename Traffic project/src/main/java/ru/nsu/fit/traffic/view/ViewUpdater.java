package ru.nsu.fit.traffic.view;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.TrafficMap;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;

import java.util.List;

public class ViewUpdater {
    private final int NODE_SIZE = 10;
    private final int LANE_SIZE = 10;
    private PoiObserver poiObserver;
    private RoadObserver roadObserver;
    private NodeObserver nodeObserver;
    private final ObjectPainter objectPainter = new ObjectPainter(LANE_SIZE, NODE_SIZE);
    private Pane mainPane;


    public ViewUpdater(
            PoiObserver poiObserver,
            RoadObserver roadObserver,
            NodeObserver nodeObserver,
            Pane mainPane) {
        this.poiObserver = poiObserver;
        this.roadObserver = roadObserver;
        this.nodeObserver = nodeObserver;
        this.mainPane = mainPane;
    }

    /**
     * Метод отрисовки текущего состояния карты
     */
    public void updateMapView(EditOperationsManager editOperationsManager) {

        Platform.runLater(() -> {
            mainPane.getChildren().clear();
            TrafficMap currMap = editOperationsManager.getMap();

            currMap.forEachPlaceOfInterest(placeOfInterest -> {
                Shape placeOfInterestShape = objectPainter.paintPlaceOfInterest(placeOfInterest);
                poiObserver.action(placeOfInterest, placeOfInterestShape);
            });

            currMap.forEachRoad(road -> {
                List<List<Shape>> roadShape = objectPainter.paintRoad(
                        road, editOperationsManager.getCurrentOperation() == EditOperation.REPORT_SHOWING);
                roadObserver.action(road, roadShape);
            });
            currMap.forEachNode(node -> {
                Shape nodeShape = objectPainter.paintNode(node);
                nodeObserver.action(node, nodeShape);
            });
        });
    }
}

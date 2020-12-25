package ru.nsu.fit.traffic.view;

import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapperButton;
import ru.nsu.fit.traffic.model.map.TrafficMap;
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
                poiObserver.action(placeOfInterestShape, placeOfInterest);
                mainPane.getChildren().add(placeOfInterestShape);
            });

            currMap.forEachRoad(road -> {
                List<List<Shape>> roadShape = objectPainter.paintRoad(
                        road, editOperationsManager.getCurrentOperation() == EditOperation.REPORT_SHOWING);
                if (roadShape.size() != road.getLanesNum()) {
                    System.err.println(roadShape.size() + "!=" + road.getLanesNum());
                    throw new RuntimeException();
                }
                for (int i = 0; i < road.getLanesNum(); i++) {
                    int finalI = i;
                    roadShape.get(i).forEach(shape -> {
                        roadObserver.action(shape, road, finalI);
                        mainPane.getChildren().add(shape);
                    });
                }
            });
            currMap.forEachNode(node -> {
                Shape nodeShape = objectPainter.paintNode(node);
                if (node.getPlaceOfInterest() != null && node.getSpawners() == null) {
                    nodeShape.setFill(Paint.valueOf("#303030"));
                }
                nodeObserver.action(nodeShape, node);
                mainPane.getChildren().add(nodeShape);
            });
        });
    }
}

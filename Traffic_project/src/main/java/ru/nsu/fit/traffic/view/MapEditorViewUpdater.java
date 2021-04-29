package ru.nsu.fit.traffic.view;

import java.util.List;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.Road;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.model.playback.CarState;
import ru.nsu.fit.traffic.view.elements.observers.NodeObserver;
import ru.nsu.fit.traffic.view.elements.observers.PoiObserver;
import ru.nsu.fit.traffic.view.elements.observers.RoadObserver;

public class MapEditorViewUpdater {
  private final int NODE_SIZE = 10;
  private final int LANE_SIZE = 10;
  private final PoiObserver poiObserver;
  private final RoadObserver roadObserver;
  private final NodeObserver nodeObserver;
  private final ObjectPainter objectPainter = new ObjectPainter(LANE_SIZE, NODE_SIZE);
  private final Pane mainPane;


  public MapEditorViewUpdater(
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

      for (int i = 0; i < currMap.getPoiCount(); i++) {
        Shape placeOfInterestShape = objectPainter.paintPlaceOfInterest(currMap.getPlaceOfInterest(i));
        poiObserver.setPoiClickHandler(placeOfInterestShape, i);
        mainPane.getChildren().add(placeOfInterestShape);
      }

      for (int i = 0; i < currMap.getRoadCount(); i++) {
        Road road = currMap.getRoad(i);
        List<List<Shape>> roadShape = objectPainter.paintRoad(
            road, editOperationsManager.getCurrentOperation() == EditOperation.REPORT_SHOWING);
        if (roadShape.size() != road.getLanesNum()) {
          System.err.println(roadShape.size() + "!=" + road.getLanesNum());
          throw new RuntimeException();
        }
        for (int j = 0; j < road.getLanesNum(); j++) {
          final int finalJ = j;
          final int finalI = i;
          roadShape.get(j).forEach(shape -> {
            roadObserver.setRoadClickHandler(shape, finalI, finalJ);
            mainPane.getChildren().add(shape);
          });
        }
      }
      for (int i = 0; i < currMap.getNodesCount(); i++) {
        Node node = currMap.getNode(i);
        List<Shape> shape = objectPainter.paintNode(node);
        for (Shape nodeShape: shape) {
          if (node.getPlaceOfInterest() != null && node.getSpawners() == null) {
            nodeShape.setFill(Paint.valueOf("#303030"));
          }
          nodeObserver.action(nodeShape, i);
          mainPane.getChildren().add(nodeShape);
        }
      }

      List<CarState> carStates = editOperationsManager.getCarStates();
      carStates.forEach(carState -> {
        //if (carState.isDraw()) {
        System.out.println(currMap.getRoad(carState.getCurrentRoad()) + " " + carState.getCurrentRoad());
          Shape carShape = objectPainter.paintCar(
            carState,
            currMap.getRoad(carState.getCurrentRoad()),
            currMap.getRoad(carState.getCurrentRoad()).getLane(carState.getCurrentLane()));
          mainPane.getChildren().add(carShape);
        //}
      });
    });
  }
}

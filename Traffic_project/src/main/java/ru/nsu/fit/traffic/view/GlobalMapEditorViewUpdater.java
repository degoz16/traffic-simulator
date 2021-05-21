package ru.nsu.fit.traffic.view;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.view.elements.observers.RegionObserver;

import java.util.List;

public class GlobalMapEditorViewUpdater {

  private final GlobalMapObjectPainter painter = new GlobalMapObjectPainter();
  private final RegionObserver regionObserver;
  private final Pane mainPane;

  public GlobalMapEditorViewUpdater(RegionObserver regionObserver, Pane mainPane) {
    this.mainPane = mainPane;
    this.regionObserver = regionObserver;
  }

  /**
   * Метод отрисовки текущего состояния карты
   */
  public void updateMapView(GlobalMapEditOpManager editOperationsManager) {
    RegionsMap map = editOperationsManager.getCurrRegMap();
    mainPane.getChildren().clear();
    Platform.runLater(() -> {
      for (int i = 0; i < map.getRegionCount(); i++) {
        RectRegion region = map.getRegion(i);
        Rectangle regionShape = painter.paintRegion(region);
        regionObserver.setRegionMouseHandlers(regionShape, i, region.getWidth(), region.getHeight());
        mainPane.getChildren().add(regionShape);
      }
      map.foreachRegion(region -> {
        region.foreachConnector(roadConnector -> {
          Shape connector = painter.paintConnector(roadConnector, true);
          mainPane.getChildren().add(connector);
        });
      });
    });
  }
}
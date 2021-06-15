package ru.nsu.fit.traffic.view;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.view.elements.observers.ConnectorObserver;
import ru.nsu.fit.traffic.view.elements.observers.RegionObserver;

import java.util.List;

public class GlobalMapEditorViewUpdater {

  private final GlobalMapObjectPainter painter = new GlobalMapObjectPainter();
  private final RegionObserver regionObserver;
  private final ConnectorObserver connectorObserver;
  private final Pane mainPane;

  public GlobalMapEditorViewUpdater(RegionObserver regionObserver, ConnectorObserver connectorObserver, Pane mainPane) {
    this.mainPane = mainPane;
    this.regionObserver = regionObserver;
    this.connectorObserver = connectorObserver;
  }

  /**
   * Метод отрисовки текущего состояния карты
   */
  public void updateMapView(GlobalMapEditOpManager editOperationsManager, boolean preview) {
    RegionsMap map = editOperationsManager.getCurrRegMap();
    mainPane.getChildren().clear();
    Platform.runLater(() -> {
      for (int i = 0; i < map.getRegionCount(); i++) {
        RectRegion region = map.getRegion(i);
        Rectangle regionShape = painter.paintRegion(region);
        regionObserver.setRegionMouseHandlers(regionShape, i, region.getWidth(), region.getHeight());
        mainPane.getChildren().add(regionShape);
        if (preview) {
          try {
            TrafficMap trafficMap =
                EditOperationsManager.loadMap(
                    ConnectionConfig.getConnectionConfig()
                        .getConnection().getMapFromServer(
                        i, ConnectionConfig.getConnectionConfig().getRoomId()));
            assert trafficMap != null;
            List<Shape> shapes = painter.paintRegionPreview(region, trafficMap);
            shapes.forEach(mainPane.getChildren()::add);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      map.foreachRegion(region -> {
        region.foreachConnector(roadConnector -> {
          if (roadConnector != null) {
            Shape connector = painter.paintConnector(roadConnector, true);
            mainPane.getChildren().add(connector);
            connectorObserver.setConnectorObserver(roadConnector, connector);
          }
        });
      });
    });
  }
}

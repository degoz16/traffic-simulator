package ru.nsu.fit.traffic.view;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalMapEditorViewUpdater {

  private final GlobalMapObjectPainter painter = new GlobalMapObjectPainter();
  private final RegionObserver regionObserver;
  private final ConnectorObserver connectorObserver;
  private final Pane mainPane;

  public GlobalMapEditorViewUpdater(
      RegionObserver regionObserver, ConnectorObserver connectorObserver, Pane mainPane) {
    this.mainPane = mainPane;
    this.regionObserver = regionObserver;
    this.connectorObserver = connectorObserver;
  }

  /** Метод отрисовки текущего состояния карты */
  public void updateMapView(GlobalMapEditOpManager editOperationsManager, boolean preview) {
    RegionsMap map = editOperationsManager.getCurrRegMap();
    List<Shape> mainPaneChild = new ArrayList<>();

    Platform.runLater(
      () -> {
        List<Long> blockedMaps = null;
        if (preview) {
          blockedMaps =
              ConnectionConfig.getConnectionConfig()
                  .getConnection()
                  .blockedMaps(ConnectionConfig.getConnectionConfig().getRoomId());
          System.out.println(Arrays.toString(blockedMaps.toArray()));
        }
        for (int i = 0; i < map.getRegionCount(); i++) {
          RectRegion region = map.getRegion(i);
          Rectangle regionShape = painter.paintRegion(region);
          regionObserver.setRegionMouseHandlers(
              regionShape, i, region.getWidth(), region.getHeight());
          mainPaneChild.add(regionShape);
          if (preview) {
            if (blockedMaps != null && blockedMaps.contains((long) i)) {
                regionShape.setFill(Color.valueOf("#a08080"));
            }

            try {
              String mapPath =
                  ConnectionConfig.getConnectionConfig()
                      .getConnection()
                      .getMapFromServer(
                          i, ConnectionConfig.getConnectionConfig().getRoomId(), false);
              TrafficMap trafficMap = EditOperationsManager.loadMap(mapPath);
              assert trafficMap != null;
              List<Shape> shapes = painter.paintRegionPreview(region, trafficMap);
              mainPaneChild.addAll(shapes);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        map.foreachRegion(
          region -> {
            List<Shape> shapes = new ArrayList<>();
            for (int i = 0; i < region.getConnectorsCount(); i++) {
              if (region.getConnector(i) == null) {
                shapes.add(null);
                continue;
              }
              Shape connector = painter.paintConnector(region.getConnector(i), true);
              shapes.add(connector);
              mainPaneChild.add(connector);
              connectorObserver.setConnectorObserver(
                  map.getRegions().indexOf(region), i, connector);
            }
            if (preview) {
              try {
                String mapPath =
                  ConnectionConfig.getConnectionConfig()
                    .getConnection()
                    .getMapFromServer(
                        editOperationsManager.getCurrRegMap().getRegions().indexOf(region),
                        ConnectionConfig.getConnectionConfig().getRoomId(),
                        false);
                TrafficMap trafficMap = EditOperationsManager.loadMap(mapPath);
                assert trafficMap != null;
                trafficMap.forEachNode(
                  node -> {
                    if (node.getConnector() != null) {
                      if (node.getRoadsInNum() == 0 && node.getRoadsOutNum() == 0) {
                        shapes.get(node.getConnector().getConnectorId()).setFill(
                          new ImagePattern(
                            new Image("ru/nsu/fit/traffic/view/Images/incorrect_connector_on_map.png")));
                      }
                    }
                  });
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          });
        mainPane.getChildren().addAll(mainPaneChild);
      });
  }
}

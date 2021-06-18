package ru.nsu.fit.traffic.controller.edit;

import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.SelectorSceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOp;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.utils.Pair;

import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.*;

public class GlobalMapSelectorController implements GlobalMapSelectorControllerInterface {
  private final SelectorSceneElementsControl sceneElementsControl;
  private GlobalMapUpdateObserver updateObserver = null;
  private GlobalMapEditOpManager editOpManager = null;
  private Connection connection = null;

  public GlobalMapSelectorController(SelectorSceneElementsControl sceneElementsControl) {
    this.sceneElementsControl = sceneElementsControl;
  }

  @Override
  public void onRegionClick(int id, MouseEventWrapper event) throws Exception {
    if (editOpManager.getCurrentOp() == GlobalMapEditOp.NONE) {
      ConnectionConfig.getConnectionConfig().setMapId(id);
      sceneElementsControl.loadFragmentScene(connection.getMapFromServer(
          id, ConnectionConfig.getConnectionConfig().getRoomId(), true));
    }
    else if (editOpManager.getCurrentOp() == GlobalMapEditOp.SET_CONNECTOR){
      editOpManager.addConnector(editOpManager.getCurrRegMap().getRegion(id), event.getX(), event.getY(), true);
    }
    else if (editOpManager.getCurrentOp() == GlobalMapEditOp.KICK_USER) {
      //todo: Сюда нужно вставить вообще всё
    }
    sceneElementsControl.redrawConnectorIcon();
  }

  @Override
  public void setRegionMap(String map) {
    editOpManager.setCurrRegMap(GlobalMapEditOpManager.loadRegMap(map));
    updateObserver.update(editOpManager, true);
    sceneElementsControl.redrawConnectorIcon();
  }

  @Override
  public double getMapWidth() {
    return editOpManager.getCurrRegMap().getWidth();
  }

  @Override
  public double getMapHeight() {
    return editOpManager.getCurrRegMap().getHeight();
  }

  @Override
  public void onConnectorClicked(int regId, int conId) throws Exception {
    if (editOpManager.getCurrentOp() == DELETE_CONNECTOR){
      RoadConnector con1 = editOpManager.getCurrRegMap().getRegion(regId).getConnector(conId);
      RoadConnector con2 = con1.getConnectorLink();
      con1.getRegion().deleteConnector(con1);
      con2.getRegion().deleteConnector(con2);
      int regId2 = editOpManager.getCurrRegMap().getRegions().indexOf(con2.getRegion());
      TrafficMap map1 = EditOperationsManager.loadMap(
          ConnectionConfig.getConnectionConfig()
              .getConnection().getMapFromServer(
                  regId, ConnectionConfig.getConnectionConfig().getRoomId(), true));
      TrafficMap map2 = EditOperationsManager.loadMap(
          ConnectionConfig.getConnectionConfig()
              .getConnection().getMapFromServer(
              regId2, ConnectionConfig.getConnectionConfig().getRoomId(), true));
      assert map1 != null;
      for (Node node : map1.getNodes()) {
        if (node.getConnector() != null) {
          if (node.getConnector().getConnectorId() == con1.getId()) {
            node.setConnector(null);
            break;
          }
        }
      }
      assert map2 != null;
      for (Node node : map2.getNodes()) {
        if (node.getConnector() != null) {
          if (node.getConnector().getConnectorId() == con2.getId()) {
            node.setConnector(null);
            break;
          }
        }
      }
      EditOperationsManager.saveMap("tmpMap.tsp", map1);
      ConnectionConfig.getConnectionConfig()
          .getConnection().pushMap(regId, ConnectionConfig.getConnectionConfig().getRoomId(), "tmpMap.tsp");
      EditOperationsManager.saveMap("tmpMap.tsp", map2);
      ConnectionConfig.getConnectionConfig()
          .getConnection().pushMap(regId2, ConnectionConfig.getConnectionConfig().getRoomId(), "tmpMap.tsp");
      GlobalMapEditOpManager.saveRegMap("tmpRegMap.tsp", editOpManager.getCurrRegMap());
      ConnectionConfig.getConnectionConfig().getConnection().pushGlobalMap(
          "tmpRegMap.tsp", ConnectionConfig.getConnectionConfig().getRoomId());
      updateObserver.update(editOpManager, true);
      sceneElementsControl.redrawConnectorIcon();
    }
  }

  @Override
  public void onSetConnector() {
    sceneElementsControl.redrawConnectorIcon();
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.SET_CONNECTOR) {
      editOpManager.setCurrOp(GlobalMapEditOp.SET_CONNECTOR);
      sceneElementsControl.setCurrentOperation("set connector");
      sceneElementsControl.setConnectorIconVisible(true);
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
      sceneElementsControl.setCurrentOperation("none");
      stopOperation();
    }
  }

  @Override
  public void onDeleteConnector() {
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.DELETE_CONNECTOR) {
      editOpManager.setCurrOp(GlobalMapEditOp.DELETE_CONNECTOR);
      sceneElementsControl.setCurrentOperation("delete connector");
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
      sceneElementsControl.setCurrentOperation("none");
    }
    sceneElementsControl.setConnectorIconVisible(false);
  }

  @Override
  public void onKick() {
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.KICK_USER) {
      editOpManager.setCurrOp(GlobalMapEditOp.KICK_USER);
      sceneElementsControl.setCurrentOperation("kick user");
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
      sceneElementsControl.setCurrentOperation("none");
    }
  }

  @Override
  public boolean testRegionsBounds(double x, double y, int id) {
    Pair<Double, Double> c = editOpManager.getSideCoordinates(x, y, editOpManager.getCurrRegMap().getRegion(id));
    return editOpManager.getCurrRegMap()
        .getRegionsInThePoint(c.getFirst(), c.getSecond(), false) != null
        || editOpManager.getCurrRegMap()
        .getRegionsInThePoint(c.getFirst(), c.getSecond(), true) != null;
  }

  @Override
  public Pair<Double, Double> getSideCoordinates(
      double x, double y, double regX, double regY, double regW, double regH) {
    return editOpManager.getSideCoordinates(x, y, regX, regY, regW, regH);
  }

  @Override
  public String onMergeMap() {
    return editOpManager.mergeGlobalMap();
  }

  public void setEditOpManager(GlobalMapEditOpManager editOpManager) {
    this.editOpManager = editOpManager;
  }

  public void setUpdateObserver(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  private void stopOperation() {
    editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    sceneElementsControl.setCurrentOperation("none");
    sceneElementsControl.setConnectorIconVisible(false);
  }

  @Override
  public void update() {
    updateObserver.update(editOpManager, true);
  }
}

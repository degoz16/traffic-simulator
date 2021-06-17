package ru.nsu.fit.traffic.controller.edit;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.SelectorSceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.javafx.controller.edit.MainController;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOp;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.utils.Pair;

import java.io.IOException;
import java.util.List;

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
      editOpManager.addConnector(editOpManager.getCurrRegMap().getRegion(id), event.getX(), event.getY());
    }
    else if (editOpManager.getCurrentOp() == GlobalMapEditOp.KICK_USER) {
      //todo: Сюда нужно вставить вообще всё
    }
  }

  @Override
  public void setRegionMap(String map) {
    editOpManager.setCurrRegMap(GlobalMapEditOpManager.loadRegMap(map));
    updateObserver.update(editOpManager, true);
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
  public void onConnectorClicked(RoadConnector connector){
    if (editOpManager.getCurrentOp() == DELETE_CONNECTOR){
      List<RectRegion> regions = editOpManager.getCurrRegMap().getRegions();
      RectRegion currRegion = regions.get(regions.indexOf(connector.getRegion()));
      currRegion.deleteConnector(connector);
      connector.deleteLink();
      updateObserver.update(editOpManager, false);
    }
  }

  @Override
  public void onSetConnector() {
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.SET_CONNECTOR) {
      editOpManager.setCurrOp(GlobalMapEditOp.SET_CONNECTOR);
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    }
  }

  @Override
  public void onDeleteConnector() {
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.DELETE_CONNECTOR) {
      editOpManager.setCurrOp(GlobalMapEditOp.DELETE_CONNECTOR);
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    }
  }

  @Override
  public void onKick() {
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.KICK_USER) {
      editOpManager.setCurrOp(GlobalMapEditOp.KICK_USER);
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
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


}

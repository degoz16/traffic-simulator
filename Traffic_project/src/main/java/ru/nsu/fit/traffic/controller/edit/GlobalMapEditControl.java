package ru.nsu.fit.traffic.controller.edit;

import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOp;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.model.map.Connector;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.utils.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.*;

public class GlobalMapEditControl implements GlobalMapEditControlInterface {
  public static final double MAP_SCALE = 6;
  private GlobalMapEditOpManager editOpManager = null;
  private final GlobalMapSceneElementsControl sceneElementsControl;
  private GlobalMapUpdateObserver updateObserver = null;
  private boolean restrictRegionSet = false;
  private double lastClickX = 0;
  private double lastClickY = 0;
  private double currX = 0;
  private double currY = 0;

  public GlobalMapEditControl(GlobalMapSceneElementsControl sceneElementsControl) {
    this.sceneElementsControl = sceneElementsControl;
  }

  public RegionsMap getCurrRegionsMap(){
    return editOpManager.getCurrRegMap();
  }

  @Override
  public double getCurrX() {
    return currX;
  }

  @Override
  public double getCurrY() {
    return currY;
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

  public void setEditOpManager(GlobalMapEditOpManager editOpManager) {
    this.editOpManager = editOpManager;
  }

  public void setUpdateObserver(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }

  @Override
  public void onMainPaneReleased(MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        switch (editOpManager.getCurrentOp()) {
          case SET_REGION -> {
            if (!restrictRegionSet) {
              editOpManager.addReg("Region", lastClickX, lastClickY, currX, currY);
            } else {
              restrictRegionSet = false;
            }
          }
        }
      }
    }
  }

  @Override
  public void onMainPanePressed(MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        lastClickX = event.getX();
        lastClickY = event.getY();
        currX = lastClickX;
        currY = lastClickY;
      }
    }
  }

  @Override
  public void onMainPaneClicked(MouseEventWrapper event) {
    switch (event.getButton()) {
      case SECONDARY -> {
        stopOperation();
      }
    }
  }

  @Override
  public void onMainPaneDrag(MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        double x = event.getX();
        double yMin = Math.min(event.getY(), lastClickY);
        double yMax = Math.max(event.getY(), lastClickY);
        double yMinC = Math.min(currY, lastClickY);
        double yMaxC = Math.max(currY, lastClickY);
        double y = event.getY();
        double xMin = Math.min(event.getX(), lastClickX);
        double xMax = Math.max(event.getX(), lastClickX);
        double xMinC = Math.min(currX, lastClickX);
        double xMaxC = Math.max(currX, lastClickX);
        Set<RectRegion> regions = new HashSet<>();
        final Set<RectRegion> finalRegions = regions;
        getCurrRegionsMap().foreachRegion(region -> {
          boolean cond1 = Math.min(yMax, region.getY() + region.getHeight()) - Math.max(yMin, region.getY()) > 0
              || (yMin < region.getY() && yMax > region.getY() + region.getHeight());
          boolean cond2 = Math.min(xMaxC, region.getX() + region.getWidth()) - Math.max(xMinC, region.getX()) > 0
              || (xMinC < region.getX() && xMaxC > region.getX() + region.getWidth());
          if (cond1 && cond2) {
            finalRegions.add(region);
          }
        });
        if (regions.size() == 0) {
          currY = y;
        }

        regions.clear();
        final Set<RectRegion> finalRegions1 = regions;
        getCurrRegionsMap().foreachRegion(region -> {
          boolean cond1 = Math.min(yMaxC, region.getY() + region.getHeight()) - Math.max(yMinC, region.getY()) > 0
              || (yMinC < region.getY() && yMaxC > region.getY() + region.getHeight());
          boolean cond2 = Math.min(xMax, region.getX() + region.getWidth()) - Math.max(xMin, region.getX()) > 0
              || (xMin < region.getX() && xMax > region.getX() + region.getWidth());
          if (cond1 && cond2) {
            finalRegions1.add(region);
          }
        });
        if (regions.size() == 0) {
          currX = x;
        }
      }
    }
  }

  @Override
  public void onRegionClick(int id, MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        event.consume();
        switch (editOpManager.getCurrentOp()) {
          case SET_CONNECTOR -> {
            RectRegion region = editOpManager.getCurrRegMap().getRegion(id);
            editOpManager.addConnector(region, event.getX(), event.getY());
          }
          case DELETE_REGION -> {
            getCurrRegionsMap().deleteRegion(id);
            editOpManager.setCurrOp(NONE);
            updateObserver.update(editOpManager, false);
            sceneElementsControl.setCurrentOperation("none");
          }
        }
      }
    }
  }

  @Override
  public void onRegionPressed(MouseEventWrapper event) {
    event.consume();
    restrictRegionSet = true;
  }

  @Override
  public void onRegionMouseMove(int id, MouseEventWrapper event) {

  }

  @Override
  public void onSetRegionButton() {
    stopOperation();
    editOpManager.setCurrOp(SET_REGION);
    sceneElementsControl.setCurrentOperation("set region");
    sceneElementsControl.setSelectRectVisible(true);
  }

  @Override
  public void onSetConnectorButton() {
    if (editOpManager.getCurrentOp() != SET_CONNECTOR){
      stopOperation();
      editOpManager.setCurrOp(SET_CONNECTOR);
      sceneElementsControl.setCurrentOperation("set connector");
      sceneElementsControl.setConnectorIconVisible(true);
    }
    else{
      stopOperation();
    }
  }

  @Override
  public void onClear() {
    editOpManager.clearMap();
  }

  @Override
  public void onPut() {
    editOpManager.saveRegMap("/save.json");
  }

  @Override
  public Integer onNewPut() {
    RegionsMap map = editOpManager.getCurrRegMap();
    Connection connection = ConnectionConfig.getConnectionConfig().getConnection();
    editOpManager.saveRegMap("/global_map.tsp");
    Integer roomId = connection.createRoom("/global_map.tsp", map.getName());
    for (int i = 0; i < map.getRegionCount(); i++) {
      TrafficMap m = new TrafficMap(i, map.getRegion(i), MAP_SCALE);
      EditOperationsManager.saveMap("/map_" + i + ".tsp", m);
      connection.pushMap(i, roomId, "/map_" + i + ".tsp");
    }
    return roomId;
  }

  @Override
  public void onNewGet() {
    editOpManager.setCurrRegMap(GlobalMapEditOpManager.loadRegMap("/save.json"));
    updateObserver.update(editOpManager, false);
  }

  @Override
  public void onGet() {
    editOpManager.setCurrRegMap(GlobalMapEditOpManager.loadRegMap("/save.json"));
    updateObserver.update(editOpManager, false);
  }

  @Override
  public void deleteRegion() {
    if (editOpManager.getCurrentOp() != DELETE_REGION){
      editOpManager.setCurrOp(DELETE_REGION);
      sceneElementsControl.setCurrentOperation("delete region");
    } else {
      stopOperation();
    }
  }

  @Override
  public void deleteConnector(){
    if (editOpManager.getCurrentOp() != DELETE_CONNECTOR){
      stopOperation();
      editOpManager.setCurrOp(DELETE_CONNECTOR);
      sceneElementsControl.setCurrentOperation("delete connector");
    } else {
      stopOperation();
    }
  }

  @Override
  public void updateMap(String filepath) {
    editOpManager.setCurrRegMap(GlobalMapEditOpManager.loadRegMap(filepath));
    updateObserver.update(editOpManager, false);
  }

  private void stopOperation() {
    editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    sceneElementsControl.setCurrentOperation("none");
    sceneElementsControl.setConnectorIconVisible(false);
    sceneElementsControl.setSelectRectVisible(false);
  }

  @Override
  public void onConnectorClicked(RoadConnector connector){
    if (editOpManager.getCurrentOp() == DELETE_CONNECTOR){
      List<RectRegion> regions = getCurrRegionsMap().getRegions();
      RectRegion currRegion = regions.get(regions.indexOf(connector.getRegion()));
      currRegion.deleteConnector(connector);
      connector.deleteLink();
      updateObserver.update(editOpManager, false);
    }
  }
}

package ru.nsu.fit.traffic.controller.edit;

import javafx.scene.shape.Rectangle;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.javafx.controller.edit.GlobalMapController;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOp;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.utils.Pair;

import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.SET_CONNECTOR;
import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.SET_REGION;

public class GlobalMapEditControl implements GlobalMapEditControlInterface {
  private GlobalMapEditOpManager editOpManager = null;
  private GlobalMapController globalMapController;
  private final GlobalMapSceneElementsControl sceneElementsControl;
  private GlobalMapUpdateObserver updateObserver = null;
  private double lastClickX = 0;
  private double lastClickY = 0;


  public GlobalMapEditControl(GlobalMapSceneElementsControl sceneElementsControl) {
    this.sceneElementsControl = sceneElementsControl;
  }

  public void setGlobalMapController(GlobalMapController globalMapController) {
    this.globalMapController = globalMapController;
  }

  public RegionsMap getCurrRegionsMap(){
    return editOpManager.getCurrRegMap();
  }

  @Override
  public Pair<Double, Double> getSideCoordinates(
      double x, double y, double regX, double regY, double regW, double regH) {
    boolean d1 = y > (regH / regW) * (x - regX) + regY;
    boolean d2 = y > -(regH / regW) * (x - regX - regW) + regY;
    if (d1 && d2) {
      return new Pair<>(x, regY + regH);
    }
    else if(d1) {
      return new Pair<>(regX, y);
    }
    else if (d2) {
      return new Pair<>(regX + regW, y);
    }
    else {
      return new Pair<>(x, regY);
    }
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
            editOpManager.addReg("Region",globalMapController.getSelectRect());
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

  }

  @Override
  public void onRegionClick(int id, MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        event.consume();
        switch (editOpManager.getCurrentOp()) {
          case SET_CONNECTOR -> {
            RectRegion region = editOpManager.getCurrRegMap().getRegion(id);
            Pair<Double, Double> coords = getSideCoordinates(event.getX(), event.getY(),
                region.getX(), region.getY(), region.getWidth(), region.getHeight());
            editOpManager.addConnector(getCurrRegionsMap().getRegionsInThePoint(coords.getFirst(), coords.getSecond()),
                    coords.getFirst(), coords.getSecond());
            //System.out.println(getCurrRegionsMap().getRegionsInThePoint(coords.getFirst(), coords.getSecond()));
            //System.out.println(getCurrRegionsMap().getRegionsInThePoint(event.getX(), event.getY()));
          }
        }
      }
    }
  }

  @Override
  public void onRegionMouseMove(int id, MouseEventWrapper event) {

  }

  @Override
  public void onSetRegionButton() {
    stopOperation();
    editOpManager.setCurrOp(SET_REGION);
    sceneElementsControl.setSelectRectVisible(true);
  }

  @Override
  public void onSetConnectorButton() {
    stopOperation();
    editOpManager.setCurrOp(SET_CONNECTOR);
    sceneElementsControl.setConnectorIconVisible(true);
  }

  private void stopOperation() {
    editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    sceneElementsControl.setConnectorIconVisible(false);
    sceneElementsControl.setSelectRectVisible(false);
  }
}

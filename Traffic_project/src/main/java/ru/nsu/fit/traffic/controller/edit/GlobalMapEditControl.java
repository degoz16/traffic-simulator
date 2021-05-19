package ru.nsu.fit.traffic.controller.edit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.json.parse.RegionMapJson;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOp;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.utils.Pair;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.*;

public class GlobalMapEditControl implements GlobalMapEditControlInterface {
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
    Pair<Double, Double> c = getSideCoordinates(x, y, editOpManager.getCurrRegMap().getRegion(id));
    return editOpManager.getCurrRegMap()
        .getRegionsInThePoint(c.getFirst(), c.getSecond(), false) != null
        || editOpManager.getCurrRegMap()
        .getRegionsInThePoint(c.getFirst(), c.getSecond(), true) != null;
  }

  private Pair<Double, Double> getSideCoordinates(double x, double y, RectRegion region) {
    return getSideCoordinates(x, y, region.getX(), region.getY(), region.getWidth(), region.getHeight());
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
            Pair<Double, Double> coords = getSideCoordinates(event.getX(), event.getY(), region);
            boolean vert = coords.getSecond() - region.getY() < 0.001
                || coords.getSecond() - region.getY() + region.getHeight() < 0.001;
            Pair<RectRegion, RectRegion> regions =
                editOpManager.getCurrRegMap()
                    .getRegionsInThePoint(coords.getFirst(), coords.getSecond(), vert);
            if(regions != null) {
              editOpManager.addConnector(
                  regions.getFirst(), regions.getSecond(),
                  coords.getFirst(), coords.getSecond());
            }
          }
          case DELETE_REGION -> {
            getCurrRegionsMap().deleteRegion(id);
            editOpManager.setCurrOp(NONE);
            updateObserver.update(editOpManager);
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
    sceneElementsControl.setSelectRectVisible(true);
  }

  @Override
  public void onSetConnectorButton() {
    stopOperation();
    editOpManager.setCurrOp(SET_CONNECTOR);
    sceneElementsControl.setConnectorIconVisible(true);
  }

  @Override
  public void onClear() {
    editOpManager.clearMap();
  }

  @Override
  public void onPut() {
    try {
      Writer writer = new FileWriter("/save.json");
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonMap = gson.toJson(new RegionMapJson(editOpManager.getCurrRegMap()));
      writer.write(jsonMap);
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void onGet() {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Reader fileReader = new FileReader("/save.json");
      RegionMapJson map = gson.fromJson(fileReader, RegionMapJson.class);
      editOpManager.setCurrRegMap(map.getMap());
      updateObserver.update(editOpManager);
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void deleteRegion() {
    editOpManager.setCurrOp(DELETE_REGION);
  }


  private void stopOperation() {
    editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    sceneElementsControl.setConnectorIconVisible(false);
    sceneElementsControl.setSelectRectVisible(false);
  }
}

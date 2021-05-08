package ru.nsu.fit.traffic.model.logic;

import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;

import java.util.List;

public class GlobalMapEditOpManager {
  private final RegionsMap currRegMap = new RegionsMap();
  private GlobalMapEditOp currOp = GlobalMapEditOp.NONE;
  private final GlobalMapUpdateObserver updateObserver;

  public GlobalMapEditOpManager(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }

  public void setCurrOp(GlobalMapEditOp currOp) {
    this.currOp = currOp;
  }

  public RegionsMap getCurrRegMap() {
    return currRegMap;
  }

  public GlobalMapEditOp getCurrentOp() {
    return currOp;
  }

  // TODO: push to server, not local edit
  public void addReg(String name, double x1, double y1, double x2, double y2) {
    double xMin = Math.min(x1, x2);
    double xMax = Math.max(x1, x2);
    double yMin = Math.min(y1, y2);
    double yMax = Math.max(y1, y2);
    RectRegion region = new RectRegion(name, xMin, yMin, xMax - xMin, yMax - yMin);
    currRegMap.addRegion(region);
    updateObserver.update(this);
  }

  public void addConnector(List<RectRegion> regions, double x, double y) {
    for (RectRegion region : regions) {
      region.addConnector(new RoadConnector(regions, x, y));
    }
    updateObserver.update(this);
  }
}

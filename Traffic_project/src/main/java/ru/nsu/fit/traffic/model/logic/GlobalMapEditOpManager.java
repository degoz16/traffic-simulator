package ru.nsu.fit.traffic.model.logic;

import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;

public class GlobalMapEditOpManager {
  private RegionsMap currRegMap = new RegionsMap();
  private GlobalMapEditOp currOp = GlobalMapEditOp.NONE;
  private final GlobalMapUpdateObserver updateObserver;

  public GlobalMapEditOpManager(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }

  public void clearMap() {
    currRegMap = new RegionsMap();
    updateObserver.update(this);
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

  public void addConnector(RectRegion region1, RectRegion region2, double x, double y) {
    RoadConnector connector1 = new RoadConnector(x - region1.getX(), y - region1.getY(), region1);
    RoadConnector connector2 = new RoadConnector(x - region2.getX(), y - region2.getY(), region2);
    connector1.setConnectorLink(connector2);
    connector2.setConnectorLink(connector1);
    region1.addConnector(connector1);
    region2.addConnector(connector2);

    updateObserver.update(this);
  }
}

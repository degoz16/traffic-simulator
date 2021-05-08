package ru.nsu.fit.traffic.model.logic;

import javafx.scene.shape.Rectangle;
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
  public void addReg(String name, Rectangle reg) {
    RectRegion region = new RectRegion(name, reg.getX() + reg.getTranslateX(),
            reg.getY() + reg.getTranslateY(), reg.getWidth(), reg.getHeight());
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

package ru.nsu.fit.traffic.model.globalmap;

import ru.nsu.fit.traffic.utils.Pair;

import java.util.*;
import java.util.function.Consumer;

public class RegionsMap {
  //private Map<String, RectRegion> regions = new HashMap<>();
  private final List<RectRegion> regions = new ArrayList<>();
  private int currConnectorsCnt = 0;

  public int getCurrConnectorsCnt() {
    return currConnectorsCnt;
  }

  public void setCurrConnectorsCnt(int currConnectorsCnt) {
    this.currConnectorsCnt = currConnectorsCnt;
  }

  public int getNextConnectorId() {
    currConnectorsCnt++;
    return currConnectorsCnt - 1;
  }

  public void addRegion(RectRegion region) {
    //regions.put(region.getName(), region);
    regions.add(region);
  }

  public void foreachRegion(Consumer<RectRegion> consumer) {
    regions.forEach(consumer);
  }

  public RectRegion getRegion(int id) {
    return regions.get(id);
  }

  public int getRegionCount() {
    return regions.size();
  }

  public void deleteRegion(int id){
    RectRegion region = regions.get(id);
    for (RoadConnector connector: region.getConnectorList()){
        connector.deleteLink();
    }
    regions.remove(region);
  }

  public Pair<RectRegion, RectRegion> getRegionsInThePoint(double x, double y, boolean vertical){
    RectRegion region1 = null;
    RectRegion region2 = null;
    double x1 = x;
    double y1 = y;
    double x2 = x;
    double y2 = y;
    if (vertical) {
      y1 -= 5;
      y2 += 5;
    } else {
      x1 -= 5;
      x2 += 5;
    }
    for (RectRegion region : regions) {
      if (region != region1
          && x1 > region.getX()
          && x1 < region.getX() + region.getWidth()
          && y1 > region.getY()
          && y1 < region.getY() + region.getHeight()) {
        region1 = region;
      }
    }
    for (RectRegion region : regions) {
      if (region != region2
          && x2 > region.getX()
          && x2 < region.getX() + region.getWidth()
          && y2 > region.getY()
          && y2 < region.getY() + region.getHeight()) {
        region2 = region;
      }
    }
    if (region1 == null || region2 == null) {
      return null;
    }
    return new Pair<>(region1, region2);
  }
}

package ru.nsu.fit.traffic.model.globalmap;

import ru.nsu.fit.traffic.model.map.Node;

import java.util.List;

public class RoadConnector {
  private RoadConnector link = null;
  private List<RectRegion> rectRegions;
  private double x;
  private double y;

  public RoadConnector(List<RectRegion> rectRegion, double x, double y) {
    this.rectRegions = rectRegion;
    this.x = x;
    this.y = y;
  }

  public RoadConnector getLink() {
    return link;
  }

  public void setLink(RoadConnector link) {
    this.link = link;
  }

  public List<RectRegion> getRectRegions() {
    return rectRegions;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }
}

package ru.nsu.fit.traffic.model.globalmap;

import ru.nsu.fit.traffic.model.map.Node;

public class RoadConnector {
  private RoadConnector link = null;
  private RectRegion rectRegion;
  private double x;
  private double y;

  public RoadConnector(RectRegion rectRegion, double x, double y) {
    this.rectRegion = rectRegion;
    this.x = x;
    this.y = y;
  }

  public RoadConnector getLink() {
    return link;
  }

  public void setLink(RoadConnector link) {
    this.link = link;
  }

  public RectRegion getRectRegion() {
    return rectRegion;
  }

  public void setRectRegion(RectRegion rectRegion) {
    this.rectRegion = rectRegion;
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

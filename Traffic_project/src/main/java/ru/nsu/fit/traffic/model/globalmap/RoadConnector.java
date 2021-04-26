package ru.nsu.fit.traffic.model.globalmap;

import ru.nsu.fit.traffic.model.map.Node;

public class RoadConnector {
  private RoadConnector link = null;
  private RectRegion rectRegion;
  private Node node;
  private double x;
  private double y;

  public RoadConnector(RectRegion rectRegion, Node node, double x, double y) {
    this.rectRegion = rectRegion;
    this.node = node;
    this.x = x;
    this.y = y;
  }
}

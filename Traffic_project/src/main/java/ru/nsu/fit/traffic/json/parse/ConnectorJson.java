package ru.nsu.fit.traffic.json.parse;

import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;

public class ConnectorJson {
  private int connectorLink = -1;
  private int region;
  private double x;
  private double y;

  public ConnectorJson(double x, double y) {
    this.x = x;
    this.y = y;
  }
}

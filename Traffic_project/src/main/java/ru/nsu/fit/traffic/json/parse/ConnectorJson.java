package ru.nsu.fit.traffic.json.parse;

public class ConnectorJson {
  private int connectorLink = -1;
  private int region;
  private double x;
  private double y;

  public ConnectorJson(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public int getConnectorLink() {
    return connectorLink;
  }

  public void setConnectorLink(int connectorLink) {
    this.connectorLink = connectorLink;
  }

  public int getRegion() {
    return region;
  }

  public void setRegion(int region) {
    this.region = region;
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

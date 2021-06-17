package ru.nsu.fit.traffic.model.globalmap;

public class RoadConnector {
  private RoadConnector connectorLink = null;
  private RectRegion region;
  private double x;
  private double y;
  private int id;

  public RoadConnector(int id, double x, double y, RectRegion region) {
    this.x = x;
    this.y = y;
    this.region = region;
    this.id = id;
  }

  public RoadConnector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void deleteLink(){
    getConnectorLink().getRegion().deleteConnector(getConnectorLink());
  }

  public void setConnectorLink(RoadConnector roadConnector) {
    this.connectorLink = roadConnector;
  }

  public RoadConnector getConnectorLink() {
    return connectorLink;
  }

  public RectRegion getRegion() {
    return region;
  }

  public void setRegion(RectRegion region) {
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

  public double getGlobalX() {
    return x + region.getX();
  }

  public double getGlobalY() {
    return y + region.getY();
  }
}

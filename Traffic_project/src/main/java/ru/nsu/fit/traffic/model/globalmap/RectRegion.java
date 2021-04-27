package ru.nsu.fit.traffic.model.globalmap;

import java.util.ArrayList;
import java.util.List;

public class RectRegion {

  //TODO: show notification on width/height less then min size
  private final int MIN_REGION_SIZE = 40;
  private String name;
  private double x;
  private double y;
  private double width;
  private double height;
  private List<RoadConnector> connectorList = new ArrayList<>();

  public RectRegion(String name, double x, double y, double width, double height) {
    this.name = name;
    this.x = x;
    this.y = y;
    if (width > MIN_REGION_SIZE) {
      this.width = width;
    }
    if (height > MIN_REGION_SIZE) {
      this.height = height;
    }
  }

  public RectRegion(double x, double y, double width, double height) {
    this.name = "";
    this.x = x;
    this.y = y;
    if (width > MIN_REGION_SIZE) {
      this.width = width;
    }
    if (height > MIN_REGION_SIZE) {
      this.height = height;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public List<RoadConnector> getConnectorList() {
    return connectorList;
  }

  public void setConnectorList(List<RoadConnector> connectorList) {
    this.connectorList = connectorList;
  }
}

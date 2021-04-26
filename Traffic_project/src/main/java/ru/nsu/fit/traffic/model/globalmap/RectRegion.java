package ru.nsu.fit.traffic.model.globalmap;

import java.util.ArrayList;
import java.util.List;

public class RectRegion {
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
    this.width = width;
    this.height = height;
  }

  public RectRegion(double x, double y, double width, double height) {
    this.name = "";
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public String getName() {
    return name;
  }
}

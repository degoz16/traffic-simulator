package ru.nsu.fit.traffic.json.parse;

import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;

import java.util.ArrayList;
import java.util.List;

public class RegionJson {
  private String name;
  private double x;
  private double y;
  private double width;
  private double height;
  private String regionMapLink = null;
  private List<Integer> connectorList = new ArrayList<>();

  public RegionJson(RectRegion region) {
    name = region.getName();
    x = region.getX();
    y = region.getY();
    width = region.getWidth();
    height = region.getHeight();
    regionMapLink = region.getRegionMapLink();
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

  public String getRegionMapLink() {
    return regionMapLink;
  }

  public void setRegionMapLink(String regionMapLink) {
    this.regionMapLink = regionMapLink;
  }

  public List<Integer> getConnectorList() {
    return connectorList;
  }

  public void setConnectorList(List<Integer> connectorList) {
    this.connectorList = connectorList;
  }
}

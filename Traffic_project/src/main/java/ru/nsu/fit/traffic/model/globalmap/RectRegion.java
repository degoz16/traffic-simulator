package ru.nsu.fit.traffic.model.globalmap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RectRegion {

  private String name;
  private double x;
  private double y;
  private double width;
  private double height;
  private String regionMapLink = null;
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

  void deleteConnector(RoadConnector connector){
    connectorList.remove(connector);
  }

  public RoadConnector getConnector(int id) {
    return connectorList.get(id);
  }

  public void addConnector(RoadConnector connector) {
    connectorList.add(connector);
  }

  public void foreachConnector(Consumer<RoadConnector> consumer) {
    connectorList.forEach(consumer);
  }

  public int getConnectorsCount() {
    return connectorList.size();
  }

  public String getRegionMapLink() {
    return regionMapLink;
  }

  public void setRegionMapLink(String regionMapLink) {
    this.regionMapLink = regionMapLink;
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

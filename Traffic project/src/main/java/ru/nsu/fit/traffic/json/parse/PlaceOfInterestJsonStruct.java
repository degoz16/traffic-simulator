package ru.nsu.fit.traffic.json.parse;

import java.util.List;

public class PlaceOfInterestJsonStruct {
  private double x;
  private double y;
  private double width;
  private double height;
  private int capacity;
  private double weight;
  private List<Integer> nodes;

  public PlaceOfInterestJsonStruct(
      double x,
      double y,
      double width,
      double height,
      int capacity,
      double weight,
      List<Integer> nodes) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.capacity = capacity;
    this.weight = weight;
    this.nodes = nodes;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public int getCapacity() {
    return capacity;
  }

  public double getWeight() {
    return weight;
  }

  public List<Integer> getNodes() {
    return nodes;
  }
}

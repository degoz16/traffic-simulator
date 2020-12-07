package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlaceOfInterest {
  private int numberOfParkingPlaces;
  private final List<Node> nodes = new ArrayList<>();
  private final double width;
  private final double length;
  private final double x;
  private final double y;
  private double weight = 1;

  public PlaceOfInterest(double width, double length, double x, double y) {
    this.width = width;
    this.length = length;
    this.x = x;
    this.y = y;
  }

  public double getWidth() {
    return width;
  }

  public double getLength() {
    return length;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public int getNumberOfParkingPlaces() {
    return numberOfParkingPlaces;
  }

  public void setNumberOfParkingPlaces(int numberOfParkingPlaces) {
    this.numberOfParkingPlaces = numberOfParkingPlaces;
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  public void removeNode(Node node) {
    nodes.remove(node);
  }

}

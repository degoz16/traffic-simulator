package ru.nsu.fit.traffic.model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlaceOfInterest {
  private final List<Node> nodes = new ArrayList<>();
  private final double width;
  private final double height;
  private double x;
  private double y;
  private int numberOfParkingPlaces;
  private double weight = 1;

  public PlaceOfInterest(double width, double height, double x, double y) {
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }

  public PlaceOfInterest(
      double x, double y, double width, double height, int numberOfParkingPlaces, double weight) {
    this.numberOfParkingPlaces = numberOfParkingPlaces;
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
    this.weight = weight;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
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

  public void foreachNodeIn(Consumer<Node> f) {
    nodes.forEach(f);
  }
}

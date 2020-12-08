package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TrafficMap {
  private List<Node> nodes;
  private List<Road> roads;
  private List<PlaceOfInterest> placesOfInterest;

  public TrafficMap() {
    roads = new ArrayList<>();
    nodes = new ArrayList<>();
    placesOfInterest = new ArrayList<>();
  }

  public void clearMap() {
    nodes.clear();
    roads.clear();
    placesOfInterest.clear(); //TODO Каждую сущность, которую добавили в мап - добавьте в clear
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  public void addRoad(Road road) {
    roads.add(road);
  }

  public void removeNode(Node node) {
    nodes.remove(node);
  }

  public void removeRoad(Road road) {
    roads.remove(road);
  }

  public void forEachPlaceOfInterest(Consumer<PlaceOfInterest> f) {
    placesOfInterest.forEach(f);
  }

  public void forEachNode(Consumer<Node> f) {
    nodes.forEach(f);
  }

  public void forEachRoad(Consumer<Road> f) {
    roads.forEach(f);
  }

  public int indexOfNode(Node node) {
    return nodes.indexOf(node);
  }

  public int indexOfRoad(Road road) {
    return roads.indexOf(road);
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public void setRoads(List<Road> roads) {
    this.roads = roads;
  }

  public void setPlacesOfInterest(List<PlaceOfInterest> placesOfInterest) {
    this.placesOfInterest = placesOfInterest;
  }

  public void addPlaceOfInterest(PlaceOfInterest placeOfInterest) {
    placesOfInterest.add(placeOfInterest);
  }
}

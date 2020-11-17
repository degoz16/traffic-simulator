package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TrafficMap {
  private List<Node> nodes = new ArrayList<>();
  private List<Road> roads = new ArrayList<>();

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

  public void forEachNodes(Consumer<Node> f) {
    nodes.forEach(f);
  }

  public void forEachRoads(Consumer<Road> f) {
    roads.forEach(f);
  }

  public int indexOfNode(Node node) {
    return nodes.indexOf(node);
  }

  public int indexOfRoad(Road road) {
    return roads.indexOf(road);
  }
}

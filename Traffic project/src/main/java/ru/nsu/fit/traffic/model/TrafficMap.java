package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TrafficMap {
  private List<Node> nodes;
  private List<Road> roads;

  public TrafficMap() {
    roads = new ArrayList<>();
    nodes = new ArrayList<>();
  }

  public TrafficMap(List<Node> nodes, List<Road> roads) {
    this.nodes = nodes;
    this.roads = roads;
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

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public void setRoads(List<Road> roads) {
    this.roads = roads;
  }
}

package ru.nsu.fit.aykaovlev.traffic.model;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.fit.dzaikov.traffic.model.Node;
import ru.nsu.fit.dzaikov.traffic.model.Road;

public class Street {

  private String name;
  private List<Road> roads;
  private List<Node> nodes;

  Street(String name) {
    this.name = name;
    roads = new ArrayList<>();
    nodes = new ArrayList<>();
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  public void addRoad(Road road) {
    roads.add(road);
  }

  public String getName() {
    return name;
  }

  public List<Road> getRoads() {
    return roads;
  }

  public List<Node> getNodes() {
    return nodes;
  }
}

package ru.nsu.fit.traffic.model.road;

import ru.nsu.fit.traffic.model.node.Node;

import java.util.ArrayList;
import java.util.List;

public class Street {

  private String name;
  private List<Road> roads;
  private List<Node> nodes;

  public Street(String name) {
    this.name = name;
    roads = new ArrayList<>();
    nodes = new ArrayList<>();
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  public void removeRoad(Road road){
    if (roads.contains(road)) {
      roads.remove(road);
      boolean removeFrom = true;
      boolean removeTo = true;
      for (Road r : roads) {
        if (road.getFrom() == r.getFrom() || road.getFrom() == r.getTo())
          removeFrom = false;
        if (road.getTo() == r.getFrom() || road.getTo() == r.getTo())
          removeTo = false;
        if (!removeFrom && !removeTo) break;
      }
      if (removeFrom) nodes.remove(road.getFrom());
      if (removeTo) nodes.remove(road.getTo());
    }
  }

  public void addRoad(Road road) {
    roads.add(road);
    nodes.add(road.getFrom());
    nodes.add(road.getTo());
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

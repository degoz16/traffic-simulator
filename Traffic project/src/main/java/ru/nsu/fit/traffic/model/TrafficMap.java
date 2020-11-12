package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficMap {
  private Map<String, Node> idToNode = new HashMap<>();
  private Map<String, Road> idToRoad = new HashMap<>();

  public void addNode(String id, Node node) {
    idToNode.put(id, node);
  }

  public void addRoad(String id, Road road) {
    idToRoad.put(id, road);
  }

  public List<Node> getNodes() {
    return new ArrayList<>(idToNode.values());
  }

  public List<Road> getRoads() {
    return new ArrayList<>(idToRoad.values());
  }

  public Node findNode(String id) {
    return idToNode.get(id);
  }
}

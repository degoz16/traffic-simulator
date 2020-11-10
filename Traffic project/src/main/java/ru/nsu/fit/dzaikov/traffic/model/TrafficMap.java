package ru.nsu.fit.dzaikov.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class TrafficMap {
    private List<Node> nodes = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addRoad(Road road) {
        roads.add(road);
    }
}

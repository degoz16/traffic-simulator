package ru.nsu.fit.dzaikov.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int x;
    private final int y;
    private List<Road> roads = new ArrayList<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public void connect(Road road, Node node) {
        road.setFrom(this);
        road.setTo(node);
        addRoad(road);
        node.addRoad(road);
    }
}

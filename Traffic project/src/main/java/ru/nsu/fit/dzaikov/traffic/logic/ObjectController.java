package ru.nsu.fit.dzaikov.traffic.logic;

import ru.nsu.fit.dzaikov.traffic.model.Node;
import ru.nsu.fit.dzaikov.traffic.model.Road;
import ru.nsu.fit.dzaikov.traffic.model.TrafficMap;

public class ObjectController {
    private TrafficMap map;
    private EditOperation currentOperation = EditOperation.NONE;
    private Node lastNode = null;

    public void addNodeOnEmpty(int x, int y) {
        Node newNode = new Node(x, y);
        map.addNode(newNode);
        if (lastNode != null) {
            Road newRoadTo = new Road(1);
            Road newRoadFrom = new Road(1);
            lastNode.connect(newRoadTo, newRoadFrom, newNode);
            map.addRoad(newRoadTo);
            map.addRoad(newRoadFrom);
        }
    }

    public void addNodeOnRoad (int x, int y, Road road) {

    }

}

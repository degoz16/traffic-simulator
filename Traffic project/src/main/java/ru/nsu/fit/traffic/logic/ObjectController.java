package ru.nsu.fit.traffic.logic;

import java.util.UUID;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;
import ru.nsu.fit.traffic.model.TrafficMap;

public class ObjectController {
    private TrafficMap map;
    private EditOperation currentOperation = EditOperation.NONE;
    private Node lastNode = null;

    public void addNodeOnEmpty(int x, int y) {
        Node newNode = new Node(x, y);
       // map.addNode(newNode);
        if (lastNode != null) {
            Road newRoadTo = new Road(1);
            Road newRoadFrom = new Road(1);
            lastNode.connect(newRoadTo, newRoadFrom, newNode);
            map.addRoad(UUID.randomUUID().toString(), newRoadTo);
            map.addRoad(UUID.randomUUID().toString(), newRoadFrom);
        }
    }

    public void addNodeOnRoad (int x, int y, Road road) {

    }

}

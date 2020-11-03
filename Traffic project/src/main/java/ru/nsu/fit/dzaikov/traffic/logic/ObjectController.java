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
            Road newRoad = new Road(1, 1);
            lastNode.connect(newRoad, newNode);
            map.addRoad(newRoad);
        }
    }

    public void addNodeOnRoad (int x, int y, Road road) {

    }

}

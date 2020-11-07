package ru.nsu.fit.dzaikov.traffic.logic;

import ru.nsu.fit.dzaikov.traffic.model.Node;
import ru.nsu.fit.dzaikov.traffic.model.Road;
import ru.nsu.fit.dzaikov.traffic.model.TrafficMap;

public class RoadBuilder {

    private TrafficMap currMap;
    private Node currNode;

    public RoadBuilder(TrafficMap map){
        currMap = map;
    }

    public Road handleOperation(EditOperation op, int x, int y){
        switch (op) {
            case ROAD_CREATION_STEP_1 -> {
                currNode = new Node(x, y);
                return null;
            }
            case ROAD_CREATION_STEP_2 -> {
                Road currRoad = new Road(2, 2);
                currRoad.setFrom(currNode);
                currRoad.setTo(new Node(x, y));
                currMap.addNode(currRoad.getFrom());
                currMap.addNode(currRoad.getTo());
                currMap.addRoad(currRoad);
                return currRoad;
            }
        }
        return null;
    }
}

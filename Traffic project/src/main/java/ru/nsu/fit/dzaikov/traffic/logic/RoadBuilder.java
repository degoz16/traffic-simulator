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
                for (Node node : currMap.getNodes()) {
                    if (Math.abs(node.getX() - x) < 10 && Math.abs(node.getY() - y) < 10) {
                        currNode = node;
                        return null;
                    }
                }
                currNode = new Node(x, y);
                return null;
            }
            case ROAD_CREATION_STEP_2 -> {
                Road currRoad = new Road(2);
                currRoad.setFrom(currNode);
                Node secondNode = null;
                for (Node node : currMap.getNodes()) {
                    if (Math.abs(node.getX() - x) < 10 && Math.abs(node.getY() - y) < 10) {
                        secondNode = node;
                        break;
                    }
                }
                if (secondNode == null) {
                    secondNode = new Node(x, y);
                }
                currRoad.setTo(secondNode);
                currMap.addNode(currRoad.getFrom());
                currMap.addNode(currRoad.getTo());
                currMap.addRoad(currRoad);
                return currRoad;
            }
        }
        return null;
    }
}

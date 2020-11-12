package ru.nsu.fit.traffic.logic;

import java.util.UUID;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;
import ru.nsu.fit.traffic.model.TrafficMap;

public class RoadBuilder {

    private TrafficMap currMap;
    private Node currNode;
    private String fromId;
    private String toId;
    private String roadId;

    public RoadBuilder(TrafficMap map, int NODE_SIZE){
        currMap = map;
    }

    public Road handleOperation(EditOperation op, int x, int y, String id){
        switch (op) {
            case ROAD_CREATION_STEP_1 -> {

                currNode = currMap.findNode(id);
                if (currNode == null) {
                    System.out.println("NINASHEL 1");
                    currNode = new Node(x,y);
                }
                fromId = id;
                return null;
            }
            case ROAD_CREATION_STEP_2 -> {
                Road currRoad = new Road(2);
                currRoad.setFrom(currNode);
                Node secondNode = currMap.findNode(id);
                if (secondNode == null) {
                    System.out.println("NINASHEL 2");
                    secondNode = new Node(x, y);
                }
                currRoad.setTo(secondNode);
                currMap.addNode(fromId, currNode);
                toId = id;
                currMap.addNode(id, secondNode);
                roadId = UUID.randomUUID().toString();
                currMap.addRoad(roadId, currRoad);
                return currRoad;
            }
        }
        return null;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getRoadId() {
        return roadId;
    }
}

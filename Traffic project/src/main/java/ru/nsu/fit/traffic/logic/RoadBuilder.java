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

    public Road[] handleOperation(EditOperation op, int x, int y, String id, int lanesBack, int lanesForward){
        switch (op) {
            case ROAD_CREATION_STEP_1 -> {

                currNode = currMap.findNode(id);
                if (currNode == null) {
                    currNode = new Node(x,y);
                }
                fromId = id;
                return null;
            }
            case ROAD_CREATION_STEP_2 -> {
                Road forwardRoad = new Road(lanesForward, Road.roadDirection.FORWARD);
                Road backRoad = new Road(lanesBack, Road.roadDirection.BACK);
                forwardRoad.setFrom(currNode);
                backRoad.setFrom(currNode);

                Node secondNode = currMap.findNode(id);
                if (secondNode == null) {
                    secondNode = new Node(x, y);
                }

                backRoad.setTo(secondNode);
                forwardRoad.setTo(secondNode);

                currMap.addNode(fromId, currNode);
                toId = id;
                currMap.addNode(id, secondNode);
                currMap.addRoad(UUID.randomUUID().toString(), backRoad);
                currMap.addRoad(UUID.randomUUID().toString(), forwardRoad);
                return new Road[]{forwardRoad, backRoad};
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

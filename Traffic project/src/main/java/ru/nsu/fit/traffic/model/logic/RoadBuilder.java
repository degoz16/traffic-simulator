package ru.nsu.fit.traffic.model.logic;

import java.util.UUID;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;
import ru.nsu.fit.traffic.model.TrafficMap;

public class RoadBuilder {

    /*private TrafficMap currMap;
    private Node outNode;
    private String fromId;
    private String toId;
    private String roadId;

    public RoadBuilder(TrafficMap map, int NODE_SIZE) {
        currMap = map;
    }

    public Road[] handleOperation(EditOperation op, int x, int y, String id, int lanesBack, int lanesForward){
        switch (op) {
            case ROAD_CREATION_STEP_1 -> {
                outNode = currMap.findNode(id);
                if (outNode == null) {
                    outNode = new Node(x,y);
                }
                fromId = id;
                return null;
            }
            case ROAD_CREATION_STEP_2 -> {
                Road forwardRoad = new Road(lanesForward);
                Road backRoad = new Road(lanesBack);

                forwardRoad.setFrom(outNode);
                outNode.addRoadIn(forwardRoad);
                backRoad.setTo(outNode);
                outNode.addRoadOut(backRoad);

                Node inNode = currMap.findNode(id);
                if (inNode == null) {
                    inNode = new Node(x, y);
                }

                backRoad.setFrom(inNode);
                forwardRoad.setTo(inNode);
                inNode.addRoadIn(backRoad);
                inNode.addRoadOut(forwardRoad);

                currMap.addNode(fromId, outNode);
                toId = id;
                currMap.addNode(id, inNode);
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
    }*/
}

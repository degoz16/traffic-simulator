package ru.nsu.fit.traffic.jsonParse;

import ru.nsu.fit.traffic.model.TrafficMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapJsonStruct {
    private List<NodeJsonStruct> nodes = new ArrayList<>();
    private List<RoadJsonStruct> roads = new ArrayList<>();

    public MapJsonStruct(TrafficMap map) {
        map.forEachNodes(node -> {
            List<Integer> roadsFrom = new ArrayList<>();
            List<Integer> roadsTo = new ArrayList<>();
            node.foreachRoadIn(road -> roadsFrom.add(map.indexOfRoad(road)));
            node.foreachRoadOut(road -> roadsTo.add(map.indexOfRoad(road)));
            nodes.add(new NodeJsonStruct(
                    node.getX(),
                    node.getY(),
                    roadsFrom,
                    roadsTo,
                    node.isSpawner(),
                    node.getTrafficLight()));
        });
        map.forEachRoads(road -> {
            List<LaneJsonStruct> lanes = new ArrayList<>();
            road.getLanes().forEach(lane -> {
                List<Map<String, String>> signs = new ArrayList<>();
                lane.getSigns().forEach(sign -> signs.add(sign.getSettings()));
                LaneJsonStruct laneJsonStruct = new LaneJsonStruct(signs);
                lanes.add(laneJsonStruct);
            });
            RoadJsonStruct roadJsonStruct = new RoadJsonStruct(
                    map.indexOfNode(road.getFrom()),
                    map.indexOfNode(road.getTo()),
                    lanes);
            roads.add(roadJsonStruct);
        });
    }

}

package ru.nsu.fit.traffic.jsonParse;

import ru.nsu.fit.traffic.model.TrafficMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapJsonStruct {
    private List<NodeJsonStruct> nodes;
    private List<RoadJsonStruct> roads;

    public MapJsonStruct(TrafficMap map) {
        map.getNodes().forEach(node -> {
            List<Integer> roadsFrom = new ArrayList<>();
            List<Integer> roadsTo = new ArrayList<>();
            node.getRoadsIn().forEach(road -> roadsFrom.add(map.getRoads().indexOf(road)));
            node.getRoadsOut().forEach(road -> roadsTo.add(map.getRoads().indexOf(road)));
            nodes.add(new NodeJsonStruct(
                    node.getX(),
                    node.getY(),
                    roadsFrom,
                    roadsTo,
                    node.isSpawner(),
                    node.getTrafficLight()));
        });
        map.getRoads().forEach(road -> {
            List<LaneJsonStruct> lanes = new ArrayList<>();
            road.getLanes().forEach(lane -> {
                List<Map<String, String>> signs = new ArrayList<>();
                lane.getSigns().forEach(sign -> signs.add(sign.getSettings()));
                LaneJsonStruct laneJsonStruct = new LaneJsonStruct(signs);
                lanes.add(laneJsonStruct);
            });
            RoadJsonStruct roadJsonStruct = new RoadJsonStruct(
                    map.getNodes().indexOf(road.getFrom()),
                    map.getNodes().indexOf(road.getTo()),
                    lanes);
            roads.add(roadJsonStruct);
        });
    }

}

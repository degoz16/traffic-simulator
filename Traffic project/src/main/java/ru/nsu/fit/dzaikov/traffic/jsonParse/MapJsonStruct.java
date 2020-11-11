package ru.nsu.fit.dzaikov.traffic.jsonParse;

import ru.nsu.fit.dzaikov.traffic.model.Road;
import ru.nsu.fit.dzaikov.traffic.model.TrafficMap;

import java.util.ArrayList;
import java.util.List;

public class MapEngineJsonStruct {
    private List<NodeJsonStruct> nodes;
    private List<Road> roads;

    public MapEngineJsonStruct(TrafficMap map) {
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

        });
    }

}

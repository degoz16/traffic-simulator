package ru.nsu.fit.dzaikov.traffic.jsonParse;

import ru.nsu.fit.dzaikov.traffic.model.TrafficLight;

import java.util.List;

public class NodeJsonStruct {
    private List<Integer> roadsFrom;
    private List<Integer> roadsTo;
    private boolean isSpawner;
    private TrafficLight trafficLight;

    public NodeJsonStruct(List<Integer> roadsFrom, List<Integer> roadsTo, boolean isSpawner, TrafficLight trafficLight) {
        this.roadsFrom = roadsFrom;
        this.roadsTo = roadsTo;
        this.isSpawner = isSpawner;
        this.trafficLight = trafficLight;
    }
}

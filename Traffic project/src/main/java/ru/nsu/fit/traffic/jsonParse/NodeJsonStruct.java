package ru.nsu.fit.traffic.jsonParse;

import ru.nsu.fit.traffic.model.TrafficLight;

import java.util.List;

public class NodeJsonStruct {
    private int x;
    private int y;
    private List<Integer> roadsIn;
    private List<Integer> roadsOut;
    private boolean isSpawner;
    private TrafficLight trafficLight;

    public NodeJsonStruct(int x, int y, List<Integer> roadsFrom, List<Integer> roadsTo, boolean isSpawner, TrafficLight trafficLight) {
        this.x = x;
        this.y = y;
        this.roadsIn = roadsFrom;
        this.roadsOut = roadsTo;
        this.isSpawner = isSpawner;
        this.trafficLight = trafficLight;
    }
}

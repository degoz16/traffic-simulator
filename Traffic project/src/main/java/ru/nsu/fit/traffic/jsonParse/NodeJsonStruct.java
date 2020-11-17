package ru.nsu.fit.traffic.jsonParse;

import ru.nsu.fit.traffic.model.TrafficLight;

import java.util.List;

public class NodeJsonStruct {
    private double x;
    private double y;
    private List<Integer> roadsIn;
    private List<Integer> roadsOut;
    private boolean isSpawner;
    private TrafficLight trafficLight;

    public NodeJsonStruct(double x, double y, List<Integer> roadsFrom, List<Integer> roadsTo, boolean isSpawner, TrafficLight trafficLight) {
        this.x = x;
        this.y = y;
        this.roadsIn = roadsFrom;
        this.roadsOut = roadsTo;
        this.isSpawner = isSpawner;
        this.trafficLight = trafficLight;
    }
}

package ru.nsu.fit.dzaikov.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int x;
    private final int y;

    private TrafficLight trafficLight = null;
    private boolean isSpawner = false;

    //На одинаковых индексах лежат противоположно направленные дороги
    private List<Road> roadsTo = new ArrayList<>();
    private List<Road> roadsFrom = new ArrayList<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public boolean isSpawner() {
        return isSpawner;
    }

    public List<Road> getRoadsTo() {
        return roadsTo;
    }

    public List<Road> getRoadsFrom() {
        return roadsFrom;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void addRoadTo(Road road) {
        roadsTo.add(road);
    }

    public void addRoadFrom(Road road) {
        roadsFrom.add(road);
    }

    public void connect(Road roadTo, Road roadFrom, Node node) {
        roadTo.setFrom(this);
        roadTo.setTo(node);
        roadFrom.setFrom(node);
        roadFrom.setTo(this);
        addRoadTo(roadTo);
        addRoadFrom(roadFrom);
        node.addRoadTo(roadFrom);
        node.addRoadFrom(roadTo);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}

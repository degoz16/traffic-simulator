package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int x;
    private final int y;

    private TrafficLight trafficLight = null;
    private boolean isSpawner = false;

    //На одинаковых индексах лежат противоположно направленные дороги
    private List<Road> roadsOut = new ArrayList<>();
    private List<Road> roadsIn = new ArrayList<>();

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

    public List<Road> getRoadsOut() {
        return roadsOut;
    }

    public List<Road> getRoadsIn() {
        return roadsIn;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void addRoadTo(Road road) {
        roadsOut.add(road);
    }

    public void addRoadFrom(Road road) {
        roadsIn.add(road);
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

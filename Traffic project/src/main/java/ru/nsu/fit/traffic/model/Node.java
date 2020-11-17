package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Node {
    private final double x;
    private final double y;

    private TrafficLight trafficLight = null;
    private boolean isSpawner = false;

    //На одинаковых индексах лежат противоположно направленные дороги
    private List<Road> roadsOut = new ArrayList<>();
    private List<Road> roadsIn = new ArrayList<>();

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public boolean isSpawner() {
        return isSpawner;
    }

    public void foreachRoadOut(Consumer<Road> f) {
        roadsOut.forEach(f);
    }

    public Stream<Road> getRoadOutStream() {
        return roadsOut.stream();
    }

    public void foreachRoadIn(Consumer<Road> f) {
        roadsIn.forEach(f);
    }

    public Stream<Road> getRoadInStream() {
        return roadsIn.stream();
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void addRoadOut(Road road) {
        roadsOut.add(road);
    }

    public void addRoadIn(Road road) {
        roadsIn.add(road);
    }

    public void removeRoadOut(Road road) {
        roadsOut.remove(road);
    }

    public void removeRoadIn(Road road) {
        roadsIn.remove(road);
    }

    public void connect(Road roadTo, Road roadFrom, Node node) {
        roadTo.setBackRoad(roadFrom);
        roadFrom.setBackRoad(roadTo);

        roadTo.setFrom(this);
        roadTo.setTo(node);
        roadFrom.setFrom(node);
        roadFrom.setTo(this);

        addRoadOut(roadTo);
        addRoadIn(roadFrom);
        node.addRoadOut(roadFrom);
        node.addRoadIn(roadTo);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}

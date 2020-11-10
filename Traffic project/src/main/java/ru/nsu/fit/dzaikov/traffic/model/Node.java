package ru.nsu.fit.dzaikov.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int x;
    private final int y;
    private List<Road> roadsTo = new ArrayList<>();
    private List<Road> roadsFrom = new ArrayList<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
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

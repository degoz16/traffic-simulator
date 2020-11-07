package ru.nsu.fit.dzaikov.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class Road {
    private Node from = null;
    private Node to = null;
    private final List<Lane> lanes;
    private int length = -1;

    public Road(int lanesNumForward, int lanesNumBack) {
        lanes = new ArrayList<>();
        for (int i = 0; i < lanesNumForward; ++i)
            lanes.add(new Lane(Lane.roadDirection.FORWARD));
        for (int i = 0; i < lanesNumBack; ++i)
            lanes.add(new Lane(Lane.roadDirection.BACK));
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public int getLen() {
        if (length == -1) {
            return length = (int) Math.sqrt
                    (Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
        } else return length;
    }

    public List<Lane> getLanes(){
        return lanes;
    }
}

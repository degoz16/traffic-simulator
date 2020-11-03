package ru.nsu.fit.dzaikov.traffic.model;

import java.util.List;

public class Road {
    private Node from = null;
    private Node to = null;
    private List<Lane> lanes;

    public Road(int lanesNumForward, int lanesNumBack) {

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
}

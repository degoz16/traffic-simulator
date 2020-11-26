package ru.nsu.fit.traffic.jsonParse;

import java.util.List;

public class RoadJsonStruct {
    private int from;
    private int to;
    private List<LaneJsonStruct> lanes;

    public RoadJsonStruct(int from, int to, List<LaneJsonStruct> lanes) {
        this.from = from;
        this.to = to;
        this.lanes = lanes;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public List<LaneJsonStruct> getLanes() {
        return lanes;
    }
}

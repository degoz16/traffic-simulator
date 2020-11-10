package ru.nsu.fit.dzaikov.traffic.jsonParse;

import java.util.List;

public class RoadJsonStruct {
    private int length;
    private int from;
    private int to;
    private List<LaneJsonStruct> lanes;

    public RoadJsonStruct(int length, int from, int to, List<LaneJsonStruct> lanes) {
        this.length = length;
        this.from = from;
        this.to = to;
        this.lanes = lanes;
    }
}

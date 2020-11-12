package ru.nsu.fit.traffic.jsonParse;

import java.util.List;

public class TrafficLightJsonStruct {
    private int delayGreen;
    private int delayRed;
    private List<Integer> pairsOfRoad;

    public TrafficLightJsonStruct(int delayGreen, int delayRed, List<Integer> pairsOfRoad) {
        this.delayGreen = delayGreen;
        this.delayRed = delayRed;
        this.pairsOfRoad = pairsOfRoad;
    }
}

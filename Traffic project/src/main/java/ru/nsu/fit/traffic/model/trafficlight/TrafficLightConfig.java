package ru.nsu.fit.traffic.model.trafficlight;

import java.util.ArrayList;
import java.util.List;

public class TrafficLightConfig {
    private int delay;
    private List<Integer> roadIndexes;

    public TrafficLightConfig(int delay, List<Integer> roadIndexes){
        this.delay = delay;
        this.roadIndexes = new ArrayList<>(roadIndexes);
    }

    public int getDelay() {
        return delay;
    }
}

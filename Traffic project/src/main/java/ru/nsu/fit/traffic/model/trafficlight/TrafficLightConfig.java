package ru.nsu.fit.traffic.model.trafficlight;

import ru.nsu.fit.traffic.model.road.Road;

import java.util.ArrayList;
import java.util.List;

public class TrafficLightConfig {
    private int delay;
    private List<Road> roads;

    public TrafficLightConfig(int delay, List<Road> roads){
        this.delay = delay;
        assert roads != null;
        this.roads = new ArrayList<>(roads);
    }

    public int getDelay() {
        return delay;
    }
}

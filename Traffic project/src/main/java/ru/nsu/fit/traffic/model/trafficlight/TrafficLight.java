package ru.nsu.fit.traffic.model.trafficlight;

import java.util.ArrayList;
import java.util.List;

public class TrafficLight {
    private final List<TrafficLightConfig> config = new ArrayList<>();

    public TrafficLight(TrafficLightConfig greenConfig, TrafficLightConfig redConfig) {
        config.add(greenConfig);
        config.add(redConfig);
    }

    public int getGreenDelay(){
        return config.get(0).getDelay();
    }

    public int getRedDelay(){
        return config.get(1).getDelay();
    }

}

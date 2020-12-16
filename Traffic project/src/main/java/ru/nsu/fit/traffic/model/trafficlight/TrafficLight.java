package ru.nsu.fit.traffic.model.trafficlight;

import java.util.List;

public class TrafficLight {
    private TrafficLightConfig greenConfig;
    private TrafficLightConfig redConfig;

    public TrafficLight(TrafficLightConfig greenConfig, TrafficLightConfig redConfig) {
        this.greenConfig = greenConfig;
        this.redConfig = redConfig;
    }

    public int getGreenDelay(){
        return greenConfig.getDelay();
    }

    public int getRedDelay(){
        return redConfig.getDelay();
    }

}

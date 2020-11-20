package ru.nsu.fit.traffic.model;

import ru.nsu.fit.traffic.model.trafficsign.RoadSign;
import ru.nsu.fit.traffic.model.trafficsign.SignType;

import java.util.Map;
import java.util.TreeMap;

public class MainRoadSign implements RoadSign {
    @Override
    public SignType getSignType() {
        return SignType.MAIN_ROAD;
    }

    @Override
    public Map<String, String> getSettings() {
        Map<String, String> map = new TreeMap<>();
        map.put("type", "mainRoad");
        return map;
    }

    @Override
    public void setSettings(Map<String, String> settings) {

    }

    @Override
    public RoadSign getCopySign() {
        return new MainRoadSign();
    }
}

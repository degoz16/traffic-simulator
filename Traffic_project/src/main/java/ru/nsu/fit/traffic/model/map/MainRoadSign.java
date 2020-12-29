package ru.nsu.fit.traffic.model.map;

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
        map.put("type", getSignType().toString());
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

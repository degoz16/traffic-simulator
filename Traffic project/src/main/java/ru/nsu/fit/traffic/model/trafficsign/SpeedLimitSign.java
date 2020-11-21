package ru.nsu.fit.traffic.model.trafficsign;

import java.util.Map;
import java.util.TreeMap;

public class SpeedLimitSign implements RoadSign {
    private int speedLimit = 0;

    public SpeedLimitSign(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public SignType getSignType() {
        return SignType.SPEED_LIMIT;
    }

    @Override
    public Map<String, String> getSettings() {
        Map<String, String> map = new TreeMap<>();
        map.put("type", "speed");
        map.put("limit", String.valueOf(speedLimit));
        return map;
    }

    @Override
    public void setSettings(Map<String, String> settings) {
        if (settings.containsKey("limit")) {
            speedLimit = Integer.parseInt(settings.get("limit"));
        }
    }

    @Override
    public RoadSign getCopySign() {
        return new SpeedLimitSign(speedLimit);
    }

}

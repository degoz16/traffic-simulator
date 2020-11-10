package ru.nsu.fit.dzaikov.traffic.model;

import java.util.Map;

public interface RoadSign {
    public SignType getSignType();
    public Map<String, String> getSettings();
    public void setSettings(Map<String, String> settings);
}

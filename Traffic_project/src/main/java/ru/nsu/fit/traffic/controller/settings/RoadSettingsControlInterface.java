package ru.nsu.fit.traffic.controller.settings;

public interface RoadSettingsControlInterface {
    void deleteRoad();
    void confirmRoadSettings(int newLanesNum, String streetName);
}

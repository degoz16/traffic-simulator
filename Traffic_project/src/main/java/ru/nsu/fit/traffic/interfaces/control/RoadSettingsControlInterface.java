package ru.nsu.fit.traffic.interfaces.control;

public interface RoadSettingsControlInterface {
    void deleteRoad();
    void confirmRoadSettings(int newLanesNum, String streetName);
}

package ru.nsu.fit.traffic.controller;

import ru.nsu.fit.traffic.model.congestion.ReportStruct;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public interface SceneElementsControl {
    void updateStatistic(
            int carSpawnersCnt,
            int streetsCnt,
            int roadsCnt,
            int buildingCnt,
            int connectivity,
            List<String> streets);
    void removeSelectRect();
    void addSelectRect(double x, double y);
    void resizeSelectRect(double x, double y);
    void buildingSettingsSetVisible(boolean status);
    void buildingSettingsSetPos(double x, double y);
    void buildingSettingsSetParams(double weight, int parkingPlaces);
    void roadSettingsSetVisible(boolean status);
    void roadSettingsSetPos(double x, double y);
    void roadSettingsSetParams(int lanesNum, String street);
    void roadSignMenuSetVisible(boolean status);
    void nodeSettingsSetVisible(boolean status);
    void nodeSettingsSetPos(double x, double y);
    void nodeSettingsSetParams(LocalTime start, LocalTime end, int spawnRate);
    void numberOfLanesPaneSetVisible(boolean status);
    void trafficLightSettingsSetVisible(boolean status);
    void trafficLightSettingsSetPos(double x, double y);
    void trafficLightSettingsSetParams(int greenDelay, int redDelay);
    void statisticSwitchVisible();
    void timeLineSliderInit(int windowsListSize, Function<Integer, Long> endGetter);
    void timeLineSliderSetMax(double max);
    void timeLineSliderAddValue(double val);
    void simulationProcessModeEnable();
    void simulationEndModeEnable();
    void editModeEnable();
    void reportModeEnable();
    void playBackModeEnable();
}

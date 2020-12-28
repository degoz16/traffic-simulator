package ru.nsu.fit.traffic.controller.settings;

public interface BuildingSettingsControlInterface {
    void closeSettings();
    void confirmSettings(double weight, int parkingPlaces);
    void deleteBuilding();
}

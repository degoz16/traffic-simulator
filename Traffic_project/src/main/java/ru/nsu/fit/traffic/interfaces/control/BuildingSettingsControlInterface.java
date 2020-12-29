package ru.nsu.fit.traffic.interfaces.control;

public interface BuildingSettingsControlInterface {
    void closeSettings();
    void confirmSettings(double weight, int parkingPlaces);
    void deleteBuilding();
}

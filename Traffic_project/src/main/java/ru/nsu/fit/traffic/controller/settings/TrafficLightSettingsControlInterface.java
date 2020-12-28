package ru.nsu.fit.traffic.controller.settings;

public interface TrafficLightSettingsControlInterface {
    void closeSettings();
    void confirmSettings(int greenDelay, int redDelay);
}

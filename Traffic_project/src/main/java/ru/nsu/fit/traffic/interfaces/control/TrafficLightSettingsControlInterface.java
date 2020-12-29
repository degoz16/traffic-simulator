package ru.nsu.fit.traffic.interfaces.control;

public interface TrafficLightSettingsControlInterface {
    void closeSettings();
    void confirmSettings(int greenDelay, int redDelay);
}

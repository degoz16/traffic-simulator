package ru.nsu.fit.traffic.controller.edit;

public interface EditControlInterface {
    void startSimulation();
    void stopSimulation();
    void rewindForward();
    void rewindBack();
    void roadButtonClicked();
    void startTimePicker(String time);
    void trafficLightButtonClicked();
    void buildingButtonClicked();
    void playbackClicked();
    void reportClicked();
    void editClicked();

}

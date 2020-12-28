package ru.nsu.fit.traffic.controller.edit;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.place.PlaceOfInterest;
import ru.nsu.fit.traffic.model.road.Road;

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
    void roadSignButtonClicked();
    void showStatistic();
    void setSpeedSign(int speed);
    void setMainRoad();
    void onLeftLanesTextFieldChange(String newValue);
    void onRightLanesTextFieldChange(String newValue);
    void onTimeLineSliderChange(Number newValue);
    void onPlaybackLineSliderChange(Number newVal);
    void onBasePaneClickedFilter(
            MouseEventWrapper event,
            double basePaneWidth,
            double basePaneHeight,
            double windowWidth,
            double windowHeight);
    void onMainPaneReleased(MouseEventWrapper event);
    void onMainPanePressed(MouseEventWrapper event);
    void onPoiClicked(PlaceOfInterest placeOfInterest, MouseEventWrapper event);
    void onMainPaneClicked(MouseEventWrapper event);
    void onNodeClick(Node node, MouseEventWrapper event);
    void onMainPaneDrag(MouseEventWrapper event);
    void onRoadClick(Road road, int i, MouseEventWrapper event);
}
